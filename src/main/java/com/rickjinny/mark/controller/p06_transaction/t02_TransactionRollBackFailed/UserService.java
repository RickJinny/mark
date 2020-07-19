package com.rickjinny.mark.controller.p06_transaction.t02_TransactionRollBackFailed;

import com.rickjinny.mark.controller.p06_transaction.bean.UserEntity;
import com.rickjinny.mark.controller.p06_transaction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 异常无法传播出方法，导致事务无法回滚。
     *
     * 在该方法 createUserWrong1() 中，会抛出一个 RuntimeException，但是由于方法内 catch 了所有的异常，
     * 异常无法从方法传播出去，事务自然无法回滚。
     */
    @Transactional
    public void createUserWrong1(String name) {
        try {
            userRepository.save(new UserEntity(name));
            throw new RuntimeException("error");
        } catch (Exception e) {
            log.error("create user failed", e);
        }
    }

    /**
     * 即使出了受检异常，也无法让事务回滚。
     *
     * 在该方法 createUserWrong2() 中，注册用户的同时会有一次 otherTask 文件读取操作，如果文件读取失败，我们希望用户注册的数据库操作回滚。
     * 虽然这里没有捕获异常，但因为 otherTask 方法抛出的是受检异常，createUserWrong2() 传播出去的也是受检异常，事务同样不会滚。
     */
    @Transactional
    public void createUserWrong2(String name) throws IOException {
        userRepository.save(new UserEntity(name));
        otherTask();
    }

    /**
     * 因为文件不存在，一定会抛出一个 IOException
     */
    private void otherTask() throws IOException {
        Files.readAllLines(Paths.get("file-that-not-exist"));
    }

    /**
     * 第一种正确处理事务回滚的方法：如果你希望自己捕获异常进行处理的话，也没关系，可以手动设置让当前事务处于回滚状态。
     */
    @Transactional
    public void createUserRight1(String name) {
        try {
            userRepository.save(new UserEntity(name));
            throw new RuntimeException("error");
        } catch (Exception e) {
            log.error("create user failed", e);
            // 手动设置异常时，进行事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 第二种正确处理事务回滚的方法：在注解中声明，期望遇到所有的 Exception 都回滚事务（未突破默认不回滚受检异常的限制）
     */
    @Transactional(rollbackFor = Exception.class)
    public void createRight2(String name) throws IOException {
        userRepository.save(new UserEntity(name));
        otherTask();
    }
}
