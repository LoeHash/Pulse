package com.ruoyi.system.recommand.recall.impl;

import com.ruoyi.common.core.domain.ForumPost;
import com.ruoyi.system.mapper.ForumPostMapper;
import com.ruoyi.system.recommand.domain.PostCandidate;
import com.ruoyi.system.recommand.domain.UserPreferenceView;
import com.ruoyi.system.recommand.recall.RecallStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ruoyi.system.recommand.chain.impl.DefaultRecallChain.EXPOSED_FILTER_DAYS;

/**
 * 热门路召回策略
 *
 * 思路：
 * 1. 先从数据库取最近一段时间的帖子（避免全表扫描）
 * 2. 在应用层计算热度分（含时间衰减）
 * 3. 按热度分倒序后返回TopN
 */
@Component
@RequiredArgsConstructor
public class HotRecallStrategy implements RecallStrategy {

    /** like 权重 */
    private static final double LIKE_FACTOR = 2D;
    /** comment 权重 */
    private static final double COMMENT_FACTOR = 3D;
    /** favorite 权重 */
    private static final double FAVORITE_FACTOR = 2D;
    /** view 权重 */
    private static final double VIEW_FACTOR = 0.1D;
    /** 时间衰减系数（小时粒度） */
    private static final double TIME_DECAY = 0.03D;


    private final ForumPostMapper postMapper;
    private final com.ruoyi.system.recommand.service.IRecommendExposureService exposureService;
    /**
     * 热门召回
     *
     * @param userId 用户ID（本路可不使用）
     * @param pref   用户偏好（本路可不使用）
     * @param limit  本路召回数量
     * @return 候选帖子列表
     */
    @Override
    public List<PostCandidate> recall(Long userId, UserPreferenceView pref, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        int fetchSize = Math.max(limit * 3, 100);

        // 获取已经曝光过的帖子ID，避免复推
        Set<Long> exposed = exposureService.getExposedPostIds(userId, EXPOSED_FILTER_DAYS);
        if (exposed == null) {
            exposed = Collections.emptySet();
        }
        List<ForumPost> posts;
        if (!exposed.isEmpty()){
            // 不为空, 就尝试排除这些曝光的帖子ID
            posts = postMapper.selectWithOutExposed(Arrays.asList(exposed.toArray()), fetchSize);
        }else{
            //only for once
            posts = postMapper.selectRecentPostsForHotRecall(fetchSize);
        }

        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        // 2) 计算热度分 + 封装候选
        long nowMs = System.currentTimeMillis();
        List<PostCandidate> candidates = new ArrayList<>(posts.size());

        for (ForumPost post : posts) {
            if (post == null || post.getId() == null) {
                continue;
            }

            long likeCount = (post.getLikeCount());
            long commentCount = (post.getCommentCount());
            long favoriteCount = (post.getFavoriteCount());
            long viewCount = (post.getViewCount());
            Date createTime = post.getCreateTime();

            // 创建时间为空则按“现在”处理，避免NPE
            if (createTime == null) {
                createTime = new Date(nowMs);
            }

            double score = calcHotScoreWithTimeDecay(
                    likeCount, commentCount, favoriteCount, viewCount, createTime, nowMs
            );

            PostCandidate c = new PostCandidate();
            c.setPostId(post.getId());
            c.addSource(name());
            c.setRecallScore(score);
            candidates.add(c);
        }

        // 3) 按热度分倒序，截断limit
        candidates.sort((a, b) -> Double.compare(
                b.getRecallScore() == null ? 0D : b.getRecallScore(),
                a.getRecallScore() == null ? 0D : a.getRecallScore()
        ));

        return candidates.size() > limit ? candidates.subList(0, limit) : candidates;
    }

    @Override
    public String name() {
        return "hot";
    }

    /**
     * Hot(p)=a*like+b*comment+c*favorite+d*view
     */
    public static double calcHotScore(
            long likeCount,
            long commentCount,
            long favoriteCount,
            long viewCount
    ) {
        return likeCount * LIKE_FACTOR
                + commentCount * COMMENT_FACTOR
                + favoriteCount * FAVORITE_FACTOR
                + viewCount * VIEW_FACTOR;
    }

    /**
     * 带时间衰减的热门分
     * score = Hot(p) * exp(-lambda * deltaHours)
     */
    public static double calcHotScoreWithTimeDecay(
            long likeCount,
            long commentCount,
            long favoriteCount,
            long viewCount,
            Date createTime,
            long nowMs
    ) {
        double hotScore = calcHotScore(likeCount, commentCount, favoriteCount, viewCount);

        double deltaHours = (nowMs - createTime.getTime()) / 3600000.0D;
        if (deltaHours < 0) {
            deltaHours = 0; // 防止时钟误差
        }

        double timeDecayFactor = Math.exp(-TIME_DECAY * deltaHours);
        return hotScore * timeDecayFactor;
    }


}