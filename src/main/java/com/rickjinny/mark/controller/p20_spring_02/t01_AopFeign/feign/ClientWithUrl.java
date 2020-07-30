package com.rickjinny.mark.controller.p20_spring_02.t01_AopFeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "anotherClient", url = "http://localhost:45678")
public interface ClientWithUrl {
    @RequestMapping(value = "/feignAop/server")
    String api();
}
