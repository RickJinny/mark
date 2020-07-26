package com.rickjinny.mark.controller.p18_advancedfeatures.t03_AnnotationInheritance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;

@Slf4j
public class AnnotationInheritanceApplication {

    private static String getAnnotationValue(MyAnnotation annotation) {
        if (annotation == null) {
            return "";
        }
        return annotation.value();
    }

    /**
     * 通过反射分别获取 Parent 和 Child 的类和方法的注解信息，并输出注解的 value 属性的值 (如果注解不存在则输出空字符串)。
     *
     * 结果：
     * ParentClass:Class
     * ParentMethod:Method
     * ChildClass:Class
     * ChildMethod:
     *
     * 可以看到：父类的类和方法上的注解，都可以正确获取，但是子类的类和方法却不能。这说明：子类以及子类的方法，无法自动继承父类和父类方法上的注解。
     */
    private static void wrong() throws NoSuchMethodException {
        // 获取父类的类和方法上的注解
        Parent parent = new Parent();
        log.info("ParentClass:{}", getAnnotationValue(parent.getClass().getAnnotation(MyAnnotation.class)));
        log.info("ParentMethod:{}", getAnnotationValue(parent.getClass().getMethod("foo").getAnnotation(MyAnnotation.class)));

        // 获取子类的类和方法上的注解
        Child child = new Child();
        log.info("ChildClass:{}", getAnnotationValue(child.getClass().getAnnotation(MyAnnotation.class)));
        log.info("ChildMethod:{}", getAnnotationValue(child.getClass().getMethod("foo").getAnnotation(MyAnnotation.class)));
    }

    /**
     * Spring 提供了 AnnotatedElementUtils 类，处理注解的继承问题。
     * AnnotatedElementUtils 类的 findMergedAnnotation 工具方法，可以帮助我们找出父类和接口、父类方法和接口方法上的注解，
     * 并可以处理桥接方法，实现一键找到继承链的注解。
     *
     * 结果：
     * ParentClass:Class
     * ParentMethod:Method
     * ChildClass:Class
     * ChildMethod:Method
     */
    private static void right() throws NoSuchMethodException {
        // 获取父类的类和方法上的注解
        Parent parent = new Parent();
        log.info("ParentClass:{}", getAnnotationValue(parent.getClass().getAnnotation(MyAnnotation.class)));
        log.info("ParentMethod:{}", getAnnotationValue(parent.getClass().getMethod("foo").getAnnotation(MyAnnotation.class)));

        // 获取子类的类和方法上的注解
        Child child = new Child();
        log.info("ChildClass:{}", getAnnotationValue(AnnotatedElementUtils.findMergedAnnotation(child.getClass(), MyAnnotation.class)));
        log.info("ChildMethod:{}", getAnnotationValue(AnnotatedElementUtils.findMergedAnnotation(child.getClass().getMethod("foo"),
                MyAnnotation.class)));
    }

    public static void main(String[] args) throws NoSuchMethodException {
//        wrong();
        right();
    }
}
