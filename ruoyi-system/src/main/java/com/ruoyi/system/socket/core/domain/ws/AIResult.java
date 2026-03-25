package com.ruoyi.system.socket.core.domain.ws;

import lombok.Data;

@Data
public class AIResult {

    private String windowId;

    private long ts;

    //富文本
    private String result;


}
