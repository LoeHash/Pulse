package com.ruoyi.system.config.domain.ins;

import lombok.Data;

@Data
public class ThreadPoolConfig {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private int keepAliveTime;
}