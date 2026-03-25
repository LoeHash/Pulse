package com.ruoyi.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Sha256Util {

    private static final String ALGORITHM = "SHA-256";

    /**
     * 对字符串进行SHA-256哈希计算
     *
     * @param input 输入字符串
     * @return 64位十六进制哈希值
     * @throws IllegalArgumentException 输入为空时抛出
     */
    public static String hash(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("输入字符串不能为空");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256是Java标准算法，理论上不会发生
            throw new RuntimeException("系统不支持SHA-256算法", e);
        } catch (java.io.UnsupportedEncodingException e) {
            // UTF-8是所有Java平台都支持的编码
            throw new RuntimeException("UTF-8编码不支持", e);
        }
    }

    /**
     * 将字节数组转换为十六进制字符串 (Java 8兼容方法)
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "";

        StringBuilder hexString = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 将字节转换为16进制表示，确保是两位
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 验证字符串与哈希值是否匹配
     *
     * @param input 原始字符串
     * @param hashValue 待验证的哈希值
     * @return 是否匹配
     */
    public static boolean verify(String input, String hashValue) {
        if (input == null || hashValue == null) {
            return false;
        }
        String computedHash = hash(input);
        return computedHash.equalsIgnoreCase(hashValue);
    }


}
