package com.ruoyi.system.config.processor;

import com.ruoyi.system.config.annotation.DynamicConfig;
import com.ruoyi.system.config.event.ConfigChangeEvent;
import com.ruoyi.system.config.event.DynamicConfigListener;
import com.ruoyi.system.config.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DynamicConfigProcessor implements BeanPostProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    private ConfigService configService;

    private ConfigService getConfigService() {
        if (configService == null) {
            configService = applicationContext.getBean(ConfigService.class);
        }
        return configService;
    }

    private final Map<String, List<FieldBinding>> fieldBindings = new ConcurrentHashMap<>();

    private final Map<String, List<DynamicConfigListener>> listeners = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        // 处理字段注入
        for (Field field : bean.getClass().getDeclaredFields()) {

            DynamicConfig annotation = field.getAnnotation(DynamicConfig.class);
            if (annotation == null) continue;

            String key = annotation.value();
            field.setAccessible(true);

            Object value = getConfigService().getConfig(key, field.getType());

            try {
                field.set(bean, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            fieldBindings
                    .computeIfAbsent(key, k -> new ArrayList<>())
                    .add(new FieldBinding(bean, field));
        }

        // 处理监听器
        if (bean instanceof DynamicConfigListener) {
            DynamicConfigListener listener = (DynamicConfigListener) bean;


            // 初始化监听器
            listeners
                    .computeIfAbsent(listener.getConfigKey(), k -> new ArrayList<>())
                    .add(listener);
        }

        return bean;
    }

    @EventListener
    public void onChange(ConfigChangeEvent event) {

        String key = event.getKey();

        // 更新字段
        List<FieldBinding> fields = fieldBindings.get(key);
        if (fields != null) {
            for (FieldBinding fb : fields) {
                Object newValue = getConfigService().getConfig(key, fb.field.getType());
                try {
                    fb.field.set(fb.bean, newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 触发业务刷新（核心）
        List<DynamicConfigListener> ls = listeners.get(key);
        if (ls != null) {
            for (DynamicConfigListener l : ls) {
                Class<?> clazz;
                switch (l.getConfigKey()) {
                    case "algo.recommend":
                        clazz = com.ruoyi.system.config.domain.ins.AlgoConfig.class;
                        break;
                    case "threadPool.global":
                        clazz = com.ruoyi.system.config.domain.ins.ThreadPoolConfig.class;
                        break;
                    default:
                        clazz = Object.class;
                }

                Object newConfig = getConfigService().getConfig(key, clazz);
                l.onChange(newConfig);
            }
        }
    }

    static class FieldBinding {
        Object bean;
        Field field;

        FieldBinding(Object bean, Field field) {
            this.bean = bean;
            this.field = field;
        }
    }
}