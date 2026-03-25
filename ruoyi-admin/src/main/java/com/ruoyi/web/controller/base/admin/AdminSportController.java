package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Sport;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.SportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理端-运动接口", description = "运动种类管理")
@RestController
@RequestMapping("/admin/sport")
public class AdminSportController extends BaseController {

    private final SportService sportService;

    public AdminSportController(SportService sportService) {
        this.sportService = sportService;
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询运动列表")
    @GetMapping("/list")
    public TableDataInfo list(Sport sport) {
        startPage();
        List<Sport> list = sportService.selectSportList(sport);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "获取运动详情")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(sportService.getById(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增运动")
    @Log(title = "运动种类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Sport sport) {
        return toAjax(sportService.save(sport));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改运动")
    @Log(title = "运动种类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Sport sport) {
        return toAjax(sportService.updateById(sport));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除运动")
    @Log(title = "运动种类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(sportService.removeById(id));
    }
}

