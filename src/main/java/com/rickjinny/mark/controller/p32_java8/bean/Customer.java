package com.rickjinny.mark.controller.p32_java8.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 顾客类
 */
@Data
@AllArgsConstructor
public class Customer {

    private Long id;

    // 顾客姓名
    private String name;
}
