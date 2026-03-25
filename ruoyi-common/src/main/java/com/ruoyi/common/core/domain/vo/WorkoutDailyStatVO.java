package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 训练记录按日统计VO
 */
@Schema(name = "WorkoutDailyStatVO", description = "训练记录按日统计")
@Data
public class WorkoutDailyStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "统计日期（yyyy-MM-dd）")
    private String statDate;

    @Schema(description = "当日训练总时长（秒）")
    private Integer totalDurationSec;

    @Schema(description = "当日训练总热量（kcal）")
    private Integer totalCalories;

    @Schema(description = "当日记录数")
    private Integer recordCount;
}

