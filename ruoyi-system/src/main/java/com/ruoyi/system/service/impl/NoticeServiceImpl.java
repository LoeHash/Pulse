package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Notice;
import com.ruoyi.common.core.domain.NoticeReceiver;
import com.ruoyi.system.mapper.NoticeMapper;
import com.ruoyi.system.service.NoticeReceiverService;
import com.ruoyi.system.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知 Service 实现
 *
 * @author loe
 * @date 2026-02-23
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
        implements NoticeService
{

    private final NoticeReceiverService noticeReceiverService;

    public NoticeServiceImpl(NoticeReceiverService noticeReceiverService) {
        this.noticeReceiverService = noticeReceiverService;
    }

    @Override
    public boolean publish(Notice notice)
    {
        notice.setStatus("NORMAL");
        return this.save(notice);
    }

    @Override
    public boolean cancel(Long noticeId)
    {
        Notice notice = new Notice();
        notice.setId(noticeId);
        notice.setStatus("CANCELED");
        return this.updateById(notice);
    }

    @Override
    public List<Notice> listByType(String type)
    {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getType, type)
                .eq(Notice::getStatus, "NORMAL")
                .orderByDesc(Notice::getCreateTime);

        return this.list(wrapper);
    }

    /**
     * 发布通知并推送给指定用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishToUsers(Notice notice, List<Long> userIds)
    {

        notice.setStatus("NORMAL");
        this.save(notice);

        Long noticeId = notice.getId();

        if (userIds == null || userIds.isEmpty())
        {
            return true;
        }

        List<NoticeReceiver> receivers = new ArrayList<>(userIds.size());

        for (Long userId : userIds)
        {
            NoticeReceiver receiver = new NoticeReceiver();
            receiver.setNoticeId(noticeId);
            receiver.setUserId(userId);
            receiver.setIsRead(0);
            receiver.setIsDeleted(0);
            receivers.add(receiver);
        }

        return noticeReceiverService.saveBatch(receivers);
    }
}