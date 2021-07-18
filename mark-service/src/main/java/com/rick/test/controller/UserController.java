package com.rick.test.controller;

import com.google.common.collect.Lists;
import com.rick.common.ServerResponse;
import com.rick.service.UserService;
import com.rick.request.CreateUserRequest;
import com.rick.response.CreateUserResponse;
import com.rick.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test() {
        return "success";
    }

    @RequestMapping(value = "/getUsers")
    @ResponseBody
    public List<UserVO> getUsers(Long userId) {
        try {
            return userService.getUserList(Lists.newArrayList(userId));
        } catch (Exception e) {
            log.error("UserController getUsers errorMsg: {} .", e.getMessage(), e);

        }
        return null;
    }

    @RequestMapping(value = "/createUser")
    @ResponseBody
    public ServerResponse<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
}
