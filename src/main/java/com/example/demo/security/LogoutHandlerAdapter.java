package com.example.demo.security;

import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 登出事件适配
 *
 * @author YanZhen
 * @since 2019-03-30 11:40
 */
public interface LogoutHandlerAdapter extends LogoutHandler, LogoutSuccessHandler {
}
