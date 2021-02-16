package com.rick.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
//        Utils.loadPropertySource(TestApplication.class, "tomcat.properties");
        SpringApplication.run(TestApplication.class, args);
    }
}
