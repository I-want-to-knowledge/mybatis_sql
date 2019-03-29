package com.example.demo.security.response;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.Result;
import com.example.demo.security.AuthenticationFilter;
import com.example.demo.security.Token;
import com.example.demo.security.TokenManager;
import com.example.demo.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * token认证返回处理
 *
 * @author YanZhen
 * @since 2019-03-28 11:18
 */
public abstract class TokenAuthenticationResultHandler implements AuthenticationResultHandler {

  private final TokenManager tokenManager;

  protected TokenAuthenticationResultHandler(TokenManager tokenManager) {
    if (tokenManager == null) {
      throw new IllegalArgumentException("TokenManager is required!");
    }
    this.tokenManager = tokenManager;
  }

  @Override
  public void afterSuccessful(HttpServletResponse response, Authentication authResult) throws IOException {
    // 生成token
    Token token = tokenManager.createToken(authResult);
    response.addHeader(AuthenticationFilter.TOKEN_HEADER, token.getId());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");

    // 交给实现者写入
    successfulContent(response, authResult);
  }

  /**
   * 认证成功后所需添加内容
   * @param response 响应信息
   * @param authResult 认证信息
   */
  protected abstract void successfulContent(HttpServletResponse response, Authentication authResult) throws IOException;

  @Override
  public void afterUnsuccessful(HttpServletResponse response, AuthenticationException failed) throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    unsuccessfulContent(response, failed);
  }

  protected abstract void unsuccessfulContent(HttpServletResponse response, AuthenticationException failed) throws IOException;
}
