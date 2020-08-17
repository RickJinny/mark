package com.rickjinny.mark.controller.p05_httpinvoke.t02_FeignAndRibbonTimeout;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "clientSdk")
public interface Client {

    @PostMapping(value = "/feignAndRibbon/server")
    void server();
}
