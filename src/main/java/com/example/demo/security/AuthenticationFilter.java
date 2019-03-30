package com.example.demo.security;

import com.example.demo.security.exception.InvalidTokenException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过滤器，用于根据用户传递的令牌生成认证信息
 *
 * @author YanZhen
 * @since 2019-03-09 10:32
 */
public class AuthenticationFilter extends OncePerRequestFilter {

    private static Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);
    public static final String TOKEN_HEADER = "Token";
    private TokenManager tokenManager;

    /**
     * 认证过滤器
     *
     * @param tokenManager token管理员
     */
    public AuthenticationFilter(TokenManager tokenManager) {
        if (tokenManager == null) {
            throw new IllegalArgumentException("TokenManager is required!");
        }
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {
        String tokenId = httpServletRequest.getHeader(TOKEN_HEADER);

        // 令牌不存在
        if (StringUtils.isBlank(tokenId)) {
            // 进入下层过滤器
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // 获取认证信息
        Authentication auth = tokenManager.getAuth(tokenId);
        if (auth != null) {
            // 后续处理有认证信息
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        // throw new InvalidTokenException("No authentication of token");
        LOG.error("Token expired {}", tokenId);

        // 无认证信息会报异常
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
