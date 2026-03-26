package com.ruoyi.system.recommend.domain;

import lombok.Data;

@Data
public class RankedPost {
    private Long postId;
    private Double rankScore;
}