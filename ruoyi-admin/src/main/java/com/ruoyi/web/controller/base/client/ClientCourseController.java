package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Course;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.CourseClientQueryDTO;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.common.utils.VoUtils;
import com.ruoyi.system.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户端课程接口
 */
@Tag(name = "客户端-课程接口", description = "课程浏览与参与")
@RestController
@RequestMapping("/client/course")
public class ClientCourseController extends BaseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "课程列表")
    @GetMapping("/list")
    public AjaxResult list(CourseClientQueryDTO queryDTO) {
        List<Course> list = courseService.selectCourseListForClient(queryDTO);
        return success(list.stream().map(VoUtils::transCourseToVo).toList());
    }

    @Operation(summary = "课程详情")
    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @GetMapping("/{id}")
    public AjaxResult detail(@Parameter(description = "课程ID", required = true) @PathVariable Long id) {
        Course course = courseService.getCourseByIdForClient(id);
        if (course == null) {
            return error("课程不存在或已下架");
        }
        Long createUserId = course.getCreateUserId();

        if (createUserId == null){
            return success(VoUtils.transCourseToVo(course));
        }

        if (!UserHolder.getUser().getId().equals(createUserId)) {
            return error("您无权查看此课程!");
        }


        return success(VoUtils.transCourseToVo(course));
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "参与课程")
    @PostMapping("/{id}/participate")
    public AjaxResult participate(@Parameter(description = "课程ID", required = true) @PathVariable("id") Long courseId) {
        User currentUser = UserHolder.getUser();
        try {
            return toAjax(courseService.participateCourse(courseId, currentUser.getId()));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "取消参与课程")
    @DeleteMapping("/{id}/participate")
    public AjaxResult cancelParticipate(@Parameter(description = "课程ID", required = true) @PathVariable("id") Long courseId) {
        User currentUser = UserHolder.getUser();
        return toAjax(courseService.cancelParticipateCourse(courseId, currentUser.getId()));
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "我的课程")
    @GetMapping("/my/list")
    public AjaxResult myCourses() {
        User currentUser = UserHolder.getUser();
        List<Course> list = courseService.selectMyCourses(currentUser.getId());
        return success(list.stream().map(VoUtils::transCourseToVo).toList());
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "收藏课程")
    @PostMapping("/{id}/favorite")
    public AjaxResult favorite(@Parameter(description = "课程ID", required = true) @PathVariable("id") Long courseId) {
        User currentUser = UserHolder.getUser();
        try {
            return toAjax(courseService.favoriteCourse(courseId, currentUser.getId()));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "取消收藏课程")
    @DeleteMapping("/{id}/favorite")
    public AjaxResult cancelFavorite(@Parameter(description = "课程ID", required = true) @PathVariable("id") Long courseId) {
        User currentUser = UserHolder.getUser();
        return toAjax(courseService.cancelFavoriteCourse(courseId, currentUser.getId()));
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "点赞课程")
    @PostMapping("/{id}/like")
    public AjaxResult like(@Parameter(description = "课程ID", required = true) @PathVariable("id") Long courseId) {
        User currentUser = UserHolder.getUser();
        try {
            return toAjax(courseService.likeCourse(courseId, currentUser.getId()));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "取消点赞课程")
    @DeleteMapping("/{id}/like")
    public AjaxResult cancelLike(@Parameter(description = "课程ID", required = true) @PathVariable("id") Long courseId) {
        User currentUser = UserHolder.getUser();
        return toAjax(courseService.cancelLikeCourse(courseId, currentUser.getId()));
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "我的收藏课程")
    @GetMapping("/my/favorites")
    public AjaxResult myFavorites() {
        User currentUser = UserHolder.getUser();
        List<Course> list = courseService.selectMyFavoriteCourses(currentUser.getId());
        return success(list.stream().map(VoUtils::transCourseToVo).toList());
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "我的点赞课程")
    @GetMapping("/my/likes")
    public AjaxResult myLikes() {
        User currentUser = UserHolder.getUser();
        List<Course> list = courseService.selectMyLikeCourses(currentUser.getId());
        return success(list.stream().map(VoUtils::transCourseToVo).toList());
    }
}

