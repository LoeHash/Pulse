package com.ruoyi.system.config.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_config")
public class Config {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置key（唯一）
     */
    private String configKey;

    /**
     * 配置值（JSON）
     */
    private String configValue;

    /**
     * 配置类型
     */
    private String configType;

    /**
     * 描述
     */
    private String description;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}