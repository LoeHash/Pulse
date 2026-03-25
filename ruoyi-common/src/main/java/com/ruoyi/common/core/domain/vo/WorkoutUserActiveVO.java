package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户训练活跃排行VO
 */
@Schema(name = "WorkoutUserActiveVO", description = "用户训练活跃排行")
@Data
public class WorkoutUserActiveVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "账号")
	private String username;

	@Schema(description = "昵称")
	private String nickname;

	@Schema(description = "训练记录数")
	private Integer recordCount;

	@Schema(description = "训练总时长（秒）")
	private Integer totalDurationSec;

	@Schema(description = "训练总热量（kcal）")
	private Integer totalCalories;
}

