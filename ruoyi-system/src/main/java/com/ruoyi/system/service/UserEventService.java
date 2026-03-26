package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.UserEvent;
import com.ruoyi.common.core.domain.dto.UserEventQueryDTO;

import java.util.List;

/**
 * 用户行为事件服务接口
 */
public interface UserEventService extends IService<UserEvent> {

    /**
     * 管理端分页/列表查询事件
     */
    List<UserEvent> selectUserEventListForAdmin(UserEventQueryDTO queryDTO);

    /**
     * 管理端查询事件详情
     */
    UserEvent getUserEventByIdForAdmin(Long id);

    /**
     * 管理端新增事件
     */
    int createUserEventByAdmin(UserEvent userEvent);

    /**
     * 管理端修改事件
     */
    int updateUserEventByAdmin(Long id, UserEvent userEvent);

    /**
     * 管理端删除事件
     */
    int deleteUserEventByAdmin(Long id);

    /**
     * 管理端批量删除事件
     */
    int deleteUserEventByIds(List<Long> ids);
}

