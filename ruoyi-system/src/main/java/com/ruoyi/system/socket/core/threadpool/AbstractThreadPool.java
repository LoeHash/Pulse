package com.ruoyi.system.socket.core.threadpool;

import jakarta.annotation.PreDestroy;
import lombok.Getter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractThreadPool {
    public TimeUnit timeUnit = TimeUnit.SECONDS;

    public static final ArrayList<AbstractThreadPool> tps = new ArrayList<>();

    public AtomicBoolean running = new AtomicBoolean(false);

    @Getter
    protected ExecutorService executorService;

    @Getter
    private final String poolName;



    public AbstractThreadPool(String poolName) {
        this.poolName = poolName;
    }

    public abstract void initTasks();


    public void close(){
        if (running.compareAndSet(true, false)) {
            executorService.shutdown();  // 不再接受新任务
            try {
                // 等待已有任务完成，最多等待 30 秒
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();  // 强制关闭
                    // 等待强制关闭完成
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        System.err.println("Executor did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }


    @PreDestroy
    public void stop(){
        running.compareAndSet(true,false);
        if (executorService != null && !executorService.isShutdown()){
            executorService.shutdownNow();
        }
    }

}
