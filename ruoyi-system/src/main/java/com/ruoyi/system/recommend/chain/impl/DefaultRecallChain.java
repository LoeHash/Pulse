package com.ruoyi.system.recommend.chain.impl;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.system.config.domain.ins.AlgoConfig;
import com.ruoyi.system.config.event.DynamicConfigListener;
import com.ruoyi.system.recommend.domain.PostCandidate;
import com.ruoyi.system.recommend.recall.RecallStrategy;
import com.ruoyi.system.recommend.chain.RecallChain;
import com.ruoyi.system.recommend.chain.RecallRequest;
import com.ruoyi.system.recommend.chain.RecallResult;
import com.ruoyi.system.recommend.service.IRecommendExposureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认召回链实现：
 * 1) 依次执行所有召回策略（latest/hot/preference）
 * 2) 对不同策略返回的候选按路权重做融合
 * 3) 对同一帖子做去重合并（按 postId）
 * 4) 增加多路命中奖励
 * 5) 排序并截断总数量
 * 6) 曝光去重策略（支持回补，避免“第二次空列表”）
 */
@Component
@RequiredArgsConstructor
public class DefaultRecallChain implements RecallChain, DynamicConfigListener<AlgoConfig> {

    /**
     * 自动注入全部召回策略实现
     * 例如：LatestRecallStrategy / HotRecallStrategy / PreferenceRecallStrategy
     */
    private final List<RecallStrategy> strategies;

    /**
     * 新增：曝光历史服务（查询已曝光、记录在外层服务做）
     */
    private final IRecommendExposureService exposureService;

    /**
     * 各召回路在融合时的权重
     */
    private static Map<String, Double> CHANNEL_WEIGHT = new ConcurrentHashMap<>();

    static {
        CHANNEL_WEIGHT.put("latest", 0.20D);
        CHANNEL_WEIGHT.put("hot", 0.05D);
        CHANNEL_WEIGHT.put("preference", 0.75D);
    }

    /**
     * 多路命中奖励的加分因子：每命中一路，额外加分
     */
    private static double ROAD_HIT_FACTOR = 0.05D;

    /**
     * 新增：曝光去重窗口（天）
     */
    public static int EXPOSED_FILTER_DAYS = 7;



    @Override
    public RecallResult recall(RecallRequest request) {

        Map<Long, PostCandidate> merged = new HashMap<Long, PostCandidate>();

        // 记录 preference 命中集合，给 hot 惩罚用
        Set<Long> preferenceHitSet = new HashSet<Long>();

        for (RecallStrategy strategy : strategies) {
            List<PostCandidate> oneRoad = strategy.recall(
                    request.getUserId(),
                    request.getPreference(),
                    request.getPerChannelLimit()
            );

            if (oneRoad == null || oneRoad.isEmpty()) {
                continue;
            }

            // 如果是 preference 路，先记录命中帖子
            if ("preference".equals(strategy.name())) {
                for (int i = 0; i < oneRoad.size(); i++) {
                    PostCandidate c = oneRoad.get(i);
                    if (c != null && c.getPostId() != null) {
                        preferenceHitSet.add(c.getPostId());
                    }
                }
            }

            double w = CHANNEL_WEIGHT.getOrDefault(strategy.name(), 0.2D);

            // 1) 计算该路 min/max
            //     归一化计算
            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;
            for (int i = 0; i < oneRoad.size(); i++) {
                PostCandidate c = oneRoad.get(i);
                if (c == null || c.getRecallScore() == null) {
                    continue;
                }
                double s = c.getRecallScore();
                if (s < min) {
                    min = s;
                }
                if (s > max) {
                    max = s;
                }
            }
            if (min == Double.MAX_VALUE) {
                min = 0D;
                max = 1D;
            }

            // 2) 合并
            for (int i = 0; i < oneRoad.size(); i++) {
                PostCandidate c = oneRoad.get(i);
                if (c == null || c.getPostId() == null) {
                    continue;
                }

                double raw = c.getRecallScore() == null ? 0D : c.getRecallScore();
                double norm;
                if (max <= min) {
                    norm = 0D;
                } else {
                    norm = (raw - min) / (max - min); // 归一化到0~1
                }

                double addScore = norm * w;

                // hot 路惩罚：若不在 preference 命中集合里，强降权
                if ("hot".equals(strategy.name())) {
                    if (!preferenceHitSet.contains(c.getPostId())) {
                        addScore = addScore * 0.10D;
                    }
                }

                // 添加
                PostCandidate exist = merged.get(c.getPostId());
                //防止重复
                if (exist == null) {
                    //直接添加
                    PostCandidate n = new PostCandidate();
                    n.setPostId(c.getPostId());
                    n.setRecallScore(addScore);

                    if (c.getRecallSources() != null) {
                        n.getRecallSources().addAll(c.getRecallSources());
                    } else {
                        n.getRecallSources().add(strategy.name());
                    }

                    merged.put(n.getPostId(), n);
                } else {
                    // 多次出现，给予分数奖励
                    double oldScore = exist.getRecallScore() == null ? 0D : exist.getRecallScore();
                    exist.setRecallScore(oldScore + addScore);

                    if (c.getRecallSources() != null) {
                        exist.getRecallSources().addAll(c.getRecallSources());
                    } else {
                        exist.getRecallSources().add(strategy.name());
                    }
                }
            }
        }

        // 多路命中奖励
        for (PostCandidate c : merged.values()) {
            int hit = c.getRecallSources() == null ? 0 : c.getRecallSources().size();
            double base = c.getRecallScore() == null ? 0D : c.getRecallScore();
            c.setRecallScore(base + ROAD_HIT_FACTOR * hit);
        }

        // 排序
        List<PostCandidate> out = new ArrayList<PostCandidate>(merged.values());
        Collections.sort(out, new Comparator<PostCandidate>() {
            @Override
            public int compare(PostCandidate a, PostCandidate b) {
                double sa = a.getRecallScore() == null ? 0D : a.getRecallScore();
                double sb = b.getRecallScore() == null ? 0D : b.getRecallScore();
                return Double.compare(sb, sa);
            }
        });

        // 曝光剔除 + 回补策略
        int total = Math.max(1, request.getTotalLimit());
        out = applyExposurePolicy(request.getUserId(), out, total);
        if (out.size() > total) {
            out = out.subList(0, total);
        }

        RecallResult result = new RecallResult();
        result.setCandidates(out);
        return result;
    }


