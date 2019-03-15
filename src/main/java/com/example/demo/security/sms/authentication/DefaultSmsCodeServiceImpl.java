package com.example.demo.security.sms.authentication;

import com.example.demo.security.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * TODO
 *
 * @author YanZhen
 * @since 2019-03-12 17:54
 */
@Service
public class DefaultSmsCodeServiceImpl implements SmsCodeService {

    @Override
    public UserDetails findSmsCode(String mobile) {
        return new User(2, new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptyList();
            }

            @Override
            public String getPassword() {
                return "admin";
            }

            @Override
            public String getUsername() {
                return "admin";
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });
    }
}
