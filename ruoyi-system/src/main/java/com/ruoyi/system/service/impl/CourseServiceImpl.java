package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.ruoyi.common.core.domain.Action;
import com.ruoyi.common.core.domain.Course;
import com.ruoyi.common.core.domain.CourseAction;
import com.ruoyi.common.core.domain.CourseFavorite;
import com.ruoyi.common.core.domain.CourseLike;
import com.ruoyi.common.core.domain.CourseParticipation;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.CourseClientQueryDTO;
import com.ruoyi.common.core.domain.dto.CourseCreateDTO;
import com.ruoyi.common.core.domain.dto.CoursePageQueryDTO;
import com.ruoyi.common.core.domain.dto.CourseUpdateDTO;
import com.ruoyi.common.core.domain.vo.CourseParticipationAdminVO;
import com.ruoyi.system.mapper.ActionMapper;
import com.ruoyi.system.mapper.CourseMapper;
import com.ruoyi.system.mapper.CourseFavoriteMapper;
import com.ruoyi.system.mapper.CourseLikeMapper;
import com.ruoyi.system.mapper.CourseParticipationMapper;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.CourseActionService;
import com.ruoyi.system.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 课程服务实现
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private final CourseParticipationMapper courseParticipationMapper;
    private final CourseFavoriteMapper courseFavoriteMapper;
    private final CourseLikeMapper courseLikeMapper;
    private final UserMapper userMapper;
    private final CourseActionService courseActionService;
    private final ActionMapper actionMapper;

    public CourseServiceImpl(CourseParticipationMapper courseParticipationMapper,
                             CourseFavoriteMapper courseFavoriteMapper,
                             CourseLikeMapper courseLikeMapper,
                             UserMapper userMapper,
                             CourseActionService courseActionService,
                             ActionMapper actionMapper) {
        this.courseParticipationMapper = courseParticipationMapper;
        this.courseFavoriteMapper = courseFavoriteMapper;
        this.courseLikeMapper = courseLikeMapper;
        this.userMapper = userMapper;
        this.courseActionService = courseActionService;
        this.actionMapper = actionMapper;
    }

    /**
     * 新增课程。
     *
     * @param dto 课程新增参数
     * @return 新增后的课程ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCourse(CourseCreateDTO dto) {
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);

        if (course.getStatus() == null) {
            course.setStatus(1);
        }
        if (course.getIsDeleted() == null) {
            course.setIsDeleted(0);
        }
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());

        this.baseMapper.insert(course);

        // 保存课程动作
        if (dto.getActions() != null && !dto.getActions().isEmpty()) {
            List<CourseAction> actionList = dto.getActions().stream().map(item -> {
                CourseAction ca = new CourseAction();
                ca.setCourseId(course.getId());
                ca.setActionId(item.getActionId());
                ca.setSortNo(item.getSortNo());
                ca.setTargetValue(item.getTargetValue());
                ca.setTipsOverride(item.getTipsOverride());
                ca.setMetricsOverride(item.getMetricsOverride());
                ca.setStatus(1);
                ca.setIsDeleted(0);
                ca.setCreateTime(new Date());
                ca.setUpdateTime(new Date());
                return ca;
            }).collect(Collectors.toList());
            courseActionService.saveBatchActions(actionList);
        }

        return course.getId();
    }

    /**
     * 修改课程。
     *
     * @param id 课程ID
     * @param dto 修改参数
     * @return 影响行数
     * @throws IllegalArgumentException 课程不存在时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCourse(Long id, CourseUpdateDTO dto) {
        Course dbCourse = this.baseMapper.selectById(id);
        if (dbCourse == null || Integer.valueOf(1).equals(dbCourse.getIsDeleted())) {
            throw new IllegalArgumentException("课程不存在");
        }

        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        course.setId(id);
        course.setUpdateTime(new Date());
        int rows = this.baseMapper.updateById(course);

        // 更新课程动作（先删后增）
        if (dto.getActions() != null) {
            // 删除旧的
            courseActionService.remove(new LambdaQueryWrapper<CourseAction>()
                    .eq(CourseAction::getCourseId, id));
            
            // 插入新的
            if (!dto.getActions().isEmpty()) {
                List<CourseAction> actionList = dto.getActions().stream().map(item -> {
                    CourseAction ca = new CourseAction();
                    ca.setCourseId(id);
                    ca.setActionId(item.getActionId());
                    ca.setSortNo(item.getSortNo());
                    ca.setTargetValue(item.getTargetValue());
                    ca.setTipsOverride(item.getTipsOverride());
                    ca.setMetricsOverride(item.getMetricsOverride());
                    ca.setStatus(1);
                    ca.setIsDeleted(0);
                    ca.setCreateTime(new Date());
                    ca.setUpdateTime(new Date());
                    return ca;
                }).collect(Collectors.toList());
                courseActionService.saveBatchActions(actionList);
            }
        }

        return rows;
    }

    /**
     * 删除课程（物理删除）。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCourse(Long id) {
        int rows = this.baseMapper.deleteById(id);
        if (rows > 0) {
            // 课程删除后同步清理参与关系，避免脏数据。
            courseParticipationMapper.delete(new LambdaQueryWrapper<CourseParticipation>()
                    .eq(CourseParticipation::getCourseId, id));
            
            // 删除课程动作
            courseActionService.remove(new LambdaQueryWrapper<CourseAction>()
                    .eq(CourseAction::getCourseId, id));
        }
        return rows;
    }

    @Override
    public Course getCourseByIdForAdmin(Long id) {
        Course course = this.baseMapper.selectById(id);
        if (course != null) {
            fillCourseActions(course);
        }
        return course;
    }

    @Override
    public List<Course> selectCourseListForAdmin(CoursePageQueryDTO queryDTO) {
        LambdaQueryWrapper<Course> wrapper = buildCommonQuery(queryDTO.getTitle(), queryDTO.getDifficulty(),
                queryDTO.getCategoryKeyword(), queryDTO.getGoalTagKeyword());

        if (queryDTO.getId() != null) {
            wrapper.eq(Course::getId, queryDTO.getId());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(Course::getStatus, queryDTO.getStatus());
        }

        wrapper.orderByDesc(Course::getCreateTime);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<Course> selectCourseListForClient(CourseClientQueryDTO queryDTO) {
        LambdaQueryWrapper<Course> wrapper = buildCommonQuery(queryDTO.getTitle(), queryDTO.getDifficulty(),
                queryDTO.getCategoryKeyword(), queryDTO.getGoalTagKeyword());

        wrapper.eq(Course::getStatus, 1)
                .eq(Course::getIsDeleted, 0)
                .orderByDesc(Course::getCreateTime);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public Course getCourseByIdForClient(Long id) {
        Course course = this.baseMapper.selectOne(new LambdaQueryWrapper<Course>()
                .eq(Course::getId, id)
                .eq(Course::getStatus, 1)
                .eq(Course::getIsDeleted, 0));
        
        if (course != null) {
            fillCourseActions(course);
        }
        return course;
    }

    /**
     * 填充课程动作信息
     */
    private void fillCourseActions(Course course) {
        List<CourseAction> courseActions = courseActionService.selectListByCourseId(course.getId());
        if (courseActions != null && !courseActions.isEmpty()) {
            // 批量查询动作详情
            List<Long> actionIds = courseActions.stream()
                    .map(CourseAction::getActionId)
                    .distinct()
                    .collect(Collectors.toList());
            
            if (!actionIds.isEmpty()) {
                List<Action> actions = actionMapper.selectBatchIds(actionIds);
                Map<Long, Action> actionMap = actions.stream()
                        .collect(Collectors.toMap(Action::getId, a -> a));
                
                for (CourseAction ca : courseActions) {
                    ca.setAction(actionMap.get(ca.getActionId()));
                }
            }
            course.setCourseActions(courseActions);
        } else {
            course.setCourseActions(Collections.emptyList());
        }
    }

    /**
     * 当前用户参与课程，重复参与时按幂等成功处理。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int participateCourse(Long courseId, Long userId) {
        Course course = getCourseByIdForClient(courseId);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在或已下架");
        }

        CourseParticipation exist = courseParticipationMapper.selectOne(new LambdaQueryWrapper<CourseParticipation>()
                .eq(CourseParticipation::getCourseId, courseId)
                .eq(CourseParticipation::getUserId, userId)
                .eq(CourseParticipation::getStatus, 1));
        if (exist != null) {
            return 1;
        }

        CourseParticipation relation = new CourseParticipation();
        relation.setCourseId(courseId);
        relation.setUserId(userId);
        relation.setStatus(1);
        relation.setCreateTime(new Date());
        relation.setUpdateTime(new Date());
        return courseParticipationMapper.insert(relation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelParticipateCourse(Long courseId, Long userId) {
        return courseParticipationMapper.delete(new LambdaQueryWrapper<CourseParticipation>()
                .eq(CourseParticipation::getCourseId, courseId)
                .eq(CourseParticipation::getUserId, userId));
    }

    @Override
    public List<Course> selectMyCourses(Long userId) {
        List<CourseParticipation> relations = courseParticipationMapper.selectList(
                new LambdaQueryWrapper<CourseParticipation>()
                        .eq(CourseParticipation::getUserId, userId)
                        .eq(CourseParticipation::getStatus, 1)
                        .orderByDesc(CourseParticipation::getCreateTime));

        if (relations == null || relations.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> courseIds = relations.stream()
                .map(CourseParticipation::getCourseId)
                .distinct()
                .collect(Collectors.toList());

        return this.baseMapper.selectList(new LambdaQueryWrapper<Course>()
                .in(Course::getId, courseIds)
                .eq(Course::getStatus, 1)
                .eq(Course::getIsDeleted, 0)
                .orderByDesc(Course::getCreateTime));
    }

    /**
     * 收藏课程，重复收藏按幂等成功处理。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int favoriteCourse(Long courseId, Long userId) {
        ensureCourseAvailableForUser(courseId);

        CourseFavorite exist = courseFavoriteMapper.selectOne(new LambdaQueryWrapper<CourseFavorite>()
                .eq(CourseFavorite::getCourseId, courseId)
                .eq(CourseFavorite::getUserId, userId)
                .eq(CourseFavorite::getStatus, 1));
        if (exist != null) {
            return 1;
        }

        CourseFavorite relation = new CourseFavorite();
        relation.setCourseId(courseId);
        relation.setUserId(userId);
        relation.setStatus(1);
        relation.setCreateTime(new Date());
        relation.setUpdateTime(new Date());
        return courseFavoriteMapper.insert(relation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelFavoriteCourse(Long courseId, Long userId) {
        return courseFavoriteMapper.delete(new LambdaQueryWrapper<CourseFavorite>()
                .eq(CourseFavorite::getCourseId, courseId)
                .eq(CourseFavorite::getUserId, userId));
    }

    /**
     * 点赞课程，重复点赞按幂等成功处理。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int likeCourse(Long courseId, Long userId) {
        ensureCourseAvailableForUser(courseId);

        CourseLike exist = courseLikeMapper.selectOne(new LambdaQueryWrapper<CourseLike>()
                .eq(CourseLike::getCourseId, courseId)
                .eq(CourseLike::getUserId, userId)
                .eq(CourseLike::getStatus, 1));
        if (exist != null) {
            return 1;
        }

        CourseLike relation = new CourseLike();
        relation.setCourseId(courseId);
        relation.setUserId(userId);
        relation.setStatus(1);
        relation.setCreateTime(new Date());
        relation.setUpdateTime(new Date());
        return courseLikeMapper.insert(relation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelLikeCourse(Long courseId, Long userId) {
        return courseLikeMapper.delete(new LambdaQueryWrapper<CourseLike>()
                .eq(CourseLike::getCourseId, courseId)
                .eq(CourseLike::getUserId, userId));
    }

    @Override
    public List<Course> selectMyFavoriteCourses(Long userId) {
        List<CourseFavorite> relations = courseFavoriteMapper.selectList(
                new LambdaQueryWrapper<CourseFavorite>()
                        .eq(CourseFavorite::getUserId, userId)
                        .eq(CourseFavorite::getStatus, 1)
                        .orderByDesc(CourseFavorite::getCreateTime));
        return queryCoursesByRelationIds(
                relations.stream().map(CourseFavorite::getCourseId).collect(Collectors.toList())
        );
    }

    @Override
    public List<Course> selectMyLikeCourses(Long userId) {
        List<CourseLike> relations = courseLikeMapper.selectList(
                new LambdaQueryWrapper<CourseLike>()
                        .eq(CourseLike::getUserId, userId)
                        .eq(CourseLike::getStatus, 1)
                        .orderByDesc(CourseLike::getCreateTime));
        return queryCoursesByRelationIds(
                relations.stream().map(CourseLike::getCourseId).collect(Collectors.toList())
        );
    }

    /**
     * 查询课程参与人数（仅统计有效参与记录）。
     *
     * @param courseId 课程ID
     * @return 参与人数
     */
    @Override
    public Long countCourseParticipationForAdmin(Long courseId) {
        ensureCourseExists(courseId);
        return courseParticipationMapper.selectCount(new LambdaQueryWrapper<CourseParticipation>()
                .eq(CourseParticipation::getCourseId, courseId)
                .eq(CourseParticipation::getStatus, 1));
    }

    /**
     * 查询课程参与明细（管理端）。
     *
     * @param courseId 课程ID
     * @return 参与明细列表（含用户基本信息）
     */
    @Override
    public List<CourseParticipationAdminVO> selectCourseParticipationListForAdmin(Long courseId) {
        ensureCourseExists(courseId);

        List<CourseParticipation> relations = courseParticipationMapper.selectList(
                new LambdaQueryWrapper<CourseParticipation>()
                        .eq(CourseParticipation::getCourseId, courseId)
                        .eq(CourseParticipation::getStatus, 1)
                        .orderByDesc(CourseParticipation::getCreateTime));

        if (relations == null || relations.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> userIds = relations.stream()
                .map(CourseParticipation::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            userMap = userMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
        }

        Map<Long, User> finalUserMap = userMap;
        List<CourseParticipationAdminVO> resultList = relations.stream().map(item -> {
            CourseParticipationAdminVO vo = new CourseParticipationAdminVO();
            vo.setId(item.getId());
            vo.setCourseId(item.getCourseId());
            vo.setUserId(item.getUserId());
            vo.setStatus(item.getStatus());
            vo.setCreateTime(item.getCreateTime());

            User user = finalUserMap.get(item.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
                vo.setNickname(user.getNickname());
            }
            return vo;
        }).collect(Collectors.toList());

        if (relations instanceof Page) {
            Page<CourseParticipation> p = (Page<CourseParticipation>) relations;
            Page<CourseParticipationAdminVO> resultPage = new Page<>(p.getPageNum(), p.getPageSize());
            resultPage.setTotal(p.getTotal());
            resultPage.addAll(resultList);
            return resultPage;
        }

        return resultList;
    }

    private void ensureCourseExists(Long courseId) {
        Course course = this.baseMapper.selectById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
    }

    private void ensureCourseAvailableForUser(Long courseId) {
        Course course = getCourseByIdForClient(courseId);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在或已下架");
        }
    }

    private List<Course> queryCoursesByRelationIds(List<Long> rawCourseIds) {
        if (rawCourseIds == null || rawCourseIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> courseIds = rawCourseIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (courseIds.isEmpty()) {
            return Collections.emptyList();
        }

        return this.baseMapper.selectList(new LambdaQueryWrapper<Course>()
                .in(Course::getId, courseIds)
                .eq(Course::getStatus, 1)
                .eq(Course::getIsDeleted, 0)
                .orderByDesc(Course::getCreateTime));
    }

    private LambdaQueryWrapper<Course> buildCommonQuery(String title,
                                                        Integer difficulty,
                                                        String categoryKeyword,
                                                        String goalTagKeyword) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(title)) {
            wrapper.like(Course::getTitle, title);
        }
        if (difficulty != null) {
            wrapper.eq(Course::getDifficulty, difficulty);
        }
        if (StringUtils.isNotBlank(categoryKeyword)) {
            wrapper.like(Course::getCategory, categoryKeyword);
        }
        if (StringUtils.isNotBlank(goalTagKeyword)) {
            wrapper.like(Course::getGoalTags, goalTagKeyword);
        }
        return wrapper;
    }
}





