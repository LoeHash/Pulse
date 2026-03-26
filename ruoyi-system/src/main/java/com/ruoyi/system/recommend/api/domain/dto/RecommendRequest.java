package com.ruoyi.system.recommend.api.domain.dto;

import lombok.Data;

@Data
public class RecommendRequest {
    /** 请求条数，默认20 */
    private Integer size = 20;
}