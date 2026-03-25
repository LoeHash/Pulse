package com.ruoyi.system.socket;

import java.io.IOException;
import java.util.Collection;
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
    //client id -> session
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    //session id -> client id
    private final Map<String, String> sessionIdToClientId = new ConcurrentHashMap<>();

    public void add(String clientId, Session session)
    {
        sessionMap.put(clientId, session);
    }

    public void remove(String clientId)
    {
        sessionMap.remove(clientId);
    }


    public Session getOnline(String clientId){
        return sessionMap.get(clientId);
    }

    public Collection<Session> onlineList(){
        return sessionMap.values();
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

    public void addSessionId(String sessionId, String clientId)
    {
        if (sessionId.isEmpty() || clientId.isEmpty()){
            throw new RuntimeException("你不能添加一个空的 sessionId 或 clientId");
        }

        sessionIdToClientId.put(sessionId, clientId);
    }

    public void removeSessionId(String sessionId)
    {
        if (sessionId.isEmpty()){
            throw new RuntimeException("你不能删除一个空的 sessionId");
        }

        sessionIdToClientId.remove(sessionId);
    }

    public String getSessionIdToClientId(String sid){
        return sessionIdToClientId.get(sid);
    }

}

