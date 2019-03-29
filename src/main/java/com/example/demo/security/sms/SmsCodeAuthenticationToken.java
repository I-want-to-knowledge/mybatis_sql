package com.example.demo.security.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 短信码
 *
 * @author YanZhen
 * @since 2019-03-12 16:12
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 2383092775910246006L;

    /**
     * 手机号或者用户信息
     */
    private final Object principal;
    private Object credentials;

    public SmsCodeAuthenticationToken(String mobile, String smsCode) {
        super(null);
        this.principal = mobile;
        this.credentials = smsCode;
        setAuthenticated(false);
    }

    /**
     * 短信验证构造
     * @param authorities 用户角色信息
     * @param principal 手机号或者用户信息
     * @param credentials 用户手机验证码
     */
    SmsCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
