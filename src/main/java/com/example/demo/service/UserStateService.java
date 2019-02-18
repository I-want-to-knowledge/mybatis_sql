package com.example.demo.service;

import java.util.List;
import com.example.demo.model.UserState;

/**
 * 服务
 *
 * @author YanZhen
 * @since 2019-02-15 14:47
 */
public interface UserStateService{

    int insert(UserState userState);

    int insertSelective(UserState userState);

    int insertList(List<UserState> userStates);

    int updateByPrimaryKeySelective(UserState userState);
}
