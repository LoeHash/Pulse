package com.ruoyi.common.utils;

import com.ruoyi.common.constant.RequireRoleConstant;

public class RoleTransUtils {


    public static String getRoleByInt(Integer roleId) {

        switch (roleId) {
            case 2:
                return RequireRoleConstant.ROLE_NORMAL;
            case 3:
                return RequireRoleConstant.ROLE_HIGHER;
            case 4:
                return RequireRoleConstant.ROLE_HIGHEST;
            default:
                return RequireRoleConstant.ROLE_GUEST;
        }
    }



}
