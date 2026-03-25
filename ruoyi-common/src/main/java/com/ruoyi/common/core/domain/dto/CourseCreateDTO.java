package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 管理端新增课程参数
 */
@Schema(description = "管理端新增课程参数")
@Data
public class CourseCreateDTO {

    @Schema(description = "课程标题", example = "7分钟燃脂入门")
    @NotBlank(message = "课程标题不能为空")
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
    @NotNull(message = "难度不能为空")
    private Integer difficulty;

    @Schema(description = "课程时长（秒）", example = "420")
    @NotNull(message = "课程时长不能为空")
    private Integer durationSec;

    @Schema(description = "消耗热量估算（kcal）", example = "60")
    private Integer calories;

    @Schema(description = "课程简介", example = "适合新手的短时燃脂训练")
    @Size(max = 500, message = "课程简介长度不能超过500个字符")
    private String intro;

    @Schema(description = "状态（1上架 0下架）", example = "1")
    private Integer status;

    @Schema(description = "课程动作列表")
    private List<CourseActionItem> actions;

    @Data
    @Schema(description = "课程动作项")
    public static class CourseActionItem {
        @Schema(description = "动作模板ID")
        @NotNull(message = "动作ID不能为空")
        private Long actionId;

        @Schema(description = "排序号")
        @NotNull(message = "排序号不能为空")
        private Integer sortNo;

        @Schema(description = "目标值（次数或秒数）")
        private Integer targetValue;

        @Schema(description = "提示覆盖")
        private String tipsOverride;

        @Schema(description = "指标扩展")
        private String metricsOverride;
    }
}
