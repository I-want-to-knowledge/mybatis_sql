package com.example.demo.config;

import com.example.demo.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Qualifier("defaultAuthenticationServiceImpl")
    @Autowired(required = false)
    private UserDetailsService userDetailsService;

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
        })
                .and()
                .authorizeRequests()
                // 指定不需要过滤的接口
                .antMatchers("/Login/**").permitAll()
                // .antMatchers("/xxx/yyy").permitAll()
                .anyRequest().authenticated()
                .and()
                // 登录
                .addFilterBefore(new LoginFilter(authManager(), tokenManager), UsernamePasswordAuthenticationFilter.class)
                // 检查令牌
                .addFilterBefore(new AuthenticationFilter(tokenManager), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .addLogoutHandler(new LogoutTokenHandler(tokenManager)).logoutSuccessHandler(new LogoutSuccessResultHandler())
                .and()
                // 安全框架不会主动创建会话，也不会利用会话获取安全上下文
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 如果不存在自定义的用户服务则使用默认的
        if (userDetailsService == null) {
            userDetailsService = new InMemoryUserDetailsManager(
                    new User("admin", "admin",
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        }
        LOG.info("Using user details service: {}", userDetailsService.getClass());

        // 验证信息的容器
        auth.authenticationProvider(new AuthenticationProvider(userDetailsService, PasswordUtils.encoder()));
    }

    // 避免自动配置
    @Bean
    public AuthenticationManager authManager() throws Exception {
        return authenticationManager();
    }
}
