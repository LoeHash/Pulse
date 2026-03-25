package com.ruoyi.system.socket.core.domain.ws;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MotionStatePayload {

    private String windowId;

    private String state;

    private Double confidence;

    private Boolean stale;
}
