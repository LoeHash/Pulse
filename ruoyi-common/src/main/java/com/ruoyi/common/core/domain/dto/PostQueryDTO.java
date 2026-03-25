package com.ruoyi.common.core.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "帖子查询参数")
@Data
public class PostQueryDTO {

    @Schema(description="帖子标题关键字", example = "Spring")
    private String title;

    @Schema(description="标签列表", example = "[\"Java\",\"后端\"]")
    private List<String> tags;

}
