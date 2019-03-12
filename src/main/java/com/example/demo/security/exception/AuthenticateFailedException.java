package com.example.demo.security.exception;

/**
 * 认证失败异常
 *
 * @author YanZhen
 * @since 2019-03-09 14:17
 */
public class AuthenticateFailedException extends AuthorizationException {
    public AuthenticateFailedException() {
        super();
    }

    public AuthenticateFailedException(String message) {
        super(message);
    }

    public AuthenticateFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticateFailedException(Throwable cause) {
        super(cause);
    }

    public AuthenticateFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
