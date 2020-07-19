package com.rickjinny.mark.controller.p06_transaction.t03_TransactionPropagation;

import com.rickjinny.mark.controller.p06_transaction.bean.UserEntity;
import com.rickjinny.mark.controller.p06_transaction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 有这么一个场景：
 * 一个用户注册的操作，会插入一个主用户到用户表，还会注册一个关联的子用户。
 * 我们希望将子用户注册的数据库操作作为一个独立事务来处理，即使失败也不会影响主流程，即不影响主用户的注册。
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubUserService subUserService;

    @Transactional
    public void createUserWrong1(UserEntity entity) {
        createMainUser(entity);
        subUserService.createSubUserWithExceptionWrong(entity);
    }

    @Transactional
    public void createUserWrong2(UserEntity entity) {
        createMainUser(entity);
        try {
            subUserService.createSubUserWithExceptionWrong(entity);
        } catch (Exception e) {
            // 虽然捕获了异常，但是因为没有开启新事务，而当前事务因为异常已被标记为 rollback 了，所以最终还是会回滚。
            log.error("create sub user error: {}", e.getMessage());
        }
    }

    /**
     * 主方法，同样需要捕获，防止异常漏出去导致主事务回滚。
     */
    @Transactional
    public void createUserRight(UserEntity entity) {
        createMainUser(entity);
        try {
            subUserService.createSubUserWithExceptionRight(entity);
        } catch (Exception e) {
            // 捕获异常，防止主方法回滚
            log.error("create sub user error:{}", e.getMessage());
        }
    }

    private void createMainUser(UserEntity entity) {
        userRepository.save(entity);
        log.info("createMainUser finish");
    }

    public int getUserCount(String name) {
        return userRepository.findByName(name).size();
    }
}
