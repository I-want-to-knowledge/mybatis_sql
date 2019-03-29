package com.example.demo.security.sms.authentication;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信码服务
 *
 * @author YanZhen
 * @since 2019-03-12 17:46
 */
public interface SmsCodeService {
    /**
     * 验证短信验证码
     * @param mobile 手机
     * @param validationCode 验证码
     * @return 认证是否成功
     */
    default boolean authenticate(String mobile, String validationCode) {
        return false;
    }
}
