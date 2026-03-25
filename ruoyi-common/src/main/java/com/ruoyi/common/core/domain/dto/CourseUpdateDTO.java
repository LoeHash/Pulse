package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 管理端修改课程参数
 */
@Schema(description = "管理端修改课程参数")
@Data
public class CourseUpdateDTO {

    @Schema(description = "课程标题", example = "7分钟燃脂入门")
    @Size(max = 128, message = "课程标题长度不能超过128个字符")
    private String title;

    @Schema(description = "封面URL", example = "https://example.com/cover.png")
    @Size(max = 255, message = "封面URL长度不能超过255个字符")
    private String coverUrl;

    @Schema(description = "视频URL/外链", example = "https://example.com/video.mp4")
    @Size(max = 255, message = "视频URL长度不能超过255个字符")
    private String videoUrl;

    @Schema(description = "分类", example = "[\"徒手\",\"HIIT\"]")
    private List<String> category;

    @Schema(description = "目标标签", example = "[\"减脂\"]")
    private List<String> goalTags;

    @Schema(description = "难度（1入门 2初级 3中级 4高级）", example = "1")
    private Integer difficulty;

    @Schema(description = "课程时长（秒）", example = "420")
    private Integer durationSec;

    @Schema(description = "消耗热量估算（kcal）", example = "60")
    private Integer calories;

    @Schema(description = "课程简介", example = "适合新手的短时燃脂训练")
    @Size(max = 500, message = "课程简介长度不能超过500个字符")
    private String intro;

    @Schema(description = "状态（1上架 0下架）", example = "1")
    private Integer status;

    @Schema(description = "课程动作列表")
    private List<CourseCreateDTO.CourseActionItem> actions;
}
