package com.rickjinny.mark.controller.p12_exception.t01_HandleException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@Slf4j
@RequestMapping(value = "/handleException")
public class HandleExceptionController {

    @GetMapping(value = "/exception")
    public void exception(@RequestParam("business") boolean b) {
        if (b) {
            throw new BusinessException("订单不存在", 2001);
        }
        throw new RuntimeException("系统错误");
    }

    /**
     * 在 wrong1() 方法中，调用 readFile() 方法，捕获异常后，完全不记录原始异常，直接抛出一个转换后异常，
     * 导致出了问题不知道 IOException 具体是哪里引起的。
     */
    @GetMapping(value = "/wrong1")
    public void wrong1() {
        try {
            readFile();
        } catch (IOException e) {
            // 原始异常信息丢失
            throw new RuntimeException("系统忙，请稍后再试!");
        }
    }

    /**
     * 只记录了异常消息，却丢失了异常的类型、栈等重要信息。
     */
    @GetMapping(value = "wrong2")
    public void wrong2() {
        try {
            readFile();
        } catch (IOException e) {
            // 只保留了异常消息, 栈没有记录
            log.error("文件读取错误, {}", e.getMessage());
            throw new RuntimeException("系统忙，请稍后再试! ");
        }
    }

    @GetMapping(value = "/wrong3")
    public void wrong3(@RequestParam("orderId") String orderId) {
        try {
            readFile();
        } catch (Exception e) {
            log.error("文件读取错误", e);
            throw new RuntimeException();
        }
    }

    /**
     * 正确的打印异常的方法1，使用：
     * log.error("文件读取错误", e);
     * throw new RuntimeException("系统繁忙，请稍后再试!");
     */
    @GetMapping(value = "/right1")
    public void right1() {
        try {
            readFile();
        } catch (Exception e) {
            log.error("文件读取错误", e);
            throw new RuntimeException("系统繁忙，请稍后再试!");
        }
    }

    /**
     * 正确的打印异常的方法2，使用：
     * throw new RuntimeException("系统忙，请稍后再试", e);
     * 把原始异常作为转换后新异常的 cause，原始异常信息同样不会丢失。
     */
    @GetMapping(value = "/right2")
    public void right2() {
        try {
            readFile();
        } catch (Exception e) {
            throw new RuntimeException("系统忙，请稍后再试", e);
        }
    }

    /**
     * 比如有这么一个会抛出受检异常的方法 readFile()
     */
    private void readFile() throws IOException {
        Files.readAllLines(Paths.get("a_file"));
    }
}
