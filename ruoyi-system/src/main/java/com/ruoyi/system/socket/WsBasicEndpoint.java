package com.ruoyi.system.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPObject;
import com.alibaba.fastjson2.util.BeanUtils;
import com.ruoyi.system.socket.core.constant.MessageType;
import com.ruoyi.system.socket.core.domain.ws.GyroWindowPayload;
import com.ruoyi.system.socket.core.domain.ws.WsMessage;
import com.ruoyi.system.socket.core.utils.SpringContextHolderUtils;
import com.ruoyi.system.socket.handler.MessageHandler;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MessageHandler messageHandler  = SpringUtils.getBean(MessageHandler.class);

    @Autowired
    private WsSessionManager sessionManager =  SpringUtils.getBean(WsSessionManager.class);

    private static final Logger log = LoggerFactory.getLogger(WsBasicEndpoint.class);


    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId)
    {

        String finalClientId = StringUtils.isEmpty(clientId) ? session.getId() : clientId;
        sessionManager.add(finalClientId, session);
        sessionManager.addSessionId(session.getId(), finalClientId);

        log.info("[ws] client connected, clientId={}, online={}", finalClientId, sessionManager.onlineCount());
        session.getAsyncRemote().sendText("connected:" + finalClientId);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("clientId") String clientId)
    {
        WsMessage<JSONObject> msg = JSON.parseObject(
                message,
                new com.alibaba.fastjson2.TypeReference<WsMessage<JSONObject>>() {}
        );

        String type = msg.getType();
        switch (type){
            case MessageType.GYRO_WINDOW:
                GyroWindowPayload payload = msg.getPayload().toJavaObject(GyroWindowPayload.class);

                // 重新组装“强类型”的完整消息
                WsMessage<GyroWindowPayload> typedMsg = new WsMessage<GyroWindowPayload>()
                        .setType(msg.getType())
                        .setSessionId(msg.getSessionId())
                        .setMsgId(msg.getMsgId())
                        .setTs(msg.getTs())
                        .setPayload(payload);

                messageHandler.handle(typedMsg, session);
                break;
            case MessageType.EXEC_TYPE:
                break;
            default:
                throw new RuntimeException("unsupported message type: " + type);
        }

//        String finalClientId = StringUtils.isEmpty(clientId) ? session.getId() : clientId;
//        log.info("[ws] receive message, clientId={}, message={}", finalClientId, message);
//        session.getAsyncRemote().sendText("echo:" + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("clientId") String clientId)
    {
        String finalClientId = StringUtils.isEmpty(clientId) ? session.getId() : clientId;
        sessionManager.remove(finalClientId);
        sessionManager.removeSessionId(session.getId());
        log.info("[ws] client closed, clientId={}, online={}", finalClientId, sessionManager.onlineCount());
    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("clientId") String clientId)
    {
        String finalClientId =
                StringUtils.isEmpty(clientId) ? (session == null ? "unknown" : session.getId()) : clientId;
        log.error("[ws] client error, clientId={}", finalClientId, error);
    }
}

