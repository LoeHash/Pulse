package com.ruoyi.common.enums;

import lombok.Getter;

/**
 * 运动类型枚举（对应 tb_sport.sport_type）
 */
@Getter
public enum SportTypeEnum {

    RUN("RUN", "跑步/走路"),
    HIIT("HIIT", "HIIT/间歇训练"),
    DANCE("DANCE", "舞蹈"),
    BALL("BALL", "球类"),
    OTHER("OTHER", "其他");

    private final String code;
    private final String zhName;

    SportTypeEnum(String code, String zhName) {
        this.code = code;
        this.zhName = zhName;
    }
}