package com.ruoyi.common.enums;

import com.ruoyi.common.constant.SystemConstant;
import lombok.Getter;

@Getter
public enum RoleEnum {
    NORMAL(SystemConstant.USER_ROLE_NORMAL, SystemConstant.USER_ROLE_NORMAL_NAME),
    HIGHER(SystemConstant.USER_ROLE_HIGHER, SystemConstant.USER_ROLE_HIGHER_NAME),
    HIGHEST(SystemConstant.USER_ROLE_HIGHEST, SystemConstant.USER_ROLE_HIGHEST_NAME);

    private final Integer code;
    private final String desc;

    RoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RoleEnum getByCode(Integer code) {
        if (code == null) return null;
        for (RoleEnum role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        RoleEnum role = getByCode(code);
        return role != null ? role.getDesc() : "";
    }
}
