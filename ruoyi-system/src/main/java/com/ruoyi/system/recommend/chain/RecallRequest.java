package com.ruoyi.system.recommend.chain;

import com.ruoyi.system.recommend.domain.UserPreferenceView;
import lombok.Data;

@Data
public class RecallRequest {
    private Long userId;
    private UserPreferenceView preference;
    /** 每路召回数量 */
    private int perChannelLimit = 100;
    /** 总截断数量（召回链输出） */
    private int totalLimit = 300;
}