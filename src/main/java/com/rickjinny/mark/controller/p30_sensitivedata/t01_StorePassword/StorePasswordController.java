package com.rickjinny.mark.controller.p30_sensitivedata.t01_StorePassword;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/storePassword")
public class StorePasswordController {

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/wrong1")
    public UserData wrong1(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        // 密码字段使用 md5 哈希后，保存
        userData.setPassword(DigestUtils.md5Hex(password));
        return userRepository.save(userData);
    }

    @RequestMapping("/wrong2")
    public UserData wrong2(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "abcd") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setPassword(DigestUtils.md5Hex("salt" + password));
        return userRepository.save(userData);
    }
}
