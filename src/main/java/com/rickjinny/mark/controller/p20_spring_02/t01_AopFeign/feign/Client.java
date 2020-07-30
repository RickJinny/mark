package com.rickjinny.mark.controller.p20_spring_02.t01_AopFeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient
public interface Client {
    @RequestMapping(value = "/feignAop/server")
    String api();
}
