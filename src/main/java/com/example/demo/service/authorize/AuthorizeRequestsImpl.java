package com.example.demo.service.authorize;

import com.example.demo.security.authorize.AuthorizeRequests;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author YanZhen
 * @since 2019-03-28 14:22
 */
@Service
public class AuthorizeRequestsImpl implements AuthorizeRequests {

  @Override
  public String[] permits() {
    return new String[] {"/Login/**"};
  }
}
