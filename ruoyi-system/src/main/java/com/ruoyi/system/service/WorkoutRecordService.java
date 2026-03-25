package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.WorkoutRecord;
import com.ruoyi.common.core.domain.dto.WorkoutRecordFinishDTO;
import com.ruoyi.common.core.domain.dto.WorkoutRecordQueryDTO;
import com.ruoyi.common.core.domain.dto.WorkoutRecordStartDTO;
import com.ruoyi.common.core.domain.vo.WorkoutCompletionTrendVO;
import com.ruoyi.common.core.domain.vo.WorkoutClientCourseStatVO;
import com.ruoyi.common.core.domain.vo.WorkoutClientSummaryVO;
import com.ruoyi.common.core.domain.vo.WorkoutCourseRankVO;
import com.ruoyi.common.core.domain.vo.WorkoutDailyStatVO;
import com.ruoyi.common.core.domain.vo.WorkoutStatSummaryVO;
import com.ruoyi.common.core.domain.vo.WorkoutUserActiveVO;

import java.util.Date;

import java.util.List;

/**
 * 训练记录服务接口
 */
public interface WorkoutRecordService extends IService<WorkoutRecord> {

    Long startWorkout(Long userId, WorkoutRecordStartDTO dto);

    int finishWorkout(Long userId, Long recordId, WorkoutRecordFinishDTO dto);

    List<WorkoutRecord> selectMyWorkoutRecords(Long userId, WorkoutRecordQueryDTO queryDTO);

    WorkoutRecord getMyWorkoutRecord(Long userId, Long recordId);

    int deleteMyWorkoutRecord(Long userId, Long recordId);

    WorkoutRecord getCurrentUnfinishedWorkout(Long userId);

    WorkoutClientSummaryVO statMyWorkoutSummary(Long userId, Date startDate, Date endDate);

    List<WorkoutDailyStatVO> statMyWorkoutDaily(Long userId, Date startDate, Date endDate);

    List<WorkoutClientCourseStatVO> statMyWorkoutCourse(Long userId, Date startDate, Date endDate);

    List<WorkoutRecord> selectWorkoutRecordsForAdmin(WorkoutRecordQueryDTO queryDTO);

    List<WorkoutDailyStatVO> statWorkoutOverviewByDay(Date startDate, Date endDate);

    List<WorkoutDailyStatVO> statWorkoutOverviewByDayPage(Date startDate, Date endDate);

    WorkoutStatSummaryVO statWorkoutSummary(Date startDate, Date endDate);

    List<WorkoutCourseRankVO> statWorkoutCourseRank(Date startDate, Date endDate);

    List<WorkoutUserActiveVO> statWorkoutUserActive(Date startDate, Date endDate);

    List<WorkoutCompletionTrendVO> statWorkoutCompletionTrend(Date startDate, Date endDate);

    WorkoutRecord getWorkoutRecordByIdForAdmin(Long id);

    int createWorkoutRecordByAdmin(WorkoutRecord record);

    int updateWorkoutRecordByAdmin(Long id, WorkoutRecord record);

    int deleteWorkoutRecordByAdmin(Long id);

    int autoCloseTimeoutWorkoutRecords();
}

