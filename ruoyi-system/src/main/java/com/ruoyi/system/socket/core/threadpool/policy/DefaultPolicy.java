package com.ruoyi.system.socket.core.threadpool.policy;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class DefaultPolicy implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 等待
        try {
            log.debug("into the reject policy!");
            executor.execute(r);
        }catch (StackOverflowError e){
            log.error("任务被拒绝执行，等待重试 stack over follow~", e);
        }
    }
}