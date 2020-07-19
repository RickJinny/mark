package com.rickjinny.mark.controller.p12_exception.t02_FinallyIssue;

/**
 * 对于实现了 AutoCloseable 接口的资源，建议使用 try-with-resources 来释放资源，否则也可能会产生刚才提到的，
 * 释放资源时出现的异常覆盖主异常的问题。比如如果我们定义一个测试资源，其 read 和 close 方法，都会抛出异常。
 */
public class TestResource implements AutoCloseable {

    @Override
    public void close() throws Exception {
        throw new Exception("Close Error");
    }

    public void read() throws Exception {
        throw new Exception("read error");
    }
}
