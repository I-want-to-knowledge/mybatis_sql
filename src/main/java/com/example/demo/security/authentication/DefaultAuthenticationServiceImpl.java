package com.example.demo.security.authentication;

import org.springframework.stereotype.Service;

/**
 * 默认实现认证服务
 * 没有该实现类，AuthenticationService接口默认实现UserDetailsService的方法将不起作用
 * <p>@Service</p>注入，把AuthenticationService实现送进spring容器
 *
 * @author YanZhen
 * @since 2019-03-11 10:53
 */
@Service
public class DefaultAuthenticationServiceImpl implements AuthenticationService {

}
