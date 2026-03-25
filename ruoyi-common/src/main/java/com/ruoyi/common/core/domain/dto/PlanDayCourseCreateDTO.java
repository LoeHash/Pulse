package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "创建训练计划-天-课程DTO")
public class PlanDayCourseCreateDTO {

    @NotNull(message = "课程序号不能为空")
    @Schema(description = "当天课程序号（从1开始）")
    private Integer sortNo;

    @NotNull(message = "课程ID不能为空")
    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "备注")
    private String remark;
}

