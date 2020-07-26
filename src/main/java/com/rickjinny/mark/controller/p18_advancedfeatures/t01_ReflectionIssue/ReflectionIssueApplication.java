package com.rickjinny.mark.controller.p18_advancedfeatures.t01_ReflectionIssue;

import lombok.extern.slf4j.Slf4j;

/**
 * 1、反射调用方法不是以传参决定重载
 * 反射的功能包括，在运行时动态获取类和类成员定义，以及动态读取属性调用方法。也就是说，针对类动态调用方法，不管类中字段和方法怎么变动，
 * 我们都可以用相同的规则来读取信息和执行方法。因此，几乎所有的 ORM(对象关系映射)、对象映射、MVC框架都使用了反射。
 * 反射的起点是 Class 类，Class 类提供了各种方法帮我们查询它的信息。
 */
@Slf4j
public class ReflectionIssueApplication {

    /**
     * 第一个坑：反射调用方法遇到重载的坑。
     * 有两个叫做 age 的方法，入参分别是基本类型 int 和 包装类型 Integer
     */
    private void age(int age) {
        log.info("int age = {}", age);
    }

    private void age(Integer age) {
        log.info("Integer age = {}", age);
    }

    /**
     * 15:51:05.029 [main] INFO com.rickjinny.mark.controller.p18_advancedfeatures.t01_ReflectionIssue
     * .ReflectionIssueApplication - int age = 30
     *
     * 其实，要通过反射进行方法调用，第一步就是通过方法签名来确定方法。具体到这个案例，getDeclaredMethod 传入的参数类型是 Integer.TYPE
     * 代表的是 int，所以实际执行方法时无论传的是包装类型还是基本类型，都会调用 int 入参的 age 方法。
     */
    public void wrong() throws Exception {
        getClass().getDeclaredMethod("age", Integer.TYPE).invoke(this, Integer.valueOf("30"));
    }

    /**
     * 15:51:05.045 [main] INFO com.rickjinny.mark.controller.p18_advancedfeatures
     * .t01_ReflectionIssue.ReflectionIssueApplication - Integer age = 30
     *
     * 15:51:05.045 [main] INFO com.rickjinny.mark.controller.p18_advancedfeatures
     * .t01_ReflectionIssue.ReflectionIssueApplication - Integer age = 30
     *
     * 把 Integer.TYPE 改为 Integer.class，执行的参数类型就是包装类型的 Integer。这时无论传入的是 Integer.valueOf("30")
     * 还是基本类型的 36。
     */
    public void right() throws Exception {
        getClass().getDeclaredMethod("age", Integer.class).invoke(this, Integer.valueOf("30"));
        getClass().getDeclaredMethod("age", Integer.class).invoke(this, 30);
    }

    public static void main(String[] args) throws Exception {
        ReflectionIssueApplication application = new ReflectionIssueApplication();
        application.wrong();
        application.right();
    }
}
