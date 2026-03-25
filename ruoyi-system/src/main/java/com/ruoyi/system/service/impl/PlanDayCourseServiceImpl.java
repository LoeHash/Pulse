package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.PlanDayCourse;
import com.ruoyi.system.mapper.PlanDayCourseMapper;
import com.ruoyi.system.service.PlanDayCourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanDayCourseServiceImpl extends ServiceImpl<PlanDayCourseMapper, PlanDayCourse> implements PlanDayCourseService {

    @Override
    public List<PlanDayCourse> getCoursesByPlanId(Long planId) {
        return this.list(new LambdaQueryWrapper<PlanDayCourse>()
                .eq(PlanDayCourse::getPlanId, planId)
                .orderByAsc(PlanDayCourse::getSortNo));
    }

    @Override
    public List<PlanDayCourse> getCoursesByPlanDayId(Long planDayId) {
        return this.list(new LambdaQueryWrapper<PlanDayCourse>()
                .eq(PlanDayCourse::getPlanDayId, planDayId)
                .orderByAsc(PlanDayCourse::getSortNo));
    }
}

