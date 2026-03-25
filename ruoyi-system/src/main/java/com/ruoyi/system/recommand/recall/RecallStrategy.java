package com.ruoyi.system.recommand.recall;

import com.ruoyi.system.recommand.domain.PostCandidate;
import com.ruoyi.system.recommand.domain.UserPreferenceView;

import java.util.List;

/**
 * 单路召回策略接口
 */
public interface RecallStrategy {

    /**
     * 召回
     * @param userId 用户ID
     * @param pref 用户偏好
     * @param limit 本路召回数量
     * @return 候选列表
     */
    List<PostCandidate> recall(Long userId, UserPreferenceView pref, int limit);

    /**
     * 策略名称：latest/hot/preference
     */
    String name();
}