package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建训练计划-天DTO")
public class PlanDayCreateDTO {

    @NotNull(message = "天数序号不能为空")
    @Schema(description = "第几天（从1开始）")
    private Integer dayNo;

    @Schema(description = "当天标题")
    private String title;

    @Schema(description = "当天提示")
    private String tips;

    @Schema(description = "课程列表")
    @Valid
    private List<PlanDayCourseCreateDTO> courses;
}

