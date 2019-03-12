package com.example.demo.security;

import org.springframework.security.core.Authentication;

/**
 * token管理员
 *
 * @author YanZhen
 * @since 2019-03-08 13:45
 */
public interface TokenManager {
    /**
     * 失效所有token
     */
    void invalidateAll();

    /**
     * 失效指定token
     *
     * @param tokenId token id
     */
    void invalidate(String tokenId);

    /**
     * 创建token
     *
     * @param auth 身份验证的信息
     * @return token
     */
    Token createToken(Authentication auth);

    /**
     * 获取token
     *
     * @param tokenId token id
     * @return token
     */
    Token getToken(String tokenId);

    /**
     * 获取身份信息
     * @param tokenId token id
     * @return 身份信息
     */
    Authentication getAuth(String tokenId);
}
