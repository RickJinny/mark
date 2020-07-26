package com.rickjinny.mark.controller.p18_advancedfeatures.t03_AnnotationInheritance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@MyAnnotation(value = "Class")
public class Parent {

    @MyAnnotation(value = "Method")
    public void foo() {

    }
}
