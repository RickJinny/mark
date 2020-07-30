package com.rickjinny.mark.controller.p20_spring_02.t01_AopFeign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 配置扫描 Fegin
 */
@Configuration
@EnableFeignClients(basePackages = "com.rickjinny.mark.controller.p20_spring_02.t01_AopFeign.feign")
public class Config {

}
