package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Action;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端动作模板接口
 */
@Tag(name = "管理端-动作模板接口", description = "动作库管理")
@RestController
@RequestMapping("/admin/action")
@Validated
public class AdminActionController extends BaseController {

    @Autowired
    private ActionService actionService;

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询动作列表")
    @GetMapping("/list")
    public TableDataInfo list(Action action) {
        startPage();
        List<Action> list = actionService.selectActionList(action);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "获取动作详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@Parameter(description = "动作ID", required = true) @PathVariable("id") Long id) {
        return AjaxResult.success(actionService.getById(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增动作")
    @PostMapping
    public AjaxResult add(@RequestBody Action action) {


        return toAjax(actionService.save(action));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改动作")
    @PutMapping
    public AjaxResult edit(@RequestBody Action action) {
        return toAjax(actionService.updateById(action));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除动作")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "动作ID串", required = true) @PathVariable List<Long> ids) {
        return toAjax(actionService.removeBatchByIds(ids));
    }
}

