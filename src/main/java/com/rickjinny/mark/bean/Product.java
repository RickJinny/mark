package com.rickjinny.mark.bean;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.concurrent.locks.ReentrantLock;

@Data
@RequiredArgsConstructor
public class Product {
    // 商品名
    public final String name;
    // 库存剩余
    public int remaining = 1000;
    // ToString 不包含这个字段
    @ToString.Exclude
    public ReentrantLock lock = new ReentrantLock();
}
