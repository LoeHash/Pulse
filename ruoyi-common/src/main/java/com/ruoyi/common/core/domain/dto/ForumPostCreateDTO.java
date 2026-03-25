package com.ruoyi.common.core.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ForumPostCreateDTO {

    @NotNull(message = "版块ID不能为空")
    private Long boardId;

    @NotBlank(message = "帖子标题不能为空")
    private String title;

    @NotBlank(message = "帖子内容不能为空")
    private String content;

    private List<String> tags;

    private List<String> coverImage;

    /** 0-草稿 1-发布 */
    private Integer status;
}

