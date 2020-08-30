package com.rick.test;

import com.rick.test.controller.p01.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        Utils.loadPropertySource(TestApplication.class, "tomcat.properties");
        SpringApplication.run(TestApplication.class, args);
    }
}
