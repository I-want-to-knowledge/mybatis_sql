package com.example.demo.security;

import com.example.demo.security.response.AuthenticationResultHandler;
import com.example.demo.security.sms.SmsCodeAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
  private static final String MOBILE_LOGIN = "mobile";
  private static final String USERNAME_LOGIN = "username";

  private AuthenticationResultHandler authenticationResultHandler;

  public LoginFilter(AuthenticationManager authMatcher, AuthenticationResultHandler authenticationResultHandler) {
    super(new AntPathRequestMatcher(REQUEST_PATH, REQUEST_METHOD));
    if (authMatcher == null) {
      throw new IllegalArgumentException("RequestMatcher is required!");
    }
    setAuthenticationManager(authMatcher);
    if (authenticationResultHandler == null) {
      throw new IllegalArgumentException("AuthenticationResultHandler is required!");
    }
    this.authenticationResultHandler = authenticationResultHandler;
  }

  @Override
  public Authentication attemptAuthentication(
          HttpServletRequest httpServletRequest,
          HttpServletResponse httpServletResponse) throws AuthenticationException {

    // if (shouldIntercept(httpServletRequest)) { }

    // 登录区分
    String loginType = httpServletRequest.getParameter("loginType");

    if (USERNAME_LOGIN.equals(loginType)) {
      // 获取页面信息
      String username = isNull(httpServletRequest.getParameter("username"));
      String password = isNull(httpServletRequest.getParameter("password"));

      // 实现一个Authentication
      // username password 的 Authentication 实现
      AbstractAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

      // 设置用户信息写入本地信息
      token.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));

      // 验证传递的身份验证对象
      return getAuthenticationManager().authenticate(token);
    } else if (MOBILE_LOGIN.equals(loginType)) {
      // 获取页面信息
      String mobile = isNull(httpServletRequest.getParameter("mobile"));
      String authCode = isNull(httpServletRequest.getParameter("authCode"));

      // 实现一个Authentication
      AbstractAuthenticationToken token = new SmsCodeAuthenticationToken(mobile, authCode);
      token.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));// 写入本地信息
      return getAuthenticationManager().authenticate(token);
    }

    // 认证无效
    throw new ProviderNotFoundException("Authentication loginType invalid or missing");
  }

  /**
   * null转空
   *
   * @param str 字符串
   * @return 空字符串
   */
  private String isNull(String str) {
    if (str == null) {
      return "";
    } else {
      return str.trim();
    }
  }

  /**
   * 拦截
   *
   * @param httpServletRequest 请求体
   * @return 是否拦截
   */
  private boolean shouldIntercept(HttpServletRequest httpServletRequest) {
    return false;
  }

  @Override
  protected void successfulAuthentication(
          HttpServletRequest request, HttpServletResponse response,
          FilterChain chain, Authentication authResult) throws IOException {
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

    // 返回用户信息
    authenticationResultHandler.afterSuccessful(response, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(
          HttpServletRequest request, HttpServletResponse response,
          AuthenticationException failed) throws IOException {
    SecurityContextHolder.clearContext();

    if (LOG.isDebugEnabled()) {
      logger.debug("Authentication request failed:" + failed.toString(), failed);
    }

    // throw new AuthenticateFailedException();
    authenticationResultHandler.afterUnsuccessful(response, failed);
  }
}
