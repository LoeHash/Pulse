package com.ruoyi.system.config.event;

public interface DynamicConfigListener<T> {

    /**
     * 监听的配置key
     */
    String getConfigKey();

    /**
     * 配置变化时回调
     */
    void onChange(T newConfig);
}