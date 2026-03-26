package com.ruoyi.system.socket.core.threadpool;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.config.domain.ins.ThreadPoolConfig;
import com.ruoyi.system.config.event.DynamicConfigListener;
import com.ruoyi.system.socket.WsSessionManager;
import com.ruoyi.system.socket.core.constant.MessageType;
import com.ruoyi.system.socket.core.domain.ws.AIResult;
import com.ruoyi.system.socket.core.domain.ws.GyroWindowPayload;
import com.ruoyi.system.socket.core.domain.ws.WsMessage;
import com.ruoyi.system.socket.core.threadpool.data.DataSource;
import com.ruoyi.system.socket.core.threadpool.factory.DefaultFactory;
import com.ruoyi.system.socket.core.threadpool.policy.DefaultPolicy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 陀螺仪数据-> AI
 * 进行数据的分析
 *
 */
@Slf4j
@Component
public class ResultSendThreadPool extends AbstractThreadPool implements DynamicConfigListener<ThreadPoolConfig> {



    @Autowired
    private WsSessionManager wsManager = SpringUtils.getBean(WsSessionManager.class);

    private static final String POOL_NAME = "GyroAIThreadPool";



    public ResultSendThreadPool(){
        this(POOL_NAME);
        executorService = new ThreadPoolExecutor(
                10,
                200,
                30,
                timeUnit,
                new LinkedBlockingQueue<>(1000),
                new DefaultFactory(POOL_NAME),
                new DefaultPolicy()
        );
    }

    public ResultSendThreadPool(String handlerName) {
        super(handlerName);
    }


    @Override
    public void initTasks() {
        initTasks(210);
    }

    public void initTasks(int runningProcessors) {
        boolean b = running.compareAndSet(false, true);
        if (!b){
            throw new RuntimeException("DispatcherThreadPool is already running");
        }

        //初始化线程池，开启线程不断读取redis队列数据
        for (int i = 0; i < runningProcessors; i++) {
            executorService.execute(new ResultSendThread());
        }
    }

    @Override
    public String getConfigKey() {
        return "threadPool.global";
    }

    @Override
    public void onChange(ThreadPoolConfig newConfig) {
        this.close();


        //help gc
        executorService = null;

        //reinitlize
        executorService = new ThreadPoolExecutor(
                newConfig.getCorePoolSize(),
                newConfig.getMaxPoolSize(),
                newConfig.getKeepAliveTime(),
                timeUnit,
                new LinkedBlockingQueue<>(newConfig.getQueueCapacity()),
                new DefaultFactory(POOL_NAME),
                new DefaultPolicy()
        );

        //init
        initTasks(newConfig.getCorePoolSize() + newConfig.getMaxPoolSize());

        System.out.println("线程池已动态更新");
    }

    /**
     * 不断的读取返回的队列数据
     * 并继续发送
     */
    private class ResultSendThread implements Runnable {
        @Override
        public void run() {
            try {
                while (running.get() && !Thread.currentThread().isInterrupted()) {
                    try {
                        //从阻塞队列中取出数据
                        WsMessage<?> wsMessage = DataSource.resultQueue.take();
//                    System.out.println(wsMessage);


                        processMessage(wsMessage);
                    }catch (Exception e){
                        log.error("error! " + e.getCause());
                    }

                }
            }catch (Exception e){
                log.debug("outting DispatcherThread: " + e.getMessage());
            }
        }

        private void processMessage(WsMessage<?> wsMessage) {
            String wsMessageType = wsMessage.getType();

            if (wsMessageType == null
                    || wsMessageType.isEmpty()) {
                throw new RuntimeException("message type is null or empty");
            }

            String sessionId = wsMessage.getSessionId();

            if (sessionId == null || sessionId.isEmpty()){
                throw new RuntimeException("sessionId is null or empty");
            }

            String clientId = wsManager.getSessionIdToClientId(sessionId);

            if (clientId == null || clientId.isEmpty()){
                //再尝试直接使用cientId
                clientId = wsMessage.getSessionId();


            }

            //返回消息
            wsManager.sendToOne(clientId, JSONObject.toJSONString(wsMessage));
        }

    }










}
