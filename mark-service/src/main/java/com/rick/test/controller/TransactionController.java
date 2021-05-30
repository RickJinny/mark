package com.rick.test.controller;

import com.rick.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/transfer")
    public String transfer(String fromName, String toName, Integer money) {
        try {
            transactionService.transfer(fromName, toName, money);
        } catch (Exception e) {
            log.error("transfer error, errorMsg: {}.", e.getMessage(), e);
        }
        return "OK";
    }

}
