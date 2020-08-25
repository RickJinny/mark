package com.rickjinny.mark.controller.p05_httpinvoke.t05_FeignPerMethodTimeout;

import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "clientSdk")
public interface Client {

    @RequestMapping(value = "/FeignPerMethodTimeout/method1")
    String method1(Request.Options options);

    @RequestMapping(value = "/FeignPerMethodTimeout/method2")
    String method2(Request.Options options);
}
