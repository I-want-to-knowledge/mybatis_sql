package com.example.demo.model;

import lombok.Data;

import java.util.Date;

/**
 * 用户状态实体
 *
 * @author YanZhen
 * @since 2019-02-14 16:20
 */
@Data
public class UserState {
    private Long id;
    private Long userId;
    private Integer type;
    private Date lastTime;
}
