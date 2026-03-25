package com.ruoyi.system.recommand.rank.impl;

import com.ruoyi.system.recommand.chain.RecallChain;
import com.ruoyi.system.recommand.chain.RecallRequest;
import com.ruoyi.system.recommand.chain.RecallResult;
import com.ruoyi.system.recommand.datain.AbstractUserPreferenceProvider;
import com.ruoyi.system.recommand.domain.PostCandidate;
import com.ruoyi.system.recommand.domain.RankedPost;
import com.ruoyi.system.recommand.domain.UserPreferenceView;
import com.ruoyi.system.recommand.rank.RankService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 最小可用排序实现：
 * 1) 调召回链拿候选
 * 2) 先以 recallScore 作为 rankScore
 * 3) 排序截断
 */
@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

    //默认链
    private final RecallChain recallChain;

    private final AbstractUserPreferenceProvider userPreferenceProvider;

    @Override
    public List<RankedPost> rank(Long userId, int size) {
        if (userId == null || size <= 0) {
            return List.of();
        }

        // 1) 读取用户画像
        UserPreferenceView pref = userPreferenceProvider.getPreference(userId);

        // 2) 组装召回请求
        RecallRequest req = new RecallRequest();
        req.setUserId(userId);
        req.setPreference(pref);
        req.setPerChannelLimit(Math.min(size * 2, 60)); // 每路多取一点
        req.setTotalLimit(Math.min(size * 6, 180));      // 总候选池加大

        // 3) 执行召回链
        RecallResult recallResult = recallChain.recall(req);
        List<PostCandidate> candidates = recallResult == null ? List.of() : recallResult.getCandidates();
        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        // 4) 排序层（当前版本：rankScore = recallScore）
        List<RankedPost> ranked = new ArrayList<>(candidates.size());
        for (PostCandidate c : candidates) {
            RankedPost rp = new RankedPost();
            rp.setPostId(c.getPostId());
            rp.setRankScore(c.getRecallScore() == null ? 0D : c.getRecallScore());
            ranked.add(rp);
        }

        ranked.sort(Comparator.comparing(RankedPost::getRankScore).reversed());

        return ranked.size() > size ? ranked.subList(0, size) : ranked;
    }
}