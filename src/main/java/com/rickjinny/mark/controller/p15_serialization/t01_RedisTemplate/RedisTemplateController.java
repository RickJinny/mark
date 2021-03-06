package com.rickjinny.mark.controller.p15_serialization.t01_RedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 1、序列化和反序列化需要确保算法一致
 */
@RestController
@RequestMapping(value = "/redisTemplate")
@Slf4j
public class RedisTemplateController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;

    @Autowired
    private RedisTemplate<String, Long> countRedisTemplate;

    /**
     * 在应用初始化完成后，向 Redis 设置两组数据：
     * 第一次使用 RedisTemplate 设置 Key 为 redisTemplate、Value 为 User 对象；
     * 第二次使用 StringRedisTemplate 设置 Key 为 stringRedisTemplate、Value 为 JSON 序列化后的 User 对象。
     */
    @PostConstruct
    public void init() throws JsonProcessingException {
        redisTemplate.opsForValue().set("redisTemplate", new User("haha", 36));
        stringRedisTemplate.opsForValue().set("stringRedisTemplate",
                objectMapper.writeValueAsString(new User("haha", 36)));
    }

    /**
     * 通过 RedisTemplate 读取 Key 为 stringRedisTemplate 的 value。
     * 通过 StringRedisTemplate 读取 Key 为 redisTemplate 的 value。
     *
     * 结果是两次都无法读取到 Value。
     */
    @RequestMapping(value = "/wrong")
    public void wrong() {
        log.info("redisTemplate get {}", redisTemplate.opsForValue().get("stringRedisTemplate"));
        log.info("stringRedisTemplate get {}", stringRedisTemplate.opsForValue().get("redisTemplate"));
    }

    /**
     * 从上面我们可以看出，RedisTemplate 和 StringRedisTemplate 保存的数据，无法通用。
     * 修复方式：让它们自己读取自己存的数据。
     *
     * 使用 RedisTemplate 读出的数据，由于是 Object 类型的，使用时可以先强制转换为 User 类型。
     * 使用 StringRedisTemplate 读取出的字符串，需要手动将 JSON 反序列化为 User 类型。
     */
    @RequestMapping(value = "/right")
    public void right() throws JsonProcessingException {
        User userFromRedisTemplate = (User) this.redisTemplate.opsForValue().get("redisTemplate");
        log.info("redisTemplate get {}", userFromRedisTemplate);
        User userFromStringRedisTemplate = objectMapper.readValue(stringRedisTemplate.opsForValue().get("stringRedisTemplate"),
                User.class);
        log.info("stringRedisTemplate get {}", userFromStringRedisTemplate);
    }

    /**
     * 直接注入类型为 RedisTemplate<String, User> 的 userRedisTemplate 字段。
     * 使用 userRedisTemplate 存入一个 User 对象，再分别使用 userRedisTemplate 和  StringRedisTemplate 取出这个对象。
     */
    @RequestMapping(value = "/right2")
    public void right2() {
        User user = new User("haha", 36);
        userRedisTemplate.opsForValue().set(user.getName(), user);
        User userFromRedis = userRedisTemplate.opsForValue().get(user.getName());
        log.info("userRedisTemplate get {} {}", userFromRedis, userFromRedis.getClass());
        log.info("stringRedisTemplate get {}", stringRedisTemplate.opsForValue().get(user.getName()));
    }

    @RequestMapping(value = "/wrong2")
    public void wrong2() {
        String key = "testCounter";
        countRedisTemplate.opsForValue().set(key, 1L);
        log.info("{} {}", countRedisTemplate.opsForValue().get(key),
                countRedisTemplate.opsForValue().get(key) instanceof Long);
        Long l1 = getLongFromRedis(key);
        countRedisTemplate.opsForValue().set(key, Integer.MAX_VALUE + 1L);
        log.info("{} {}", countRedisTemplate.opsForValue().get(key),
                countRedisTemplate.opsForValue().get(key) instanceof Long);
        Long l2 = getLongFromRedis(key);
        log.info("{} {}", l1, l2);
    }

    private Long getLongFromRedis(String key) {
        Object o = countRedisTemplate.opsForValue().get(key);
        if (o instanceof Integer) {
            return ((Integer) o).longValue();
        }
        if (o instanceof Long) {
            return (Long) o;
        }
        return null;
    }
}
