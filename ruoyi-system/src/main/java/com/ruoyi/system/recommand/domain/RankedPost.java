package com.ruoyi.system.recommand.domain;

import lombok.Data;

@Data
public class RankedPost {
    private Long postId;
    private Double rankScore;
}