package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户训练课程维度统计VO
 */
@Schema(name = "WorkoutClientCourseStatVO", description = "用户训练课程维度统计")
@Data
public class WorkoutClientCourseStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "课程标题")
    private String courseTitle;

    @Schema(description = "训练次数")
    private Integer recordCount;

    @Schema(description = "总时长（秒）")
    private Integer totalDurationSec;

    @Schema(description = "总热量（kcal）")
    private Integer totalCalories;
}

