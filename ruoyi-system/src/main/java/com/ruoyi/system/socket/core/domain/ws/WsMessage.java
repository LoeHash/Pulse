package com.ruoyi.system.socket.core.domain.ws;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
//@Schema(description = "WebSocket统一消息信封")
public class WsMessage<T> {

    private String type;

    private String sessionId;

    private String msgId;

    private Long ts;

    private T payload;
}