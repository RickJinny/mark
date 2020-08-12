package com.rickjinny.mark.controller.p08_equals.t04_lombokequals;

import lombok.Data;

@Data
public class Person {

    private String name;

    private String identity;

    public Person(String name, String identity) {
        this.name = name;
        this.identity = identity;
    }
}
