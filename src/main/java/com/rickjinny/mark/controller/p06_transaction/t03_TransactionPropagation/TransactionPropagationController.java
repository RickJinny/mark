package com.rickjinny.mark.controller.p06_transaction.t03_TransactionPropagation;

import com.rickjinny.mark.controller.p06_transaction.bean.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transactionPropagation")
@Slf4j
public class TransactionPropagationController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/wrong")
    public int wrong(@RequestParam("name") String name) {
        try {
            userService.createUserWrong1(new UserEntity(name));
        } catch (Exception e) {
            log.error("createUserWrong failed, reason:{}", e.getMessage());
        }
        return userService.getUserCount(name);
    }

    @RequestMapping(value = "/wrong2")
    public int wrong2(@RequestParam("name") String name) {
        try {
            userService.createUserWrong2(new UserEntity(name));
        } catch (Exception e) {
            log.error("createUserWrong2 failed, reason:{}", e.getMessage(), e);
        }
        return userService.getUserCount(name);
    }

    @RequestMapping(value = "/right")
    public int right(@RequestParam("name") String name) {
        userService.createUserRight(new UserEntity(name));
        return userService.getUserCount(name);
    }
}
