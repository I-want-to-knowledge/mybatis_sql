package com.example.demo.service.user;

import com.example.demo.security.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 用户接口
 *
 * @author YanZhen
 * @since 2019-03-27 18:03
 */
public interface UserService {

  /**
   * 获取用户信息
   * @param mobile 手机号
   * @return 用户详情
   */
  default UserDetails findUser(String mobile) {
    return new User(-1, new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }

      @Override
      public String getPassword() {
        return null;
      }

      @Override
      public String getUsername() {
        return null;
      }

      @Override
      public boolean isAccountNonExpired() {
        return false;
      }

      @Override
      public boolean isAccountNonLocked() {
        return false;
      }

      @Override
      public boolean isCredentialsNonExpired() {
        return false;
      }

      @Override
      public boolean isEnabled() {
        return false;
      }
    });
  }

}
