package com.example.demo.security.response;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证返回处理
 *
 * @author YanZhen
 * @since 2019-03-28 11:07
 */
public interface AuthenticationResultHandler {

  /**
   * 成功认证之后
   * @param response 响应
   * @param authResult 认证信息
   */
  void afterSuccessful(HttpServletResponse response, Authentication authResult) throws IOException;

  /**
   * 认证失败后
   * @param response 响应
   * @param failed 认证异常信息
   */
  void afterUnsuccessful(HttpServletResponse response, AuthenticationException failed) throws IOException;
}
