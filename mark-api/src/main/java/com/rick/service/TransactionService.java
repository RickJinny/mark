package com.rick.service;

public interface TransactionService {

    /**
     * 转账
     * @param fromName 转出用户
     * @param toName 转入用户
     * @param money 转账金额
     */
    void transfer(String fromName, String toName, Integer money);
}
