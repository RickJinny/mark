package com.rickjinny.mark.controller.p11_null.t03_DBNull;

import com.rickjinny.mark.controller.p11_null.t03_DBNull.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(value = "/dbNull")
@Slf4j
public class DBNullController {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        userRepository.save(new User());
    }

    @GetMapping(value = "/wrong")
    public void wrong() {
        log.info("result: {} {} {} ", userRepository.wrong1(), userRepository.wrong2(), userRepository.wrong3());
    }

    @GetMapping(value = "/right")
    public void right() {
        log.info("result: {} {} {} ", userRepository.right1(), userRepository.right2(), userRepository.right2());
    }
}
