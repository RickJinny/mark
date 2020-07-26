package com.rickjinny.mark.controller.p18_advancedfeatures.t03_AnnotationInheritance;

import java.lang.annotation.*;

/**
 * 定义一个包含 value 属性的 MyAnnotation 注解，可以标记在方法或类上。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyAnnotation {
    String value();
}
