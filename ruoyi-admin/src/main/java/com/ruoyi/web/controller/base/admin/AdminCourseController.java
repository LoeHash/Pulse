package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Course;
import com.ruoyi.common.core.domain.dto.CourseCreateDTO;
import com.ruoyi.common.core.domain.dto.CoursePageQueryDTO;
import com.ruoyi.common.core.domain.dto.CourseUpdateDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.vo.CourseParticipationAdminVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.VoUtils;
import com.ruoyi.system.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理端课程接口
 */
@Tag(name = "管理端-课程接口", description = "课程基础管理")
@RestController
@RequestMapping("/admin/course")
@Validated
public class AdminCourseController extends BaseController {

    @Autowired
    private CourseService courseService;

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增课程")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody CourseCreateDTO dto) {
        Long id = courseService.createCourse(dto);
        return AjaxResult.success("新增成功", id);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除课程")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@Parameter(description = "课程ID", required = true) @PathVariable Long id) {
        return toAjax(courseService.deleteCourse(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改课程")
    @PutMapping("/{id}")
    public AjaxResult edit(@Parameter(description = "课程ID", required = true) @PathVariable Long id,
                           @Valid @RequestBody CourseUpdateDTO dto) {
        try {
            return toAjax(courseService.updateCourse(id, dto));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "获取课程详情")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "课程ID", required = true) @PathVariable Long id) {
        Course course = courseService.getCourseByIdForAdmin(id);
        if (course == null) {
            return error("课程不存在");
        }
        return success(VoUtils.transCourseToVo(course));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询课程列表")
    @GetMapping("/list")
    public AjaxResult list(CoursePageQueryDTO queryDTO) {
        List<Course> list = courseService.selectCourseListForAdmin(queryDTO);
        return success(list.stream().map(VoUtils::transCourseToVo).toList());
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "分页查询课程")
    @GetMapping("/page")
    public TableDataInfo page(CoursePageQueryDTO queryDTO) {
        startPage();
        List<Course> list = courseService.selectCourseListForAdmin(queryDTO);
        List<?> voList = list.stream().map(VoUtils::transCourseToVo).toList();
        TableDataInfo rspData = getDataTable(list);
        rspData.setRows(voList);
        return rspData;
    }

    /**
     * 查询某个课程的参与概览信息。
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询课程参与概览")
    @GetMapping("/{id}/participation/summary")
    public AjaxResult participationSummary(
            @Parameter(description = "课程ID", required = true) @PathVariable("id") Long courseId) {
        try {
            Long count = courseService.countCourseParticipationForAdmin(courseId);
            return success(Map.of("courseId", courseId, "participantCount", count));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 查询某个课程的参与明细（分页）。
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询课程参与明细")
    @GetMapping("/{id}/participation/page")
    public TableDataInfo participationPage(
            @Parameter(description = "课程ID", required = true) @PathVariable("id") Long courseId) {
        try {
            startPage();
            List<CourseParticipationAdminVO> list = courseService.selectCourseParticipationListForAdmin(courseId);
            return getDataTable(list);
        } catch (IllegalArgumentException e) {
            TableDataInfo table = new TableDataInfo();
            table.setCode(HttpStatus.ERROR);
            table.setMsg(e.getMessage());
            table.setRows(List.of());
            table.setTotal(0);
            return table;
        }
    }
}
