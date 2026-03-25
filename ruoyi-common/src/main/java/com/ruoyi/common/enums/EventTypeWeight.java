package com.ruoyi.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件类型与权重配置（推荐/画像打分使用）
 *
 * 说明：
 * 1) 正反馈：加分（click/like/comment/favorite...）
 * 2) 负反馈：减分（dislike/report）
 * 3) 时长类事件可配合 durationMs 二次计算（见 calcDurationScore）
 */
public enum EventTypeWeight {

    IMPRESSION("impression", 0.1D),
    IMPRESSION_DURATION("impression_duration", 0.0D), // 时长事件基础分设为0，交给时长函数计算
    CLICK("click", 1.0D),
    DETAIL_VIEW("detail_view", 1.2D),
    DETAIL_DURATION("detail_duration", 0.0D),         // 同上，走时长函数
    LIKE("like", 3.0D),
    COMMENT("comment", 5.0D),
    FAVORITE("favorite", 4.0D),
    SHARE("share", 4.5D),
    FOLLOW_AUTHOR("follow_author", 2.5D),
    DISLIKE("dislike", -6.0D),
    REPORT("report", -10.0D);

    private final String code;
    private final double weight;

    EventTypeWeight(String code, double weight) {
        this.code = code;
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public double getWeight() {
        return weight;
    }

    private static final Map<String, Double> WEIGHT_MAP = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(e -> WEIGHT_MAP.put(e.code, e.weight));
    }

    /** 是否是合法事件类型 */
    public static boolean isValid(String code) {
        return WEIGHT_MAP.containsKey(code);
    }

    /** 获取基础权重（非时长分） */
    public static double getBaseWeight(String code) {
        return WEIGHT_MAP.getOrDefault(code, 0.0D);
    }

    /**
     * 计算最终事件分
     * - 非时长事件：直接返回基础权重
     * - impression_duration / detail_duration：按 durationMs 分段计分
     */
    public static double score(String eventType, Long durationMs) {
        if (!isValid(eventType)) {
            return 0.0D;
        }

        if ("impression_duration".equals(eventType)) {
            return calcImpressionDurationScore(durationMs);
        }

        if ("detail_duration".equals(eventType)) {
            return calcDetailDurationScore(durationMs);
        }

        return getBaseWeight(eventType);
    }

    /** 列表曝光时长计分 */
    public static double calcImpressionDurationScore(Long durationMs) {
        long d = safeDuration(durationMs);
        if (d < 1000) return 0.0D;
        if (d < 3000) return 0.5D;
        if (d < 8000) return 1.0D;
        return 1.5D;
    }

    /** 详情页停留时长计分 */
    public static double calcDetailDurationScore(Long durationMs) {
        long d = safeDuration(durationMs);
        if (d < 3000) return 0.5D;
        if (d < 10000) return 1.2D;
        if (d < 30000) return 2.0D;
        return 2.5D;
    }

    /** 时长清洗：小于0归0，超过10分钟截断 */
    private static long safeDuration(Long durationMs) {
        if (durationMs == null || durationMs < 0) return 0L;
        return Math.min(durationMs, 10 * 60 * 1000L);
    }
}