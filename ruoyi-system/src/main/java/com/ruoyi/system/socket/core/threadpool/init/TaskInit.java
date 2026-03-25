package com.ruoyi.system.socket.core.threadpool.init;

import com.ruoyi.system.socket.core.threadpool.DispatcherThreadPool;
import com.ruoyi.system.socket.core.threadpool.GyroAIThreadPool;
import com.ruoyi.system.socket.core.threadpool.ResultSendThreadPool;
import com.ruoyi.system.socket.handler.MessageHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 系统启动时，初始化所有的线程池
 */
@Configuration
public class TaskInit {

    @Autowired
    public DispatcherThreadPool dispatcherThreadPool;
    @Autowired
    public GyroAIThreadPool gyroAIThreadPool;
    @Autowired
    public ResultSendThreadPool resultSendThreadPool;
    @Autowired
    public MessageHandler messageHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        //初始化线程池
        dispatcherThreadPool.initTasks();
        gyroAIThreadPool.initTasks();
        resultSendThreadPool.initTasks();
        messageHandler.initTasks();
    }
}
