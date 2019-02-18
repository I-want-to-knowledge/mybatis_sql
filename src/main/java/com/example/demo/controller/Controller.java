package com.example.demo.controller;

import com.example.demo.model.UserState;
import com.example.demo.service.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 控制
 *
 * @author YanZhen
 * @since 2019-02-15 14:47
 */
@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserStateService userStateService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getHello() {
        UserState userState = new UserState();
        userState.setLastTime(new Date());
        userState.setType(1);
        userState.setUserId(1L);
        userStateService.insert(userState);
        return "Store successful!!!";
    }
}
