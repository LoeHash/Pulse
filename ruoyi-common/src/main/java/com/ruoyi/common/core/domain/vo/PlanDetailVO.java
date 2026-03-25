package com.ruoyi.common.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "计划详情VO")
public class PlanDetailVO {

    @Schema(description = "计划ID")
    private Long id;

    @Schema(description = "计划标题")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "计划简介")
    private String intro;

    @Schema(description = "计划天数")
    private Integer days;

    @Schema(description = "适应人群标签")
    private List<String> fitPeopleTags;

    @Schema(description = "目标标签")
    private List<String> goalTags;

    @Schema(description = "部位标签")
    private List<String> bodyPartTags;

    @Schema(description = "难度")
    private Integer difficulty;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "计划每日详情")
    private List<PlanDayVO> planDays;
}

