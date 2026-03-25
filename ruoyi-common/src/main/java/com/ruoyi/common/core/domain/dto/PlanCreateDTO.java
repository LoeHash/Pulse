package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建训练计划DTO")
public class PlanCreateDTO {
    
    @NotBlank(message = "计划标题不能为空")
    @Schema(description = "计划标题")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "计划简介")
    private String intro;

    @Schema(description = "适应人群标签", example = "[\"宝妈\",\"大体重\"]")
    private List<String> fitPeopleTags;

    @Schema(description = "目标标签", example = "[\"增肌\",\"舒缓放松\"]")
    private List<String> goalTags;

    @Schema(description = "部位标签", example = "[\"胸\",\"肩\",\"腿\"]")
    private List<String> bodyPartTags;

    @NotNull(message = "难度不能为空")
    @Schema(description = "难度（1零基础 2初级 3进阶 4强化）")
    private Integer difficulty;

    @Schema(description = "计划天数详情")
    @Valid
    private List<PlanDayCreateDTO> days;
}

