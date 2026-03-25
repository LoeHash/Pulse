package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Course;
import com.ruoyi.common.core.domain.Plan;
import com.ruoyi.common.core.domain.PlanDay;
import com.ruoyi.common.core.domain.PlanDayCourse;
import com.ruoyi.common.core.domain.dto.PlanCreateDTO;
import com.ruoyi.common.core.domain.dto.PlanDayCourseCreateDTO;
import com.ruoyi.common.core.domain.dto.PlanDayCreateDTO;
import com.ruoyi.common.core.domain.dto.PlanUpdateDTO;
import com.ruoyi.common.core.domain.vo.PlanDayCourseVO;
import com.ruoyi.common.core.domain.vo.PlanDayVO;
import com.ruoyi.common.core.domain.vo.PlanDetailVO;
import com.ruoyi.system.mapper.CourseMapper;
import com.ruoyi.system.mapper.PlanMapper;
import com.ruoyi.system.service.PlanDayCourseService;
import com.ruoyi.system.service.PlanDayService;
import com.ruoyi.system.service.PlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

    private final PlanDayService planDayService;
    private final PlanDayCourseService planDayCourseService;
    private final CourseMapper courseMapper;

    public PlanServiceImpl(PlanDayService planDayService, 
                           PlanDayCourseService planDayCourseService,
                           CourseMapper courseMapper) {
        this.planDayService = planDayService;
        this.planDayCourseService = planDayCourseService;
        this.courseMapper = courseMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPlan(PlanCreateDTO dto) {
        Plan plan = new Plan();
        BeanUtils.copyProperties(dto, plan);
        plan.setStatus(1); // 默认上架
        plan.setIsDeleted(0);
        plan.setCreateTime(new Date());
        plan.setUpdateTime(new Date());

        // 计算天数
        if (dto.getDays() != null) {
            plan.setDays(dto.getDays().size());
        } else {
            plan.setDays(0);
        }

        this.save(plan);

        if (dto.getDays() != null) {
            savePlanDays(plan.getId(), dto.getDays());
        }

        return plan.getId();
    }

    private void savePlanDays(Long planId, List<PlanDayCreateDTO> dayDTOs) {
        for (PlanDayCreateDTO dayDTO : dayDTOs) {
            PlanDay planDay = new PlanDay();
            BeanUtils.copyProperties(dayDTO, planDay);
            planDay.setPlanId(planId);
            planDay.setIsDeleted(0);
            planDay.setStatus(1);
            planDay.setCreateTime(new Date());
            planDay.setUpdateTime(new Date());
            
            planDayService.save(planDay);

            if (dayDTO.getCourses() != null) {
                savePlanDayCourses(planId, planDay.getId(), dayDTO.getCourses());
            }
        }
    }

    private void savePlanDayCourses(Long planId, Long planDayId, List<PlanDayCourseCreateDTO> courseDTOs) {
        for (PlanDayCourseCreateDTO courseDTO : courseDTOs) {
            PlanDayCourse pdc = new PlanDayCourse();
            BeanUtils.copyProperties(courseDTO, pdc);
            pdc.setPlanId(planId); // 冗余字段
            pdc.setPlanDayId(planDayId);
            pdc.setIsDeleted(0);
            pdc.setStatus(1);
            pdc.setCreateTime(new Date());
            pdc.setUpdateTime(new Date());
            
            planDayCourseService.save(pdc);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePlan(Long id, PlanUpdateDTO dto) {
        Plan plan = this.getById(id);
        if (plan == null) {
            throw new IllegalArgumentException("计划不存在");
        }

        BeanUtils.copyProperties(dto, plan);
        plan.setUpdateTime(new Date());
        
        // 如果更新了天数详情，重新计算天数
        if (dto.getDays() != null) {
            plan.setDays(dto.getDays().size());
        }

        this.updateById(plan);

        // 如果提供了Days数据，则全量替换（删除旧的，插入新的）
        if (dto.getDays() != null) {
            // 删除旧的 Day 和 Course
            planDayCourseService.remove(new LambdaQueryWrapper<PlanDayCourse>().eq(PlanDayCourse::getPlanId, id));
            planDayService.remove(new LambdaQueryWrapper<PlanDay>().eq(PlanDay::getPlanId, id));

            // 插入新的
            savePlanDays(id, dto.getDays());
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePlan(Long id) {
        // 逻辑删除 Plan
        int rows = this.baseMapper.deleteById(id);
        if (rows > 0) {
            // 逻辑删除关联的 Day 和 Course
            planDayService.remove(new LambdaQueryWrapper<PlanDay>().eq(PlanDay::getPlanId, id));
            planDayCourseService.remove(new LambdaQueryWrapper<PlanDayCourse>().eq(PlanDayCourse::getPlanId, id));
        }
        return rows;
    }

    @Override
    public Plan getPlanEntity(Long id) {
        return this.getById(id);
    }

    @Override
    public PlanDetailVO getPlanDetail(Long id) {
        Plan plan = this.getById(id);
        if (plan == null) {
            return null;
        }

        PlanDetailVO vo = new PlanDetailVO();
        BeanUtils.copyProperties(plan, vo);

        // 查询天数
        List<PlanDay> days = planDayService.getDaysByPlanId(id);
        if (days == null || days.isEmpty()) {
            vo.setPlanDays(Collections.emptyList());
            return vo;
        }

        // 查询有课程安排
        List<PlanDayCourse> planCourses = planDayCourseService.getCoursesByPlanId(id);
        
        // 批量查询课程详情
        List<Long> courseIds = planCourses.stream()
                .map(PlanDayCourse::getCourseId)
                .distinct()
                .collect(Collectors.toList());
        
        Map<Long, Course> courseMap = new HashMap<>();
        if (!courseIds.isEmpty()) {
            List<Course> courses = courseMapper.selectBatchIds(courseIds);
            courseMap = courses.stream().collect(Collectors.toMap(Course::getId, c -> c));
        }

        // 组装Days VO
        List<PlanDayVO> dayVOs = new ArrayList<>();
        Map<Long, List<PlanDayCourse>> dayCoursesMap = planCourses.stream()
                .collect(Collectors.groupingBy(PlanDayCourse::getPlanDayId));

        for (PlanDay day : days) {
            PlanDayVO dayVO = new PlanDayVO();
            dayVO.setId(day.getId());
            dayVO.setDayNo(day.getDayNo());
            dayVO.setTitle(day.getTitle());
            dayVO.setTips(day.getTips());

            List<PlanDayCourse> currentDayCourses = dayCoursesMap.getOrDefault(day.getId(), Collections.emptyList());
            // 按sortNo排序
            currentDayCourses.sort(Comparator.comparingInt(PlanDayCourse::getSortNo));

            List<PlanDayCourseVO> courseVOs = new ArrayList<>();
            for (PlanDayCourse pdc : currentDayCourses) {
                PlanDayCourseVO cVO = new PlanDayCourseVO();
                cVO.setId(pdc.getId());
                cVO.setSortNo(pdc.getSortNo());
                cVO.setCourseId(pdc.getCourseId());
                
                Course course = courseMap.get(pdc.getCourseId());
                if (course != null) {
                    cVO.setCourseTitle(course.getTitle());
                    cVO.setCourseCoverUrl(course.getCoverUrl());
                    cVO.setDurationSec(course.getDurationSec());
                    cVO.setDifficulty(course.getDifficulty());
                    cVO.setCalories(course.getCalories());
                }
                courseVOs.add(cVO);
            }
            dayVO.setCourses(courseVOs);
            dayVOs.add(dayVO);
        }
        
        vo.setPlanDays(dayVOs);
        return vo;
    }
}
