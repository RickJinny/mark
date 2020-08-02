package com.rickjinny.mark.controller.p27_clientdata.t04_TrustClientUserid;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 要在 WebMvcConfigurer 接口的 addArgumentResolvers 方法里面，来增加这个自定义的处理器 LoginRequiredArgumentResolver。
 */
public class TrustClientUserIdApplication implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginRequiredArgumentResolver());
    }
}
