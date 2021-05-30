package com.rick.service;

public interface TransactionService {

    /**
     * 转入
     * @param name 账户名称
     * @param money 金额
     */
    void transferIn(String name, Integer money);


    /**
     * 转出
     * @param name 账户名称
     * @param money 金额
     */
    void transferOut(String name, Integer money);

}
