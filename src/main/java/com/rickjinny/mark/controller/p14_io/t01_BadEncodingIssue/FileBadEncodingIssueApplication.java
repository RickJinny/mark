package com.rickjinny.mark.controller.p14_io.t01_BadEncodingIssue;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileBadEncodingIssueApplication {

    /**
     * 使用 GBK 编码把 "你好 hi" 写入一个名为 hello.txt 的文本文件，然后直接以字节数组形式读取文件内容，转换为十六进制字符串输出到日志中。
     * 23:32:34.664 [main] INFO com.rickjinny.mark.controller.p14_io.t01_BadEncodingIssue.FileBadEncodingIssueApplication - byte:C4E3BAC36869
     */
    private static void init() throws IOException {
        Files.deleteIfExists(Paths.get("hello.txt"));
        Files.write(Paths.get("hello.txt"), "你好hi".getBytes(Charset.forName("GBK")));
        log.info("byte:{}", Hex.encodeHexString(Files.readAllBytes(Paths.get("hello.txt"))).toUpperCase());
    }

    /**
     * 使用了 FileReader 类，以字符方式进行文件读取，日志中读取出来的 "你好" 变为了乱码。
     * FileReader 是以当前机器的默认字符集来读取文件的。如果希望指定字符集的话，需要直接使用 InputStreamReader 和 FileInputStream。
     */
    private static void wrong() throws IOException {
        log.info("charset: {}", Charset.defaultCharset());
        char[] chars = new char[10];
        String content = StringUtils.EMPTY;
        try (FileReader fileReader = new FileReader("hello.txt")) {
            int count;
            while ((count = fileReader.read(chars)) != -1) {
                content += new String(chars, 0, count);
            }
        }
        log.info("result:{}", content);
        Files.write(Paths.get("hell02.txt"), "您好Hi".getBytes(Charsets.UTF_8));
        log.info("bytes:{}", Hex.encodeHexString(Files.readAllBytes(Paths.get("hello2.txt"))).toUpperCase());
    }

    /**
     * 上面的 FileReader 是以当前机器的默认字符集来读取文件的，如果希望指定字符集的话，需要直接使用 InputStreamReader 和 FileInputStream。
     *
     */
    private static void right1() throws IOException {
        char[] chars = new char[10];
        String content = "";
        try (FileInputStream fileInputStream = new FileInputStream("hello.txt");
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("GBK"))) {
            int count;
            while ((count = inputStreamReader.read(chars)) != -1) {
                content += new String(chars, 0, count);
            }
        }
        log.info("result:{}", content);
    }

    /**
     * 从 JDK1.7 推出的 Files 类的 readAllLines 方法，可以很方便地用一行代码完成文件内容读取。
     * 
     */
    private static void right2() throws IOException {
        log.info("result: {}", Files.readAllLines(Paths.get("hello.txt"), Charset.forName("GBK"))
                .stream().findFirst().orElse(""));
    }

    public static void main(String[] args) throws IOException {
//        init();
//        wrong();
//        right1();
        right2();
    }
}
