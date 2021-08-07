package com.rick.test.controller;

import com.alibaba.fastjson.JSON;
import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/redisTemplate")
public class Redis_RedisTemplate_Controller {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ResponseBody
    @PostMapping(value = "/test")
    public ServerResponse<String> testRedisTemplate() {
        // String 类型相关操作
        stringType();
        // Hash 类型相关操作
        hashType();
        // Set 类型相关操作
        setType();

        return ServerResponse.createBySuccess("success");
    }

    /**
     * Set 类型相关操作
     */
    private void setType() {
        System.out.println("-------------- RedisTemplate Set Type ------------------");
        // 1、添加 Set 缓存
        // 方式一: 通过 SetOperations 方式
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add("set_01", "111", "222", "333", "444", "555");
        Set<String> setKey01Value = setOperations.members("set_01");
        log.info("setType set_01 value: {} ", JSON.toJSONString(setKey01Value));
        // 方式二: 通过 BoundSetOperations 方式
        BoundSetOperations<String, String> boundSetOperations = redisTemplate.boundSetOps("set_02");
        boundSetOperations.add("set_02", "aaa", "bbb", "ccc", "ddd", "eee");
        Set<String> setKey02Value = boundSetOperations.members();
        log.info("setType set_02 value: {} ", JSON.toJSONString(setKey02Value));
        // 方式三: 通过 redisTemplate.boundSetOps
        redisTemplate.boundSetOps("set_03").add("1a1a", "2b2b", "3c3c", "4d4d", "5e5e");
        Set<String> setKey03Value = redisTemplate.boundSetOps("set_03").members();
        log.info("setType set_03 value: {} ", JSON.toJSONString(setKey03Value));

        // 2、根据 key 获取 Set 中的所有值
        // 通过 redisTemplate.boundSetOps 获取值
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        Set<String> value01 = setOps.members("set_01");
        log.info("setType set_01 value01: {} ", JSON.toJSONString(value01));
        // 通过 BoundSetOperations 获取值
        BoundSetOperations<String, String> boundSetOps = redisTemplate.boundSetOps("set_02");
        Set<String> value02 = boundSetOps.members();
        log.info("setType set_02 value02: {} ", JSON.toJSONString(value02));
        // 通过 redisTemplate.boundSetOps 获取值
        Set<String> value03 = redisTemplate.boundSetOps("set_03").members();
        log.info("setType set_03 value03: {} ", JSON.toJSONString(value03));

        // 3、根据 value 从一个 set 中查询，是否存在
        redisTemplate.opsForSet().isMember("set_01", "333");
        redisTemplate.boundSetOps("set_01").isMember("333");

        // 4、获取 Set 的长度
        Long set01Size = redisTemplate.boundSetOps("set_01").size();
        log.info("setType set_01 value size: {} ", set01Size);

        // 5、设置过期时间
        redisTemplate.expire("set_01", 1, TimeUnit.MINUTES);
        redisTemplate.boundSetOps("set_01").expire(10, TimeUnit.MINUTES);

        // 6、移除指定的元素
        redisTemplate.boundSetOps("set_02").remove("aaa");

        // 7、移除指定的 key
        redisTemplate.delete("set_03");

        System.out.println("-------------- RedisTemplate Set Type ------------------");
    }

