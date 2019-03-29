package com.example.demo.service.login;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.Result;
import com.example.demo.security.TokenManager;
import com.example.demo.security.User;
import com.example.demo.security.response.TokenAuthenticationResultHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 默认token认证返回处理
 *
 * @author YanZhen
 * @since 2019-03-28 11:44
 */
public class DefaultTokenAuthenticationResultHandlerImpl extends TokenAuthenticationResultHandler {

  public DefaultTokenAuthenticationResultHandlerImpl(TokenManager tokenManager) {
    super(tokenManager);
  }

  @Override
  protected void successfulContent(HttpServletResponse response, Authentication authResult) throws IOException {
    Object principal = authResult.getPrincipal();
    Result result;
    if (principal instanceof User) {
      // 用户信息
      User user = (User) principal;
      HashMap<String, Object> userMap = new HashMap<>();
      userMap.put("id", user.getId());
      userMap.put("username", user.getUsername());

      // 身份认证成功，返回用户信息给客户端
      result = Result.success(userMap);
    } else {
      result = Result.success(principal + "");
    }
    response.getWriter().write(JSON.toJSONString(result));
  }

  @Override
  protected void unsuccessfulContent(HttpServletResponse response, AuthenticationException failed) throws IOException {
    response.getWriter().write(JSON.toJSONString(Result.failure(9, "认证失败！")));
  }
}
