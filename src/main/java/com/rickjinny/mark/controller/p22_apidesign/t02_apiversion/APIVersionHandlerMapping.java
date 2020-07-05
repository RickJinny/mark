package com.rickjinny.mark.controller.p22_apidesign.t02_apiversion;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * 定义 APIVersionHandlerMapping 类，继承自 RequestMappingHandlerMapping 类
 *
 * RequestMappingHandlerMapping 的作用: 是根据类或方法上的 @RequestMapping 来生成 RequestMappingInfo 实例。
 * 我们覆盖 registerHandlerMethod 方法的实现, 从 @APIVersion 自定义注解中读取版本信息, 拼接上原有的、不带版本号的 URL Pattern，
 * 构成新的 RequestMappingInfo, 来通过注解的方式为接口增加基于 URL 的版本号。
 */
public class APIVersionHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Controller.class);
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        Class<?> controllerClass = method.getDeclaringClass();
        // 类上的 APIVersion 注解
        APIVersion apiVersion = AnnotationUtils.findAnnotation(controllerClass, APIVersion.class);
        // 方法上的 APIVersion 注解
        APIVersion methodAnnotation = AnnotationUtils.findAnnotation(method, APIVersion.class);
        // 以上方法上的注解优先
        if (methodAnnotation != null) {
            apiVersion = methodAnnotation;
        }
        String[] urlPatterns = apiVersion == null ? new String[0] : apiVersion.value();
        PatternsRequestCondition apiPattern = new PatternsRequestCondition(urlPatterns);
        PatternsRequestCondition oldPattern = mapping.getPatternsCondition();
        PatternsRequestCondition updatedFinalPattern = apiPattern.combine(oldPattern);
        mapping = new RequestMappingInfo(mapping.getName(), updatedFinalPattern, mapping.getMethodsCondition(),
                mapping.getParamsCondition(), mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                mapping.getProducesCondition(), mapping.getCustomCondition());
        super.registerHandlerMethod(handler, method, mapping);
    }
}
