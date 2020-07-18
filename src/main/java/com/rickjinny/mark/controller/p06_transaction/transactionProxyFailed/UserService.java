package com.rickjinny.mark.controller.p06_transaction.transactionProxyFailed;

import com.rickjinny.mark.controller.p06_transaction.UserRepository;
import com.rickjinny.mark.controller.p06_transaction.bean.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        log.info("this {} self {}", getClass().getClass(), getClass().getName());
    }

    /**
     * 一个公共方法供 Controller 调用，内部调用事务性的私有方法
     */
    public int createUserWrong1(String name) {
        try {
            createUserPrivate(new UserEntity(name));
        } catch (Exception e) {
            log.error("create user failed because {}", e.getMessage());
        }
        return userRepository.findByName(name).size();
    }

    /**
     * 标记了 @Transactional 的 private 方法
     * 这里使用了 private，为了不报错，先使用 public 代替
     */
    @Transactional
    public void createUserPrivate(UserEntity entity) {
        userRepository.save(entity);
        if (entity.getName().contains("test")) {
            throw new RuntimeException("invalid username!");
        }
    }
}
