package com.rickjinny.mark.controller.p28_securitylastdefense.t03_preventduplicatepay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RequestMapping(value = "/preventDuplicatePay")
@RestController
@Slf4j
public class PreventDuplicatePayController {

    /**
     * 错误：每次使用 UUID 作为订单号
     */
    @RequestMapping(value = "/wrong")
    public void wrong(@RequestParam("orderId") String orderId) {
        PayChannel.pay(UUID.randomUUID().toString(), "123", new BigDecimal("100"));
    }

    /**
     * 正确的方法：使用相同的业务订单号
     */
    @RequestMapping(value = "/right")
    public void right(@RequestParam("orderId") String orderId) {
        PayChannel.pay(orderId, "123", new BigDecimal("100"));
    }
}
