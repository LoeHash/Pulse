package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理端课程分页查询参数
 */
@Schema(description = "管理端课程分页查询参数")
@Data
public class CoursePageQueryDTO {

    @Schema(description = "课程ID", example = "1")
    private Long id;

    @Schema(description = "课程标题（模糊）", example = "燃脂")
    private String title;

    @Schema(description = "难度", example = "1")
    private Integer difficulty;

    @Schema(description = "状态（1上架 0下架）", example = "1")
    private Integer status;

    @Schema(description = "分类关键字（模糊）", example = "HIIT")
    private String categoryKeyword;

    @Schema(description = "目标标签关键字（模糊）", example = "减脂")
    private String goalTagKeyword;
}

