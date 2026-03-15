package com.ruoyi.system.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * WebSocket 最小管理接口，方便快速验证联通性。
 */
@RestController
@RequestMapping("/ws")
public class WsController
{
    @Autowired
    private WsSessionManager wsSessionManager;

    @GetMapping("/online")
    public AjaxResult online()
    {
        return AjaxResult.success("online", wsSessionManager.onlineCount());
    }

    @PostMapping("/send")
    public AjaxResult sendToOne(@RequestParam String clientId, @RequestParam String message)
    {
        boolean sent = wsSessionManager.sendToOne(clientId, message);
        return sent ? AjaxResult.success() : AjaxResult.error("clientId 不在线或连接已关闭");
    }

    @PostMapping("/broadcast")
    public AjaxResult broadcast(@RequestParam String message)
    {
        int count = wsSessionManager.broadcast(message);
        return AjaxResult.success("sent", count);
    }
}

