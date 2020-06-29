package com.rickjinny.mark.controller.p31_java8;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

/**
 * 并行流: 前面看到的 Stream 操作都是串行的 Stream, 操作只是在一个线程中执行。
 * 此外 Java8 还提供了并行流的功能: 通过 parallel 方法, 一键把 Stream 转换为并行操作提交到线程池处理。
 */
@Slf4j
public class T31_03_ParallelTest {

    /**
     * 通过线程池来处理 1 到 100
     */
    @Test
    public void parallel() {
        IntStream.rangeClosed(1, 100).parallel().forEach(i -> {
            System.out.println(LocalDateTime.now() + " : " + i + " " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
