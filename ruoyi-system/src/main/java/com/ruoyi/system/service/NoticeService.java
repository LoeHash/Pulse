package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Notice;

import java.util.List;

/**
 * 通知 Service 接口
 *
 * @author loe
 * @date 2026-02-23
 */
public interface NoticeService extends IService<Notice>
{
    /**
     * 发布通知
     */
    boolean publish(Notice notice);

    /**
     * 撤销通知
     */
    boolean cancel(Long noticeId);

    /**
     * 根据类型查询通知
     */
    List<Notice> listByType(String type);

    boolean publishToUsers(Notice notice, List<Long> userIds);
}