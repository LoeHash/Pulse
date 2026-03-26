package com.ruoyi.system.recommend.api.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendItemVO {
    private Long postId;
    private Double score;
}