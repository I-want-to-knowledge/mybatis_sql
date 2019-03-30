package com.example.demo.security.exception;

/**
 * 异常信息
 *
 * @author YanZhen
 * @since 2019-03-29 18:08
 */
public enum ErrorCode {
  SYSTEM_ERROR(9999, "系统异常"),
  HTTP_ACCESS_DENIED(9998, "拒绝访问"),
  HTTP_HANDLER_NOT_FOUND(9997, "没有找到对应的请求处理器"),
  TOKEN_EXPIRED_ERROR(9996, "令牌无效")
  ;

  private int code;
  private String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }}
