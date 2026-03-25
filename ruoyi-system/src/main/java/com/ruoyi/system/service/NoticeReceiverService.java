package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.NoticeReceiver;

import java.util.List;

/**
 * 通知接收记录 Service
 *
 * @author loe
 * @date 2026-02-23
 */
public interface NoticeReceiverService extends IService<NoticeReceiver>
{
    /**
     * 标记已读
     */
    boolean markAsRead(Long noticeId, Long userId);

    /**
     * 查询用户未读通知
     */
    List<NoticeReceiver> listUnreadByUser(Long userId);
    
    /**
     * 查询用户已读通知
     */
    List<NoticeReceiver> listReadByUser (Long userId);
    
    /**
     * 统计未读数量
     */
    long countUnread(Long userId);
}