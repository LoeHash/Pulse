package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Plan;
import com.ruoyi.common.core.domain.dto.PlanCreateDTO;
import com.ruoyi.common.core.domain.dto.PlanUpdateDTO;
import com.ruoyi.common.core.domain.vo.PlanDetailVO;

public interface PlanService extends IService<Plan> {
    
    Long createPlan(PlanCreateDTO dto);

    int updatePlan(Long id, PlanUpdateDTO dto);

    int deletePlan(Long id);

    Plan getPlanEntity(Long id);

    PlanDetailVO getPlanDetail(Long id);
}
