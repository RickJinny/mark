package com.rickjinny.mark.controller.p08_equals.t03_compareto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class Student implements Comparable<Student> {

    private int id;

    private String name;

    @Override
    public int compareTo(Student other) {
        int result = Integer.compare(other.id, id);
        if (result == 0) {
            log.info("this {} == other {}", this, other);
        }
        return result;
    }
}
