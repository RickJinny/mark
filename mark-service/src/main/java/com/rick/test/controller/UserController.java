package com.rick.test.controller;

import com.google.common.collect.Lists;
import com.rick.test.dao.model.User;
import com.rick.test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getUsers(Integer userId) {
        try {
            return userService.getUserList(Lists.newArrayList(userId));
        } catch (Exception e) {
            log.error("UserController getUsers errorMsg: {} .", e.getMessage(), e);

        }
        return null;
    }
}
