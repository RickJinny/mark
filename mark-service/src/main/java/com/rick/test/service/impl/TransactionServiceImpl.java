package com.rick.test.service.impl;

import com.rick.service.TransactionService;
import com.rick.test.dao.mapper.AccountMapper;
import com.rick.test.dao.mapper.UserMapper;
import com.rick.test.dao.model.Account;
import com.rick.test.dao.model.AccountExample;
import com.rick.test.dao.model.User;
import com.rick.vo.AddAccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private TransactionServiceImpl transactionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(String fromName, String toName, Integer money) {
        // 转出钱
        transferOut(fromName, money);

        // 制造出错
        int x = 1;
        if (x == 1) {
            throw new RuntimeException("出错啦!");
        }
        // 转入钱
        transferIn(toName, money);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAccount(AddAccountRequest addAccountRequest) {
        User user = new User();
        user.setUserId(new Random(5).nextLong());
        user.setUserName(addAccountRequest.getUserName());
        user.setUserType(addAccountRequest.getUserType().byteValue());
        userMapper.insertSelective(user);

        Account account = new Account();
        account.setName(addAccountRequest.getUserName());
        account.setMoney(1000);
        accountMapper.insertSelective(account);

        int x = 1;
        if (x == 1) {
            throw new RuntimeException("出错了!");
        }
    }

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
        account.setMoney(selectAccount.getMoney() + money);
        AccountExample updateExample = new AccountExample();
        updateExample.createCriteria().andNameEqualTo(name);
        accountMapper.updateByExampleSelective(account, updateExample);
    }

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
        account.setMoney(selectAccount.getMoney() - money);
        AccountExample updateExample = new AccountExample();
        updateExample.createCriteria().andNameEqualTo(name);
        accountMapper.updateByExampleSelective(account, updateExample);
    }


    /**
     * 2、事务传播特性测试
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void testTransactionalPropagation(String fromName, String toName, Integer money) {
        // 转出钱
        transferOut(fromName, money);
        try {
            transactionService.testSecond(toName, money);
        } catch (Exception e) {
            log.error("error", e);
            e.printStackTrace();
        }

        int x = 1;
        if (x == 3) {
            throw new RuntimeException("first 出错了!");
        }

    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void testSecond(String toName, Integer money) {
        // 转入钱
        transferIn(toName, money);
        int x = 1;
        if (x == 1) {
            throw new RuntimeException("second 出错啦!");
        }
    }

}
