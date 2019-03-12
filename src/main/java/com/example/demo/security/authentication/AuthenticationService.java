package com.example.demo.security.authentication;

import com.example.demo.security.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

/**
 * 认证服务
 *
 * @author YanZhen
 * @since 2019-03-09 17:21
 */
public interface AuthenticationService extends UserDetailsService {
    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户的信息
        long userId = 1;
        boolean expired = true;
        boolean locked = true;
        boolean passwordExpired = true;
        boolean enabled = true;

        return new User(userId, new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                // 用户权限
                return Collections.emptyList();
            }

            @Override
            public String getPassword() {
                // 假装加密后的密码
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();// 密码加密装置
                return passwordEncoder.encode("123456a");
            }

            @Override
            public String getUsername() {
                return "yz";
            }

            @Override
            public boolean isAccountNonExpired() {
                // 账户是否过期
                return expired;
            }

            @Override
            public boolean isAccountNonLocked() {
                // 是否锁定
                return locked;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                // 密码是否过期
                return passwordExpired;
            }

            @Override
            public boolean isEnabled() {
                // 是否启用
                return enabled;
            }
        });
    }
}
