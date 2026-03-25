package com.ruoyi.system.socket.core.domain.ws;

import lombok.Data;
import java.util.List;

@Data
public class GyroWindowPayload {

    private String windowId;

    private List<GyroSample> samples;
}