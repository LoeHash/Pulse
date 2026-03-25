package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.NoticeReceiver;
import com.ruoyi.system.mapper.NoticeReceiverMapper;
import com.ruoyi.system.service.NoticeReceiverService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知接收记录 Service 实现
 *
 * @author loe
 * @date 2026-02-23
 */
@Service
public class NoticeReceiverServiceImpl
        extends ServiceImpl<NoticeReceiverMapper, NoticeReceiver>
        implements NoticeReceiverService
{

    @Override
    public boolean markAsRead(Long noticeId, Long userId)
    {
        LambdaUpdateWrapper<NoticeReceiver> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(NoticeReceiver::getNoticeId, noticeId)
                .eq(NoticeReceiver::getUserId, userId)
                .set(NoticeReceiver::getIsRead, 1)
                .set(NoticeReceiver::getReadTime, LocalDateTime.now());

        return this.update(wrapper);
    }

    @Override
    public List<NoticeReceiver> listUnreadByUser (Long userId)
    {
        LambdaQueryWrapper<NoticeReceiver> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeReceiver::getUserId, userId)
                .eq(NoticeReceiver::getIsRead, 0)
                .orderByAsc(NoticeReceiver::getCreateTime);
        
        return this.list(wrapper);
    }
    
    @Override
    public List<NoticeReceiver> listReadByUser (Long userId)
    {
        LambdaQueryWrapper<NoticeReceiver> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeReceiver::getUserId, userId)
                .eq(NoticeReceiver::getIsRead, 1)
                .orderByAsc(NoticeReceiver::getCreateTime);
        
        return this.list(wrapper);
    }

    @Override
    public long countUnread(Long userId)
    {
        LambdaQueryWrapper<NoticeReceiver> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeReceiver::getUserId, userId)
                .eq(NoticeReceiver::getIsRead, 0);

        return this.count(wrapper);
    }
}