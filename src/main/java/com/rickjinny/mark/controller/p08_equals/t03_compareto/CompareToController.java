package com.rickjinny.mark.controller.p08_equals.t03_compareto;

import com.rickjinny.mark.controller.p08_equals.t03_compareto.bean.Student;
import com.rickjinny.mark.controller.p08_equals.t03_compareto.bean.StudentRight;
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

    /**
     * binarySearch 方法内部调用了元素的 compareTo 方法进行比较
     * indexOf 的结果没有问题，
     */
    public static void wrong() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "zhang"));
        list.add(new Student(2, "wang"));
        Student student = new Student(2, "li");

        log.info("ArrayList.indexOf");
        int index01 = list.indexOf(student);
        Collections.sort(list);
        log.info("Collections.binarySearch");
        int index02 = Collections.binarySearch(list, student);

        log.info("index01 = " + index01); // index01 = -1
        log.info("index02 = " + index02); // index02 = 0
    }

    public static void right() {
        List<StudentRight> list = new ArrayList<>();
        list.add(new StudentRight(1, "zhang"));
        list.add(new StudentRight(2, "wang"));
        StudentRight student = new StudentRight(2, "li");

        log.info("ArrayList.indexOf");
        int index01 = list.indexOf(student);
        Collections.sort(list);
        log.info("Collections.binarySearch");
        int index02 = Collections.binarySearch(list, student);
        log.info("index01 = " + index01); // index01 = -1
        log.info("index02 = " + index02); // index02 = -1
    }

    public static void main(String[] args) {
//        wrong();
        right();
    }
}
