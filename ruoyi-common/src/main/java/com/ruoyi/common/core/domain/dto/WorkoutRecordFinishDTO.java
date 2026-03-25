package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * 结束训练参数
 */
@Schema(description = "结束训练参数")
@Data
public class WorkoutRecordFinishDTO {

    @Schema(description = "结束时间，不传则使用当前时间")
    private Date endTime;

    @Schema(description = "手动上报热量，空则由服务端计算", example = "120")
    private Integer calories;

    @Schema(description = "是否完成（1完成 0中断）", example = "1")
    private Integer completed;

    @Schema(description = "备注")
    @Size(max = 255, message = "备注长度不能超过255个字符")
    private String note;

    @Schema(description = "训练指标扩展JSON")
    @Size(max = 4096, message = "训练指标扩展长度不能超过4096个字符")
    private String metrics;
}

