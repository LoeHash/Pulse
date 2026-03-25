package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Course;
import com.ruoyi.common.core.domain.dto.CourseClientQueryDTO;
import com.ruoyi.common.core.domain.dto.CourseCreateDTO;
import com.ruoyi.common.core.domain.dto.CoursePageQueryDTO;
import com.ruoyi.common.core.domain.dto.CourseUpdateDTO;
import com.ruoyi.common.core.domain.vo.CourseParticipationAdminVO;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService extends IService<Course> {

    Long createCourse(CourseCreateDTO dto);

    int updateCourse(Long id, CourseUpdateDTO dto);

    int deleteCourse(Long id);

    Course getCourseByIdForAdmin(Long id);

    List<Course> selectCourseListForAdmin(CoursePageQueryDTO queryDTO);

    List<Course> selectCourseListForClient(CourseClientQueryDTO queryDTO);

    Course getCourseByIdForClient(Long id);

    int participateCourse(Long courseId, Long userId);

    int cancelParticipateCourse(Long courseId, Long userId);

    List<Course> selectMyCourses(Long userId);

    int favoriteCourse(Long courseId, Long userId);

    int cancelFavoriteCourse(Long courseId, Long userId);

    int likeCourse(Long courseId, Long userId);

    int cancelLikeCourse(Long courseId, Long userId);

    List<Course> selectMyFavoriteCourses(Long userId);

    List<Course> selectMyLikeCourses(Long userId);

    Long countCourseParticipationForAdmin(Long courseId);

    List<CourseParticipationAdminVO> selectCourseParticipationListForAdmin(Long courseId);
}

