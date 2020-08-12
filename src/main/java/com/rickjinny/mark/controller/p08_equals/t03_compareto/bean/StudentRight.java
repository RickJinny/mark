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

    /**
     * 确保 compareTo 的比较逻辑和 equals 的实现一致即可。
     * 通过 Comparator.comparing 方法来实现两个字段的比较。
     */
    @Override
    public int compareTo(StudentRight other) {
        return Comparator.comparing(StudentRight::getName)
                .thenComparing(StudentRight::getId)
                .compare(this, other);
    }
}
