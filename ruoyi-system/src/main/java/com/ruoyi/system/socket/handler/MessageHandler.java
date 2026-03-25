package com.ruoyi.system.socket.handler;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.RedisKeyPrefix;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.RedisKeyGenUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.socket.WsSessionManager;
import com.ruoyi.system.socket.core.domain.ws.WsMessage;
import com.ruoyi.system.socket.core.threadpool.factory.DefaultFactory;
import com.ruoyi.system.socket.core.threadpool.policy.DefaultPolicy;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ruoyi.common.utils.RedisKeyGenUtils.genMqGyroKey;

/**
 * 仅把当前传递的信息直接存入 Redis，过期时间为1分钟。
 */
@Component
@Slf4j
public class MessageHandler extends AbstractHandler {

    private static final String HANDLER_NAME = "MessageHandler";

    private ExecutorService executorService;

    @Autowired
    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    private BlockingQueue<String> messsageQueue = new LinkedBlockingQueue<>(1000);

    @Autowired
    private WsSessionManager wsManager = SpringUtils.getBean(WsSessionManager.class);



    public MessageHandler(String handlerName) {
        super(handlerName);

    }

    public void initTasks(){
        for (int i = 0; i < 200; i++) {
            executorService.execute(new MessageTask());
        }
    }

    public MessageHandler(){
        this(HANDLER_NAME);


        executorService = new ThreadPoolExecutor(
                200,
                200,
                30,
                timeunit,
                new LinkedBlockingQueue<>(10),
                new DefaultFactory(HANDLER_NAME),
                new DefaultPolicy()
        );

    }

    @Override
    public void handle(WsMessage<?> ms, Session session) {
        String jsonString = JSONObject.toJSONString(ms);

        //we don't care;
        messsageQueue.offer(jsonString);
    }

    private class MessageTask implements Runnable{

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    // 转换对象
                    String messageJson = messsageQueue.take();

                    // 存入redis消息队列
                    redisCache
                            .stringRedisTemplate
                            .opsForList()
                            .leftPush(
                                    RedisKeyGenUtils
                                            .genMqGyroKey(),
                                    messageJson
                            );

                }catch (InterruptedException e){
                    log.error("Error! while executing the task " + Thread.currentThread().getName());
                    log.error(e.getCause() + " \n " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }

        }
    }
}
