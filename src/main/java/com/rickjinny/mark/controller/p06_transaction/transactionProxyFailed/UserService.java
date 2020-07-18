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
     * 发现调用 createUserPrivate() 方法不生效，
     * 注解 @Transactional 生效原则1：除非特殊配置（比如使用 AspectJ 静态织入实现 AOP）, 否则只有定义在 public 方法上的 @Transactional 才能生效。
     * 原因是：Spring 默认通过动态代理的方式实现 AOP，对目标方法进行增强，private 方法无法代理到，Spring 自然也无法动态增强事务处理逻辑。
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
     * 标记了 @Transactional 的 private 方法。
     * 这里使用了 private，为了不报错，先使用 public 代替。
     */
    @Transactional
    public void createUserPrivate(UserEntity entity) {
        userRepository.save(entity);
        if (entity.getName().contains("test")) {
            throw new RuntimeException("invalid username!");
        }
    }

    /**
     * 现在把 @Transactional 改为 public 方法。发现还是不生效。
     * 此时 @Transactional 没有生效的原则是：必须通过代理过的类从外部调用目标方法才能生效。
     * Spring 通过 AOP 技术对方法进行增强，要调用增强过的方法必然是调用代理后的对象。
     */
    public int createWrong2(String name) {
        try {
            createUserPublic(new UserEntity(name));
        } catch (Exception e) {
            log.error("create user failed because {}", e.getMessage());
        }
        return userRepository.findByName(name).size();
    }

    /**
     * 标记了 @Transactional 的 public 方法
     */
    @Transactional
    public void createUserPublic(UserEntity entity) {
        userRepository.save(entity);
        if (entity.getName().contains("test")) {
            throw new RuntimeException("invalid username!");
        }
    }

    private int getUserCount(String name) {
        return userRepository.findByName(name).size();
    }
}
