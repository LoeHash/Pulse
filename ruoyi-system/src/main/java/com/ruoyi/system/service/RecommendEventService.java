package com.ruoyi.system.service;


import com.ruoyi.common.core.domain.UserEventBatchReq;

/**
 * 推荐事件服务接口
 *
 * 职责：
 * 1. 接收前端上报的用户行为事件
 * 2. 落库用户行为明细
 * 3. 基于事件更新用户画像（标签偏好、版块偏好）
 */
public interface RecommendEventService {

    /**
     * 批量处理用户行为事件
     *
     * @param req 前端批量事件请求对象
     */
    void handleBatch(UserEventBatchReq req);
}