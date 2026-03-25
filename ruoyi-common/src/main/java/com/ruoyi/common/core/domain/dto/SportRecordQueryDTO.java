package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "运动记录查询参数")
public class SportRecordQueryDTO {

    @Schema(description = "运动ID")
    private Long sportId;

    @Schema(description = "是否完成（1完成 0中断）")
    private Integer completed;

    @Schema(description = "开始时间区间-起")
    private Date startDate;

    @Schema(description = "开始时间区间-止")
    private Date endDate;
}

