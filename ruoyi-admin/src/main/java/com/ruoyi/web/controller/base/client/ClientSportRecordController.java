package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.SportRecord;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.SportRecordFinishDTO;
import com.ruoyi.common.core.domain.dto.SportRecordQueryDTO;
import com.ruoyi.common.core.domain.dto.SportRecordStartDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.system.service.SportRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "客户端-运动记录接口", description = "我的运动记录")
@RestController
@RequestMapping("/client/sport-record")
public class ClientSportRecordController extends BaseController {

    private final SportRecordService sportRecordService;

    public ClientSportRecordController(SportRecordService sportRecordService) {
        this.sportRecordService = sportRecordService;
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "开始运动")
    @PostMapping("/start")
    public AjaxResult start(@Valid @RequestBody SportRecordStartDTO dto) {
        User user = UserHolder.getUser();
        Long id = sportRecordService.startSport(user.getId(), dto);
        return success(Map.of("recordId", id));
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "结束运动")
    @PutMapping("/{id}/finish")
    public AjaxResult finish(@PathVariable Long id, @RequestBody SportRecordFinishDTO dto) {
        User user = UserHolder.getUser();
        return toAjax(sportRecordService.finishSport(user.getId(), id, dto));
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "分页查询我的运动记录")
    @GetMapping("/page")
    public TableDataInfo page(SportRecordQueryDTO queryDTO) {
        User user = UserHolder.getUser();
        startPage();
        List<SportRecord> list = sportRecordService.selectMyRecords(user.getId(), queryDTO);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "我的运动记录详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        User user = UserHolder.getUser();
        SportRecord record = sportRecordService.getMyRecord(user.getId(), id);
        if (record == null) {
            return error("记录不存在");
        }
        return success(record);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "查询当前未结束运动")
    @GetMapping("/current")
    public AjaxResult current() {
        User user = UserHolder.getUser();
        SportRecord record = sportRecordService.getCurrentUnfinished(user.getId());
        return success(record == null ? Map.of() : record);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "删除我的运动记录")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        User user = UserHolder.getUser();
        return toAjax(sportRecordService.deleteMyRecord(user.getId(), id));
    }
}

