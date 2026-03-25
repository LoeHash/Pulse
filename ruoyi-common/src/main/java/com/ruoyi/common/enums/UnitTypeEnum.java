package com.ruoyi.common.enums;

import lombok.Getter;

/**
 * 单位类型枚举（对应 tb_action.unit_type）
 * 规则：unit_type 以动作模板为准，编排表默认不允许改
 */
@Getter
public enum UnitTypeEnum {

    REPS("REPS", "次数"),
    SECONDS("SECONDS", "秒");

    private final String code;
    private final String desc;

    UnitTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}