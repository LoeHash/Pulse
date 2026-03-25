package com.ruoyi.system.recommand.rank;



import com.ruoyi.system.recommand.domain.RankedPost;

import java.util.List;

/**
 * 排序服务：对召回候选做排序，输出最终推荐结果
 */
public interface RankService {
    List<RankedPost> rank(Long userId, int size);
}