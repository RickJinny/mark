package com.rickjinny.mark.controller.p08_equals.t03_compareto.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;

@Slf4j
@Data
@AllArgsConstructor
public class StudentRight implements Comparable<StudentRight> {

    private int id;

    private String name;

    @Override
    public int compareTo(StudentRight other) {
        return Comparator.comparing(StudentRight::getName)
                .thenComparing(StudentRight::getId)
                .compare(this, other);
    }
}
