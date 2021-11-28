package com.rick.test.util.spring01;

import java.util.Arrays;

public class LambdaDemo {

    /**
     * 函数签名: 返回类型 + 函数名 + 参数类型的列表
     */
    interface MathOperation {
        int operation(int a, int b);
    }

    interface GreetingService {
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }

    public static void main(String[] args) {
        LambdaDemo lambdaDemo = new LambdaDemo();
        // 类型声明
        MathOperation mathOperation1 = (int a, int b) -> a + b;
        // 不用类型声明
        MathOperation mathOperation2 = (a, b) -> a - b;
        // 大括号中的返回语句
        MathOperation mathOperation3 = (int a, int b) -> {
            return a * b;
        };
        // 没有大括号及返回语句
        MathOperation mathOperation4 = (int a, int b) -> a / b;

        System.out.println("10 + 5 = " + lambdaDemo.operate(10, 5, mathOperation1));
        System.out.println("10 - 5 = " + lambdaDemo.operate(10, 5, mathOperation2));
        System.out.println("10 * 5 = " + lambdaDemo.operate(10, 5, mathOperation3));
        System.out.println("10 / 5 = " + lambdaDemo.operate(10, 5, mathOperation4));

        System.out.println("10 ^ 5 = " + lambdaDemo.operate(10, 5,
                (a, b) -> new Double(Math.pow(a, b)).intValue()));


        Arrays.asList(1, 2, 3, 4).forEach(a -> System.out.println(a + 3));
        Arrays.asList(1, 2, 3, 4).forEach(System.out::print);

    }

}