    /**
     * Hash 类型相关操作
     */
    private void hashType() {
        System.out.println("-------------- RedisTemplate Hash Type ------------------");
        // 1、添加缓存
        // 方式一: 通过 redisTemplate.opsForHash() 添加 Hash 缓存
        redisTemplate.opsForHash().put("user01", "name", "zhangsan01");
        redisTemplate.opsForHash().put("user01", "age", "20");
        Set<Object> user01HashKeySet = redisTemplate.opsForHash().keys("user01");
        List<Object> user01HashValueList = redisTemplate.opsForHash().values("user01");
        Map<Object, Object> user01 = redisTemplate.opsForHash().entries("user01");
        log.info("redisTemplate.opsForHash, user01HashKeySet: {}, user01HashValueList:{}, user01:{}",
                JSON.toJSONString(user01HashKeySet), JSON.toJSONString(user01HashValueList), JSON.toJSONString(user01));
        // 方式二: 通过  HashOperations 添加 hash 类型缓存
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("user02", "name", "zhangsan02");
        hashOperations.put("user02", "age", "21");
        Map<Object, Object> user02 = hashOperations.entries("user02");
        String user02Name = (String) hashOperations.get("user02", "name");
        String user02Age = (String) hashOperations.get("user02", "age");
        log.info("RedisTemplate Hash, user02: {}, user02Name: {}, user02Age: {} .", JSON.toJSONString(user02), user02Name, user02Age);
        // 方式三: 通过 redisTemplate.boundHashOps() 添加 Hash 缓存
        redisTemplate.boundHashOps("user03").put("name", "zhangsan03");
        redisTemplate.boundHashOps("user03").put("age", "22");
        Map<Object, Object> user03 = redisTemplate.boundHashOps("user03").entries();
        Set<Object> user03HashKeySet = redisTemplate.boundHashOps("user03").keys();
        List<Object> user03HashValueList = redisTemplate.boundHashOps("user03").values();
        String user03Name = (String) redisTemplate.boundHashOps("user03").get("name");
        String user03Age = (String) redisTemplate.boundHashOps("user03").get("age");
        log.info("RedisTemplate Hash, user03: {}, user03HashKeySet: {}, user03HashValueList: {}, user03Name: {}, user03Age: {}",
                JSON.toJSONString(user03), JSON.toJSONString(user03HashKeySet), JSON.toJSONString(user03HashValueList), user03Name,
                user03Age);
        // 方式四: 通过  BoundHashOperations 添加 Hash 缓存
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps("user04");
        boundHashOperations.put("name", "zhangsan04");
        boundHashOperations.put("age", "23");
        Map<Object, Object> user04 = boundHashOperations.entries();
        Set<Object> user04HashKeySet = boundHashOperations.keys();
        List<Object> user04HashValueList = boundHashOperations.values();
        String user04Name = (String) boundHashOperations.get("name");
        String user04Age = (String) boundHashOperations.get("age");
        log.info("RedisTemplate Hash, user04: {}, user04HashKeySet: {}, user04HashValueList: {}, user04Name: {}, user04Age: {}",
                JSON.toJSONString(user04), JSON.toJSONString(user04HashKeySet), JSON.toJSONString(user04HashValueList), user04Name,
                user04Age);

        // 2、设置过期时间
        Boolean user041 = redisTemplate.boundValueOps("user03").expire(1, TimeUnit.MINUTES);
        Boolean user042 = redisTemplate.expire("user04", 10, TimeUnit.MINUTES);

        // 3、添加一个 Map 集合
        Map<String, String> map = new HashMap<>();
        map.put("name", "zhangsan05");
        map.put("age", "26");
        BoundHashOperations<String, Object, Object> boundHashOperations1 = redisTemplate.boundHashOps("user05");
        boundHashOperations1.putAll(map);
        Map<Object, Object> user05 = boundHashOperations1.entries();
        log.info("RedisTemplate Hash, user05: {} ", JSON.toJSONString(user05));

        // 4、提取所有的 key 和 value
        Set<Object> user04Keys = redisTemplate.opsForHash().keys("user04");
        List<Object> user04Values = redisTemplate.opsForHash().values("user04");
        Set<Object> user05Keys = redisTemplate.boundHashOps("user05").keys();
        List<Object> user05Values = redisTemplate.boundHashOps("user05").values();
        log.info("RedisTemplate Hash, user04Keys: {}, user04Values: {} ", JSON.toJSONString(user04Keys), JSON.toJSONString(user04Values));
        log.info("RedisTemplate Hash, user05Keys: {}, user05Values: {} ", JSON.toJSONString(user05Keys), JSON.toJSONString(user05Values));

        // 5、根据 key 提取 value
        String user04Name_ = (String) redisTemplate.opsForHash().get("user04", "name");
        String user04Age_ = (String) redisTemplate.opsForHash().get("user04", "age");
        String user05Name_ = (String) redisTemplate.boundHashOps("user05").get("name");
        String user05Age_ = (String) redisTemplate.boundHashOps("user05").get("age");
        log.info("RedisTemplate Hash, user04Name: {}, user04Age: {} .", user04Name_, user04Age_);
        log.info("RedisTemplate Hash, user05Name: {}, user05Age: {} .", user05Name_, user05Age_);

        // 6、获取所有的键值对集合
        Map<Object, Object> user04Map = redisTemplate.opsForHash().entries("user04");
        Map<Object, Object> user05Map = redisTemplate.boundHashOps("user05").entries();
        log.info("RedisTemplate Hash, user04Map: {} .", JSON.toJSONString(user04Map));
        log.info("RedisTemplate Hash, user05Map: {} .", JSON.toJSONString(user05Map));

        // 7、删除
        // 删除小key
        redisTemplate.boundHashOps("user04").delete("name");
        user04Map = redisTemplate.opsForHash().entries("user04");
        log.info("RedisTemplate Hash, delete name, user04Map: {} .", JSON.toJSONString(user04Map));
        // 删除大key
        redisTemplate.delete("user05");
        user05Map = redisTemplate.boundHashOps("user05").entries();
        log.info("RedisTemplate Hash, user05Map: {} .", JSON.toJSONString(user05Map));

        // 8、判断 Hash 中是否有该值
        Boolean hasKey = redisTemplate.boundHashOps("user02").hasKey("name");
        log.info("user02 has name key: {}", hasKey);

        System.out.println("-------------- RedisTemplate Hash Type ------------------");
    }

    /**
     * String类型相关操作
     */
    private void stringType() {
        System.out.println("-------------- RedisTemplate String Type ------------------");
        // 通过 redisTemplate.opsForValue(), 即 ValueOperations 设置
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("opsForValue_key01", "xiaowang01");
        String key01 = operations.get("opsForValue_key01");
        log.info("opsForValue_key01: {}", key01);
        // 通过 redisTemplate.boundValueOps 设置
        redisTemplate.boundValueOps("boundValueOps_key02").set("xiaowang02");
        String key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);
        redisTemplate.boundValueOps("boundValueOps_key03").set("xiaowang03", 1, TimeUnit.MINUTES);
        String key03 = redisTemplate.boundValueOps("boundValueOps_key03").get();
        log.info("boundValueOps_key03: {}", key03);

        // 设置过期时间
        redisTemplate.boundValueOps("boundValueOps_key02").expire(10, TimeUnit.MINUTES);
        redisTemplate.expire("boundValueOps_key02", 12, TimeUnit.MINUTES);
        key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);

        // 删除 key
        redisTemplate.delete("boundValueOps_key02");
        key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);

        // 顺序递增
        redisTemplate.boundValueOps("boundValueOps_key02").increment(10L);
        key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);

        // 顺序递减
        redisTemplate.boundValueOps("boundValueOps_key02").increment(-10L);
        key02 = redisTemplate.boundValueOps("boundValueOps_key02").get();
        log.info("boundValueOps_key02: {}", key02);

        System.out.println("-------------- RedisTemplate String Type ------------------");
    }


    /**
     * 删除 key
     */
    private Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 指定 key 的失效时间
     */
    private Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.MINUTES);
    }

    /**
     * 根据 key 获取过期时间
     */
    private Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key 是否存在
     */
    private Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
