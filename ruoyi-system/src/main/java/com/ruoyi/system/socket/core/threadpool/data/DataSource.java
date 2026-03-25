package com.ruoyi.system.socket.core.threadpool.data;

import com.ruoyi.system.socket.core.domain.ws.GyroWindowPayload;
import com.ruoyi.system.socket.core.domain.ws.WsMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataSource {

    // 陀螺仪消息Map
    // 只存储最新的
    // session to ws
    public static ConcurrentHashMap<String, WsMessage<GyroWindowPayload>> gyroDataMap
            = new ConcurrentHashMap<>(1000);

    // 已经准备好的sessionId队列，等待发送结果
    public static BlockingQueue<String> readySessions = new LinkedBlockingQueue<>(10000);

    // per-session “正在处理”标记
    public static final ConcurrentHashMap<String, AtomicBoolean> inFlight = new ConcurrentHashMap<>();

    // 每个 session 是否已入队（readySessions 去重）
    public static final ConcurrentHashMap<String, AtomicBoolean> enqueued = new ConcurrentHashMap<>();


    //=============================================================
    // 返回消息队列
    public static LinkedBlockingQueue<WsMessage<?>> resultQueue = new LinkedBlockingQueue<>(1000);


}
