package com.rickjinny.mark.controller.p08_equals.t03_compareto;

import com.rickjinny.mark.controller.p08_equals.t03_compareto.bean.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestMapping(value = "/compareTo")
@RestController
@Slf4j
public class CompareToController {

    public static void wrong() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "aa"));
        list.add(new Student(2, "bb"));
        Student student = new Student(2, "cc");

        log.info("ArrayList.indexOf");
        int index01 = list.indexOf(student);
        Collections.sort(list);
        log.info("Collections.binarySearch");
        int index02 = Collections.binarySearch(list, student);

        log.info("index01 = " + index01); // index01 = -1
        log.info("index02 = " + index02); // index02 = 0
    }

    public static void main(String[] args) {
        wrong();
    }
}
