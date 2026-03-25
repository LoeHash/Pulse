package com.ruoyi.web.controller.base.client;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.constant.SystemConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.UserLoginDTO;
import com.ruoyi.common.core.domain.dto.UserRegisterDTO;
import com.ruoyi.common.enums.RoleEnum;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author loe
 * @date 2026-01-22
 */
@Tag(name = "客户端-用户认证接口", description = "用户注册、登录、退出等认证相关操作")
@RestController
@RequestMapping("/client/auth")
public class ClientUserLoginController extends BaseController
{
    @Autowired
    private UserService userService;

    @Operation(summary = "用户退出登录", description = "清除客户端 token 并清理当前用户上下文")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "退出成功"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/logout")
    public AjaxResult logout(
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response)
    {

        //TODO 将 token加入黑名单

        // 清除客户端的Token
        clearTokenFromClient(response);

        // 清理ThreadLocal
        UserHolder.removeUser();

        return success("退出成功！");
    }

    @Operation(summary = "用户注册", description = "注册成功后直接返回 token 和用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "注册成功"),
            @ApiResponse(responseCode = "400", description = "参数验证失败"),
            @ApiResponse(responseCode = "409", description = "用户名已存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/register")
    public AjaxResult register(
            @Parameter(description = "用户注册参数", required = true)
            @Valid @RequestBody UserRegisterDTO dto)
    {
        // 验证两次密码是否一致
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return AjaxResult.error("两次输入的密码不一致");
        }

        //验证用户名是否已存在
        User existUser = userService.getUserByUsername(dto.getUsername());
        if (existUser != null) {
            return error("用户名已存在");
        }

        // 创建用户实体
        User user = new User();
        BeanUtils.copyProperties(dto, user, "password", "confirmPassword", "captcha", "uuid");

        // 设置密码
        String hashedPassword = Sha256Util.hash(dto.getPassword());
        user.setPassword(hashedPassword);

        // 设置默认值
        user.setStatus(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        // 如果没有提供昵称，生成一个用户名
        if (StringUtils.isEmpty(user.getNickname())) {
            user.setNickname(SystemConstant.USER_PREFIX + RandomUtil.randomString(6));
        }

        // 默认角色为普通用户
        user.setRoleId(RoleEnum.NORMAL.getCode());


        // 保存用户
        boolean result = userService.save(user);
        if (!result) {
            return error("注册失败，请稍后重试");
        }


        //生成token，返回
        Map<String, Object> data = generateTokenData(user);

        return AjaxResult.success("注册成功", data);
    }

    @Operation(summary = "用户登录", description = "校验用户名密码，成功后返回 token 和用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功"),
            @ApiResponse(responseCode = "401", description = "用户名或密码错误"),
            @ApiResponse(responseCode = "404", description = "用户不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/login")
    public AjaxResult login(
            @Parameter(description = "用户登录参数", required = true)
            @RequestBody UserLoginDTO dto)
    {
        // 验证用户名密码
        User user = userService.getUserByUsername(dto.getUsername());

        if (user == null) {
            return error("用户不存在！");
        }

        String hash = Sha256Util.hash(dto.getPassword());

        //TODO 密码加盐
        if (!user.getPassword().equals(hash)) {
            return error("密码错误!");
        }

        Map<String, Object> data = generateTokenData(user);

        return AjaxResult.success("登录成功", data);
    }

    public Map<String, Object> generateTokenData(User user) {

        Map<String, Object> data = new HashMap<>();
        String role = RoleTransUtils.getRoleByInt(user.getRoleId());

        String token = JwtUtils.generateToken(user, role);

        data.put("token", token);
        data.put("user", VoUtils.transUserToVo(user));
        return data;
    }

    private void clearTokenFromClient(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.setHeader("Clear-Token", "true");
    }
}