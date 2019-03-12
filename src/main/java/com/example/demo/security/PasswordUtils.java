package com.example.demo.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具类
 *
 * @author YanZhen
 * @since 2019-03-09 15:41
 */
public class PasswordUtils {
    /**
     * 密码编码器
     */
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder(4);

    private PasswordUtils() {
    }

    /**
     * 预定义编码器
     *
     * @return 编码器
     */
    public static PasswordEncoder encoder() {
        return ENCODER;
    }

    /**
     * 编码明文
     * @param plainText 明文
     * @return 密文
     */
    public static String encode(String plainText) {
        return ENCODER.encode(plainText);
    }

    /**
     * 明文密文比较
     * @param plainText 明文
     * @param encodedText 密文
     * @return 是否相同
     */
    public static boolean matches(String plainText, String encodedText) {
        return ENCODER.matches(plainText, encodedText);
    }
}
