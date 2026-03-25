package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "客户端计划查询参数")
public class PlanQueryDTO {

    @Schema(description = "计划标题（模糊）")
    private String title;

    @Schema(description = "难度")
    private Integer difficulty;

    @Schema(description = "标签关键字（适应人群/目标/部位）")
    private String tagKeyword;
}

