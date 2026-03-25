package com.ruoyi.common.utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class JwtUtils {
    private final static String secret = "your-secret-key";
    private final static long expire = 7L * 24 * 60 * 60 * 10000; // 70天

    // 生成Token
    public static String generateToken(User user, String role) {
        String jsonString = JSON.toJSONString(user);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("user_string", jsonString)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 验证Token
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 从Token获取用户名
    public static String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 从Token获取用户ID
    public static User getUser(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) {
                return null;
            }

            String userJson = claims.get("user_string").toString();

            if (userJson == null) {
                return null;
            }

            //转换
            User user = JSONObject.parseObject(userJson, User.class);

            return user;

        } catch (Exception e) {
            return null;
        }

    }

    public static void refreshToken(String token) {

    }

    public static String getRole(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * 解析Token
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {

            return null;
        }
    }
}