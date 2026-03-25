package com.ruoyi.system.socket.core.threadpool;

import jakarta.annotation.PreDestroy;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractThreadPool {
    public TimeUnit timeUnit = TimeUnit.SECONDS;

    public static final ArrayList<AbstractThreadPool> tps = new ArrayList<>();

    public AtomicBoolean running = new AtomicBoolean(false);

    protected ExecutorService executorService;


    private String poolName;



    public AbstractThreadPool(String poolName) {
        this.poolName = poolName;
    }

    public abstract void initTasks();

    @PreDestroy
    public void stop(){
        running.compareAndSet(true,false);
        if (executorService != null && !executorService.isShutdown()){
            executorService.shutdownNow();
        }
    }

}
