package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "更新训练计划DTO")
public class PlanUpdateDTO {
    
    @Schema(description = "计划标题")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "计划简介")
    private String intro;

    @Schema(description = "适应人群标签")
    private List<String> fitPeopleTags;

    @Schema(description = "目标标签")
    private List<String> goalTags;

    @Schema(description = "部位标签")
    private List<String> bodyPartTags;

    @Schema(description = "难度（1零基础 2初级 3进阶 4强化）")
    private Integer difficulty;

    @Schema(description = "状态（1上架 0下架）")
    private Integer status;

    @Schema(description = "计划天数详情（如果提供，将覆盖原有天数配置）")
    @Valid
    private List<PlanDayCreateDTO> days;
}

