package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.CourseAction;

import java.util.List;

/**
 * 课程动作编排Service接口
 *
 * @author loe
 * @date 2026-03-22
 */
public interface CourseActionService extends IService<CourseAction> {

    /**
     * 查询课程动作编排列表
     *
     * @param courseAction 课程动作编排
     * @return 课程动作编排集合
     */
    public List<CourseAction> selectCourseActionList(CourseAction courseAction);

    /**
     * 根据课程ID查询动作列表
     *
     * @param courseId 课程ID
     * @return 结果
     */
    public List<CourseAction> selectListByCourseId(Long courseId);

    /**
     * 批量插入课程动作
     * @param list 列表
     * @return 结果
     */
    public boolean saveBatchActions(List<CourseAction> list);
}

