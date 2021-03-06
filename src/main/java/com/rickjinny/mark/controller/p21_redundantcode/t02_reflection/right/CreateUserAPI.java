package com.rickjinny.mark.controller.p21_redundantcode.t02_reflection.right;

import lombok.Data;

@Data
@BankAPI(url = "/bank/createUser", desc = "创建用户接口")
public class CreateUserAPI extends AbstractAPI {
    @BankAPIField(order = 1, type = "S", length = 10)
    private String name;
    @BankAPIField(order = 2, type = "S", length = 18)
    private String identity;
    @BankAPIField(order = 4, type = "S", length = 11) // 注意这里的 order 需要按照 API 表格中的顺序
    private String mobile;
    @BankAPIField(order = 3, type = "N", length = 5)
    private int age;
}
