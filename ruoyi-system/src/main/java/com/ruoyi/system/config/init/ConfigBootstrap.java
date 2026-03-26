package com.ruoyi.system.config.init;

import com.ruoyi.system.config.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigBootstrap implements CommandLineRunner {

    private final ConfigService configService;

    @Override
    public void run(String... args) {
        // 启动时加载配置
        configService.initCache();
        System.out.println("初始化！");
        // 🔥 主动触发一次全量刷新
        configService.refreshAll();
    }
}