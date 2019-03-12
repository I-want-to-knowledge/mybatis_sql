package com.example.demo.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.security.core.Authentication;

import java.util.concurrent.TimeUnit;

/**
 * 内存管理实现
 *
 * @author YanZhen
 * @since 2019-03-08 15:21
 */
public class InMemoryTokenManager implements TokenManager {
    private static final long DEFAULT_TTL = 1800;// 默认30分钟
    private long timeToLive;
    private Cache<String, TokenWrapper> tokens;

    /**
     * token管理，默认时间30分钟
     */
    public InMemoryTokenManager() {
        this(DEFAULT_TTL);
    }

    /**
     * token管理器
     *
     * @param timeToLive token失活时间
     */
    public InMemoryTokenManager(long timeToLive) {
        if (timeToLive < 0) {
            timeToLive = 0;
        }
        this.timeToLive = timeToLive;
        // 最大条数及失活时间
        this.tokens = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(timeToLive, TimeUnit.SECONDS).build();
    }

    @Override
    public void invalidateAll() {
        tokens.invalidateAll();
    }

    @Override
    public void invalidate(String tokenId) {
        tokens.invalidate(tokenId);
    }

    @Override
    public Token createToken(Authentication auth) {
        Token token = new Token();
        TokenWrapper TokenWrapper = new TokenWrapper(token, auth);

        tokens.put(token.getId(), TokenWrapper);
        return token;
    }

    @Override
    public Token getToken(String tokenId) {
        TokenWrapper tokenWrapper = tokens.getIfPresent(tokenId);
        if (tokenWrapper == null) {
            return null;
        }
        return tokenWrapper.getToken();
    }

    @Override
    public Authentication getAuth(String tokenId) {
        TokenWrapper tokenWrapper = tokens.getIfPresent(tokenId);
        if (tokenWrapper == null) {
            return null;
        }
        return tokenWrapper.getAuth();
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    private class TokenWrapper {
        private Token token;
        private Authentication auth;

        TokenWrapper(Token token, Authentication auth) {
            this.token = token;
            this.auth = auth;
        }

        Token getToken() {
            return token;
        }

        Authentication getAuth() {
            return auth;
        }
    }
}
