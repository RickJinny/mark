package com.rick.test.controller;

import com.rick.service.TransactionService;
import com.rick.vo.AddAccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/transfer")
    @ResponseBody
    public String transfer(String fromName, String toName, Integer money) {
        try {
            transactionService.transfer(fromName, toName, money);
        } catch (Exception e) {
            log.error("transfer error", e);
        }
        return "OK";
    }

    @PostMapping(value = "/addAccount")
    @ResponseBody
    public String addAccount(@RequestBody AddAccountRequest addAccountRequest) {
        try {
            transactionService.addAccount(addAccountRequest);
        } catch (Exception e) {
            log.error("add account error", e);
        }
        return "OK";
    }
}
