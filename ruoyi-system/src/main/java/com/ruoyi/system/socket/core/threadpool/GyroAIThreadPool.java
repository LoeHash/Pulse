package com.ruoyi.system.socket.core.threadpool;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.RedisKeyGenUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.socket.WsSessionManager;
import com.ruoyi.system.socket.core.constant.MessageType;
import com.ruoyi.system.socket.core.domain.ws.AIResult;
import com.ruoyi.system.socket.core.domain.ws.GyroWindowPayload;
import com.ruoyi.system.socket.core.domain.ws.WsMessage;
import com.ruoyi.system.socket.core.threadpool.data.DataSource;
import com.ruoyi.system.socket.core.threadpool.factory.DefaultFactory;
import com.ruoyi.system.socket.core.threadpool.policy.DefaultPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 陀螺仪数据-> AI
 * 进行数据的分析
 *
 */
@Slf4j
@Component
public class GyroAIThreadPool extends AbstractThreadPool {

    @Autowired
    private WsSessionManager wsManager = SpringUtils.getBean(WsSessionManager.class);

    private static final String POOL_NAME = "GyroAIThreadPool";




    public GyroAIThreadPool(String handlerName) {
        super(handlerName);
    }


    @Override
    public void initTasks() {
        running.compareAndSet(false, true);
        //初始化线程池，开启线程不断读取redis队列数据
        for (int i = 0; i < 210; i++) {
            executorService.execute(new AiGyroGenThread());
        }
    }

    /**
     * 不断的读取redis队列数据
     * 并进行任务的派发
     */
    private class AiGyroGenThread implements Runnable {
        private Random rd = new Random();

        @Override
        public void run() {
            while (running.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    // 取出就绪的的session
                    // 死等
                    String sessionId = DataSource.readySessions.take();

                    // 允许后续更新再次入队
                    AtomicBoolean eq = DataSource.enqueued.get(sessionId);
                    if (eq != null) {
                        eq.set(false);
                    }

                    // 通知占用
                    AtomicBoolean flag =
                            DataSource
                                    .inFlight
                                    .computeIfAbsent
                                            (sessionId, k -> new AtomicBoolean(false));

                    if (!flag.compareAndSet(false, true)){
                        // 说明已经被处理了
                        // 跳过
                        continue;
                    }

                    //否则, 就尝试处理
                    try {
                        //先移除
                        WsMessage<GyroWindowPayload> wsMsg = DataSource.gyroDataMap.remove(sessionId);

                        // 说明被别人处理过了
                        // 可能是都在等待compareSet
                        // 都成功了, 但是只有一个能拿到数据
                        // 所以进行一次判断
                        if (wsMsg == null){
                            // 正在被处理
                            // 跳过
                            continue;
                        }

                        processMessage(wsMsg);

                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                        break;
                    } finally {
                        flag.set(false);
                    }
                }catch (InterruptedException e){
                    //恢复标志
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void processMessage(WsMessage<GyroWindowPayload> wsMessage) throws InterruptedException {
            if (wsMessage == null){
                throw new RuntimeException("gyroQueue is empty");
            }

            String wsMessageType = wsMessage.getType();

            if (wsMessageType == null
                    || wsMessageType.isEmpty()){
                throw new RuntimeException("message type is null or empty");
            }

            switch (wsMessageType){
                case MessageType.GYRO_WINDOW:
                    //调用AI, 进行分析
//                    Thread.sleep(1000L * rd.nextInt(2, 4));

                    //收到AI结果
                    //TODO: 这里可以根据AI分析结果进行不同的处理，比如发送消息给特定的客户端，或者存储分析结果等
                    //封装, 直接返回结果
                    AIResult aiResult = new AIResult();
                    aiResult.setResult("跑步");
                    aiResult.setTs(System.currentTimeMillis());
                    aiResult.setWindowId(wsMessage.getPayload().getWindowId());

                    WsMessage<AIResult> resultMsg = new WsMessage<AIResult>()
                            .setType(MessageType.AI_RESULT)
                            .setSessionId(wsMessage.getSessionId())
                            .setMsgId(wsMessage.getMsgId())
                            .setTs(System.currentTimeMillis())
                            .setPayload(aiResult);
                    //加入返回客户端的消息队列
                    DataSource.resultQueue.put(resultMsg);
                    break;
//                        default:
//                            throw new RuntimeException("unsupported message type: " + wsMessageType);
            }
        }


    }


    public GyroAIThreadPool(){
        this(POOL_NAME);
        executorService = new ThreadPoolExecutor(
                210,
                210,
                30,
                timeUnit,
                new LinkedBlockingQueue<>(1000),
                new DefaultFactory(POOL_NAME),
                new DefaultPolicy()
        );
    }


}
