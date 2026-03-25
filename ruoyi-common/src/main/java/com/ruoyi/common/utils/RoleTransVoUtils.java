package com.ruoyi.common.utils;

import com.ruoyi.common.constant.RequireRoleConstant;

public class RoleTransVoUtils {


    public static String getRoleByInt(Integer roleId) {

        switch (roleId) {
            case 2:
                return RequireRoleConstant.ROLE_APPLICANT_VO;
            case 3:
                return RequireRoleConstant.ROLE_HR_VO;
            case 4:
                return RequireRoleConstant.ROLE_ADMIN_VO;
            default:
                return "宾客";
        }
    }



}
