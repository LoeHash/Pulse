package com.ruoyi.system.socket.debug;

import com.ruoyi.system.socket.core.threadpool.data.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class ShowInfo {

    // 每5秒打印一次（你可改成 1s/10s）
//    @Scheduled(fixedDelay = 5000)
    public void printQueues() {
        int gyroMapSize = DataSource.gyroDataMap.size();
        int readySize = DataSource.readySessions.size();
        int resultSize = DataSource.resultQueue.size();

        long inflightCount = DataSource.inFlight.values()
                .stream()
                .filter(AtomicBoolean::get)
                .count();

        log.info("[METRICS] gyroDataMap.size={}, readySessions.size={}, resultQueue.size={}, inFlight={}",
                gyroMapSize, readySize, resultSize, inflightCount);
    }
}