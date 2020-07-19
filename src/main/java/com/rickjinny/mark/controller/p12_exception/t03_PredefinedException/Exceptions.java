package com.rickjinny.mark.controller.p12_exception.t03_PredefinedException;

/**
 * 千万不要将异常定义为静态变量。
 * 把异常定义为静态变量会导致异常信息固化，这就和异常的栈一定是需要根据当前调用来动态获取相矛盾。
 */
public class Exceptions {

    /**
     * 把异常定义为静态变量，导致异常栈信息错乱。
     * 类似于定义一个 Exceptions 类来汇总所有的异常，把异常存放在静态字段中。
     */
    public static BusinessException orderExists = new BusinessException("订单已经存在", 3001);

    public static BusinessException orderExists() {
        return new BusinessException("订单已经存在", 3001);
    }
}
