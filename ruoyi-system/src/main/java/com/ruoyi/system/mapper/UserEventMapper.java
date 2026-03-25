package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.UserEvent;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户行为事件 Mapper
 *
 * 说明：
 * 继承 BaseMapper 后，具备通用 CRUD 能力
 */
@Mapper
public interface UserEventMapper extends BaseMapper<UserEvent> {
}