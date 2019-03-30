package com.example.demo.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.demo.security.exception.ErrorCode.TOKEN_EXPIRED_ERROR;

/**
 * 登出，token处理
 *
 * @author YanZhen
 * @since 2019-03-09 14:36
 */
public abstract class LogoutTokenHandler implements LogoutHandlerAdapter {

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
        if (!StringUtils.isBlank(tokenId)) {
            // 失效指定token
            tokenManager.invalidate(tokenId);

            // 后续业务
        } else {
            response.setStatus(TOKEN_EXPIRED_ERROR.getCode());
        }

        // throw new IllegalArgumentException("Token is queried!");
    }
}
