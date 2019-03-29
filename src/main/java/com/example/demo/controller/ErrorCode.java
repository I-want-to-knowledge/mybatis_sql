package com.example.demo.controller;

/**
 * TODO
 *
 * @author YanZhen
 * @since 2019-03-29 18:08
 */
public enum ErrorCode {
  HTTP_ACCESS_DENIED(9, "拒绝访问"),
  HTTP_HANDLER_NOT_FOUND(9, "没有找到对应的请求处理器"),
  SYSTEM_ERROR(9, "系统异常")
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
