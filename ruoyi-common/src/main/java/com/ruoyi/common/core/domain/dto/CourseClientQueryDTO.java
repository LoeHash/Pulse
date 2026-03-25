package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 客户端课程查询参数
 */
@Schema(description = "客户端课程查询参数")
@Data
public class CourseClientQueryDTO {

    @Schema(description = "课程标题（模糊）", example = "燃脂")
    private String title;

    @Schema(description = "难度", example = "2")
    private Integer difficulty;

    @Schema(description = "分类关键字（模糊）", example = "拉伸")
    private String categoryKeyword;

    @Schema(description = "目标标签关键字（模糊）", example = "塑形")
    private String goalTagKeyword;
}

