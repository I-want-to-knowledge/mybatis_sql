package com.example.demo.controller;

/**
 * 响应信息
 *
 * @author YanZhen
 * @since 2019-03-09 13:56
 */
public class Result {
    private int code;
    private String message;
    private Object data;

    private Result() {
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return success(null);
    }

    public static Result success(Object data) {
        return new Result(0, null, data);
    }

    public static Result failure(int code) {
        return failure(code, null);
    }

    public static Result failure(int code, String message) {
        return failure(code, message, null);
    }

    private static Result failure(int code, String message, Object data) {
        return new Result(code, message, data);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
