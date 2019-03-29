package com.example.demo.service.user.impl;

import com.example.demo.security.User;
import com.example.demo.service.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * 假装一个用户实现XML
 *
 * @author YanZhen
 * @since 2019-03-27 18:04
 */
@Service
public class UserServiceImpl implements UserService {

  @Override
  public UserDetails findUser(String mobile) {
    return new User(1, new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
      }

      @Override
      public String getPassword() {
        return "123456a";
      }

      @Override
      public String getUsername() {
        return mobile;
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
