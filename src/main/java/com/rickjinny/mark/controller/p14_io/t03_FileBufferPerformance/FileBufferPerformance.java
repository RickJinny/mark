package com.rickjinny.mark.controller.p14_io.t03_FileBufferPerformance;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 注意读写文件要考虑设置缓冲区
 */
@Slf4j
public class FileBufferPerformance {

    /**
     * 创建一个文件随机写入 100 万行数据，文件大小在 35MB 左右
     */
    private static void init() throws IOException {
        Files.write(Paths.get("src.txt"), IntStream.rangeClosed(1, 1000000)
                        .mapToObj(i -> UUID.randomUUID().toString()).collect(Collectors.toList()),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * 使用 FileInputStream 获得一个文件输入流，然后调用其 read 方法每次读取一个字节，最后通过一个 FileOutputStream 文件输出流
     * 把处理后的结果写入另一个文件。
     * 为了简化逻辑便于理解，这里我们不对数据进行处理，直接把原文件数据写入目标文件，相当于文件复制。
     *
     * 这样的实现，复制一个 35MB 的文件居然耗时 190 秒。
     */
    private static void perByteOperation() throws IOException {
        Files.deleteIfExists(Paths.get("dest.txt"));
        try (FileInputStream fileInputStream = new FileInputStream("src.txt");
             FileOutputStream fileOutputStream = new FileOutputStream("dest.txt")) {
            int i;
            while ((i = fileInputStream.read()) != -1) {
                fileOutputStream.write(i);
            }
        }
    }
}
