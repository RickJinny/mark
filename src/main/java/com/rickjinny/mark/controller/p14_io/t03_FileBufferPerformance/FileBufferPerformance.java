package com.rickjinny.mark.controller.p14_io.t03_FileBufferPerformance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.channels.FileChannel;
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
     *
     * 分析：每读取一个字节，每写入一个字节都进行一次 IO 操作，代价太大了。
     *
     * 解决方案：考虑使用缓冲区作为过渡，一次性从原文件读取一定数量的数据到缓冲区，一次性写入一定数量的数据到目标文件。
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

    /**
     * 解决方案：考虑使用缓冲区作为过渡，一次性从原文件读取一定数量的数据到缓冲区，一次性写入一定数量的数据到目标文件。
     *
     * 修改后，使用 100 字节作为缓冲区，使用 FileInputStream 的 byte[] 的重载来一次性读取一定字节的数据，同时使用 FileOutputStream 的
     * byte[] 的重载实现一次性从缓冲区写入一定字节的数据到文件。
     *
     * 仅仅使用了 100 个字节的缓冲区作为过渡，完成 35M 文件的复制耗时缩短到了 26 秒，是无缓冲时性能的 7 倍；如果把缓冲区放大到 1000 字节，耗时
     * 可以进一步缩短到 342 毫秒。可以看到，在进行文件 IO 处理的时候，使用合适的缓冲区可以明显提高性能。
     */
    private static void bufferOperationWith100Buffer() throws IOException {
        Files.deleteIfExists(Paths.get("dest.txt"));
        try (FileInputStream fileInputStream = new FileInputStream("src.txt");
             FileOutputStream fileOutputStream = new FileOutputStream("dest.txt")) {
            byte[] buffer = new byte[100];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        }
    }

    /**
     * 实现文件读写，还要自己 new 一个缓冲区出来，太麻烦了，不是有一个 BufferedInputStream 和 BufferedOutputStream 可以
     * 实现输入输出流的缓冲处理吗？
     * 是的，它们在内部实现了一个默认 8KB 大小的缓冲区。但是，在使用 BufferedInputStream 和 BufferedOutputStream 时，我还
     * 是建议你再使用一个缓冲进行读写，不要因为它们实现了内部缓冲就进行逐字节的操作。
     *
     * 接下来，我们来比较下面三种方式读写一个字节的性能:
     * 1、直接使用 BufferedInputStream 和 BufferedOutputStream;
     * 2、额外使用一个 8KB 缓冲，使用 BufferedInputStream 和 BufferedOutputStream;
     * 3、直接使用 FileInputStream 和 FileOutputStream，再使用一个 8KB 的缓冲
     */

    /**
     * 使用 BufferedInputStream 和 BufferedOutputStream
     *
     * 耗时：1.4秒
     */
    private static void bufferedStreamByteOperation() throws IOException {
        Files.deleteIfExists(Paths.get("dest.txt"));
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src.txt"));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("dest.txt"))) {
            int i;
            while ((i = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(i);
            }
        }
    }

    /**
     * 额外使用一个 8KB 缓冲，再使用 BufferedInputStream 和 BufferedOutputStream
     *
     * 耗时：0.11秒
     */
    private static void bufferedStreamBufferOperation() throws IOException {
        Files.deleteIfExists(Paths.get("dest.txt"));
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src.txt"));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("dest.txt"))) {
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, len);
            }
        }
    }

    /**
     * 直接使用 FileInputStream 和 FileOutputStream，再使用一个 8KB 的缓冲
     *
     * 耗时：0.11秒
     */
    private static void largerBufferOperation() throws IOException {
        Files.deleteIfExists(Paths.get("dest.txt"));
        try (FileInputStream fileInputStream = new FileInputStream("src.txt");
             FileOutputStream fileOutputStream = new FileOutputStream("dest.txt")) {
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        }
    }

    /**
     * 最高效的方法：
     *
     * 对于类似文件复制操作，如果希望有更高的性能，可以使用 FileChannel 的 transferTo 方法进行流的复制。
     * 在一些操作系统（比如高版本的 Linux 和 Unix）上，可以实现 DMA（直接内存访问），也就是数据从磁盘经过总线直接发送到目标文件，
     * 无需经过内存和 CPU 进行数据中转。
     */
    private static void fileChannelOperation() throws IOException {
        Files.deleteIfExists(Paths.get("dest.txt"));
        FileChannel in = FileChannel.open(Paths.get("src.txt"), StandardOpenOption.READ);
        FileChannel out = FileChannel.open(Paths.get("dest.txt"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        in.transferTo(0, in.size(), out);
    }

    public static void main(String[] args) throws IOException {

        StopWatch stopWatch = new StopWatch();
        init();
        stopWatch.start("perByteOperation");
        perByteOperation();
        stopWatch.stop();
        stopWatch.start("bufferOperationWith100Buffer");
        bufferOperationWith100Buffer();
        stopWatch.stop();
        stopWatch.start("bufferedStreamByteOperation");
        bufferedStreamByteOperation();
        stopWatch.stop();
        stopWatch.start("bufferedStreamBufferOperation");
        bufferedStreamBufferOperation();
        stopWatch.stop();
        stopWatch.start("largerBufferOperation");
        largerBufferOperation();
        stopWatch.stop();
        stopWatch.start("fileChannelOperation");
        fileChannelOperation();
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }
}
