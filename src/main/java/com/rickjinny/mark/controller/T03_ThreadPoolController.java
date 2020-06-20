package com.rickjinny.mark.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("/pool")
public class T03_ThreadPoolController {

    @RequestMapping("/oom1")
    public void oom1() {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        // 打印线程池的信息, 稍后我会解释这段代码

    }
}
