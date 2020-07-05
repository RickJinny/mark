package com.rickjinny.mark.controller.p22_apidesign.t02_apiversion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 版本策略最好一开始就要考虑。
 * 1、通过 URL Path 实现
 * 2、通过 QueryString 实现。
 * 3、通过 HTTP 头实现。
 */
@Slf4j
@RestController
@RequestMapping("/apiVersion")
@APIVersion("v1")
public class APIVersionController {

    /**
     * 第一方法: 通过 URL PATH 实现版本控制
     */
    @GetMapping("/api/item/v1")
    public void wrong1() {

    }

    @GetMapping("/api/v1/shop")
    public void wrong2() {

    }

    @GetMapping("/v1/api/merchant")
    public void wrong3() {

    }

    @GetMapping("/v1/api/user")
    public int right1() {
        return 1;
    }

    /**
     * 第二种方法: 通过 QueryString 中的 version 参数实现版本控制
     */
    @GetMapping(value = "/api/user", params = "version=2")
    public int right2(@RequestParam("version") int version) {
        return 2;
    }

    /**
     * 第三种方法: 通过请求头中的 X-API-VERSION 参数实现版本控制
     */
    @RequestMapping(value = "/api/user", headers = "X-API-VERSION=3")
    public int right3(@RequestHeader("X-API-VERSION") int version) {
        return 3;
    }

    @GetMapping(value = "/api/user")
    @APIVersion("v4")
    public int right4() {
        return 4;
    }
}
