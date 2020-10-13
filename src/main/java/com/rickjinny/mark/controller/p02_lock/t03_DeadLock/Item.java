package com.rickjinny.mark.controller.p02_lock.t03_DeadLock;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.concurrent.locks.ReentrantLock;

@Data
@RequiredArgsConstructor
public class Item {
    // 商品名
    final String name;
    // 库存剩余
    int remaining = 1000;
    @ToString.Exclude // ToString 不包含这个字段
    ReentrantLock lock = new ReentrantLock();
}
