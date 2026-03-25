package com.ruoyi.system.recommand.recall.impl;


import com.ruoyi.common.core.domain.ForumPost;
import com.ruoyi.system.mapper.ForumPostMapper;
import com.ruoyi.system.recommand.domain.PostCandidate;
import com.ruoyi.system.recommand.domain.UserPreferenceView;
import com.ruoyi.system.recommand.recall.RecallStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.system.recommand.chain.impl.DefaultRecallChain.EXPOSED_FILTER_DAYS;

@Component
@RequiredArgsConstructor
public class PreferenceRecallStrategy implements RecallStrategy {

    private final ForumPostMapper postMapper;
    private final com.ruoyi.system.recommand.service.IRecommendExposureService exposureService;

    private static final int TOP_TAG_N = 5;
    private static final int TOP_BOARD_N = 3;
    private static final double LAMBDA = 0.03D;

    @Override
    public List<PostCandidate> recall(Long userId, UserPreferenceView pref, int limit) {
        if (limit <= 0 || pref == null){
            return List.of();
        }

        Map<String, Double> tagPref = pref.getTagPref() == null ? Map.of() : pref.getTagPref();
        Map<String, Double> boardPref = pref.getBoardPref() == null ? Map.of() : pref.getBoardPref();

        List<String> topTags = topKeys(tagPref, TOP_TAG_N);
        List<Long> topBoards = topKeys(boardPref, TOP_BOARD_N).stream()
                .map(Long::valueOf).collect(Collectors.toList());

        // 获取已经曝光的帖子id
        Set<Long> exposedPostIds = exposureService.getExposedPostIds(userId, EXPOSED_FILTER_DAYS);

        // 防御
        if (exposedPostIds == null) {
            exposedPostIds = Collections.emptySet();
        }

        // 分两路取候选（各取2倍，给后续排序留空间）
        int fetchSize = Math.max(limit * 2, 100);

        List<ForumPost> byBoards;
        List<ForumPost> byTags;


        if (!exposedPostIds.isEmpty()){
            byBoards = topBoards.isEmpty() ? List.of()
                    : postMapper.selectRecentByBoardIdsWithOutExposed(Arrays.asList(exposedPostIds.toArray()), topBoards, fetchSize);


            byTags = topTags.isEmpty() ? List.of()
                    : postMapper.selectRecentByTagsWithOutExposed(Arrays.asList(exposedPostIds.toArray()), topTags, fetchSize);

        }else{

            byBoards = topBoards.isEmpty() ? List.of()
                    : postMapper.selectRecentByBoardIds(topBoards, fetchSize);
            byTags = topTags.isEmpty() ? List.of()
                    : postMapper.selectRecentByTags(topTags, fetchSize);
        }


        // 合并去重
        Map<Long, ForumPost> merged = new HashMap<>();
        for (ForumPost p : byBoards)
            merged.put(p.getId(), p);

        for (ForumPost p : byTags)
            merged.put(p.getId(), p);

        long now = System.currentTimeMillis();
        List<PostCandidate> candidates = new ArrayList<>();

        for (ForumPost p : merged.values()) {
            // 计算当前帖子的tags 和 用户画像的tags的相似度, 并打分
            double sTag = calcTagScore(p.getTags(), tagPref);
            double sBoard = calcBoardScore(p.getBoardId(), boardPref);
            double sFresh = calcFresh(p.getCreateTime(), now);

            //得到一个最终的分数
            double finalScore = 0.7D * sTag + 0.2D * sBoard + 0.1D * sFresh;

            PostCandidate c = new PostCandidate();
            c.setPostId(p.getId());
            c.addSource(name());
            c.setRecallScore(finalScore);
            candidates.add(c);
        }

        candidates.sort((a, b) -> Double.compare(b.getRecallScore(), a.getRecallScore()));
        return candidates.size() > limit ? candidates.subList(0, limit) : candidates;
    }

    @Override
    public String name() {
        return "preference";
    }

    /**
     * 前n个key
     * @param map
     * @param n
     * @return
     */
    private List<String> topKeys(Map<String, Double> map, int n) {
        return map.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calcTagScore(List<String> postTags, Map<String, Double> tagPref) {
        if (postTags == null || postTags.isEmpty() || tagPref.isEmpty()){
            return 0D;
        }

        double sum = 0D;

        for (String t : postTags) {
            sum += tagPref.getOrDefault(t, 0D);
        }

        return sum / (Math.sqrt(postTags.size()) + 1e-6);
    }

    private double calcBoardScore(Long boardId, Map<String, Double> boardPref) {
        if (boardId == null || boardPref.isEmpty()){
            return 0D;
        }
        return boardPref.getOrDefault(String.valueOf(boardId), 0D);
    }

    private double calcFresh(Date createTime, long nowMs) {
        if (createTime == null) return 0D;
        double h = (nowMs - createTime.getTime()) / 3600000.0D;
        if (h < 0) h = 0;
        return Math.exp(-LAMBDA * h);
    }
}