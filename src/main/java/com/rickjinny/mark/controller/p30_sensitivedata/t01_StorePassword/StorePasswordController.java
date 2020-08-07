package com.rickjinny.mark.controller.p30_sensitivedata.t01_StorePassword;

import jodd.crypt.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    /**
     * 不能在代码中写死盐，且盐需要有一定的长度
     */
    @RequestMapping("/wrong2")
    public UserData wrong2(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "abcd") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setPassword(DigestUtils.md5Hex("salt" + password));
        return userRepository.save(userData);
    }

    @RequestMapping(value = "/wrong3")
    public UserData wrong3(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setPassword(DigestUtils.md5Hex(name + password));
        return userRepository.save(userData);
    }

    @RequestMapping(value = "/wrong4")
    public UserData wrong4(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(password)));
        return userRepository.save(userData);
    }

    public UserData right(@RequestParam(value = "name", defaultValue = "haha") String name,
                          @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setSalt(UUID.randomUUID().toString());
        userData.setPassword(DigestUtils.md5Hex(userData.getSalt() + password));
        return userRepository.save(userData);
    }

    @RequestMapping(value = "/better")
    public UserData better(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setPassword(passwordEncoder.encode(password));
        userRepository.save(userData);
        log.info("match ? {}", passwordEncoder.matches(password, userData.getPassword()));
        return userData;
    }

    @RequestMapping(value = "/performance")
    public void performance() {
        StopWatch stopWatch = new StopWatch();
        String password = "abcd1234";
        stopWatch.start("MD5");
        DigestUtils.md5Hex(password);
        stopWatch.stop();

        stopWatch.start("BCrypt(10)");
        String hash = BCrypt.gensalt(10);
        BCrypt.hashpw(password, hash);
        System.out.println(hash);
        stopWatch.stop();

        stopWatch.start("BCrypt(12)");
        String hash2 = BCrypt.gensalt(12);
        BCrypt.hashpw(password, hash2);
        System.out.println(hash2);

        stopWatch.start("BCrypt(14)");
        String hash3 = BCrypt.gensalt(14);
        BCrypt.hashpw(password, hash3);
        System.out.println(hash3);
        stopWatch.stop();

        log.info("{}", stopWatch.prettyPrint());
    }
}
