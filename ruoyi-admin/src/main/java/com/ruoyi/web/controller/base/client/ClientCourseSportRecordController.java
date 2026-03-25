package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.WorkoutRecord;
import com.ruoyi.common.core.domain.dto.WorkoutRecordFinishDTO;
import com.ruoyi.common.core.domain.dto.WorkoutRecordQueryDTO;
import com.ruoyi.common.core.domain.dto.WorkoutRecordStartDTO;
import com.ruoyi.common.core.domain.vo.WorkoutClientCourseStatVO;
import com.ruoyi.common.core.domain.vo.WorkoutClientSummaryVO;
import com.ruoyi.common.core.domain.vo.WorkoutDailyStatVO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.system.service.WorkoutRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 客户端-课程运动记录接口（原 WorkoutRecord 的语义别名）
 */
@Tag(name = "客户端-课程运动记录接口", description = "课程运动记录")
@RestController
@RequestMapping("/client/course-sport-record")
public class ClientCourseSportRecordController extends BaseController {

    private final WorkoutRecordService workoutRecordService;

    public ClientCourseSportRecordController(WorkoutRecordService workoutRecordService) {
        this.workoutRecordService = workoutRecordService;
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "开始训练")
    @PostMapping("/start")
    public AjaxResult startWorkout(@Valid @RequestBody WorkoutRecordStartDTO dto) {
        User currentUser = UserHolder.getUser();
        try {
            Long recordId = workoutRecordService.startWorkout(currentUser.getId(), dto);
            return success(Map.of("recordId", recordId));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "结束训练")
    @PutMapping("/{id}/finish")
    public AjaxResult finishWorkout(
            @Parameter(description = "训练记录ID", required = true) @PathVariable("id") Long id,
            @Valid @RequestBody WorkoutRecordFinishDTO dto) {
        User currentUser = UserHolder.getUser();
        try {
            return toAjax(workoutRecordService.finishWorkout(currentUser.getId(), id, dto));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "分页查询我的训练记录")
    @GetMapping("/page")
    public TableDataInfo myWorkoutPage(WorkoutRecordQueryDTO queryDTO) {
        User currentUser = UserHolder.getUser();
        startPage();
        List<WorkoutRecord> list = workoutRecordService.selectMyWorkoutRecords(currentUser.getId(), queryDTO);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "查询我的训练记录详情")
    @GetMapping("/{id}")
    public AjaxResult myWorkoutDetail(
            @Parameter(description = "训练记录ID", required = true) @PathVariable("id") Long id) {
        User currentUser = UserHolder.getUser();
        WorkoutRecord record = workoutRecordService.getMyWorkoutRecord(currentUser.getId(), id);
        if (record == null) {
            return error("训练记录不存在");
        }
        return success(record);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "查询当前未结束训练")
    @GetMapping("/current")
    public AjaxResult currentWorkout() {
        User currentUser = UserHolder.getUser();
        WorkoutRecord record = workoutRecordService.getCurrentUnfinishedWorkout(currentUser.getId());
        if (record == null) {
            return success(Map.of());
        }
        return success(record);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "我的训练汇总统计")
    @GetMapping("/stat/summary")
    public AjaxResult myWorkoutSummary(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        User currentUser = UserHolder.getUser();
        WorkoutClientSummaryVO data = workoutRecordService.statMyWorkoutSummary(currentUser.getId(), startDate, endDate);
        return success(data);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "我的训练按日统计")
    @GetMapping("/stat/daily")
    public AjaxResult myWorkoutDaily(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        User currentUser = UserHolder.getUser();
        List<WorkoutDailyStatVO> data = workoutRecordService.statMyWorkoutDaily(currentUser.getId(), startDate, endDate);
        return success(data);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "我的训练课程分布")
    @GetMapping("/stat/course")
    public AjaxResult myWorkoutCourse(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        User currentUser = UserHolder.getUser();

        List<WorkoutClientCourseStatVO> data = workoutRecordService.statMyWorkoutCourse(currentUser.getId(), startDate, endDate);
        return success(data);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "删除我的训练记录")
    @DeleteMapping("/{id}")
    public AjaxResult deleteMyWorkout(
            @Parameter(description = "训练记录ID", required = true) @PathVariable("id") Long id) {
        User currentUser = UserHolder.getUser();
        return toAjax(workoutRecordService.deleteMyWorkoutRecord(currentUser.getId(), id));
    }
}

