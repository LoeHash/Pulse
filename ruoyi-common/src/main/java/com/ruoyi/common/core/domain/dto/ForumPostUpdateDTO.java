package com.ruoyi.common.core.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ForumPostUpdateDTO {

    private Long boardId;

    private String title;

    private String content;

    private List<String> tags;

    private List<String> coverImage;

    /** 0-草稿 1-发布 2-下架 */
    private Integer status;
}

