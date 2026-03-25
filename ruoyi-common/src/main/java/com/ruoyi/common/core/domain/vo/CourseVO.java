package com.ruoyi.common.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.CourseAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程展示VO
 */
@Schema(name = "CourseVO", description = "课程展示对象")
@Data
public class CourseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "课程ID")
    private Long id;

    @Schema(description = "课程标题")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "视频URL/外链")
    private String videoUrl;

    @Schema(description = "分类")
    private List<String> category;

    @Schema(description = "目标标签")
    private List<String> goalTags;

    @Schema(description = "难度（1入门 2初级 3中级 4高级）")
    private Integer difficulty;

    @Schema(description = "课程时长（秒）")
    private Integer durationSec;

    @Schema(description = "消耗热量估算（kcal）")
    private Integer calories;

    @Schema(description = "课程简介")
    private String intro;

    @Schema(description = "状态（1上架 0下架）")
    private Integer status;

    @Schema(description = "课程动作列表")
    private List<CourseAction> courseActions;
}
