package com.example.demo.service.impl;

import com.example.demo.dao.UserStateMapper;
import com.example.demo.model.UserState;
import com.example.demo.service.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStateServiceImpl implements UserStateService{

    @Autowired
    private UserStateMapper userStateMapper;

    @Override
    public int insert(UserState userState){
        System.out.println("service层进入");
        return userStateMapper.insert(userState);
    }

    @Override
    public int insertSelective(UserState userState){
        return userStateMapper.insertSelective(userState);
    }

    @Override
    public int insertList(List<UserState> userStates){
        return userStateMapper.insertList(userStates);
    }

    @Override
    public int updateByPrimaryKeySelective(UserState userState){
        return userStateMapper.updateByPrimaryKeySelective(userState);
    }
}
