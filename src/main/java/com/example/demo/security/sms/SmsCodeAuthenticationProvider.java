package com.example.demo.security.sms;

import com.example.demo.security.exception.AuthorizationException;
import com.example.demo.security.sms.authentication.SmsCodeService;
import com.example.demo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 短信号验证供应器
 *
 * @author YanZhen
 * @since 2019-03-12 16:27
 */
@Slf4j
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsChecker userDetailsChecker = new DefaultAuthenticationChecks();
    private SmsCodeService smsCodeService;
    private UserService userService;

    public SmsCodeAuthenticationProvider(SmsCodeService smsCodeService, UserService userService) {
        this.smsCodeService = smsCodeService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {

        // 判断手机号是否存在
        UserDetails user = userService.findUser(authentication.getName());

        // 判断手机验证码
        boolean flag = smsCodeService.authenticate((String) authentication.getPrincipal(),
                (String) authentication.getCredentials());

        if (!flag) {
            throw new AuthorizationException("手机认证失败");
        }

        log.info("手机号（{}）认证成功", authentication.getPrincipal());

        // 检查用户的状态
        userDetailsChecker.check(user);

        // 验证码信息清除
        ((SmsCodeAuthenticationToken) authentication).eraseCredentials();

        // 重构 SmsCodeAuthenticationToken，将手机号替换成查询到的用户信息
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user.getAuthorities(),
                user, authentication.getCredentials());
        authenticationResult.setDetails(authentication.getDetails());// 放入ip地址，证书序列号
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断是否为对应的鉴定对象
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public SmsCodeService getUserDetailsService() {
        return smsCodeService;
    }

    private class DefaultAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                throw new LockedException("User account is locked");
            }

            if (!user.isEnabled()) {
                throw new DisabledException("User is disabled");
            }

            if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException("User account has expired");
            }

            if (!user.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("User credentials have expired");
            }
        }
    }
}
