package com.example.demo.security;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.UUID;

/**
 * 令牌
 *
 * @author YanZhen
 * @since 2019-03-08 13:49
 */
public class Token implements Serializable {
    private String id;
    private long timestamp;


    public Token() {
        this(UUID.randomUUID().toString().replaceAll("-", ""), System.currentTimeMillis());
    }

    private Token(String id, long timestamp) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id is required");
        }
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Token other = (Token) obj;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
