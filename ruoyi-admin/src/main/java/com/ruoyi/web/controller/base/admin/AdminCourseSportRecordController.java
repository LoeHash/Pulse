package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.WorkoutRecord;
import com.ruoyi.common.core.domain.dto.WorkoutRecordQueryDTO;
import com.ruoyi.common.core.domain.vo.WorkoutCompletionTrendVO;
import com.ruoyi.common.core.domain.vo.WorkoutCourseRankVO;
import com.ruoyi.common.core.domain.vo.WorkoutDailyStatVO;
import com.ruoyi.common.core.domain.vo.WorkoutStatSummaryVO;
import com.ruoyi.common.core.domain.vo.WorkoutUserActiveVO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.WorkoutRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;

/**
 * 管理端训练记录接口
 */
@Tag(name = "管理端-训练记录接口", description = "训练记录基础管理")
@RestController
@RequestMapping("/admin/workout-record")
@Validated
public class AdminCourseSportRecordController extends BaseController {

    private final WorkoutRecordService workoutRecordService;

    public AdminCourseSportRecordController(WorkoutRecordService workoutRecordService) {
        this.workoutRecordService = workoutRecordService;
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增训练记录")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody WorkoutRecord record) {
        return toAjax(workoutRecordService.createWorkoutRecordByAdmin(record));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除训练记录")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@Parameter(description = "记录ID", required = true) @PathVariable Long id) {
        return toAjax(workoutRecordService.deleteWorkoutRecordByAdmin(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改训练记录")
    @PutMapping("/{id}")
    public AjaxResult edit(
            @Parameter(description = "记录ID", required = true) @PathVariable Long id,
            @Valid @RequestBody WorkoutRecord record) {
        try {
            return toAjax(workoutRecordService.updateWorkoutRecordByAdmin(id, record));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询训练记录详情")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "记录ID", required = true) @PathVariable Long id) {
        WorkoutRecord record = workoutRecordService.getWorkoutRecordByIdForAdmin(id);
        if (record == null) {
            return error("训练记录不存在");
        }
        return success(record);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询训练记录列表")
    @GetMapping("/list")
    public AjaxResult list(WorkoutRecordQueryDTO queryDTO) {
        List<WorkoutRecord> list = workoutRecordService.selectWorkoutRecordsForAdmin(queryDTO);
        return success(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "按日统计训练概览")
    @GetMapping("/stat/overview")
    public AjaxResult statOverview(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        List<WorkoutDailyStatVO> statList = workoutRecordService.statWorkoutOverviewByDay(startDate, endDate);
        return success(statList);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "按日分页统计训练概览")
    @GetMapping("/stat/overview/page")
    public TableDataInfo statOverviewPage(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        // 分页必须在首次ORM调用前声明。
        startPage();
        List<WorkoutDailyStatVO> statList = workoutRecordService.statWorkoutOverviewByDayPage(startDate, endDate);
        return getDataTable(statList);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "训练统计汇总")
    @GetMapping("/stat/summary")
    public AjaxResult statSummary(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        WorkoutStatSummaryVO summary = workoutRecordService.statWorkoutSummary(startDate, endDate);
        return success(summary);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "课程训练排行（分页）")
    @GetMapping("/stat/course-rank/page")
    public TableDataInfo statCourseRankPage(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        startPage();
        List<WorkoutCourseRankVO> list = workoutRecordService.statWorkoutCourseRank(startDate, endDate);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "用户训练活跃排行（分页）")
    @GetMapping("/stat/user-active/page")
    public TableDataInfo statUserActivePage(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        startPage();
        List<WorkoutUserActiveVO> list = workoutRecordService.statWorkoutUserActive(startDate, endDate);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "按日完课趋势（分页）")
    @GetMapping("/stat/completion-trend/page")
    public TableDataInfo statCompletionTrendPage(
            @Parameter(description = "开始时间") @RequestParam(value = "startDate", required = false) Date startDate,
            @Parameter(description = "结束时间") @RequestParam(value = "endDate", required = false) Date endDate) {
        startPage();
        List<WorkoutCompletionTrendVO> list = workoutRecordService.statWorkoutCompletionTrend(startDate, endDate);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "分页查询训练记录")
    @GetMapping("/page")
    public TableDataInfo page(WorkoutRecordQueryDTO queryDTO) {
        startPage();
        List<WorkoutRecord> list = workoutRecordService.selectWorkoutRecordsForAdmin(queryDTO);
        return getDataTable(list);
    }
}

