package com.ruoyi.common.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 单条用户行为事件请求对象
 */
@Data
@Schema(description = "单条用户事件请求")
public class UserEventReq {

    /** 事件类型（如 click / like / impression_duration） */
    @Schema(description = "事件类型", example = "impression_duration")
    private String eventType;

    /** 帖子ID */
    @Schema(description = "帖子ID", example = "1001")
    private Long postId;

    /** 事件场景（feed_list / post_detail） */
    @Schema(description = "场景", example = "feed_list")
    private String scene;

    /** 分页页码 */
    @Schema(description = "页码", example = "1")
    private Integer pageNo;

    /** 开始时间戳（毫秒） */
    @Schema(description = "开始时间戳(ms)", example = "1764000000000")
    private Long startTs;

    /** 结束时间戳（毫秒） */
    @Schema(description = "结束时间戳(ms)", example = "1764000003500")
    private Long endTs;

    /** 停留时长（毫秒） */
    @Schema(description = "停留时长(ms)", example = "3500")
    private Long durationMs;
}