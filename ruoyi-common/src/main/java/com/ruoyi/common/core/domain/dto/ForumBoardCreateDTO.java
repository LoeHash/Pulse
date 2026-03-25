package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "新增版块参数")
public class ForumBoardCreateDTO {

    @NotBlank(message = "版块名称不能为空")
    private String name;

    private String description;

    private String icon;

    @NotNull(message = "排序不能为空")
    private Integer sort;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private String remark;
}

