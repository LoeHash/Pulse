package com.ruoyi.system.config.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.ruoyi.system.config.domain.Config;
import com.ruoyi.system.config.event.ConfigChangeEvent;
import com.ruoyi.system.config.mapper.ConfigMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConfigService {


    @Autowired
    private ConfigMapper mapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public void initCache() {
        mapper.selectList(null).forEach(c ->
                cache.put(c.getConfigKey(), c.getConfigValue())
        );
    }

    public void refreshAll() {
        for (String key : cache.keySet()) {
            eventPublisher.publishEvent(new ConfigChangeEvent(this, key));
        }
    }

    public <T> T getConfig(String key, Class<T> clazz) {
        String json = cache.get(key);
        if (json == null) {
            return null;
        }

        try {
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("配置解析失败: " + key, e);
        }
    }

    @Transactional
    public void update(String key, String json) {


        Config config = mapper.selectOne(
                new LambdaQueryWrapper<Config>()
                        .eq(Config::getConfigKey, key)
        );
        System.out.println(config);
        if (config == null) {
            config = new Config();
            config.setConfigKey(key);
            config.setConfigValue(json);
            mapper.insert(config);
        } else {
            config.setConfigValue(json);
            mapper.updateById(config);
        }

        cache.put(key, json);

        // 发布事件
        eventPublisher.publishEvent(new ConfigChangeEvent(this, key));
    }

    private String toJson(Object value) {
        try {
            return JSONObject.toJSONString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}