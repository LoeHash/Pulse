package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Course;
import com.ruoyi.common.core.domain.User;
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
import com.ruoyi.system.mapper.CourseMapper;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.mapper.WorkoutRecordMapper;
import com.ruoyi.system.service.WorkoutRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 训练记录服务实现
 */
@Service
public class WorkoutRecordServiceImpl extends ServiceImpl<WorkoutRecordMapper, WorkoutRecord>
        implements WorkoutRecordService {

    private static final int AUTO_CLOSE_HOURS = 48;

    private final UserMapper userMapper;
    private final CourseMapper courseMapper;

    public WorkoutRecordServiceImpl(UserMapper userMapper, CourseMapper courseMapper) {
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
    }

    /**
     * 开始训练：创建一条未结束记录。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long startWorkout(Long userId, WorkoutRecordStartDTO dto) {
        ensureCourseAvailable(dto.getCourseId());

        WorkoutRecord record = new WorkoutRecord();
        record.setUserId(userId);
        record.setCourseId(dto.getCourseId());
        record.setStartTime(dto.getStartTime() == null ? new Date() : dto.getStartTime());
        record.setCompleted(0);
        record.setDurationSec(0);
        record.setAutoClosed(0);
        record.setCloseReason("manual");
        record.setIsDeleted(0);
        record.setNote(dto.getNote());
        record.setMetrics(dto.getMetrics());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());

        this.baseMapper.insert(record);
        return record.getId();
    }

    /**
     * 结束训练：回填结束时间、时长、热量和完成状态。
     *
     * @throws IllegalArgumentException 记录不存在或不属于当前用户时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int finishWorkout(Long userId, Long recordId, WorkoutRecordFinishDTO dto) {
        WorkoutRecord db = getOwnedRecord(userId, recordId);
        if (db == null) {
            throw new IllegalArgumentException("训练记录不存在");
        }

        if (db.getEndTime() != null) {
            return 1;
        }

        Date endTime = dto.getEndTime() == null ? new Date() : dto.getEndTime();
        long duration = Math.max(0L, (endTime.getTime() - db.getStartTime().getTime()) / 1000L);

        WorkoutRecord update = new WorkoutRecord();
        update.setId(recordId);
        update.setEndTime(endTime);
        update.setDurationSec((int) duration);
        update.setCompleted(dto.getCompleted() == null ? 1 : dto.getCompleted());
        update.setAutoClosed(0);
        update.setCloseReason("user");

        if (dto.getCalories() != null) {
            update.setCalories(dto.getCalories());
        } else {
            update.setCalories(calculateCalories(userId, (int) duration));
        }

        if (StringUtils.isNotBlank(dto.getNote())) {
            update.setNote(dto.getNote());
        }
        if (StringUtils.isNotBlank(dto.getMetrics())) {
            update.setMetrics(dto.getMetrics());
        }
        update.setUpdateTime(new Date());

        return this.baseMapper.updateById(update);
    }

    @Override
    public List<WorkoutRecord> selectMyWorkoutRecords(Long userId, WorkoutRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<WorkoutRecord> wrapper = buildQueryWrapper(queryDTO);
        wrapper.eq(WorkoutRecord::getUserId, userId)
                .eq(WorkoutRecord::getIsDeleted, 0)
                .orderByDesc(WorkoutRecord::getCreateTime);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public WorkoutRecord getMyWorkoutRecord(Long userId, Long recordId) {
        return getOwnedRecord(userId, recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMyWorkoutRecord(Long userId, Long recordId) {
        WorkoutRecord db = getOwnedRecord(userId, recordId);
        if (db == null) {
            return 0;
        }

        WorkoutRecord update = new WorkoutRecord();
        update.setId(recordId);
        update.setIsDeleted(1);
        update.setUpdateTime(new Date());
        return this.baseMapper.updateById(update);
    }

    @Override
    public WorkoutRecord getCurrentUnfinishedWorkout(Long userId) {
        List<WorkoutRecord> list = this.baseMapper.selectList(new LambdaQueryWrapper<WorkoutRecord>()
                .eq(WorkoutRecord::getUserId, userId)
                .eq(WorkoutRecord::getIsDeleted, 0)
                .isNull(WorkoutRecord::getEndTime)
                .orderByDesc(WorkoutRecord::getStartTime)
                .last("LIMIT 1"));
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public WorkoutClientSummaryVO statMyWorkoutSummary(Long userId, Date startDate, Date endDate) {
        List<WorkoutRecord> records = listFinishedRecords(userId, startDate, endDate);
        return buildSummary(records);
    }

    @Override
    public List<WorkoutDailyStatVO> statMyWorkoutDaily(Long userId, Date startDate, Date endDate) {
        List<WorkoutRecord> records = listFinishedRecords(userId, startDate, endDate);
        return buildDailyStats(records);
    }

    @Override
    public List<WorkoutClientCourseStatVO> statMyWorkoutCourse(Long userId, Date startDate, Date endDate) {
        List<WorkoutRecord> records = listFinishedRecords(userId, startDate, endDate);
        return buildCourseStats(records).stream()
                .map(item -> {
                    WorkoutClientCourseStatVO vo = new WorkoutClientCourseStatVO();
                    vo.setCourseId(item.getCourseId());
                    vo.setCourseTitle(item.getCourseTitle());
                    vo.setRecordCount(item.getRecordCount());
                    vo.setTotalDurationSec(item.getTotalDurationSec());
                    vo.setTotalCalories(item.getTotalCalories());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkoutRecord> selectWorkoutRecordsForAdmin(WorkoutRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<WorkoutRecord> wrapper = buildQueryWrapper(queryDTO);
        wrapper.eq(WorkoutRecord::getIsDeleted, 0)
                .orderByDesc(WorkoutRecord::getCreateTime);
        return this.baseMapper.selectList(wrapper);
    }

    /**
     * 管理端按日统计训练总时长和总热量。
     */
    @Override
    public List<WorkoutDailyStatVO> statWorkoutOverviewByDay(Date startDate, Date endDate) {
        return buildDailyStats(listFinishedRecords(null, startDate, endDate));
    }

    @Override
    public List<WorkoutDailyStatVO> statWorkoutOverviewByDayPage(Date startDate, Date endDate) {
        return buildDailyStats(listFinishedRecords(null, startDate, endDate));
    }

    @Override
    public WorkoutStatSummaryVO statWorkoutSummary(Date startDate, Date endDate) {
        return buildAdminSummary(listFinishedRecords(null, startDate, endDate));
    }

    @Override
    public List<WorkoutCourseRankVO> statWorkoutCourseRank(Date startDate, Date endDate) {
        return buildCourseStats(listFinishedRecords(null, startDate, endDate));
    }

    @Override
    public List<WorkoutUserActiveVO> statWorkoutUserActive(Date startDate, Date endDate) {
        return buildUserStats(listFinishedRecords(null, startDate, endDate));
    }

    @Override
    public List<WorkoutCompletionTrendVO> statWorkoutCompletionTrend(Date startDate, Date endDate) {
        List<WorkoutRecord> records = listFinishedRecords(null, startDate, endDate);
        Map<String, WorkoutCompletionTrendVO> statMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (WorkoutRecord item : records) {
            String dateKey = item.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
            WorkoutCompletionTrendVO vo = statMap.computeIfAbsent(dateKey, k -> {
                WorkoutCompletionTrendVO tmp = new WorkoutCompletionTrendVO();
                tmp.setStatDate(k);
                tmp.setTotalCount(0);
                tmp.setCompletedCount(0);
                tmp.setInterruptedCount(0);
                tmp.setCompletionRate(0D);
                return tmp;
            });

            vo.setTotalCount(vo.getTotalCount() + 1);
            if (Integer.valueOf(1).equals(item.getCompleted())) {
                vo.setCompletedCount(vo.getCompletedCount() + 1);
            } else {
                vo.setInterruptedCount(vo.getInterruptedCount() + 1);
            }
        }

        statMap.values().forEach(v -> {
            if (v.getTotalCount() == 0) {
                v.setCompletionRate(0D);
            } else {
                double rate = (double) v.getCompletedCount() / (double) v.getTotalCount() * 100D;
                v.setCompletionRate(round(rate));
            }
        });

        return new ArrayList<>(statMap.values());
    }

    @Override
    public WorkoutRecord getWorkoutRecordByIdForAdmin(Long id) {
        WorkoutRecord record = this.baseMapper.selectById(id);
        if (record == null || Integer.valueOf(1).equals(record.getIsDeleted())) {
            return null;
        }
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createWorkoutRecordByAdmin(WorkoutRecord record) {
        normalizeCreateRecord(record);
        return this.baseMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWorkoutRecordByAdmin(Long id, WorkoutRecord record) {
        WorkoutRecord db = this.baseMapper.selectById(id);
        if (db == null || Integer.valueOf(1).equals(db.getIsDeleted())) {
            throw new IllegalArgumentException("训练记录不存在");
        }

        WorkoutRecord update = new WorkoutRecord();
        BeanUtils.copyProperties(record, update);
        update.setId(id);
        update.setUpdateTime(new Date());
        return this.baseMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteWorkoutRecordByAdmin(Long id) {
        WorkoutRecord db = this.baseMapper.selectById(id);
        if (db == null || Integer.valueOf(1).equals(db.getIsDeleted())) {
            return 0;
        }

        WorkoutRecord update = new WorkoutRecord();
        update.setId(id);
        update.setIsDeleted(1);
        update.setUpdateTime(new Date());
        return this.baseMapper.updateById(update);
    }

    /**
     * 自动关闭超时未结束训练（超过48小时）。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoCloseTimeoutWorkoutRecords() {
        Date now = new Date();
        long thresholdMs = now.getTime() - AUTO_CLOSE_HOURS * 3600_000L;
        Date threshold = new Date(thresholdMs);

        List<WorkoutRecord> pendingList = this.baseMapper.selectList(new LambdaQueryWrapper<WorkoutRecord>()
                .isNull(WorkoutRecord::getEndTime)
                .le(WorkoutRecord::getStartTime, threshold)
                .eq(WorkoutRecord::getIsDeleted, 0));

        int affected = 0;
        for (WorkoutRecord item : pendingList) {
            int duration = (int) Math.max(0L, (now.getTime() - item.getStartTime().getTime()) / 1000L);
            WorkoutRecord update = new WorkoutRecord();
            update.setId(item.getId());
            update.setEndTime(now);
            update.setDurationSec(duration);
            update.setCalories(calculateCalories(item.getUserId(), duration));
            update.setCompleted(0);
            update.setAutoClosed(1);
            update.setCloseReason("system_timeout");
            update.setUpdateTime(now);
            affected += this.baseMapper.updateById(update);
        }
        return affected;
    }

    private List<WorkoutRecord> listFinishedRecords(Long userId, Date startDate, Date endDate) {
        LambdaQueryWrapper<WorkoutRecord> wrapper = new LambdaQueryWrapper<WorkoutRecord>()
                .eq(WorkoutRecord::getIsDeleted, 0)
                .isNotNull(WorkoutRecord::getEndTime)
                .orderByDesc(WorkoutRecord::getStartTime);

        if (userId != null) {
            wrapper.eq(WorkoutRecord::getUserId, userId);
        }
        if (startDate != null) {
            wrapper.ge(WorkoutRecord::getStartTime, startDate);
        }
        if (endDate != null) {
            wrapper.le(WorkoutRecord::getStartTime, endDate);
        }
        return this.baseMapper.selectList(wrapper);
    }

    private WorkoutClientSummaryVO buildSummary(List<WorkoutRecord> records) {
        WorkoutClientSummaryVO summary = new WorkoutClientSummaryVO();
        summary.setTotalCount(records.size());

        int totalDuration = 0;
        int totalCalories = 0;
        int completedCount = 0;
        for (WorkoutRecord item : records) {
            totalDuration += item.getDurationSec() == null ? 0 : item.getDurationSec();
            totalCalories += item.getCalories() == null ? 0 : item.getCalories();
            if (Integer.valueOf(1).equals(item.getCompleted())) {
                completedCount++;
            }
        }
        summary.setTotalDurationSec(totalDuration);
        summary.setTotalCalories(totalCalories);
        summary.setCompletedCount(completedCount);
        summary.setCompletionRate(records.isEmpty() ? 0D : round((double) completedCount / records.size() * 100D));
        return summary;
    }

    private WorkoutStatSummaryVO buildAdminSummary(List<WorkoutRecord> records) {
        WorkoutStatSummaryVO summary = new WorkoutStatSummaryVO();
        summary.setTotalRecords(records.size());

        int totalDuration = 0;
        int totalCalories = 0;
        int completedCount = 0;
        int autoClosedCount = 0;

        for (WorkoutRecord item : records) {
            totalDuration += item.getDurationSec() == null ? 0 : item.getDurationSec();
            totalCalories += item.getCalories() == null ? 0 : item.getCalories();
            if (Integer.valueOf(1).equals(item.getCompleted())) {
                completedCount++;
            }
            if (Integer.valueOf(1).equals(item.getAutoClosed())) {
                autoClosedCount++;
            }
        }

        summary.setTotalDurationSec(totalDuration);
        summary.setTotalCalories(totalCalories);
        summary.setCompletedCount(completedCount);
        summary.setInterruptedCount(records.size() - completedCount);
        summary.setAutoClosedCount(autoClosedCount);
        summary.setCompletionRate(records.isEmpty() ? 0D : round((double) completedCount / records.size() * 100D));
        summary.setAvgDurationSec(records.isEmpty() ? 0 : Math.round((float) totalDuration / (float) records.size()));
        return summary;
    }

    private List<WorkoutDailyStatVO> buildDailyStats(List<WorkoutRecord> records) {
        Map<String, WorkoutDailyStatVO> statMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (WorkoutRecord item : records) {
            String dateKey = item.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
            WorkoutDailyStatVO vo = statMap.computeIfAbsent(dateKey, k -> {
                WorkoutDailyStatVO tmp = new WorkoutDailyStatVO();
                tmp.setStatDate(k);
                tmp.setTotalDurationSec(0);
                tmp.setTotalCalories(0);
                tmp.setRecordCount(0);
                return tmp;
            });

            vo.setTotalDurationSec(vo.getTotalDurationSec() + (item.getDurationSec() == null ? 0 : item.getDurationSec()));
            vo.setTotalCalories(vo.getTotalCalories() + (item.getCalories() == null ? 0 : item.getCalories()));
            vo.setRecordCount(vo.getRecordCount() + 1);
        }

        return new ArrayList<>(statMap.values());
    }

    private List<WorkoutCourseRankVO> buildCourseStats(List<WorkoutRecord> records) {
        Map<Long, WorkoutCourseRankVO> courseMap = new LinkedHashMap<>();

        for (WorkoutRecord item : records) {
            WorkoutCourseRankVO vo = courseMap.computeIfAbsent(item.getCourseId(), cid -> {
                WorkoutCourseRankVO tmp = new WorkoutCourseRankVO();
                tmp.setCourseId(cid);
                tmp.setRecordCount(0);
                tmp.setTotalDurationSec(0);
                tmp.setTotalCalories(0);
                return tmp;
            });

            vo.setRecordCount(vo.getRecordCount() + 1);
            vo.setTotalDurationSec(vo.getTotalDurationSec() + (item.getDurationSec() == null ? 0 : item.getDurationSec()));
            vo.setTotalCalories(vo.getTotalCalories() + (item.getCalories() == null ? 0 : item.getCalories()));
        }

        if (!courseMap.isEmpty()) {
            Map<Long, String> titleMap = courseMapper.selectBatchIds(courseMap.keySet()).stream()
                    .collect(Collectors.toMap(Course::getId, Course::getTitle, (a, b) -> a));
            courseMap.values().forEach(v -> v.setCourseTitle(titleMap.get(v.getCourseId())));
        }

        return courseMap.values().stream()
                .sorted((a, b) -> {
                    int cmp = Integer.compare(b.getTotalDurationSec(), a.getTotalDurationSec());
                    if (cmp != 0) return cmp;
                    cmp = Integer.compare(b.getTotalCalories(), a.getTotalCalories());
                    if (cmp != 0) return cmp;
                    return Integer.compare(b.getRecordCount(), a.getRecordCount());
                })
                .collect(Collectors.toList());
    }

    private List<WorkoutUserActiveVO> buildUserStats(List<WorkoutRecord> records) {
        Map<Long, WorkoutUserActiveVO> userMap = new LinkedHashMap<>();

        for (WorkoutRecord item : records) {
            WorkoutUserActiveVO vo = userMap.computeIfAbsent(item.getUserId(), uid -> {
                WorkoutUserActiveVO tmp = new WorkoutUserActiveVO();
                tmp.setUserId(uid);
                tmp.setRecordCount(0);
                tmp.setTotalDurationSec(0);
                tmp.setTotalCalories(0);
                return tmp;
            });

            vo.setRecordCount(vo.getRecordCount() + 1);
            vo.setTotalDurationSec(vo.getTotalDurationSec() + (item.getDurationSec() == null ? 0 : item.getDurationSec()));
            vo.setTotalCalories(vo.getTotalCalories() + (item.getCalories() == null ? 0 : item.getCalories()));
        }

        if (!userMap.isEmpty()) {
            Map<Long, User> dbUserMap = userMapper.selectBatchIds(userMap.keySet()).stream()
                    .collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
            userMap.values().forEach(v -> {
                User u = dbUserMap.get(v.getUserId());
                if (u != null) {
                    v.setUsername(u.getUsername());
                    v.setNickname(u.getNickname());
                }
            });
        }

        return userMap.values().stream()
                .sorted((a, b) -> {
                    int cmp = Integer.compare(b.getTotalDurationSec(), a.getTotalDurationSec());
                    if (cmp != 0) return cmp;
                    cmp = Integer.compare(b.getTotalCalories(), a.getTotalCalories());
                    if (cmp != 0) return cmp;
                    return Integer.compare(b.getRecordCount(), a.getRecordCount());
                })
                .collect(Collectors.toList());
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private WorkoutRecord getOwnedRecord(Long userId, Long recordId) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<WorkoutRecord>()
                .eq(WorkoutRecord::getId, recordId)
                .eq(WorkoutRecord::getUserId, userId)
                .eq(WorkoutRecord::getIsDeleted, 0));
    }

    private LambdaQueryWrapper<WorkoutRecord> buildQueryWrapper(WorkoutRecordQueryDTO queryDTO) {
        WorkoutRecordQueryDTO q = queryDTO == null ? new WorkoutRecordQueryDTO() : queryDTO;
        LambdaQueryWrapper<WorkoutRecord> wrapper = new LambdaQueryWrapper<>();

        if (q.getId() != null) {
            wrapper.eq(WorkoutRecord::getId, q.getId());
        }
        if (q.getUserId() != null) {
            wrapper.eq(WorkoutRecord::getUserId, q.getUserId());
        }
        if (q.getCourseId() != null) {
            wrapper.eq(WorkoutRecord::getCourseId, q.getCourseId());
        }
        if (q.getCompleted() != null) {
            wrapper.eq(WorkoutRecord::getCompleted, q.getCompleted());
        }
        if (q.getAutoClosed() != null) {
            wrapper.eq(WorkoutRecord::getAutoClosed, q.getAutoClosed());
        }
        if (q.getStartTimeFrom() != null) {
            wrapper.ge(WorkoutRecord::getStartTime, q.getStartTimeFrom());
        }
        if (q.getStartTimeTo() != null) {
            wrapper.le(WorkoutRecord::getStartTime, q.getStartTimeTo());
        }
        return wrapper;
    }

    private void normalizeCreateRecord(WorkoutRecord record) {
        if (record.getStartTime() == null) {
            record.setStartTime(new Date());
        }
        if (record.getIsDeleted() == null) {
            record.setIsDeleted(0);
        }
        if (record.getAutoClosed() == null) {
            record.setAutoClosed(0);
        }
        if (StringUtils.isBlank(record.getCloseReason())) {
            record.setCloseReason("manual");
        }
        if (record.getEndTime() != null && record.getDurationSec() == null) {
            long sec = Math.max(0L, (record.getEndTime().getTime() - record.getStartTime().getTime()) / 1000L);
            record.setDurationSec((int) sec);
        }
        if (record.getCompleted() == null) {
            record.setCompleted(record.getEndTime() == null ? 0 : 1);
        }
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
    }

    private void ensureCourseAvailable(Long courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null || Integer.valueOf(0).equals(course.getStatus()) || Integer.valueOf(1).equals(course.getIsDeleted())) {
            throw new IllegalArgumentException("课程不存在或已下架");
        }
    }

    /**
     * 通用热量估算公式：
     * kcal = MET * 3.5 * 体重kg / 200 * 运动分钟 * 年龄系数 * 身高系数。
     */
    private Integer calculateCalories(Long userId, int durationSec) {
        User user = userMapper.selectById(userId);

        BigDecimal weight = user == null || user.getWeightKg() == null
                ? BigDecimal.valueOf(60)
                : user.getWeightKg();
        int age = user == null || user.getAge() == null ? 25 : user.getAge();
        int height = user == null || user.getHeightCm() == null ? 170 : user.getHeightCm();

        BigDecimal minutes = BigDecimal.valueOf(durationSec).divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);
        BigDecimal met = BigDecimal.valueOf(6.0);
        BigDecimal ageFactor = BigDecimal.valueOf(1.0 + Math.max(0, age - 20) * 0.003);
        BigDecimal heightFactor = BigDecimal.valueOf(1.0 + Math.max(0, height - 160) * 0.0015);

        BigDecimal calories = met.multiply(BigDecimal.valueOf(3.5))
                .multiply(weight)
                .divide(BigDecimal.valueOf(200), 6, RoundingMode.HALF_UP)
                .multiply(minutes)
                .multiply(ageFactor)
                .multiply(heightFactor);

        return calories.setScale(0, RoundingMode.HALF_UP).intValue();
    }
}

