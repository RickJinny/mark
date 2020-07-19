package com.rickjinny.mark.controller.p12_exception.t02_FinallyIssue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/finallyIssue")
@Slf4j
public class FinallyIssueController {

    /**
     * 在 finally 中也有可能出现异常。
     * 最后在日志中，只能看到 finally 中的异常，虽然 try 中的逻辑出现了异常，但却被 finally 中的异常覆盖了。
     * 这是非常危险的，特别是 finally 中出现的异常是偶发的，就会在部分时候覆盖 try 中的异常，让问题更不明显。
     *
     * 为什么异常会被覆盖？
     * 原因很简单，因为一个方法无法出现两个异常。修复的方式是 finally 代码块自己负责异常捕获和处理。
     */
    @RequestMapping(value = "/wrong")
    public void wrong() {
        try {
            log.info("try");
            // 异常丢失
            throw new RuntimeException("try");
        } finally {
            log.info("finally");
            throw new RuntimeException("finally");
        }
    }

    /**
     * 第一个种修复的方法：在 finally 代码块自己负责异常捕获和处理。
     */
    @RequestMapping(value = "/right")
    public void right() {
        try {
            log.info("try");
            throw new RuntimeException("try");
        } finally {
            log.info("finally");
            try {
                throw new RuntimeException("finally");
            } catch (Exception e) {
                log.error("finally", e);
            }
        }
    }

    /**
     * 第一个种修复的方法：可以把 try 中的异常作为主异常常抛出，使用 addSuppressed 方法把 finally 中的异常附加到主异常上。
     */
    @RequestMapping(value = "/right2")
    public void right2() throws Exception {
        Exception e = null;
        try {
            log.info("try");
            throw new RuntimeException("try");
        } catch (Exception ex) {
            e = ex;
        } finally {
            log.info("finally");
            try {
                throw new RuntimeException("finally");
            } catch (Exception ex) {
                if (e != null) {
                    e.addSuppressed(ex);
                } else {
                    e = ex;
                }
            }
        }
        throw e;
    }

    /**
     * 使用传统的 try - finally 语句，在 try 中调用 read 方法，在 finally 中调用 close 方法。
     * 结果：同样出现了 finally 中的异常覆盖了 try 中异常的问题。
     */
    @GetMapping(value = "/useResourceWrong")
    public void useResourceWrong() throws Exception {
        TestResource testResource = new TestResource();
        try {
            testResource.read();
        } finally {
            testResource.close();
        }
    }

    /**
     * 改为 try - with - resources 模式
     */
    @GetMapping(value = "/useResourceRight")
    public void useResourceRight() throws Exception {
        try (TestResource testResource = new TestResource()) {
            testResource.read();
        }
    }
}
