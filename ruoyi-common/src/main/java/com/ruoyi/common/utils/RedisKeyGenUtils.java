package com.ruoyi.common.utils;

import com.ruoyi.common.constant.RedisKeyPrefix;
import org.jspecify.annotations.NonNull;

/**
 * Redis key生成器
 */
public class RedisKeyGenUtils {

    public static String genUserLoginKey(String username) {
        return RedisKeyPrefix.USER_LOGIN_PREFIX + username;
    }

    public static String genUserKey(String username) {
        return RedisKeyPrefix.USER_PREFIX + username;
    }

    public static String genGyroKey(String clientId) {
        return RedisKeyPrefix.WEBSOCKET_CLIENT_PREFIX + clientId + ":" + RedisKeyPrefix.GYRO_PREFIX;
    }

    public static  String genMqGyroKey() {
        return RedisKeyPrefix.REDIS_MQ_PREFIX + RedisKeyPrefix.GYRO_PREFIX;
    }

}
