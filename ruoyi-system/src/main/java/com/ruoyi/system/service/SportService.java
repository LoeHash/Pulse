package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Sport;

import java.util.List;

public interface SportService extends IService<Sport> {

    List<Sport> selectSportList(Sport query);

    List<Sport> selectSportListForClient(Sport query);
}

