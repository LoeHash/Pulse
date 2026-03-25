package com.ruoyi.common.core.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ForumPostQueryDTO {

    private Long boardId;

    private Long userId;

    private String title;

    private List<String> tags;

    /** 0-草稿 1-发布 2-下架 */
    private Integer status;

    /** 排序字段：time 或 hot */
    private String sortBy;
}

