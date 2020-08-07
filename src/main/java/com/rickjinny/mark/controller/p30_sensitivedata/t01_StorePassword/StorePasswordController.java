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
     * 不能在代码中写死盐，且盐需要有一定的长度。
     * DigestUtils.md5Hex("salt" + password)
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

    /**
     * 最好每一个密码都有独立的盐，并且盐要长一点，比如超过 20 位。
     */
    @RequestMapping(value = "/wrong3")
    public UserData wrong3(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setPassword(DigestUtils.md5Hex(name + password));
        return userRepository.save(userData);
    }

    /**
     * 两次 MD5 比较安全，其实并不是这样
     */
    @RequestMapping(value = "/wrong4")
    public UserData wrong4(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(password)));
        return userRepository.save(userData);
    }

    /**
     * 正确的做法：使用全球唯一的、和用户无关的、足够长的随机值作为盐值。比如：可以使用 UUID 作为盐值，把盐一起保存到数据库中。
     * 每次用户修改密码，都需要重新计算盐，重新保存新的密码。
     */
    public UserData right(@RequestParam(value = "name", defaultValue = "haha") String name,
                          @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        userData.setSalt(UUID.randomUUID().toString());
        userData.setPassword(DigestUtils.md5Hex(userData.getSalt() + password));
        return userRepository.save(userData);
    }

    /**
     * 使用 BCryptPasswordEncoder 来进行密码哈希。
     * BCrypt 是为保存密码设计的算法，相比 MD5 要慢很多。
     */
    @RequestMapping(value = "/better")
    public UserData better(@RequestParam(value = "name", defaultValue = "haha") String name,
                           @RequestParam(value = "password", defaultValue = "123456") String password) {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(name);
        // 保存哈希后的密码
        userData.setPassword(passwordEncoder.encode(password));
        userRepository.save(userData);
        // 判断密码是否匹配
        log.info("match ? {}", passwordEncoder.matches(password, userData.getPassword()));
        return userData;
    }

    /**
     * 测试一下 MD5, 以及使用不同代价因子的 BCrypt, 看看哈希一次密码的耗时。
     * 结果：MD5 只需要 0.8ms;
     *      BCrypt（代价因子为10）耗时 82ms;
     *      BCrypt（代价因子为12）耗时 312ms;
     *      BCrypt（代价因子为14）耗时 1200ms;
     */
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
