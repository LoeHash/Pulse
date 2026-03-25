package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 训练记录查询参数
 */
@Schema(description = "训练记录查询参数")
@Data
public class WorkoutRecordQueryDTO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "用户ID（管理端可用）")
    private Long userId;

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "是否完成（1完成 0中断）")
    private Integer completed;

    @Schema(description = "是否自动结束（1是 0否）")
    private Integer autoClosed;

    @Schema(description = "开始时间-起")
    private Date startTimeFrom;

    @Schema(description = "开始时间-止")
    private Date startTimeTo;
}

