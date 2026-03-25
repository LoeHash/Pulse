package com.ruoyi.system.socket.core.threadpool.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String POOL_NAME;

    public DefaultFactory(String POOL_NAME) {
        this.POOL_NAME = POOL_NAME;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(POOL_NAME + "-" + threadNumber.getAndIncrement());
        thread.setDaemon(false);
        return thread;
    }
}