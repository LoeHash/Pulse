package com.ruoyi.framework.interceptor;

import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.utils.JwtUtils;
import com.ruoyi.common.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;


public class JwtInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 获取Token
        String token = getToken(request);

        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("token错误!");
        }


        // 2. 解析Token获取用户信息
        User user = JwtUtils.getUser(token);

        if (user == null) {
            throw new RuntimeException("token过期");
        }

        // 存入UserHolder
        UserHolder.saveUser(user);

        return true; // 继续执行，让切面去检查权限
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {
        // 请求结束后清理ThreadLocal，防止内存泄漏
        UserHolder.removeUser();
    }

    private String getToken(HttpServletRequest request) {
        // 从Header获取
        String authHeader = request.getHeader("token");
        if (!StringUtils.isEmpty(authHeader)) {
            return authHeader;
        }
        // 从参数获取（可选）
        return request.getParameter("token");
    }










}
