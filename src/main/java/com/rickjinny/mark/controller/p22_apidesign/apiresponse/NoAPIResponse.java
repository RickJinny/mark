package com.rickjinny.mark.controller.p22_apidesign.apiresponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个 @NoAPIResponse 自定义注解。
 * 如果某些 @RestController 的接口不希望实现自动实现包装的话，可以标注这个注解。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoAPIResponse {

}
