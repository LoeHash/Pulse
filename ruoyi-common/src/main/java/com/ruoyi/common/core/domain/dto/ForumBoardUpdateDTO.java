package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新版块参数")
public class ForumBoardUpdateDTO {

    private String name;

    private String description;

    private String icon;

    private Integer sort;

    private Integer status;

    private String remark;
}

