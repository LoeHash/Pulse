package com.ruoyi.system.socket.core.threadpool;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.RedisKeyGenUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.config.domain.ins.ThreadPoolConfig;
import com.ruoyi.system.config.event.DynamicConfigListener;
import com.ruoyi.system.socket.WsSessionManager;
import com.ruoyi.system.socket.core.constant.MessageType;
import com.ruoyi.system.socket.core.domain.ws.GyroWindowPayload;
import com.ruoyi.system.socket.core.domain.ws.WsMessage;
import com.ruoyi.system.socket.core.threadpool.data.DataSource;
import com.ruoyi.system.socket.core.threadpool.factory.DefaultFactory;
import com.ruoyi.system.socket.core.threadpool.policy.DefaultPolicy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 将redis的消息队列中的数据持续取出
 * 进行任务的派发
 */
@Slf4j
@Component
public class DispatcherThreadPool extends AbstractThreadPool implements DynamicConfigListener<ThreadPoolConfig> {

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WsSessionManager wsManager = SpringUtils.getBean(WsSessionManager.class);

    private static final String POOL_NAME = "DispatcherThreadPool";

    //同时取得300条
    private static final int BATCH_SIZE = 300;




    public DispatcherThreadPool(String handlerName) {
        super(handlerName);
    }


    @Override
    public void initTasks() {
        int processors = Runtime.getRuntime().availableProcessors();
        initTasks(processors * 6);
    }


    public void initTasks(int runningProcessors) {
        boolean b = running.compareAndSet(false, true);
        if (!b){
            throw new RuntimeException("DispatcherThreadPool is already running");
        }

        //初始化线程池，开启线程不断读取redis队列数据
        for (int i = 0; i < runningProcessors; i++) {
            executorService.execute(new DispatcherThread());
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
     * 不断的读取redis队列数据
     * 并进行任务的派发
     */
    private class DispatcherThread implements Runnable {
        @Override
        public void run() {
            String gyroKey = RedisKeyGenUtils.genMqGyroKey();

            try {
                while (running.get() && !Thread.currentThread().isInterrupted()) {

                    List<String> messages = batchRightPop(gyroKey, BATCH_SIZE);

                    if (messages.isEmpty()) {
                        //为空
                        //尝试阻塞pop
                        String message = stringRedisTemplate
                                .opsForList()
                                .rightPop(RedisKeyGenUtils.genMqGyroKey(), 8, TimeUnit.SECONDS);

                        dispatchMessage(message);
                        continue;
                    }

                    for (String msg : messages) {
                        dispatchMessage(msg);
                    }

                }
            }catch (Exception e){
                log.debug("outting DispatcherThread: " + e.getMessage());
            }
        }

        private void dispatchMessage(String message) {
            try {


                if (message == null || message.isEmpty()){
                    return;
                }

                WsMessage<JSONObject> wsMessage = JSON.parseObject(
                        message,
                        new com.alibaba.fastjson2.TypeReference<WsMessage<JSONObject>>() {
                        }
                );

                String type = wsMessage.getType();
                switch (type){
                    case MessageType.GYRO_WINDOW:

                        dispatchToAiTP(wsMessage);
                        break;
                    case MessageType.EXEC_TYPE:
                        break;
                    default:
                        throw new RuntimeException("unsupported message type: " + type);
                }
            }catch (Exception e){
//                    log.error("DispatcherThreadPool error: " + ;);
                e.printStackTrace();
                log.error("caused:  " + e.getCause());
            }
        }

        private void dispatchToAiTP(WsMessage<JSONObject> wsMessage) {
            //放入AI分析的map中
            String sessionId = wsMessage.getSessionId();
            WsMessage<GyroWindowPayload> gyroWindowPayloadWsMessage = reloadGyroWsMessage(wsMessage);

            //放入map
            DataSource.gyroDataMap.put(sessionId, gyroWindowPayloadWsMessage);

            //去重入队：队列里最多一个 sessionId
            AtomicBoolean eq =
                    DataSource
                            .enqueued
                            .computeIfAbsent(sessionId, k -> new AtomicBoolean(false));

            if (eq.compareAndSet(false, true)) {
                // 第一次入队成功标记 enqueued=true
                // offer 失败就丢弃一个旧通知再塞（你的策略可以保留）

                while (!DataSource.readySessions.offer(sessionId)) {
                    // maybe never reached if the queue is big enough and the processing is fast enough
                    DataSource.readySessions.poll();
                }
            }
        }

        /**
         * 使用pipeline批量从List右侧弹出数据
         */
        private List<String> batchRightPop(String key, int batchSize) {
            List<String> results = new ArrayList<>();

            // 使用pipeline批量执行多个rightPop
            List<Object> pipelineResults = stringRedisTemplate.executePipelined(
                    (RedisCallback<Object>) connection -> {
                        for (int i = 0; i < batchSize; i++) {
                            connection.listCommands().rPop(key.getBytes());
                        }
                        return null;
                    }
            );

            // 过滤空结果
            for (Object result : pipelineResults) {
                if (result != null) {
                    results.add(result.toString());
                } else {
                    // 遇到null说明队列已空，停止继续获取
                    break;
                }
            }

            return results;
        }


        public WsMessage<GyroWindowPayload> reloadGyroWsMessage(WsMessage<JSONObject> msg){
            GyroWindowPayload payload = msg.getPayload().toJavaObject(GyroWindowPayload.class);

            WsMessage<GyroWindowPayload> typedMsg = new WsMessage<GyroWindowPayload>()
                    .setType(msg.getType())
                    .setSessionId(msg.getSessionId())
                    .setMsgId(msg.getMsgId())
                    .setTs(msg.getTs())
                    .setPayload(payload);

            return typedMsg;
        }
    }



    public DispatcherThreadPool(){
        this(POOL_NAME);

        int processors = Runtime.getRuntime().availableProcessors();

        // IO密集型任务：核心线程数可以稍多
        int maxPoolSize = processors * 6;   // 例如：8核 -> 32
        int queueCapacity = 500;

        executorService = new ThreadPoolExecutor(
                maxPoolSize,
                maxPoolSize,
                30,
                timeUnit,
                new LinkedBlockingQueue<>(queueCapacity),
                new DefaultFactory(POOL_NAME),
                new DefaultPolicy()
        );
    }







}
