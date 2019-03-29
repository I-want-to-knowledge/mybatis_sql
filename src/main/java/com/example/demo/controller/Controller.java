package com.example.demo.controller;

import com.example.demo.model.UserState;
import com.example.demo.security.User;
import com.example.demo.service.user.UserStateService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制
 *
 * @author YanZhen
 * @since 2019-02-15 14:47
 */
@RestController
@RequestMapping("/user")
public class Controller {

    @Resource
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

    @GetMapping("/test")
    public Result test(@AuthenticationPrincipal User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("username", user.getUsername());
        map.put("a", "成功访问！");
        return Result.success(map);
    }
}
