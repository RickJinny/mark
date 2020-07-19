package com.rickjinny.mark.controller.p12_exception.t03_PredefinedException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/predefinedException")
@RestController
@Slf4j
public class PredefinedExceptionController {

    @GetMapping(value = "/wrong")
    public void wrong() {
        try {
            createOrderWrong();
        } catch (Exception e) {
            log.error("createOrder got error", e);
        }

        try {
            cancelOrderWrong();
        } catch (Exception e) {
            log.error("cancelOrder got error", e);
        }
    }

    @GetMapping(value = "/right")
    public void right() {
        try {
            createOrderRight();
        } catch (Exception e) {
            log.error("createOrder got error", e);
        }

        try {
            cancelOrderRight();
        } catch (Exception e) {
            log.error("cancelOrder got error", e);
        }
    }

    /**
     * 定义方法 createOrderWrong() 和 cancelOrderWrong()，他们内部都会通过 Exceptions 来获得一个订单不存在的异常;
     * 先后调用两个方法，然后抛出。
     */
    private void createOrderWrong() {
        // 这里有问题
        throw Exceptions.orderExists;
    }

    private void cancelOrderWrong() {
        // 这里有问题
        throw Exceptions.orderExists;
    }

    private void createOrderRight() {
        throw Exceptions.orderExists();
    }

    private void cancelOrderRight() {
        throw Exceptions.orderExists();
    }
}
