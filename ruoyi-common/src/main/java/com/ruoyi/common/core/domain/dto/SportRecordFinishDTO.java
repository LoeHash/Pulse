package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "结束运动参数")
public class SportRecordFinishDTO {

    @Schema(description = "结束时间，不传默认当前时间")
    private Date endTime;

    @Schema(description = "运动时长（秒），不传则按开始结束时间计算")
    private Integer durationSec;

    @Schema(description = "平均心率")
    private Integer avgHr;

    @Schema(description = "是否完成（1完成 0中断），默认1")
    private Integer completed;

    @Schema(description = "结束原因（manual/timeout/app_close等）")
    private String closeReason;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "扩展指标JSON")
    private String metrics;
}

