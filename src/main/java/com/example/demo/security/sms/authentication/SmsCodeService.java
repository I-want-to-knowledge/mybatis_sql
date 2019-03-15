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
     * 获取短信验证码
     * @param mobile 手机
     * @return 用户详情
     */
    UserDetails findSmsCode(String mobile);
}
