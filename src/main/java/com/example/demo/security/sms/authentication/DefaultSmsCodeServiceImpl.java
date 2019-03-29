package com.example.demo.security.sms.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 手机验证码验证器
 * @author YanZhen
 * @since 2019-03-12 17:54
 */
@Slf4j
@Service
public class DefaultSmsCodeServiceImpl implements SmsCodeService {

    @Override
    public boolean authenticate(String mobile, String validationCode) {
        log.info("验证的手机号：{}", mobile);
        return "123456".equals(validationCode);
    }
}
