package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.UserUpdateDTO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.common.utils.VoUtils;
import com.ruoyi.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller
 *
 * @author loe
 * @date 2026-01-22
 */
@Tag(name = "客户端-用户接口", description = "用户信息管理")
@RestController
@RequestMapping("/client/user")
public class ClientUserController extends BaseController
{
    @Autowired
    private UserService userService;

    /**
     * 获取用户详细信息
     */
    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "根据用户ID获取用户信息", description = "通过用户ID查询用户详细信息")
    @GetMapping(value = "/profile/{id}")
    public AjaxResult getInfo(
            @Parameter(description = "用户ID", required = true, example = "1")
            @PathVariable("id") Long id)
    {
        User user = userService.getById(id);
        if (user == null) {
            return error("用户不存在");
        }
        return success(VoUtils.transUserToVo(user));
    }

    /**
     * 获取当前用户的信息
     */
    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "获取当前登录用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping(value = "/profile/me")
    public AjaxResult getMeInfo()
    {
        User user = UserHolder.getUser();
        return success(VoUtils.transUserToVo(
                userService.getById(user.getId())
        ));
    }

    /**
     * 修改当前用户信息
     */
    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "修改当前登录用户信息", description = "修改当前登录用户的基本信息")
    @PutMapping("/profile/me")
    public AjaxResult edit(
            @Parameter(description = "用户修改信息", required = true)
            @RequestBody @Validated UserUpdateDTO uud)
    {
        return toAjax(userService.updateUserSafety(uud));
    }
}