package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.PlanDayCourse;

import java.util.List;

public interface PlanDayCourseService extends IService<PlanDayCourse> {
    List<PlanDayCourse> getCoursesByPlanId(Long planId);
    List<PlanDayCourse> getCoursesByPlanDayId(Long planDayId);
}

