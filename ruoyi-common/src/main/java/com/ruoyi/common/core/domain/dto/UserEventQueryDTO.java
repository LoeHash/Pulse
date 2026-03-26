package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户行为事件查询参数
 */
@Schema(description = "用户行为事件查询参数")
@Data
public class UserEventQueryDTO {

    @Schema(description = "事件ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "帖子ID")
    private Long postId;

    @Schema(description = "事件类型")
    private String eventType;

    @Schema(description = "场景")
    private String scene;

    @Schema(description = "事件时间-起")
    private Date eventTimeFrom;

    @Schema(description = "事件时间-止")
    private Date eventTimeTo;
}

