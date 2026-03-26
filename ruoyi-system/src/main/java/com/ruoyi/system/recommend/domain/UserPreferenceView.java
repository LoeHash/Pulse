package com.ruoyi.system.recommend.domain;

import lombok.Data;

import java.util.Map;

/**
 * 推荐侧统一用户偏好视图（上层唯一感知对象）
 * 无论底层是短期/长期/多表聚合，这个结构保持稳定
 */
@Data
public class UserPreferenceView {

    /** 用户ID */
    private Long userId;

    /** 最终标签偏好（已融合） */
    private Map<String, Double> tagPref;

    /** 最终版块偏好（已融合） */
    private Map<String, Double> boardPref;

    /** 最近活跃时间戳（毫秒，可选） */
    private Long lastActiveTs;
}