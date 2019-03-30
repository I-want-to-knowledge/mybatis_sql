package com.example.demo.security;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.security.exception.ErrorCode.TOKEN_EXPIRED_ERROR;

/**
 * 登出成功，处理器响应
 *
 * @author YanZhen
 * @since 2019-03-09 14:59
 */
public class LogoutSuccessResultHandler extends LogoutTokenHandler {

  public LogoutSuccessResultHandler(TokenManager tokenManager) {
    super(tokenManager);
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication) throws IOException, ServletException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    if (response.getStatus() == TOKEN_EXPIRED_ERROR.getCode()) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().write(JSON.toJSONString(Result.failure(TOKEN_EXPIRED_ERROR.getCode(),
              TOKEN_EXPIRED_ERROR.getMessage())));
    } else {
      response.getWriter().write(JSON.toJSONString(Result.success()));
    }
  }
}
