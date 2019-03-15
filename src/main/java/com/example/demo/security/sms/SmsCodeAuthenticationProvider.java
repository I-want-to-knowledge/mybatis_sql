package com.example.demo.security.sms;

import com.example.demo.security.sms.authentication.SmsCodeService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 短信号验证供应器
 *
 * @author YanZhen
 * @since 2019-03-12 16:27
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private SmsCodeService smsCodeService;

    public SmsCodeAuthenticationProvider(SmsCodeService smsCodeService) {
        this.smsCodeService = smsCodeService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken token = (SmsCodeAuthenticationToken) authentication;
        //
        UserDetails userDetails = smsCodeService.findSmsCode((String) token.getPrincipal());
        if (userDetails == null) {
            throw new IllegalArgumentException("UserDetails is required!");
        }

        // 重构 SmsCodeAuthenticationToken
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails.getAuthorities(), userDetails, null);
        authenticationResult.setDetails(token.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public SmsCodeService getUserDetailsService() {
        return smsCodeService;
    }
}
