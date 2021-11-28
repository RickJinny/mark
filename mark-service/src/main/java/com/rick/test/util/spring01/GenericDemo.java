package com.rick.test.util.spring01;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 怎么输出一个泛型，去找这个泛型
 */
public class GenericDemo {

    public static class Person<T> {

    }

    public static class Test extends Person<GenericDemo> {

    }

    public static void main(String[] args) {
        Test test = new Test();
        Class<? extends Test> aClass = test.getClass();

        /**
         * getSuperclass() 获得该类的父类
         */
        Class<?> superclass = aClass.getSuperclass();
        System.out.println(superclass);

        /**
         * getGenericSuperclass() 获得带有泛型的父类
         * Type 是 Java 编程语言中所有类型的公共高级接口，他们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
         */
        Type type = aClass.getGenericSuperclass();
        System.out.println(type);

        /**
         * ParameterizedType 参数化类型，即泛型
         */
        ParameterizedType parameterizedType = (ParameterizedType) type;
        // getActualTypeArguments 获取参数化类型的数组，泛型可能有多个
        Class c = (Class) parameterizedType.getActualTypeArguments()[0];
        System.out.println(c);
    }

}
