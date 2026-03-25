package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.SportRecord;
import com.ruoyi.common.core.domain.dto.SportRecordQueryDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.SportRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理端-运动记录接口", description = "运动记录管理")
@RestController
@RequestMapping("/admin/sport-record")
public class AdminSportRecordController extends BaseController {

    private final SportRecordService sportRecordService;

    public AdminSportRecordController(SportRecordService sportRecordService) {
        this.sportRecordService = sportRecordService;
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "分页查询运动记录")
    @GetMapping("/page")
    public TableDataInfo page(SportRecordQueryDTO queryDTO) {
        startPage();
        List<SportRecord> list = sportRecordService.selectAdminList(queryDTO);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询运动记录详情")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(sportRecordService.getAdminDetail(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增运动记录")
    @Log(title = "运动记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SportRecord record) {
        return toAjax(sportRecordService.createByAdmin(record));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改运动记录")
    @Log(title = "运动记录", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @RequestBody SportRecord record) {
        return toAjax(sportRecordService.updateByAdmin(id, record));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除运动记录")
    @Log(title = "运动记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(sportRecordService.deleteByAdmin(id));
    }
}

