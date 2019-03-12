package com.example.demo.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登出，token处理
 *
 * @author YanZhen
 * @since 2019-03-09 14:36
 */
public class LogoutTokenHandler implements LogoutHandler {

    private final TokenManager tokenManager;

    public LogoutTokenHandler(TokenManager tokenManager) {
        if (tokenManager == null) {
            throw new IllegalArgumentException("TokenManager is required!");
        }
        this.tokenManager = tokenManager;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // token
        String tokenId = request.getHeader(AuthenticationFilter.TOKEN_HEADER);
        if (StringUtils.isBlank(tokenId)) {
            throw new IllegalArgumentException();
        }

        tokenManager.invalidate(tokenId);
    }
}
