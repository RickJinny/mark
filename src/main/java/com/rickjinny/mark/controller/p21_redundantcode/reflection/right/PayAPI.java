package com.rickjinny.mark.controller.p21_redundantcode.reflection.right;

import lombok.Data;

import java.math.BigDecimal;

@Data
@BankAPI(url = "/bank/pay", desc = "支付接口")
public class PayAPI extends AbstractAPI {
    @BankAPIField(order = 1, type = "N", length = 20)
    private long userId;
    @BankAPIField(order = 2, type = "M", length = 10)
    private BigDecimal amount;
}
