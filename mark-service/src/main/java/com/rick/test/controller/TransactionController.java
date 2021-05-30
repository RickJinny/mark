package com.rick.test.controller;

import com.rick.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/transfer")
    public String transfer(String fromName, String toName, Integer money) {
        // 转出钱
        transactionService.transferOut(fromName, money);
        int x = 1;
        if (x == 1) {
            throw new RuntimeException("出错啦!");
        }
        // 转入钱
        transactionService.transferIn(toName, money);
        return "OK";
    }

}
