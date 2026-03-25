package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * 开始训练参数
 */
@Schema(description = "开始训练参数")
@Data
public class WorkoutRecordStartDTO {

    @Schema(description = "课程ID", example = "1")
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @Schema(description = "开始时间，不传则使用当前时间")
    private Date startTime;

    @Schema(description = "备注", example = "晚间训练")
    @Size(max = 255, message = "备注长度不能超过255个字符")
    private String note;

    @Schema(description = "训练指标扩展JSON")
    @Size(max = 4096, message = "训练指标扩展长度不能超过4096个字符")
    private String metrics;
}

