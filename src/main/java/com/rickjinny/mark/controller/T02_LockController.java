package com.rickjinny.mark.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock")
@Slf4j
public class T02_LockController {

    @RequestMapping("/bb")
    @ResponseBody
    public String aa() {
        return "OK";
    }
}
