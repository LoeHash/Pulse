package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.CourseAction;
import com.ruoyi.system.mapper.CourseActionMapper;
import com.ruoyi.system.service.CourseActionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程动作编排Service业务层处理
 *
 * @author loe
 * @date 2026-03-22
 */
@Service
public class CourseActionServiceImpl extends ServiceImpl<CourseActionMapper, CourseAction> implements CourseActionService {

    /**
     * 查询课程动作编排列表
     *
     * @param courseAction 课程动作编排
     * @return 课程动作编排
     */
    @Override
    public List<CourseAction> selectCourseActionList(CourseAction courseAction) {
        LambdaQueryWrapper<CourseAction> queryWrapper = new LambdaQueryWrapper<>();

        if (courseAction.getCourseId() != null) {
            queryWrapper.eq(CourseAction::getCourseId, courseAction.getCourseId());
        }
        if (courseAction.getActionId() != null) {
            queryWrapper.eq(CourseAction::getActionId, courseAction.getActionId());
        }
        if (courseAction.getStatus() != null) {
            queryWrapper.eq(CourseAction::getStatus, courseAction.getStatus());
        }

        queryWrapper.eq(CourseAction::getIsDeleted, 0); // 默认查询未删除
        queryWrapper.orderByAsc(CourseAction::getSortNo); // 按步骤序号排序

        return this.list(queryWrapper);
    }

    @Override
    public List<CourseAction> selectListByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseAction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseAction::getCourseId, courseId);
        queryWrapper.eq(CourseAction::getIsDeleted, 0);
        queryWrapper.orderByAsc(CourseAction::getSortNo);
        return this.list(queryWrapper);
    }

    @Override
    public boolean saveBatchActions(List<CourseAction> list) {
        return this.saveBatch(list);
    }
}

