package com.ruoyi.common.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "计划每日课程详情VO")
public class PlanDayCourseVO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "课程序号")
    private Integer sortNo;

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "课程标题")
    private String courseTitle;

    @Schema(description = "课程封面")
    private String courseCoverUrl;

    @Schema(description = "课程时长（秒）")
    private Integer durationSec;

    @Schema(description = "课程难度")
    private Integer difficulty;

    @Schema(description = "消耗热量")
    private Integer calories;
}

