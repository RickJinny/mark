package com.rick.test.util.spring01;

public class LambdaDemo {



    interface MathOperation {
        // 函数签名: 返回类型 + 函数名 + 参数类型的列表
        int operation(int a, int b);
    }

    interface GreetingService {
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }
}
