package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.UserEvent;
import com.ruoyi.common.core.domain.dto.UserEventQueryDTO;
import com.ruoyi.system.mapper.UserEventMapper;
import com.ruoyi.system.service.UserEventService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户行为事件服务实现
 */
@Service
public class UserEventServiceImpl extends ServiceImpl<UserEventMapper, UserEvent>
        implements UserEventService {

    @Override
    public List<UserEvent> selectUserEventListForAdmin(UserEventQueryDTO queryDTO) {
        LambdaQueryWrapper<UserEvent> wrapper = buildQueryWrapper(queryDTO);
        wrapper.orderByDesc(UserEvent::getEventTime)
                .orderByDesc(UserEvent::getId);
        return this.list(wrapper);
    }

    @Override
    public UserEvent getUserEventByIdForAdmin(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createUserEventByAdmin(UserEvent userEvent) {
        if (userEvent.getEventTime() == null) {
            userEvent.setEventTime(new Date());
        }
        userEvent.setCreateTime(new Date());
        userEvent.setUpdateTime(new Date());
        return this.baseMapper.insert(userEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserEventByAdmin(Long id, UserEvent userEvent) {
        UserEvent db = this.getById(id);
        if (db == null) {
            throw new IllegalArgumentException("用户事件不存在");
        }

        UserEvent update = new UserEvent();
        BeanUtils.copyProperties(userEvent, update);
        update.setId(id);
        update.setUpdateTime(new Date());
        return this.baseMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserEventByAdmin(Long id) {
        return this.baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserEventByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return this.baseMapper.deleteBatchIds(ids);
    }

    private LambdaQueryWrapper<UserEvent> buildQueryWrapper(UserEventQueryDTO queryDTO) {
        LambdaQueryWrapper<UserEvent> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO == null) {
            return wrapper;
        }

        wrapper.eq(queryDTO.getId() != null, UserEvent::getId, queryDTO.getId())
                .eq(queryDTO.getUserId() != null, UserEvent::getUserId, queryDTO.getUserId())
                .eq(queryDTO.getPostId() != null, UserEvent::getPostId, queryDTO.getPostId())
                .eq(StringUtils.isNotBlank(queryDTO.getEventType()), UserEvent::getEventType, queryDTO.getEventType())
                .eq(StringUtils.isNotBlank(queryDTO.getScene()), UserEvent::getScene, queryDTO.getScene())
                .ge(queryDTO.getEventTimeFrom() != null, UserEvent::getEventTime, queryDTO.getEventTimeFrom())
                .le(queryDTO.getEventTimeTo() != null, UserEvent::getEventTime, queryDTO.getEventTimeTo());
        return wrapper;
    }
}

