package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.UserProfile;
import com.ruoyi.common.core.domain.dto.UserProfileQueryDTO;
import com.ruoyi.system.mapper.UserProfileMapper;
import com.ruoyi.system.service.UserProfileService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户画像服务实现
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile>
        implements UserProfileService {

    @Override
    public List<UserProfile> selectUserProfileListForAdmin(UserProfileQueryDTO queryDTO) {
        LambdaQueryWrapper<UserProfile> wrapper = buildQueryWrapper(queryDTO);
        wrapper.orderByDesc(UserProfile::getUpdateTime)
                .orderByDesc(UserProfile::getId);
        return this.list(wrapper);
    }

    @Override
    public UserProfile getUserProfileByIdForAdmin(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createUserProfileByAdmin(UserProfile profile) {
        if (profile.getProfileVersion() == null) {
            profile.setProfileVersion(1);
        }
        profile.setCreateTime(new Date());
        profile.setUpdateTime(new Date());
        return this.baseMapper.insert(profile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserProfileByAdmin(Long id, UserProfile profile) {
        UserProfile db = this.getById(id);
        if (db == null) {
            throw new IllegalArgumentException("用户画像不存在");
        }

        UserProfile update = new UserProfile();
        BeanUtils.copyProperties(profile, update);
        update.setId(id);
        update.setUpdateTime(new Date());
        return this.baseMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserProfileByAdmin(Long id) {
        return this.baseMapper.deleteById(id);
    }

    private LambdaQueryWrapper<UserProfile> buildQueryWrapper(UserProfileQueryDTO queryDTO) {
        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO == null) {
            return wrapper;
        }

        wrapper.eq(queryDTO.getId() != null, UserProfile::getId, queryDTO.getId())
                .eq(queryDTO.getUserId() != null, UserProfile::getUserId, queryDTO.getUserId())
                .eq(queryDTO.getProfileVersion() != null, UserProfile::getProfileVersion, queryDTO.getProfileVersion())
                .ge(queryDTO.getLastActiveTimeFrom() != null, UserProfile::getLastActiveTime, queryDTO.getLastActiveTimeFrom())
                .le(queryDTO.getLastActiveTimeTo() != null, UserProfile::getLastActiveTime, queryDTO.getLastActiveTimeTo());
        return wrapper;
    }
}

