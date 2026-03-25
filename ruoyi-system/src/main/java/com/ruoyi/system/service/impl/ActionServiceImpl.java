package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Action;
import com.ruoyi.common.utils.TagConditionUtils;
import com.ruoyi.system.mapper.ActionMapper;
import com.ruoyi.system.service.ActionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 动作模板Service业务层处理
 *
 * @author loe
 * @date 2026-03-22
 */
@Service
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements ActionService {

    /**
     * 查询动作列表
     *
     * @param action 动作
     * @return 动作
     */
    @Override
    public List<Action> selectActionList(Action action) {
        LambdaQueryWrapper<Action> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(action.getName())) {
            queryWrapper.like(Action::getName, action.getName());
        }
        if (StringUtils.hasText(action.getActionType())) {
            queryWrapper.eq(Action::getActionType, action.getActionType());
        }
        if (StringUtils.hasText(action.getUnitType())) {
            queryWrapper.eq(Action::getUnitType, action.getUnitType());
        }

        // 分类标签等查询
        if (action.getCategory() != null && !action.getCategory().isEmpty()) {
            TagConditionUtils.applyJsonLikeTags(queryWrapper, Action::getCategory, action.getCategory());
        }
        if (action.getGoalTags() != null && !action.getGoalTags().isEmpty()) {
            TagConditionUtils.applyJsonLikeTags(queryWrapper, Action::getGoalTags, action.getGoalTags());
        }
        if (action.getTags() != null && !action.getTags().isEmpty()) {
            TagConditionUtils.applyJsonLikeTags(queryWrapper, Action::getTags, action.getTags());
        }

        if (action.getStatus() != null) {
            queryWrapper.eq(Action::getStatus, action.getStatus());
        }
        if (action.getCreateUserId() != null) {
            queryWrapper.eq(Action::getCreateUserId, action.getCreateUserId());
        }

        // 默认查询未删除的
        queryWrapper.eq(Action::getIsDeleted, 0);
        queryWrapper.orderByDesc(Action::getCreateTime);

        return this.list(queryWrapper);
    }

    @Override
    public List<Action> selectActionListForClient(Action action, Long userId) {
        LambdaQueryWrapper<Action> queryWrapper = new LambdaQueryWrapper<>();

        // 基本字符串查询
        if (StringUtils.hasText(action.getName())) {
            queryWrapper.like(Action::getName, action.getName());
        }
        if (StringUtils.hasText(action.getActionType())) {
            queryWrapper.eq(Action::getActionType, action.getActionType());
        }
        if (StringUtils.hasText(action.getUnitType())) {
            queryWrapper.eq(Action::getUnitType, action.getUnitType());
        }

        // 分类标签等查询
        if (action.getCategory() != null && !action.getCategory().isEmpty()) {
            TagConditionUtils.applyJsonLikeTags(queryWrapper, Action::getCategory, action.getCategory());
        }
        if (action.getGoalTags() != null && !action.getGoalTags().isEmpty()) {
            TagConditionUtils.applyJsonLikeTags(queryWrapper, Action::getGoalTags, action.getGoalTags());
        }
        if (action.getTags() != null && !action.getTags().isEmpty()) {
            TagConditionUtils.applyJsonLikeTags(queryWrapper, Action::getTags, action.getTags());
        }

        // 只查询启用的
        queryWrapper.eq(Action::getStatus, 1);
        queryWrapper.eq(Action::getIsDeleted, 0);

        // 系统动作（create_user_id IS NULL） 或 当前用户的自定义动作
        if (userId != null) {
            queryWrapper.and(w -> w.isNull(Action::getCreateUserId)
                    .or()
                    .eq(Action::getCreateUserId, userId));
        } else {
            // 未登录或者只需要系统动作
            queryWrapper.isNull(Action::getCreateUserId);
        }

        queryWrapper.orderByDesc(Action::getCreateTime);

        return this.list(queryWrapper);
    }
}
