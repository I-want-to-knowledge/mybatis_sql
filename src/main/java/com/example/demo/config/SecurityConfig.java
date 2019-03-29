package com.example.demo.config;

import com.example.demo.security.*;
import com.example.demo.security.authorize.AuthorizeRequests;
import com.example.demo.security.sms.SmsCodeAuthenticationProvider;
import com.example.demo.security.sms.authentication.SmsCodeService;
import com.example.demo.service.login.DefaultTokenAuthenticationResultHandlerImpl;
import com.example.demo.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

/**
 * security配置
 *
 * @author YanZhen
 * @since 2019-03-08 11:12
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

  // @Autowired(required = false)
  private UserDetailsService userDetailsService;

  private SmsCodeService smsCodeService;

  private UserService userService;

  private AuthorizeRequests authorizeRequests;

//    @Autowired
//    private AuthenticationResultHandler authenticationResultHandler;

  public SecurityConfig(
          @Qualifier("defaultAuthenticationServiceImpl") UserDetailsService userDetailsService,
          SmsCodeService smsCodeService,
          UserService userService, AuthorizeRequests authorizeRequests) {
    this.userDetailsService = userDetailsService;
    this.smsCodeService = smsCodeService;
    this.userService = userService;
    this.authorizeRequests = authorizeRequests;
  }

  /*@Bean
  @ConditionalOnMissingBean(UserDetailsService.class)
  public UserDetailsService userDetailsService() {
    if (userDetailsService == null) {
      userDetailsService = userDetailsService = new InMemoryUserDetailsManager(
              new User("admin", "admin",
                      Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }
    return userDetailsService;
  }*/

  @Bean
  @ConditionalOnMissingBean(SmsCodeService.class)
  public SmsCodeService smsCodeService() {
    if (smsCodeService == null) {
      smsCodeService = new SmsCodeService() {
      };
    }
    return smsCodeService;
  }

  @Bean
  @ConditionalOnMissingBean(UserService.class)
  public UserService userService() {
    if (userService == null) {
      userService = new UserService() {
      };
    }
    return userService;
  }

  /*@Bean
  @ConditionalOnMissingBean(AuthorizeRequests.class)
  public AuthorizeRequests authorizeRequests() {
    if (authorizeRequests == null) {
      authorizeRequests = new AuthorizeRequests() {};
    }
    return authorizeRequests;
  }*/

  @Bean
  public TokenManager tokenManager() {
    return new InMemoryTokenManager(1800);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    TokenManager tokenManager = tokenManager();

    http.csrf().disable()
            .cors().configurationSource(httpServletRequest -> {
      // 跨域
      CorsConfiguration corsConfig = new CorsConfiguration();
      corsConfig.setAllowCredentials(true);
      corsConfig.setExposedHeaders(Collections.singletonList("Token"));
      corsConfig.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
      corsConfig.setAllowedMethods(Arrays.asList("GET", "POST"));
      corsConfig.setAllowedOrigins(Collections.singletonList(CorsConfiguration.ALL));
      corsConfig.setMaxAge(1800L);
      return corsConfig;
    });

    // 配置拦截或不拦截指定接口
    if (authorizeRequests == null) {
      http.authorizeRequests().anyRequest().permitAll();
    } else {
      http.authorizeRequests()
              // 指定不需要过滤的接口
              .antMatchers(authorizeRequests.permits()).permitAll()
              // .antMatchers("/xxx/yyy").permitAll()
              .anyRequest().authenticated();
    }

    // 访问时拦截器
    // 登录拦截器
    http.addFilterBefore(new LoginFilter(authManager(),
                    new DefaultTokenAuthenticationResultHandlerImpl(tokenManager)),// 默认的Token认证实现
            UsernamePasswordAuthenticationFilter.class)
            // 检查令牌
            .addFilterBefore(new AuthenticationFilter(tokenManager), LoginFilter.class)

            // 登出事件
            .logout()
            .addLogoutHandler(new LogoutTokenHandler(tokenManager)).logoutSuccessHandler(new LogoutSuccessResultHandler())

            .and()
            // 安全框架不会主动创建会话，也不会利用会话获取安全上下文
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    // 如果不存在自定义的用户服务则使用默认的
    if (userDetailsService == null) {
      userDetailsService = new InMemoryUserDetailsManager(
              new User("admin", "admin",
                      Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }
    LOG.info("Using user details service: {}", userDetailsService.getClass());

    // 添加供应商，处理过滤器
    // 验证信息的容器
    auth.authenticationProvider(new AuthenticationProvider(userDetailsService, PasswordUtils.encoder()))
            .authenticationProvider(new SmsCodeAuthenticationProvider(smsCodeService, userService));
  }

  // 避免自动配置
  @Bean
  public AuthenticationManager authManager() throws Exception {
    return authenticationManager();
  }
}
