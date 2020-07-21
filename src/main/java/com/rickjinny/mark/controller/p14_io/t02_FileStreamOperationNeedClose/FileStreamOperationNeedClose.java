package com.rickjinny.mark.controller.p14_io.t02_FileStreamOperationNeedClose;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /**
     * 上面的第一案例：程序在生产环境上运行一段时间后，就会出现 too many files 的错误。
     * 经排查发现，其实是文件句柄没有释放导致的，问题就出在 Files.lines() 方法上。
     *
     * 我们来重现这个问题，随便写入 10 行数据到一个 demo.txt 文件中。
     */
    private static void init() throws IOException {
        Files.write(Paths.get("demo.txt"), IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> UUID.randomUUID().toString())
                        .collect(Collectors.toList()),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * 然后使用 Files.lines() 方法读取这个文件 100 万次，每读取一行计数器+1
     */
    private static void wrong() {
        LongAdder longAdder = new LongAdder();
        IntStream.rangeClosed(1, 1000000).forEach(i -> {
            try {
                Files.lines(Paths.get("demo.txt")).forEach(line -> longAdder.increment());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        log.info("total : {}", longAdder.longValue());
    }

    public static void main(String[] args) {

    }
}
