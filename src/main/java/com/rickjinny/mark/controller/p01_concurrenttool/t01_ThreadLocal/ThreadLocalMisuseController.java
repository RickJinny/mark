package com.rickjinny.mark.controller.p01_concurrenttool.t01_ThreadLocal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@RestController
@RequestMapping(value = "/threadlocal")
@Slf4j
public class ThreadLocalMisuseController {

    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(new Supplier<Integer>() {
        @Override
        public Integer get() {
            return null;
        }
    });

    @RequestMapping(value = "/wrong")
    public Map wrong(@RequestParam("userId") Integer userId) {
        // 设置用户信息之前先查询一次 ThreadLocal 中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        // 设置用户信息到 ThreadLocal
        currentUser.set(userId);
        // 设置用户信息之后，再查询一次 ThreadLocal 中的用户信息
        String after = Thread.currentThread().getName() + ":" + currentUser.get();
        // 汇总输出两次查询结果
        Map<String, Object> result = new HashMap<>();
        result.put("before", before);
        result.put("after", after);
        return result;
    }
}
