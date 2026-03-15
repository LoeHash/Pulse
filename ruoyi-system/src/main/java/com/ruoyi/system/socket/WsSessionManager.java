package com.ruoyi.system.socket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import jakarta.websocket.Session;

/**
 * WebSocket 会话管理器，维护 clientId 与 Session 的映射。
 */
@Component
public class WsSessionManager
{
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public void add(String clientId, Session session)
    {
        sessionMap.put(clientId, session);
    }

    public void remove(String clientId)
    {
        sessionMap.remove(clientId);
    }

    public int onlineCount()
    {
        return sessionMap.size();
    }

    public boolean sendToOne(String clientId, String message)
    {
        Session session = sessionMap.get(clientId);
        if (session == null || !session.isOpen())
        {
            return false;
        }
        session.getAsyncRemote().sendText(message);
        return true;
    }

    public int broadcast(String message)
    {
        int successCount = 0;
        for (Session session : sessionMap.values())
        {
            if (session != null && session.isOpen())
            {
                session.getAsyncRemote().sendText(message);
                successCount++;
            }
        }
        return successCount;
    }

    public void close(String clientId) throws IOException
    {
        Session session = sessionMap.get(clientId);
        if (session != null && session.isOpen())
        {
            session.close();
        }
    }
}

