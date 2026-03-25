package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.SportRecord;
import com.ruoyi.common.core.domain.dto.SportRecordFinishDTO;
import com.ruoyi.common.core.domain.dto.SportRecordQueryDTO;
import com.ruoyi.common.core.domain.dto.SportRecordStartDTO;

import java.util.List;

public interface SportRecordService extends IService<SportRecord> {

    Long startSport(Long userId, SportRecordStartDTO dto);

    int finishSport(Long userId, Long recordId, SportRecordFinishDTO dto);

    List<SportRecord> selectMyRecords(Long userId, SportRecordQueryDTO queryDTO);

    SportRecord getMyRecord(Long userId, Long recordId);

    SportRecord getCurrentUnfinished(Long userId);

    int deleteMyRecord(Long userId, Long recordId);

    List<SportRecord> selectAdminList(SportRecordQueryDTO queryDTO);

    SportRecord getAdminDetail(Long id);

    int createByAdmin(SportRecord record);

    int updateByAdmin(Long id, SportRecord record);

    int deleteByAdmin(Long id);
}

