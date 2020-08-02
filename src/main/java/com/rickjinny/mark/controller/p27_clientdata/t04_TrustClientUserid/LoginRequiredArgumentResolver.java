package com.rickjinny.mark.controller.p27_clientdata.t04_TrustClientUserid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginRequiredArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LongRequired.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        LongRequired longRequired = methodParameter.getParameterAnnotation(LongRequired.class);
        Object object = nativeWebRequest.getAttribute(longRequired.sessionKey(), NativeWebRequest.SCOPE_REQUEST);
        if (object == null) {
            log.error("接口 {} 非法调用。", methodParameter.getMethod().toString());
            throw new RuntimeException("请先登录!");
        }
        return object;
    }
}
