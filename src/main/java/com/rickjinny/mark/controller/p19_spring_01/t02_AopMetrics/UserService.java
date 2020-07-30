package com.rickjinny.mark.controller.p19_spring_01.t02_AopMetrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Metrics(ignoreException = true)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Metrics // 应用方法监控
    public void createUser(UserEntity entity) {
        userRepository.save(entity);
        if (entity.getName().contains("test")) {
            throw new RuntimeException("invalid username");
        }
    }

    public int getUserCount(String name) {
        return userRepository.findByName(name).size();
    }
}
