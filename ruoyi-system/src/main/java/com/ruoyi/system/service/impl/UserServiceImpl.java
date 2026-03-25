package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.SystemConstant;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.UserAdminUpdateDTO;
import com.ruoyi.common.core.domain.dto.UserCreateDTO;
import com.ruoyi.common.core.domain.dto.UserUpdateDTO;
import com.ruoyi.common.utils.Sha256Util;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
                            implements UserService {


    @Override
    public User getUserByUsername(String username) {
        return this.baseMapper
                .selectOne(
                        new LambdaQueryWrapper<User>()
                                .eq(User::getUsername, username)
                );
    }

    @Override
    public int updateUserSafety(UserUpdateDTO uud) {
        User currentUser = UserHolder.getUser();

        User user = new User();
        user.setId(currentUser.getId());
        BeanUtils.copyProperties(uud, user);

        return this.baseMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUserByAdmin(UserCreateDTO dto) {
        User existUser = getUserByUsername(dto.getUsername());
        if (existUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user, "password");
        user.setPassword(Sha256Util.hash(dto.getPassword()));

        if (StringUtils.isBlank(user.getNickname())) {
            user.setNickname(SystemConstant.USER_PREFIX + dto.getUsername());
        }
        if (user.getRoleId() == null) {
            user.setRoleId(SystemConstant.USER_ROLE_NORMAL);
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getIsDeleted() == null) {
            user.setIsDeleted(0);
        }
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        this.baseMapper.insert(user);
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserByAdmin(Long id, UserAdminUpdateDTO dto) {
        User dbUser = this.baseMapper.selectById(id);
        if (dbUser == null || Integer.valueOf(1).equals(dbUser.getIsDeleted())) {
            throw new IllegalArgumentException("用户不存在");
        }

        if (StringUtils.isNotBlank(dto.getUsername()) && !dto.getUsername().equals(dbUser.getUsername())) {
            User existUser = getUserByUsername(dto.getUsername());
            if (existUser != null && !existUser.getId().equals(id)) {
                throw new IllegalArgumentException("用户名已存在");
            }
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user, "password");
        user.setId(id);
        if (StringUtils.isNotBlank(dto.getPassword())) {
            user.setPassword(Sha256Util.hash(dto.getPassword()));
        }
        user.setUpdateTime(new Date());

        return this.baseMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByAdmin(Long id) {
        User dbUser = this.baseMapper.selectById(id);
        if (dbUser == null || Integer.valueOf(1).equals(dbUser.getIsDeleted())) {
            return 0;
        }

        return this.baseMapper.deleteById(id);
    }

    @Override
    public User getUserDetailByAdmin(Long id) {
        User user = this.baseMapper.selectById(id);
        if (user == null || Integer.valueOf(1).equals(user.getIsDeleted())) {
            return null;
        }
        return user;
    }

    @Override
    public List<User> selectUserList(User searchUser) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        if (searchUser == null) {
            searchUser = new User();
        }

        // 基本字段精确匹配
        if (searchUser.getId() != null) {
            queryWrapper.eq(User::getId, searchUser.getId());
        }

        if (searchUser.getRoleId() != null) {
            queryWrapper.eq(User::getRoleId, searchUser.getRoleId());
        }

        if (searchUser.getStatus() != null) {
            queryWrapper.eq(User::getStatus, searchUser.getStatus());
        }

        if (searchUser.getIsDeleted() != null) {
            queryWrapper.eq(User::getIsDeleted, searchUser.getIsDeleted());
        } else {
            queryWrapper.eq(User::getIsDeleted, 0);
        }

        if (searchUser.getGender() != null) {
            queryWrapper.eq(User::getGender, searchUser.getGender());
        }

        // 字符串模糊匹配
        if (StringUtils.isNotBlank(searchUser.getUsername())) {
            queryWrapper.like(User::getUsername, searchUser.getUsername());
        }

        if (StringUtils.isNotBlank(searchUser.getNickname())) {
            queryWrapper.like(User::getNickname, searchUser.getNickname());
        }

        if (StringUtils.isNotBlank(searchUser.getSignature())) {
            queryWrapper.like(User::getSignature, searchUser.getSignature());
        }

        // 时间范围查询
        if (searchUser.getCreateTime() != null) {
            queryWrapper.ge(User::getCreateTime, searchUser.getCreateTime());
            // 如果需要结束时间，可以另外传入一个参数
        }

        if (searchUser.getUpdateTime() != null) {
            queryWrapper.ge(User::getUpdateTime, searchUser.getUpdateTime());
        }

        // 排序
        queryWrapper.orderByDesc(User::getCreateTime);

        return this.list(queryWrapper);
    }
}
