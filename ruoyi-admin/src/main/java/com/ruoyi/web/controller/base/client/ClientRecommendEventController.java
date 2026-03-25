package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.UserEventBatchReq;
import com.ruoyi.system.service.RecommendEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推荐事件控制器
 *
 * 对外提供：
 * - 批量上报用户行为事件接口
 */
@RestController
@RequestMapping("/api/recommend/event")
@RequiredArgsConstructor
public class ClientRecommendEventController extends BaseController {

    /** 推荐事件服务 */
    private final RecommendEventService recommendEventService;

    /**
     * 批量上报用户行为事件
     *
     * @param req 事件批量请求
     * @return 统一响应
     */
    @PostMapping("/batch")
    public AjaxResult batch(@RequestBody UserEventBatchReq req) {
        recommendEventService.handleBatch(req);
        return AjaxResult.success("事件处理成功");
    }
}