package com.ruoyi.system.socket.debug;

import com.ruoyi.system.socket.core.threadpool.DispatcherThreadPool;
import com.ruoyi.system.socket.core.threadpool.GyroAIThreadPool;
import com.ruoyi.system.socket.core.threadpool.ResultSendThreadPool;
import com.ruoyi.system.socket.core.threadpool.data.DataSource;
import com.ruoyi.system.socket.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowInfo {

    private final DispatcherThreadPool dispatcherThreadPool;
    private final GyroAIThreadPool gyroAIThreadPool;
    private final ResultSendThreadPool resultSendThreadPool;
    private final MessageHandler messageHandler;
    /**
     * 每5秒打印一次线程池监控信息
     */
    @Scheduled(fixedDelay = 5000)
    public void printQueues() {
        // 1. 获取数据源信息
        int gyroMapSize = DataSource.gyroDataMap.size();
        int readySize = DataSource.readySessions.size();
        int resultSize = DataSource.resultQueue.size();

        long inflightCount = DataSource.inFlight.values()
                .stream()
                .filter(AtomicBoolean::get)
                .count();

        // 2. 获取线程池信息
        printThreadPoolInfo("Dispatcher", dispatcherThreadPool.getExecutorService());
        printThreadPoolInfo("GyroAI", gyroAIThreadPool.getExecutorService());
        printThreadPoolInfo("ResultSend", resultSendThreadPool.getExecutorService());

        System.out.println(gyroAIThreadPool.getExecutorService().getClass());

        // 3. 打印业务指标
        log.info("[METRICS] gyroDataMap.size={}, readySessions.size={}, resultQueue.size={}, inFlight={}",
                gyroMapSize, readySize, resultSize, inflightCount);
    }

    // 修正后的 printThreadPoolInfo 方法
    private void printThreadPoolInfo(String poolName, ExecutorService executorService) {
        if (executorService == null) {
            log.warn("[{}] ExecutorService is null", poolName);
            return;
        }

        if (!(executorService instanceof ThreadPoolExecutor executor)) {
            log.info("[{}] ExecutorService type: {}, shutdown={}, terminated={}",
                    poolName,
                    executorService.getClass().getSimpleName(),
                    executorService.isShutdown(),
                    executorService.isTerminated());
            return;
        }

        int corePoolSize = executor.getCorePoolSize();
        int maximumPoolSize = executor.getMaximumPoolSize();
        int poolSize = executor.getPoolSize();
        int activeCount = executor.getActiveCount();
        long completedTaskCount = executor.getCompletedTaskCount();
        long taskCount = executor.getTaskCount();

        BlockingQueue<Runnable> queue = executor.getQueue();
        int queueSize = queue.size();
        int queueRemainingCapacity = queue.remainingCapacity();
        int queueTotalCapacity = queueSize + queueRemainingCapacity;

        boolean isShutdown = executor.isShutdown();
        boolean isTerminated = executor.isTerminated();

        // 计算使用率
        double poolUsageRate = maximumPoolSize > 0 ? (double) poolSize / maximumPoolSize * 100 : 0;
        double activeUsageRate = poolSize > 0 ? (double) activeCount / poolSize * 100 : 0;
        double queueUsageRate = queueTotalCapacity > 0 ? (double) queueSize / queueTotalCapacity * 100 : 0;

        // 方案1：使用 String.format 格式化百分比
        log.info("[{} ThreadPool] core={}, max={}, current={}, active={}, queue={}/{}, tasks={}/{}, usage: pool={}%, active={}%, queue={}%, shutdown={}, terminated={}",
                poolName,
                corePoolSize, maximumPoolSize, poolSize, activeCount,
                queueSize, queueTotalCapacity,
                completedTaskCount, taskCount,
                String.format("%.1f", poolUsageRate),
                String.format("%.1f", activeUsageRate),
                String.format("%.1f", queueUsageRate),
                isShutdown, isTerminated);
    }

}