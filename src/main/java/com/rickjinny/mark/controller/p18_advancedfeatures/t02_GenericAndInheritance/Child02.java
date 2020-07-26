package com.rickjinny.mark.controller.p18_advancedfeatures.t02_GenericAndInheritance;

public class Child02 extends Parent<String> {

    @Override
    public void setValue(String value) {
        System.out.println("Child02.setValue called");
        super.setValue(value);
    }
}
