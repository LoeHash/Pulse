package com.ruoyi.web.controller.base.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Plan;
import com.ruoyi.common.core.domain.dto.PlanQueryDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.TagConditionUtils;
import com.ruoyi.system.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 客户端-训练计划接口
 */
@Tag(name = "客户端-训练计划接口", description = "计划浏览")
@RestController
@RequestMapping("/client/plan")
public class ClientPlanController extends BaseController {

    private final PlanService planService;

    public ClientPlanController(PlanService planService) {
        this.planService = planService;
    }

    @Operation(summary = "计划列表")
    @GetMapping("/list")
    public TableDataInfo list(PlanQueryDTO dto) {
        startPage();
        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Plan::getStatus, 1); // 上架
        queryWrapper.eq(Plan::getIsDeleted, 0);

        if (StringUtils.hasText(dto.getTitle())) {
            queryWrapper.like(Plan::getTitle, dto.getTitle());
        }
        if (dto.getDifficulty() != null) {
            queryWrapper.eq(Plan::getDifficulty, dto.getDifficulty());
        }

        // 标签查询（模糊查询任意标签）
        if (StringUtils.hasText(dto.getTagKeyword())) {
            List<String> tags = Collections.singletonList(dto.getTagKeyword());
            queryWrapper.and(w -> {
                 // 这是一个复杂的OR逻辑，因为可以匹配 fitPeopleTags OR goalTags OR bodyPartTags
                 // 但 TagConditionUtils.applyJsonLikeTags 是 AND 逻辑添加到 wrapper 中
                 // 因此我们需要手动构建 OR 块

                 // 手动模拟 TagConditionUtils 的逻辑，但应用于三个字段的 OR 关系
                 // (fit LIKE tag OR goal LIKE tag OR body LIKE tag)
                 String likeValue = "\"" + dto.getTagKeyword() + "\"";
                 w.like(Plan::getFitPeopleTags, likeValue)
                  .or().like(Plan::getGoalTags, likeValue)
                  .or().like(Plan::getBodyPartTags, likeValue);
            });
        }
        
        queryWrapper.orderByDesc(Plan::getCreateTime);

        List<Plan> list = planService.list(queryWrapper);
        return getDataTable(list);
    }

    @Operation(summary = "计划详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@Parameter(description = "计划ID", required = true) @PathVariable Long id) {
        // 对于详情，直接复用 Service 中的 getPlanDetail，它已经组装了所有数据
        // 前端展示时直接渲染即可
        return success(planService.getPlanDetail(id));
    }
}

