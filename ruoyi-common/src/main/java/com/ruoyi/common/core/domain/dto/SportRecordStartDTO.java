package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "开始运动参数")
public class SportRecordStartDTO {

    @NotNull(message = "运动ID不能为空")
    @Schema(description = "运动ID")
    private Long sportId;

    @Schema(description = "开始时间，不传默认当前时间")
    private Date startTime;
}

