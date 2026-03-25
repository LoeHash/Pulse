package com.ruoyi.system.socket.handler;

import com.ruoyi.system.socket.core.domain.ws.WsMessage;
import jakarta.websocket.Session;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;


public abstract class AbstractHandler {

    private String handlerName;

    public static final TimeUnit timeunit = TimeUnit.SECONDS;

    public AbstractHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    public abstract void handle(WsMessage<?> ms, Session session);


}
