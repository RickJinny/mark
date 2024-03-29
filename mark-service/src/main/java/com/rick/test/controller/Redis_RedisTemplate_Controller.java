package com.rick.test.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
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
        // List 类型相关操作
        listType();
        // Set 类型相关操作
        setType();
        // ZSet 类型相关操作
        zSetType();

        return ServerResponse.createBySuccess("success");
    }

    /**
     * ZSet 类型相关操作
     */
    private void zSetType() {
        System.out.println("-------------- RedisTemplate ZSet Type ------------------");
        // 1、向集合中插入元素，并设置分数
        // 通过 redisTemplate.opsForZSet() 设置缓存
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("ZSet01", "a1", 100);
        zSetOperations.add("ZSet01", "a2", 300);
        zSetOperations.add("ZSet01", "a3", 350);
        zSetOperations.add("ZSet01", "a4", 651);
        Set<String> zSet01Set = redisTemplate.boundZSetOps("ZSet01").range(0, -1);
        log.info("zSetType, zSet01 : {} ", JSON.toJSONString(zSet01Set));
        // 通过 BoundZSetOperations 设置缓存
        BoundZSetOperations<String, String> boundZSetOperations = redisTemplate.boundZSetOps("ZSet02");
        boundZSetOperations.add("b1", 0.21D);
        boundZSetOperations.add("b2", -0.1D);
        boundZSetOperations.add("b3", 10.09D);
        boundZSetOperations.add("b4", 26.92D);
        Set<String> zSet02Set = redisTemplate.boundZSetOps("ZSet02").range(0, -1);
        log.info("zSetType, zSet02 : {} ", JSON.toJSONString(zSet02Set));
        // 通过 redisTemplate.boundZSetOps 设置缓存
        redisTemplate.boundZSetOps("ZSet03").add("c1", -10.23D);
        redisTemplate.boundZSetOps("ZSet03").add("c2", 13.13D);
        redisTemplate.boundZSetOps("ZSet03").add("c3", 30.98D);
        redisTemplate.boundZSetOps("ZSet03").add("c4", -40.78D);
        Set<String> zSet03Set = redisTemplate.boundZSetOps("ZSet03").range(0, -1);
        log.info("zSetType, zSet03 : {} ", JSON.toJSONString(zSet03Set));

        // 2、向集合中插入多个元素，并设置分数
        DefaultTypedTuple<String> defaultTypedTuple01 = new DefaultTypedTuple<>("d1", 3.9D);
        DefaultTypedTuple<String> defaultTypedTuple02 = new DefaultTypedTuple<>("d2", 2.1D);
        DefaultTypedTuple<String> defaultTypedTuple03 = new DefaultTypedTuple<>("d3", 1.4D);
        DefaultTypedTuple<String> defaultTypedTuple04 = new DefaultTypedTuple<>("d4", -2.9D);
        redisTemplate.boundZSetOps("ZSet04").add(new HashSet<>(Lists.newArrayList(defaultTypedTuple01,
                defaultTypedTuple02, defaultTypedTuple03, defaultTypedTuple04)));
        Set<String> zSet04Set = redisTemplate.boundZSetOps("ZSet04").range(0, -1);
        log.info("zSetType, zSet04 : {} ", JSON.toJSONString(zSet04Set));

        // 3、获取指定元素的分数
        Double score = redisTemplate.boundZSetOps("ZSet01").score("a2");
        log.info("zSetType, ZSet01 a2 score: {} ", score);

        // 4、按照排名先后顺序（从小到大），打印指定区间内的元素。(-1为打印全部)
        Set<String> zSet02 = redisTemplate.boundZSetOps("ZSet02").range(0, -1);
        log.info("zSetType, zSet02 : {} ", JSON.toJSONString(zSet02));

        // 5、返回集合内的成员个数
        Long size = redisTemplate.boundZSetOps("zSet01").size();

        // 6、返回集合内，指定分数范围的成员个数
        Long zSet01Count = redisTemplate.boundZSetOps("zSet01").count(0D, 500D);
        log.info("zSetType, zSet01 : {}, 分数在 0-500 之间的个数: {}", JSON.toJSONString(zSet01Set), zSet01Count);

        // 7、返回集合内，元素在指定分数范围内的排名（从小到大）
        Set<String> zSet01 = redisTemplate.boundZSetOps("zSet01").rangeByScore(0, 500D);
        log.info("zSetType, zSet01 : {}, 分数在 0-500 之间的个数: {}, 排名: {} ", JSON.toJSONString(zSet01Set),
                zSet01Count, JSON.toJSONString(zSet01));

        // 8、返回集合内元素的排名，以及分数(从小到大)
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.boundZSetOps("zSet01").rangeWithScores(0L, 1000L);
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            log.info("返回集合内元素的排名，以及分数(从小到大): " + tuple.getValue() + " : " + tuple.getScore());
        }

        System.out.println("-------------- RedisTemplate ZSet Type ------------------");
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
     * List 类型相关操作
     */
    private void listType() {
        System.out.println("-------------- RedisTemplate List Type ------------------");
        // 1、添加缓存
        // 通过 redisTemplate.opsForList() 设置缓存
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.leftPush("list01", "aaa");
        listOperations.rightPush("list01", "bbb");
        listOperations.rightPush("list01", "ccc");
        String list01Value01 = listOperations.leftPop("list01");
        log.info("listType, list01Value01: {} ", list01Value01);
        // 通过 BoundListOperations 设置缓存
        BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps("list02");
        boundListOperations.leftPush("111");
        boundListOperations.leftPush("222");
        boundListOperations.rightPush("333");
        boundListOperations.rightPush("444");
        String list02Value01 = boundListOperations.leftPop();
        log.info("listType, list02Value01: {} ", list02Value01);
        // 通过 redisTemplate.boundListOps 来设置缓存
        redisTemplate.boundListOps("list03").leftPush("1a1a");
        redisTemplate.boundListOps("list03").leftPush("2b2b");
        redisTemplate.boundListOps("list03").leftPush("3c3c");
        redisTemplate.boundListOps("list03").leftPush("4d4d");
        String list03Value01 = redisTemplate.boundListOps("list03").leftPop();
        log.info("listType, list03Value01: {} ", list03Value01);

        // 2、缓存
        redisTemplate.boundListOps("list04").rightPushAll("q1", "q2", "q3");
        redisTemplate.boundListOps("list04").leftPushAll("q1", "q2", "q3");

        // 3、获取 List 缓存的全部内容（起始索引 -> 结束索引）
        List<String> list04 = redisTemplate.boundListOps("list04").range(0, 20);
        log.info("listType, list04: {}", JSON.toJSONString(list04));

        // 4、从左或右，弹出一个元素
        String list04LeftValue = redisTemplate.boundListOps("list04").leftPop();
        String list04RightValue = redisTemplate.boundListOps("list04").rightPop();
        log.info("listType, list04LeftValue: {}, list04RightValue:{}.", list04LeftValue, list04RightValue);

        // 5、根据索引查询元素
        String list04_1_Value = redisTemplate.boundListOps("list04").index(1);
        log.info("listType, list04_1_Value: {} .", list04_1_Value);

        // 6、获取 List 缓存的长度
        Long size = redisTemplate.boundListOps("list04").size();
        log.info("listType, list04 size : {} ", size);

        // 7、根据索引修改 List 中的某条数据 (key, 索引, 值)
        redisTemplate.boundListOps("list04").set(2L, "修改第2个索引位置上值");
        list04 = redisTemplate.boundListOps("list04").range(0, 20);
        log.info("listType, list04: {}", JSON.toJSONString(list04));

        // 8、移除 N 个值为 value(移除个数, 值)
        redisTemplate.boundListOps("list04").remove(3L, "q1");
        list04 = redisTemplate.boundListOps("list04").range(0, 20);
        log.info("listType, list04: {}", JSON.toJSONString(list04));

        // 9、设置过期时间
        redisTemplate.expire("list04", 1, TimeUnit.MINUTES);
        redisTemplate.boundListOps("list04").expire(1, TimeUnit.MINUTES);
        
        System.out.println("-------------- RedisTemplate List Type ------------------");
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
    
}
