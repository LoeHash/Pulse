package com.ruoyi.web.controller.base.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Plan;
import com.ruoyi.common.core.domain.dto.PlanCreateDTO;
import com.ruoyi.common.core.domain.dto.PlanUpdateDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-训练计划接口
 */
@Tag(name = "管理端-训练计划接口", description = "计划管理")
@RestController
@RequestMapping("/admin/plan")
@Validated
public class AdminPlanController extends BaseController {

    private final PlanService planService;

    public AdminPlanController(PlanService planService) {
        this.planService = planService;
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询计划列表")
    @GetMapping("/list")
    public TableDataInfo list(Plan plan) {
        startPage();
        List<Plan> list = planService.list(new LambdaQueryWrapper<Plan>()
                .like(StringUtils.hasText(plan.getTitle()), Plan::getTitle, plan.getTitle())
                .eq(plan.getDifficulty() != null, Plan::getDifficulty, plan.getDifficulty())
                .eq(plan.getStatus() != null, Plan::getStatus, plan.getStatus())
                .orderByDesc(Plan::getCreateTime));
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "获取计划详情（含天数与课程）")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@Parameter(description = "计划ID", required = true) @PathVariable Long id) {
        return success(planService.getPlanDetail(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增计划")
    @Log(title = "训练计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody PlanCreateDTO dto) {
        return success(planService.createPlan(dto));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改计划")
    @Log(title = "训练计划", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @Valid @RequestBody PlanUpdateDTO dto) {
        return toAjax(planService.updatePlan(id, dto));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除计划")
    @Log(title = "训练计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(planService.deletePlan(id));
    }
}

