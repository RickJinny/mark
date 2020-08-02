package com.rickjinny.mark.controller.p27_clientdata.t04_TrustClientUserid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自定义 LoginRequiredArgumentResolver 类，实现 HandlerMethodArgumentResolver 类
 */
@Slf4j
public class LoginRequiredArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * supportsParameter 方法用于解析哪些参数。
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // 匹配参数上具有 @LongRequired 注解的参数
        return methodParameter.hasParameterAnnotation(LongRequired.class);
    }

    /**
     * resolveArgument 方法用来实现解析本身。
     * 在这里，尝试从 Session 中获取当前用户的标识，如果无法取到的话提示非法调用的错误，如果获取到则返回 userId。
     * 这样 Controller 中的 userId 参数就可以自动赋值了。
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 从参数上获得注解
        LongRequired longRequired = methodParameter.getParameterAnnotation(LongRequired.class);
        // 根据注解中的 Session Key，从 session 中查询用户信息
        Object object = nativeWebRequest.getAttribute(longRequired.sessionKey(), NativeWebRequest.SCOPE_REQUEST);
        if (object == null) {
            log.error("接口 {} 非法调用。", methodParameter.getMethod().toString());
            throw new RuntimeException("请先登录!");
        }
        return object;
    }
}
