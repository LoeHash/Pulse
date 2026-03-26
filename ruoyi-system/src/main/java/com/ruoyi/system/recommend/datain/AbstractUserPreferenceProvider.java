package com.ruoyi.system.recommend.datain;

import com.ruoyi.system.recommend.domain.UserPreferenceView;

public interface AbstractUserPreferenceProvider {

    /**
     * 获取用户“可用于推荐”的最终偏好
     *
     * @param userId 用户ID
     * @return 统一偏好视图（永不返回null，内部兜底空画像）
     */
    UserPreferenceView getPreference(Long userId);

}
