package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Action;

import java.util.List;

/**
 * 动作模板Service接口
 *
 * @author loe
 * @date 2026-03-22
 */
public interface ActionService extends IService<Action> {

    /**
     * 查询动作列表
     *
     * @param action 动作
     * @return 动作集合
     */
    public List<Action> selectActionList(Action action);

    /**
     * 查询客户端动作列表（系统动作 + 用户自定义动作）
     *
     * @param action 动作查询条件
     * @param userId 当前用户ID（可选）
     * @return 动作集合
     */
    public List<Action> selectActionListForClient(Action action, Long userId);
}
