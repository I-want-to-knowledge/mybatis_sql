package com.example.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 认证用户是否合法
 *
 * @author YanZhen
 * @since 2019-03-09 15:55
 */
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private Logger LOG = LoggerFactory.getLogger(AuthenticationProvider.class);
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 构造认证
     * @param userDetailsService 用户信息
     * @param passwordEncoder 密码编码器
     */
    public AuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        if (userDetailsService == null) {
            throw new IllegalArgumentException("UserDetailsService is required!");
        }
        if (passwordEncoder == null) {
            throw new IllegalArgumentException("PasswordEncoder is required!");
        }
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 检查密码

        String requestPassword = (String) authentication.getCredentials();

        if (!passwordEncoder.matches(requestPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials!");
        }
        authentication.eraseCredentials();
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            LOG.debug("user '{}' not found!", username);
            throw new BadCredentialsException("Bad credentials!");
        }
    }
}
