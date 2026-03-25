package com.ruoyi.framework.aspectj;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.utils.RoleTransVoUtils;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.lang.reflect.Method;

@Aspect
@Component
public class RequireRoleAspect {


    /**
     * 使用@Around环绕通知
     */
    @Around("@annotation(com.ruoyi.common.annotation.RequireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1. 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new AuthException("无法获取请求信息");
        }

        HttpServletRequest request = attributes.getRequest();

        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);

        // 如果是来宾权限
        if (requireRole.value().equals(RequireRoleConstant.ROLE_GUEST_CODE)){
            return joinPoint.proceed();
        }

        //  从请求中获取用户角色
        User user = UserHolder.getUser();
        System.out.println(user);
        if (user == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor("error~!", Constants.FAIL, "没有登录","尝试访问接口: " + request.getRequestURI().toString()));
            throw new AuthException("请先登录");
        }

        Integer userRoleId = user.getRoleId();


        if (userRoleId == null) {
            //没有角色
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUsername(), Constants.FAIL, "没有登录","尝试访问接口: " + request.getRequestURI().toString()));
            throw new AuthException("请先登录");
        }


        // 4. 获取需要的角色
        Integer requiredRole = Integer.parseInt(requireRole.value());

        // 5. 检查权限 检查权限是否大于所需权限
        if (!(userRoleId >= requiredRole)) {

            throw new RuntimeException("权限不足，需要权限：" + RoleTransVoUtils.getRoleByInt(requiredRole));
        }

        // 6. 权限检查通过，执行原方法
        return joinPoint.proceed();
    }





}
