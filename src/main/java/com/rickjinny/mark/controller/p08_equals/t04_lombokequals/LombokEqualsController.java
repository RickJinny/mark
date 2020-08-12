package com.rickjinny.mark.controller.p08_equals.t04_lombokequals;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/lombokEquals")
@Slf4j
public class LombokEqualsController {

    public static void test1() {
        Person person01 = new Person("wang", "001");
        Person person02 = new Person("zhang", "001");
        log.info("person01.equals(person02) ? {} ", person01.equals(person02)); //  person01.equals(person02) ? false
    }

    public static void main(String[] args) {
        test1();
    }
}
