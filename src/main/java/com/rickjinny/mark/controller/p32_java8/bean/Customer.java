package com.rickjinny.mark.controller.p32_java8.bean;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 顾客类
 */
@Data
@AllArgsConstructor
public class Customer {

    private Long id;

    // 顾客姓名
    private String name;

    public static List<Customer> getData() {
        return Lists.newArrayList(
                new Customer(10L, "小张"),
                new Customer(11L, "小王"),
                new Customer(12L, "小李"),
                new Customer(13L, "小朱"),
                new Customer(14L, "小徐")
        );
    }
}
