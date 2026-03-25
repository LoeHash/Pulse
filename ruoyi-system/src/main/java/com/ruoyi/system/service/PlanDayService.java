package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.PlanDay;

import java.util.List;

public interface PlanDayService extends IService<PlanDay> {
    List<PlanDay> getDaysByPlanId(Long planId);
}

