package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.UserProfile;
import com.ruoyi.common.core.domain.dto.UserProfileQueryDTO;

import java.util.List;

/**
 * 用户画像服务接口
 */
public interface UserProfileService extends IService<UserProfile> {

    /**
     * 管理端分页/列表查询用户画像
     */
    List<UserProfile> selectUserProfileListForAdmin(UserProfileQueryDTO queryDTO);

    /**
     * 管理端查询画像详情
     */
    UserProfile getUserProfileByIdForAdmin(Long id);

    /**
     * 管理端新增画像
     */
    int createUserProfileByAdmin(UserProfile profile);

    /**
     * 管理端修改画像
     */
    int updateUserProfileByAdmin(Long id, UserProfile profile);

    /**
     * 管理端删除画像
     */
    int deleteUserProfileByAdmin(Long id);
}

