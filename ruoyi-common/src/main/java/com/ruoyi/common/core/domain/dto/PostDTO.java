package com.ruoyi.common.core.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "新建帖子参数")
@Data
public class PostDTO {

    @Schema(description="帖子标题", required = true, example = "Spring Security 问题求助")
    @NotBlank(message = "帖子标题不能为空")
    private String title;

    @Schema(description="帖子内容（富文本）", required = true)
    @NotBlank(message = "帖子内容不能为空")
    private String content;

    @Schema(description="版块ID", required = true, example = "3")
    @NotNull(message = "版块ID不能为空")
    private Long boardId;

    @Schema(description="帖子标签", example = "[\"Spring\",\"安全\"]")
    private List<String> tags;
}
