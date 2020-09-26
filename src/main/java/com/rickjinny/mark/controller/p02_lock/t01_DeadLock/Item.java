package com.rickjinny.mark.controller.p02_lock.t01_DeadLock;

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
