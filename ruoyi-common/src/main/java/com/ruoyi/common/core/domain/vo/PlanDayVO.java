package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "计划每日安排VO")
public class PlanDayVO {

    @Schema(description = "计划天ID")
    private Long id;

    @Schema(description = "第几天（从1开始）")
    private Integer dayNo;

    @Schema(description = "当天标题")
    private String title;

    @Schema(description = "当天提示")
    private String tips;

    @Schema(description = "当天课程列表")
    private List<PlanDayCourseVO> courses;
}

