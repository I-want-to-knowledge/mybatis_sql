package com.example.demo.security;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.Result;
import com.example.demo.security.exception.AuthenticateFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 登录过滤器
 *
 * @author YanZhen
 * @since 2019-03-08 16:53
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    private static Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
    private static final String REQUEST_PATH = "/login";
    private static final String REQUEST_METHOD = "POST";
    private final TokenManager tokenManager;
    // private static final String mobileCode = "mobile";

    public LoginFilter(AuthenticationManager authMatcher, TokenManager tokenManager) {
        super(new AntPathRequestMatcher(REQUEST_PATH, REQUEST_METHOD));
        if (authMatcher == null) {
            throw new IllegalArgumentException("RequestMatcher is required!");
        }
        if (tokenManager == null) {
            throw new IllegalArgumentException("TokenManager is required!");
        }
        setAuthenticationManager(authMatcher);
        this.tokenManager = tokenManager;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        // if (shouldIntercept(httpServletRequest)) { }

        // 获取页面信息
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");

        // 判断值是否为空
        if (username == null) {
            username = "";
        } else {
            username = username.trim();
        }
        if (password == null) {
            password = "";
        }

        // 实现一个Authentication
        // TODO
        // username password 的 Authentication 实现
        AbstractAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // 设置用户信息写入本地信息
        token.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));

        // 验证传递的身份验证对象
        return getAuthenticationManager().authenticate(token);
    }

    /**
     * 拦截
     * @param httpServletRequest 请求体
     * @return 是否拦截
     */
    private boolean shouldIntercept(HttpServletRequest httpServletRequest) {
        return false;
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                    + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                    authResult, this.getClass()));
        }

        // 生成token
        Token token = tokenManager.createToken(authResult);
        response.addHeader(AuthenticationFilter.TOKEN_HEADER, token.getId());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        User user = (User) authResult.getPrincipal();
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("username", user.getUsername());

        // 身份认证成功，返回用户信息给客户端
        response.getWriter().write(JSON.toJSONString(Result.success(userMap)));
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (LOG.isDebugEnabled()) {
            logger.debug("Authentication request failed:" + failed.toString(), failed);
        }

        throw new AuthenticateFailedException();
    }
}
