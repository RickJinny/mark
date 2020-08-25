package com.rickjinny.mark.controller.p05_httpinvoke.t05_FeignPerMethodTimeout;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "clientSdk")
public interface Client {

    @RequestMapping(value = "/")
    String method1();
}
