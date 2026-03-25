package com.ruoyi.system.socket.core.domain.ws;

import lombok.Data;

@Data

public class GyroSample {

    private Long t;

    private Double gx;

    private Double gy;

    private Double gz;
}
