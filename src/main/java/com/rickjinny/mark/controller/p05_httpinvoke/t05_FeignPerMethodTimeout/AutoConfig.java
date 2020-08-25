package com.rickjinny.mark.controller.p05_httpinvoke.t05_FeignPerMethodTimeout;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.rickjinny.mark.controller.p05_httpinvoke.t05_FeignPerMethodTimeout")
public class AutoConfig {

}
