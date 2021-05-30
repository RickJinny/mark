package com.rick.test.service.impl;

import com.rick.service.TransactionService;
import com.rick.test.dao.mapper.AccountMapper;
import com.rick.test.dao.model.Account;
import com.rick.test.dao.model.AccountExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public void transferIn(String name, Integer money) {
        AccountExample example = new AccountExample();
        example.createCriteria().andNameEqualTo(name);
        List<Account> accounts = accountMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(accounts)) {
            log.warn("account name: {},  not exist!", name);
            throw new RuntimeException(name + "账户不存在");
        }

        Account selectAccount = accounts.get(0);
        Account account = new Account();
        account.setMoney(selectAccount.getMoney() - money);
        AccountExample updateExample = new AccountExample();
        updateExample.createCriteria().andNameEqualTo(name);
        accountMapper.updateByExampleSelective(account, updateExample);
    }

    @Override
    public void transferOut(String name, Integer money) {
        AccountExample example = new AccountExample();
        example.createCriteria().andNameEqualTo(name);
        List<Account> accounts = accountMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(accounts)) {
            log.warn("account name: {},  not exist!", name);
            throw new RuntimeException(name + "账户不存在");
        }
        Account selectAccount = accounts.get(0);
        Account account = new Account();
        account.setMoney(selectAccount.getMoney() + money);
        AccountExample updateExample = new AccountExample();
        updateExample.createCriteria().andNameEqualTo(name);
        accountMapper.updateByExampleSelective(account, updateExample);
    }
}
