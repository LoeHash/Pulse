package com.ruoyi.system.recommend.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 候选帖子对象（Recall层输出给Rank层）
 */
@Data
public class PostCandidate {

    /** 帖子ID */
    private Long postId;

    /** 召回来源（latest/hot/preference） */
    private Set<String> recallSources = new HashSet<>();

    /** 粗排分（Recall阶段可选） */
    private Double recallScore = 0.0D;

    public void addSource(String source) {
        this.recallSources.add(source);
    }
}