    // 因为前面已经在数据库里进行了一次过滤
    // 能出现的情况：推荐的过多，post个数跟不上，此时只能尝试从已经曝光的里补一些 可以全部都是以前曝光过的
    // 这是数据量的原因
    private List<PostCandidate> applyExposurePolicy(Long userId, List<PostCandidate> sorted, int totalLimit) {

        // 为empty 我们有自己的兜底策略，不直接返回空列表
        if (sorted == null) {
            return Collections.emptyList();
        }
        if (userId == null) {
            return sorted.size() > totalLimit ? sorted.subList(0, totalLimit) : sorted;
        }

        // 不需要补充
        if (sorted.size() >= totalLimit){
            return sorted;
        }

        // 接下来获取这个用户已经曝光过的帖子
        // 随机返回一些
        Set<Long> exposed = exposureService.getExposedPostIds(userId, EXPOSED_FILTER_DAYS);

        if (exposed == null || exposed.isEmpty()) {
            // 没有已经曝光的帖子
            // 返回原本的数据
            return sorted;
        }


        Object[] exposedIds = exposed.toArray();

        // 需要补充的数量
        int needCount = totalLimit - sorted.size();

        //补充的list
        ArrayList<PostCandidate> attachPostCandidates = new ArrayList<>(needCount);

        for (int i = 0; i < needCount; i++) {

            PostCandidate postCandidate = new PostCandidate();
            postCandidate.setRecallScore(0.02D); // 复推的分数可以设置为0.02，表示优先级最低
            postCandidate.setRecallSources(Set.of("repeat_exposure"));
            //随机
            postCandidate.setPostId((Long) exposedIds[RandomUtil.randomInt(Integer.MAX_VALUE - 1) % exposedIds.length]); // 从已曝光列表中循环取ID
            attachPostCandidates.add(postCandidate);
        }

        List<PostCandidate> result = new ArrayList<PostCandidate>(totalLimit);

        // 先塞 原有的
        for (int i = 0; i < sorted.size() && result.size() < totalLimit; i++) {
            result.add(sorted.get(i));
        }

        // 再塞补充的
        if (result.size() < totalLimit) {
            for (int i = 0; i < attachPostCandidates.size() && result.size() < totalLimit; i++) {
                result.add(attachPostCandidates.get(i));
            }
        }

        // 最终返回
        return result;
    }

    @Override
    public String getConfigKey() {
        return "algo.recommend";
    }

    @Override
    public void onChange(AlgoConfig newConfig) {
        synchronized (this){
            ROAD_HIT_FACTOR = newConfig.getRoadHitFactor();
            EXPOSED_FILTER_DAYS = newConfig.getExposedFilterDays();
            CHANNEL_WEIGHT.put("latest", newConfig.getLatestWeight());
            CHANNEL_WEIGHT.put("hot", newConfig.getHotWeight());
            CHANNEL_WEIGHT.put("preference", newConfig.getPreferenceWeight());
        }
    }
}