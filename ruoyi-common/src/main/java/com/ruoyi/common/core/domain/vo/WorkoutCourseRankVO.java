package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程训练排行VO
 */
@Schema(name = "WorkoutCourseRankVO", description = "课程训练排行")
@Data
public class WorkoutCourseRankVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "课程ID")
	private Long courseId;

	@Schema(description = "课程标题")
	private String courseTitle;

	@Schema(description = "训练记录数")
	private Integer recordCount;

	@Schema(description = "训练总时长（秒）")
	private Integer totalDurationSec;

	@Schema(description = "训练总热量（kcal）")
	private Integer totalCalories;
}

