package com.rickjinny.mark.controller.p14_io.t02_FileStreamOperationNeedClose;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
public class FileStreamOperationNeedClose {

    /**
     * 测试 Files.line 方法：
     * 我们通过一段代码测试一下，我们尝试读取一个 1 亿 1 万 行的文件，文件占用磁盘的空间超过 4GB。
     * 如果使用 -Xmx512m  -Xms512m 启动 JVM 控制最大堆内存为 512MB 的话，肯定无法一次性读取这样的大文件，但通过 Files.lines() 方法就没问题。
     *
     * 在下面的代码中，首先输出这个文件的大小，然后计算读取 20 万行数据和 200 万行数据的耗时差异，最后逐行读取文件，统计文件的总行数。
     */
    private static void linesTest() throws IOException {
        // 输出文件大小
        log.info("file size:{}", Files.size(Paths.get("text.txt")));
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("read 200000 lines");
        // 使用 Files.lines 方法读取 20 万行数据
        log.info("lines {}", Files.lines(Paths.get("test.txt")).limit(200000).collect(Collectors.toList()).size());
        stopWatch.stop();

        stopWatch.start("read 2000000 lines");
        // 使用 Files.lines 方法读取 200 万行数据
        log.info("lines {}", Files.lines(Paths.get("test.txt")).limit(2000000).collect(Collectors.toList()).size());
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());

        AtomicLong atomicLong = new AtomicLong();
        // 使用 Files.lines() 方法统计文件总行数
        Files.lines(Paths.get("test.txt")).forEach(line -> atomicLong.incrementAndGet());
        log.info("total lines {}", atomicLong.get());
    }

    public static void main(String[] args) {

    }
}
