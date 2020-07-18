package com.rickjinny.mark.controller.p06_transaction.TransactionPropagation;

import com.rickjinny.mark.controller.p06_transaction.bean.UserEntity;
import com.rickjinny.mark.controller.p06_transaction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SubUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 最后我们抛出了一个运行时异常，错误原因是用户状态无效，所以子用户的注册肯定是失败的。
     * 我们期望子用户的注册作为一个事务单独回滚，不影响主用户的注册。
     */
    @Transactional
    public void createSubUserWithExceptionWrong(UserEntity entity) {
        log.info("createSubUserWithExceptionWrong start");
        userRepository.save(entity);
        throw new RuntimeException("invalid status");
    }

    /**
     * 在 @Transactional 注解上加上 propagation = Propagation.REQUIRES_NEW 来设置 REQUIRES_NEW 方式的事务传播策略，
     * 也就是执行到这个方法时需要开启新的事务，并挂起当前事务。
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createSubUserWithExceptionRight(UserEntity entity) {
        log.info("createSubUserWithExceptionRight start");
        userRepository.save(entity);
        throw new RuntimeException("invalid status");
    }
}
