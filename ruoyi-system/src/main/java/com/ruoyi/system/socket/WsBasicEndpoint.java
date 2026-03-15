package com.ruoyi.system.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * 最基础的 WebSocket 端点：支持连接、回显、断开和异常处理。
 */
@Component
@ServerEndpoint("/ws/basic/{clientId}")
public class WsBasicEndpoint
{
    private static final Logger log = LoggerFactory.getLogger(WsBasicEndpoint.class);

    private WsSessionManager getSessionManager()
    {
        return SpringUtils.getBean(WsSessionManager.class);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId)
    {
        String finalClientId = StringUtils.isEmpty(clientId) ? session.getId() : clientId;
        getSessionManager().add(finalClientId, session);
        log.info("[ws] client connected, clientId={}, online={}", finalClientId, getSessionManager().onlineCount());
        session.getAsyncRemote().sendText("connected:" + finalClientId);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("clientId") String clientId)
    {
        String finalClientId = StringUtils.isEmpty(clientId) ? session.getId() : clientId;
        log.info("[ws] receive message, clientId={}, message={}", finalClientId, message);
        session.getAsyncRemote().sendText("echo:" + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("clientId") String clientId)
    {
        String finalClientId = StringUtils.isEmpty(clientId) ? session.getId() : clientId;
        getSessionManager().remove(finalClientId);
        log.info("[ws] client closed, clientId={}, online={}", finalClientId, getSessionManager().onlineCount());
    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("clientId") String clientId)
    {
        String finalClientId = StringUtils.isEmpty(clientId) ? (session == null ? "unknown" : session.getId()) : clientId;
        log.error("[ws] client error, clientId={}", finalClientId, error);
    }
}

