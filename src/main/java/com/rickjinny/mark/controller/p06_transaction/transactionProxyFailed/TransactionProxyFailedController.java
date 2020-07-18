package com.rickjinny.mark.controller.p06_transaction.transactionProxyFailed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transactionProxyFailed")
public class TransactionProxyFailedController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/wrong1")
    public int wrong1(@RequestParam("name") String name) {
        return userService.createUserWrong1(name);
    }

}
