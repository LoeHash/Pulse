package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.PlanDay;
import com.ruoyi.system.mapper.PlanDayMapper;
import com.ruoyi.system.service.PlanDayService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanDayServiceImpl extends ServiceImpl<PlanDayMapper, PlanDay> implements PlanDayService {

    @Override
    public List<PlanDay> getDaysByPlanId(Long planId) {
        return this.list(new LambdaQueryWrapper<PlanDay>()
                .eq(PlanDay::getPlanId, planId)
                .orderByAsc(PlanDay::getDayNo));
    }
}

