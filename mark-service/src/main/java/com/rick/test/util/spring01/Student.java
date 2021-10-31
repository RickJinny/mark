package com.rick.test.util.spring01;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;

public class Student implements Serializable, ApplicationContextAware {

    private int id;
    private String name;
    private ApplicationContext applicationContext;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void init() {
        System.out.println("hello----");
    }

    public Student create() {
        // 可以调用 ApplicationContext 的方法
        applicationContext.publishEvent(null);
        return new Student(80, "P80");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
