package com.rickjinny.mark.controller.p14_io.t01_BadEncodingIssue;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        init();
    }
}
