package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.UserAdminUpdateDTO;
import com.ruoyi.common.core.domain.dto.UserCreateDTO;
import com.ruoyi.common.core.domain.dto.UserUpdateDTO;

import java.util.List;

public interface UserService extends IService<User> {



    public User getUserByUsername(String username);


    int updateUserSafety(UserUpdateDTO uud);

    List<User> selectUserList(User user);

    Long createUserByAdmin(UserCreateDTO dto);

    int updateUserByAdmin(Long id, UserAdminUpdateDTO dto);

    int deleteUserByAdmin(Long id);

    User getUserDetailByAdmin(Long id);

}
