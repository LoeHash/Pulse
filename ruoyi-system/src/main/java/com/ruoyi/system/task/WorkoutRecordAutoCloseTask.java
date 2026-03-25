package com.ruoyi.system.task;

import com.ruoyi.system.service.WorkoutRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 训练记录自动收口任务
 */
@Component
public class WorkoutRecordAutoCloseTask {

    private static final Logger log = LoggerFactory.getLogger(WorkoutRecordAutoCloseTask.class);

    private final WorkoutRecordService workoutRecordService;

    public WorkoutRecordAutoCloseTask(WorkoutRecordService workoutRecordService) {
        this.workoutRecordService = workoutRecordService;
    }

    /**
     * 每5分钟扫描一次超时未结束的训练记录。
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void autoCloseTimeoutRecords() {
        int affected = workoutRecordService.autoCloseTimeoutWorkoutRecords();
        if (affected > 0) {
            log.info("auto close timeout workout records success, affected={}", affected);
        }
    }
}

