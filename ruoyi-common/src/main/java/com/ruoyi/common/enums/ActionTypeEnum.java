package com.ruoyi.common.enums;

import lombok.Getter;

/**
 * 动作类型枚举（对应 tb_action.action_type）
 */
@Getter
public enum ActionTypeEnum {

    EXERCISE("EXERCISE", "训练动作"),
    REST("REST", "休息"),
    OTHER("OTHER", "其他");

    private final String code;
    private final String desc;

    ActionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}