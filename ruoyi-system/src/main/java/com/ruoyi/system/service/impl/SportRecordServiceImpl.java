package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Sport;
import com.ruoyi.common.core.domain.SportRecord;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.SportRecordFinishDTO;
import com.ruoyi.common.core.domain.dto.SportRecordQueryDTO;
import com.ruoyi.common.core.domain.dto.SportRecordStartDTO;
import com.ruoyi.system.mapper.SportMapper;
import com.ruoyi.system.mapper.SportRecordMapper;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.SportRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class SportRecordServiceImpl extends ServiceImpl<SportRecordMapper, SportRecord> implements SportRecordService {

    private final SportMapper sportMapper;
    private final UserMapper userMapper;

    public SportRecordServiceImpl(SportMapper sportMapper, UserMapper userMapper) {
        this.sportMapper = sportMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long startSport(Long userId, SportRecordStartDTO dto) {
        Sport sport = sportMapper.selectById(dto.getSportId());
        if (sport == null || Integer.valueOf(1).equals(sport.getIsDeleted())) {
            throw new IllegalArgumentException("运动类型不存在");
        }
        if (!Integer.valueOf(1).equals(sport.getStatus())) {
            throw new IllegalArgumentException("运动类型已禁用");
        }

        SportRecord record = new SportRecord();
        record.setUserId(userId);
        record.setSportId(dto.getSportId());
        record.setStartTime(dto.getStartTime() == null ? new Date() : dto.getStartTime());
        record.setCompleted(0);
        record.setAutoClosed(0);
        record.setCloseReason("manual");
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        this.save(record);
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int finishSport(Long userId, Long recordId, SportRecordFinishDTO dto) {
        SportRecord record = this.getById(recordId);
        if (record == null || Integer.valueOf(1).equals(record.getIsDeleted())) {
            throw new IllegalArgumentException("运动记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权操作该记录");
        }
        if (record.getEndTime() != null) {
            return 1;
        }

        Date endTime = dto.getEndTime() == null ? new Date() : dto.getEndTime();
        Integer durationSec = dto.getDurationSec();
        if (durationSec == null) {
            long diff = Math.max(0L, (endTime.getTime() - record.getStartTime().getTime()) / 1000L);
            durationSec = (int) diff;
        }

        Sport sport = sportMapper.selectById(record.getSportId());
        if (sport == null) {
            throw new IllegalArgumentException("运动类型不存在");
        }

        User user = userMapper.selectById(userId);

        int caloriesBase = calculateCaloriesBase(user, durationSec, dto.getAvgHr());
        BigDecimal factor = sport.getCalorieFactor() == null ? BigDecimal.ONE : sport.getCalorieFactor();
        int calories = BigDecimal.valueOf(caloriesBase)
                .multiply(factor)
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();

        record.setEndTime(endTime);
        record.setDurationSec(durationSec);
        record.setAvgHr(dto.getAvgHr());
        record.setCaloriesBase(caloriesBase);
        record.setCalorieFactorSnapshot(factor);
        record.setCalories(calories);
        record.setCompleted(dto.getCompleted() == null ? 1 : dto.getCompleted());
        record.setCloseReason(dto.getCloseReason() == null ? "manual" : dto.getCloseReason());
        record.setNote(dto.getNote());
        record.setMetrics(dto.getMetrics());
        record.setUpdateTime(new Date());
        this.updateById(record);
        return 1;
    }

    /**
     * 基础消耗估算。
     * 这里使用轻量级近似公式：
     * base = 体重kg * 运动小时 * 心率修正。
     */
    private int calculateCaloriesBase(User user, Integer durationSec, Integer avgHr) {
        double weight = 60.0d;
        if (user != null && user.getWeightKg() != null) {
            weight = user.getWeightKg().doubleValue();
        }

        double hours = Math.max(0, durationSec) / 3600.0d;
        double hrFactor = 1.0d;
        if (avgHr != null && avgHr > 0) {
            if (avgHr < 100) {
                hrFactor = 0.9d;
            } else if (avgHr < 140) {
                hrFactor = 1.0d;
            } else if (avgHr < 170) {
                hrFactor = 1.15d;
            } else {
                hrFactor = 1.3d;
            }
        }

        double base = weight * hours * 6.0d * hrFactor;
        return (int) Math.round(base);
    }

    @Override
    public List<SportRecord> selectMyRecords(Long userId, SportRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<SportRecord> wrapper = buildQuery(queryDTO)
                .eq(SportRecord::getUserId, userId)
                .orderByDesc(SportRecord::getCreateTime);
        List<SportRecord> list = this.list(wrapper);
        fillSportName(list);
        return list;
    }

    @Override
    public SportRecord getMyRecord(Long userId, Long recordId) {
        SportRecord record = this.getOne(new LambdaQueryWrapper<SportRecord>()
                .eq(SportRecord::getId, recordId)
                .eq(SportRecord::getUserId, userId));
        fillSportName(record);
        return record;
    }

    @Override
    public SportRecord getCurrentUnfinished(Long userId) {
        SportRecord record = this.getOne(new LambdaQueryWrapper<SportRecord>()
                .eq(SportRecord::getUserId, userId)
                .isNull(SportRecord::getEndTime)
                .orderByDesc(SportRecord::getCreateTime)
                .last("limit 1"));
        fillSportName(record);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMyRecord(Long userId, Long recordId) {
        return this.baseMapper.delete(new LambdaQueryWrapper<SportRecord>()
                .eq(SportRecord::getId, recordId)
                .eq(SportRecord::getUserId, userId));
    }

    @Override
    public List<SportRecord> selectAdminList(SportRecordQueryDTO queryDTO) {
        List<SportRecord> list = this.list(buildQuery(queryDTO).orderByDesc(SportRecord::getCreateTime));
        fillSportName(list);
        return list;
    }

    @Override
    public SportRecord getAdminDetail(Long id) {
        SportRecord record = this.getById(id);
        fillSportName(record);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createByAdmin(SportRecord record) {
        if (record.getStartTime() == null) {
            record.setStartTime(new Date());
        }
        if (record.getCompleted() == null) {
            record.setCompleted(0);
        }
        if (record.getCloseReason() == null) {
            record.setCloseReason("manual");
        }
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        return this.baseMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByAdmin(Long id, SportRecord record) {
        record.setId(id);
        record.setUpdateTime(new Date());
        return this.baseMapper.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByAdmin(Long id) {
        return this.baseMapper.deleteById(id);
    }

    private LambdaQueryWrapper<SportRecord> buildQuery(SportRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<SportRecord> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO == null) {
            return wrapper;
        }
        if (queryDTO.getSportId() != null) {
            wrapper.eq(SportRecord::getSportId, queryDTO.getSportId());
        }
        if (queryDTO.getCompleted() != null) {
            wrapper.eq(SportRecord::getCompleted, queryDTO.getCompleted());
        }
        if (queryDTO.getStartDate() != null) {
            wrapper.ge(SportRecord::getStartTime, queryDTO.getStartDate());
        }
        if (queryDTO.getEndDate() != null) {
            wrapper.le(SportRecord::getStartTime, queryDTO.getEndDate());
        }
        return wrapper;
    }

    private void fillSportName(List<SportRecord> list) {
        if (list == null) {
            return;
        }
        for (SportRecord record : list) {
            fillSportName(record);
        }
    }

    private void fillSportName(SportRecord record) {
        if (record == null) {
            return;
        }
        Sport sport = sportMapper.selectById(record.getSportId());
        if (sport != null) {
            record.setSportName(sport.getName());
        }
    }
}

