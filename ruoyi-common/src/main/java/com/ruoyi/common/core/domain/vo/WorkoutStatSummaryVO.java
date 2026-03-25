package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 训练记录汇总统计VO
 */
@Schema(name = "WorkoutStatSummaryVO", description = "训练记录汇总统计")
@Data
public class WorkoutStatSummaryVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "训练总记录数")
	private Integer totalRecords;

	@Schema(description = "总训练时长（秒）")
	private Integer totalDurationSec;

	@Schema(description = "总热量消耗（kcal）")
	private Integer totalCalories;

	@Schema(description = "完成记录数")
	private Integer completedCount;

	@Schema(description = "中断记录数")
	private Integer interruptedCount;

	@Schema(description = "自动结束记录数")
	private Integer autoClosedCount;

	@Schema(description = "完课率（%）")
	private Double completionRate;

	@Schema(description = "平均每次训练时长（秒）")
	private Integer avgDurationSec;
}

