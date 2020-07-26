package com.rickjinny.mark.controller.p18_advancedfeatures.t03_AnnotationInheritance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Child extends Parent {

    @Override
    public void foo() {
        super.foo();
    }
}
