package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.ruoyi.common.core.domain.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户画像 Mapper
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {

    /**
     * 根据用户ID查询画像（仅查未删除数据）
     *
     * @param userId 用户ID
     * @return 用户画像
     */
    @Select("SELECT * FROM tb_user_profile WHERE user_id = #{userId} LIMIT 1")
    UserProfile selectByUserId(Long userId);
}