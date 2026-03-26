package com.ruoyi.system.config.domain.ins;

import lombok.Data;

@Data
public class AlgoConfig {
    // 喜欢因子
    private double likeFactor;

    // 评论因子
    private double commentFactor;

    // 收藏因子
    private double favoriteFactor;

    // 浏览因子
    private double viewFactor;

    // 时间衰减因子
    private double timeDecay;

    // 多路命中奖励因子
    private double roadHitFactor;

    // 曝光去重窗口（天）
    private int exposedFilterDays;

    // 最新召回路融合权重因子
    private double latestWeight;

    // 热门召回路融合权重因子
    private double hotWeight;

    // 偏好召回路融合权重因子
    private double preferenceWeight;

    // 偏好tags前N条
    private int preferenceTopN;

    // 偏好板块前N条
    private int preferenceBoardTopN;

}
