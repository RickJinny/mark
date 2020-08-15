package com.rickjinny.mark.controller.p11_null.t01_AvoidNullPointerException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("avoidNull")
@Slf4j
public class AvoidNullPointerExceptionController {

    private static List<String> wrongMethod(FooService fooService, Integer i, String s, String t) {
        log.info("result {} {} {} {}", i + 1, s.equals("OK"), s.equals(t),
                new ConcurrentHashMap<String, String>().put(null, null));
        if (fooService.getBarService().bar().equals("OK")) {
            log.info("OK");
        }
        return null;
    }

    /**
     * 使用 Java 的 Optional 类进行判空，可以消除 if - else 逻辑，使用一行代码进行判空和处理。
     */
    private static List<String> rightMethod(FooService fooService, Integer i, String s, String t) {
        // result 1 false false null
        log.info("result {} {} {} {}",
                Optional.ofNullable(i).orElse(0) + 1,
                "OK".equals(s),
                Objects.equals(s, t),
                new HashMap<String, String>().put(null, null));

        Optional.ofNullable(fooService)
                .map(FooService::getBarService)
                .filter(barService -> "OK".equals(barService.bar()))
                .ifPresent(result -> log.info("OK"));
        return new ArrayList<>();
    }

    @RequestMapping("/wrong")
    public int wrong(@RequestParam(value = "/test", defaultValue = "1111") String test) {
        return wrongMethod(test.charAt(0) == '1' ? null : new FooService(),
                test.charAt(1) == '1' ? null : 1,
                test.charAt(2) == '1' ? null : "OK",
                test.charAt(3) == '1' ? null : "OK").size();
    }

    @GetMapping(value = "/right")
    public int right(@RequestParam(value = "test", defaultValue = "1111") String test) {
        return Optional.ofNullable(rightMethod(test.charAt(0) == '1' ? null : new FooService(),
                test.charAt(1) == '1' ? null : 1,
                test.charAt(2) == '1' ? null : "OK",
                test.charAt(3) == '1' ? null : "OK"))
                .orElse(Collections.emptyList()).size();
    }

    public static void main(String[] args) {
        List<String> list = Collections.emptyList();
        List<String> list1 = Optional.of(rightMethod(new FooService(), null, "a", "b")).orElse(Collections.emptyList());
        System.out.println(list1);
    }
}
