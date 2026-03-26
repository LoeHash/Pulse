package com.ruoyi.system.config.event;

import org.springframework.context.ApplicationEvent;

public class ConfigChangeEvent extends ApplicationEvent {

    private final String key;

    public ConfigChangeEvent(Object source, String key) {
        super(source);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}