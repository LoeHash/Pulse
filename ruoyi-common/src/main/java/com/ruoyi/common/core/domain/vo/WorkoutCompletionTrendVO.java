package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 训练完课趋势VO
 */
@Schema(name = "WorkoutCompletionTrendVO", description = "训练完课趋势")
@Data
public class WorkoutCompletionTrendVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "统计日期（yyyy-MM-dd）")
	private String statDate;

	@Schema(description = "总训练记录数")
	private Integer totalCount;

	@Schema(description = "完成记录数")
	private Integer completedCount;

	@Schema(description = "中断记录数")
	private Integer interruptedCount;

	@Schema(description = "完课率（%）")
	private Double completionRate;
}

