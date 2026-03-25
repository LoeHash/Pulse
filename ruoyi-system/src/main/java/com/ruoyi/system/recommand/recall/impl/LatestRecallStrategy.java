package com.ruoyi.system.recommand.recall.impl;


import com.ruoyi.common.core.domain.ForumPost;
import com.ruoyi.system.mapper.ForumPostMapper;
import com.ruoyi.system.recommand.domain.PostCandidate;
import com.ruoyi.system.recommand.domain.UserPreferenceView;
import com.ruoyi.system.recommand.recall.RecallStrategy;
import static com.ruoyi.system.recommand.chain.impl.DefaultRecallChain.EXPOSED_FILTER_DAYS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 最新路 召回策略
 * 逻辑：按发布时间倒序召回 + Fresh分计算
 */
@Component
@RequiredArgsConstructor
public class LatestRecallStrategy implements RecallStrategy {

    private final ForumPostMapper postMapper;

    /** 时间衰减系数（可配置） */
    @Value("${recommend.recall.latest.lambda:0.03}")
    private static final double LAMBDA = 0.03D;

    private final com.ruoyi.system.recommand.service.IRecommendExposureService exposureService;

    /**
     * the data of the function can be stored to the redis
     * @param userId 用户ID
     * @param pref 用户偏好
     * @param limit 本路召回数量
     * @return
     */
    @Override
    public List<PostCandidate> recall(Long userId, UserPreferenceView pref, int limit) {
        // 获取已经曝光的帖子id
        Set<Long> exposedPostIds = exposureService.getExposedPostIds(userId, EXPOSED_FILTER_DAYS);
        if (exposedPostIds == null) {
            exposedPostIds = Collections.emptySet();
        }
        List<ForumPost> posts;
        if (!exposedPostIds.isEmpty()){
            posts = postMapper.selectWithOutExposed(Arrays.asList(exposedPostIds.toArray()), limit);
        }else{
            posts = postMapper.selectLatestForRecall(limit);
        }

        List<PostCandidate> result = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (ForumPost p : posts) {
            PostCandidate c = new PostCandidate();
            c.setPostId(p.getId());
            c.addSource(name());

            double freshScore = calcFreshScore(p.getCreateTime(), now);
            c.setRecallScore(freshScore);

            result.add(c);
        }
        return result;
    }

    @Override
    public String name() {
        return "latest";
    }

    /**
     * Fresh(p)=e^(-lambda * deltaHours)
     */
    public static double calcFreshScore(Date createTime, long nowMs) {
        if (createTime == null)
            return 0.0D;

        double deltaHours = (nowMs - createTime.getTime()) / 3600000.0D;

        if (deltaHours < 0)
            deltaHours = 0; // 防止时钟误差

        return Math.exp(-LAMBDA * deltaHours);
    }
}