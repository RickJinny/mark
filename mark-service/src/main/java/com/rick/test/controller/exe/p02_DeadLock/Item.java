package com.rick.test.controller.exe.p02_DeadLock;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReentrantLock;

@Data
@RequiredArgsConstructor
public class Item {
    final String name;
    int remaining = 1000;
    ReentrantLock lock = new ReentrantLock();
}
