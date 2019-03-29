package com.example.demo.security.authorize;

/**
 * TODO
 *
 * @author YanZhen
 * @since 2019-03-28 14:20
 */
public interface AuthorizeRequests {

  default String[] permits() {
    return new String[0];
  }

}
