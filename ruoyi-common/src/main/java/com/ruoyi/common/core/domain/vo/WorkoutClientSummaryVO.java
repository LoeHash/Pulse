package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户训练统计汇总VO
 */
@Schema(name = "WorkoutClientSummaryVO", description = "用户训练统计汇总")
@Data
public class WorkoutClientSummaryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "训练记录数")
    private Integer totalCount;

    @Schema(description = "总时长（秒）")
    private Integer totalDurationSec;

    @Schema(description = "总热量（kcal）")
    private Integer totalCalories;

    @Schema(description = "完成次数")
    private Integer completedCount;

    @Schema(description = "完课率（%）")
    private Double completionRate;
}

