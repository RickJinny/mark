package com.rick.test.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.rick.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @ResponseBody
    @PostMapping(value = "/testRedis")
    public ServerResponse<String> testRedis() {
        // 和 Redis 服务创建连接，参数为 Redis 服务器，所在机器的 ip 和 Redis 的端口号
        Jedis jedis = new Jedis("192.168.0.121", 6379);
        // 清除一下
        jedis.flushAll();

        // 第一种类型: String
        stringType(jedis);
        // 第二种类型: Hash
        hashType(jedis);
        // 第三种类型: list
        listType(jedis);
        // 第四种类型: set
        setType(jedis);
        // 第五种类型：zset
        zSetType(jedis);

        // 关闭连接
        jedis.close();
        return ServerResponse.createBySuccess("success");
    }

    /**
     * 第五种类型：ZSet
     */
    private void zSetType(Jedis jedis) {
        System.out.println("------------   ZSet  ------------------");
        
        Map<String, Double> scoreMembers = Maps.newHashMap();
        scoreMembers.put("xiaowang01", 30D);
        scoreMembers.put("xiaowang02", 50D);
        scoreMembers.put("xiaowang03", 80D);
        scoreMembers.put("xiaowang04", 100D);
        scoreMembers.put("xiaowang05", 300D);
        // 第一、添加元素：zadd zsetName score1 value1 score2 value2 score3 value3 ......
        jedis.zadd("userZSet", scoreMembers);
        jedis.zadd("userZSet", 800, "xiaowang06");
        Set<String> userZSet = jedis.zrange("userZSet", 0, -1);
        log.info("userZSet: {}", JSON.toJSONString(userZSet));

        // 第二、查看所有元素：zrange zsetName 0 -1
        userZSet = jedis.zrange("userZSet", 0, -1);
        log.info("查看所有元素 userZSet: {}", JSON.toJSONString(userZSet));

        // 第三、查看所有元素，按 score 逆序排列：zrevrange zsetName 0 -1
        Set<String> userZSet1 = jedis.zrevrange("userZSet", 0, -1);
        log.info("userZSet 逆序: {}", JSON.toJSONString(userZSet1));

        // 第四、元素数量：zcard zsetName
        Long userZSetCount = jedis.zcard("userZSet");
        userZSet = jedis.zrange("userZSet", 0, -1);
        log.info("userZSet: {}", JSON.toJSONString(userZSet));
        log.info("userZSet userZSetCount: {}", userZSetCount);

        // 第五、获取指定 value 的分数：zscore zsetName value
        Double xiaowang02Score = jedis.zscore("userZSet", "xiaowang02");
        log.info("userZSet, xiaowang02Score: {}", xiaowang02Score);

        // 第六、获取指定 value 的排名：zrank zsetName value（从 0 开始）
        jedis.zrank("userZSet", "xiaowang03");
        jedis.zrank("userZSet", "xiaowang05");
        jedis.zrank("userZSet", "xiaowang01");
        jedis.zrank("userZSet", "xiaowang04");
        jedis.zrank("userZSet", "xiaowang02");
        userZSet = jedis.zrange("userZSet", 0, -1);
        log.info("userZSet: {}", JSON.toJSONString(userZSet));

        // 第七、获取指定分值区间中的元素： zrangebyscore zsetName scoreStart scoreEnd
        Set<String> userZSet2 = jedis.zrangeByScore("userZSet", 100, 800);
        log.info("userZSet 100 分到 800 分之间: {}", JSON.toJSONString(userZSet2));

        // 第八、获取指定分值区间中的元素，并且返回分数：zrangebyscore zsetName scoreStart scoreEnd withscores
        Set<Tuple> userZSet3 = jedis.zrangeByScoreWithScores("userZSet", 100, 700);
        log.info("userZSet3: {}", JSON.toJSONString(userZSet3));

        // 第九、删除元素：zrem zsetName value
        jedis.zrem("userZSet", "xiaowang03");
        userZSet = jedis.zrange("userZSet", 0, -1);
        log.info("userZSet: {}", JSON.toJSONString(userZSet));
        
        System.out.println("------------   ZSet  ------------------");
    }

    /**
     * 第四种类型：Set
     */
    private void setType(Jedis jedis) {
        System.out.println("------------   Set  ------------------");

        // 第一个、添加一个或多个指定的元素到集合的 key 中： sadd key member [member ...]
        jedis.sadd("userSet", "xiaowang01", "xiaowang02", "xiaowang03");
        Set<String> userSet = jedis.smembers("userSet");
        log.info("sadd, userSet: {}", JSON.toJSONString(userSet));

        // 第二个、在 key 集合中，移除指定的元素：srem key member [member ...]
        jedis.srem("userSet", "xiaowang01");
        userSet = jedis.smembers("userSet");
        log.info("srem xiaowang01, userSet: {}", JSON.toJSONString(userSet));

        // 第三个、返回集合存储的 key 的基数（集合元素的数量）：scard key
        Long UserCount = jedis.scard("userSet");
        userSet = jedis.smembers("userSet");
        log.info("userSet: {}", JSON.toJSONString(userSet));
        log.info("userSet, count: {}", UserCount);

        // 第四个、返回成员 member 是否是存储的集合 key 的成员：sismember key member
        Boolean xiaowang02Boolean = jedis.sismember("userSet", "xiaowang02");
        Boolean xiaowang01Boolean = jedis.sismember("userSet", "xiaowang01");
        userSet = jedis.smembers("userSet");
        log.info("userSet: {}, xiaowang02 is exist : {} ", JSON.toJSONString(userSet), xiaowang02Boolean);
        log.info("userSet: {}, xiaowang01 is exist : {} ", JSON.toJSONString(userSet), xiaowang01Boolean);

        // 第五个、仅提供 key 参数，那么随机返回 key 集合中的一个元素：srandmember key [count]
        userSet = jedis.smembers("userSet");
        log.info("userSet srandmember before : {} ", JSON.toJSONString(userSet));
        jedis.srandmember("userSet", 2);
        userSet = jedis.smembers("userSet");
        log.info("userSet srandmember after : {} ", JSON.toJSONString(userSet));

        // 第六个、返回 key 集合所有的元素：smembers keys
        userSet = jedis.smembers("userSet");
        log.info("smembers userSet : {} ", JSON.toJSONString(userSet));

        // 第七个、从存储在 key 的集合中移除并返回一个或多个随机元素：spop key [count]
        jedis.spop("userSet", 2);
        userSet = jedis.smembers("userSet");
        log.info("smembers userSet : {} ", JSON.toJSONString(userSet));

        System.out.println("------------   Set  ------------------");
    }

    /**
     * 第三种类型：list
     */
    private void listType(Jedis jedis) {
        System.out.println("------------List------------------");

        // 第一个、从头部加入元素: lpush listName value01
        jedis.lpush("userList", "xiaowang01");
        // 第二个、从尾部加入元素: rpush listName value01
        jedis.rpush("userList", "zhangsan");
        // 第三个、获取所有元素: lrange listName 0 -1
        List<String> userList = jedis.lrange("userList", 0, -1);
        log.info("获取所有元素 userList: {}", JSON.toJSONString(userList));
        // 第四个、删除元素: lrem key_name count value
        jedis.lrem("userList", 0, "zhangsan");
        log.info("获取所有元素 userList: {}", JSON.toJSONString(jedis.lrange("userList", 0, -1)));
        // 第五个、清空列表: ltrim listName 1 0
        jedis.ltrim("userList", 1, 0);
        log.info("获取所有元素 userList: {}", JSON.toJSONString(jedis.lrange("userList", 0, -1)));
        // 第六个、从 list 头部删除元素，并返回该元素: lpop listName
        jedis.lpush("userList", "wang01", "wang02", "wang03");
        jedis.lpop("userList");
        log.info("获取所有元素 userList: {}", JSON.toJSONString(jedis.lrange("userList", 0, -1)));
        // 第七个、从 list 尾部删除元素，并返回该元素: rpop listName
        jedis.rpop("userList");
        log.info("获取所有元素 userList: {}", JSON.toJSONString(jedis.lrange("userList", 0, -1)));
        Long count = jedis.llen("userList");
        log.info("userList 元素的长度: {}", count);
        // 第八个、返回指定下标的元素: lindex listName <index> // 会遍历整个列表，效率不高。
        log.info("获取所有元素 userList: {}", JSON.toJSONString(jedis.lrange("userList", 0, -1)));
        String user1 = jedis.lindex("userList", 1);
        log.info("获取 userList 的第 1 位元素: {}", user1);

        // 第九个、lpush 和 rpop，组成一个队列结构
        jedis.lpush("queue", "queue0001", "queue0002", "queue0003");
        log.info("获取所有元素 queue: {}", JSON.toJSONString(jedis.lrange("queue", 0, -1)));
        jedis.rpop("queue");
        log.info("获取所有元素 queue: {}", JSON.toJSONString(jedis.lrange("queue", 0, -1)));
        jedis.rpop("queue");
        log.info("获取所有元素 queue: {}", JSON.toJSONString(jedis.lrange("queue", 0, -1)));
        jedis.rpop("queue");
        log.info("获取所有元素 queue: {}", JSON.toJSONString(jedis.lrange("queue", 0, -1)));
        jedis.rpop("queue");
        log.info("获取所有元素 queue: {}", JSON.toJSONString(jedis.lrange("queue", 0, -1)));

        // 第十个、lpush 和 lpop，组成一个栈结构
        jedis.lpush("stack", "stack001", "stack002", "stack003");
        log.info("获取所有元素 stack: {}", JSON.toJSONString(jedis.lrange("stack", 0, -1)));
        jedis.lpop("stack");
        log.info("获取所有元素 stack: {}", JSON.toJSONString(jedis.lrange("stack", 0, -1)));
        jedis.lpop("stack");
        log.info("获取所有元素 stack: {}", JSON.toJSONString(jedis.lrange("stack", 0, -1)));
        jedis.lpop("stack");
        log.info("获取所有元素 stack: {}", JSON.toJSONString(jedis.lrange("stack", 0, -1)));
        jedis.lpop("stack");
        log.info("获取所有元素 stack: {}", JSON.toJSONString(jedis.lrange("stack", 0, -1)));

        System.out.println("------------List------------------");

    }

    /**
     * 第二种类型：Hash
     */
    private void hashType(Jedis jedis) {
        System.out.println("------------Hash------------------");

        jedis.hset("user01", "name", "wangxiaoer");
        log.info("hashType user01: {}", JSON.toJSONString(jedis.hgetAll("user01")));

        jedis.hset("user", "name", "zhangsan");
        jedis.hset("user", "age", "28");
        jedis.hset("user", "name", "lisi");
        jedis.hset("user", "age", "36");

        Map<String, String> map = jedis.hgetAll("user");
        log.info("hashType user: {}", JSON.toJSONString(map));

        String names = jedis.hget("user", "name");
        log.info("hashType user name: {}", names);

        String ages = jedis.hget("user", "age");
        log.info("hashType user age: {}", ages);
        
        System.out.println("------------Hash------------------");
    }

    /**
     * 第一种类型: String
     */
    private void stringType(Jedis jedis) {
        System.out.println("------------String------------------");

        jedis.set("key01", "Hello World!");
        String key01Value = jedis.get("key01");
        log.info("string set key01: {}", key01Value);

        jedis.incr("counter01");
        String counter01Value = jedis.get("counter01");
        log.info("string incr counter01: {}", counter01Value);

        jedis.incrBy("counter01", 200);
        String counter01Value2 = jedis.get("counter01");
        log.info("string incrBy counter01: {}", counter01Value2);

        jedis.decr("counter01");
        String counter01Value3 = jedis.get("counter01");
        log.info("string decr counter01: {}", counter01Value3);

        jedis.decrBy("counter01", 100);
        String counter01Value4 = jedis.get("counter01");
        log.info("string decrBy counter01: {}", counter01Value4);

        System.out.println("------------String------------------");

    }

    @ResponseBody
    @PostMapping(value = "/testRedisPool")
    public ServerResponse<String> testRedisPool() {
        // 创建连接池，连接池为单例，参数为 Redis 服务器所在机器的 ip 和端口号
        JedisPool jedisPool = new JedisPool("192.168.0.121", 6379);
        // 从连接池中获取一个连接
        Jedis jedis = jedisPool.getResource();
        jedis.set("testRedisPool_time", String.valueOf(System.currentTimeMillis()));
        String result = jedis.get("testRedis_time");
        log.info("result: {}", result);
        // 关闭连接
        jedis.close();
        return ServerResponse.createBySuccess(result);
    }

    @ResponseBody
    @PostMapping(value = "/testRedisCluster")
    public ServerResponse<String> testRedisCluster() {
        // 设置集群中的节点
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.0.121", 6379));

        // 创建集群操作对象
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("testRedisPool_time", String.valueOf(System.currentTimeMillis()));
        String result = jedisCluster.get("testRedisPool_time");
        log.info("result: {}", result);
        jedisCluster.close();
        return ServerResponse.createBySuccess(result);
    }

    @ResponseBody
    @PostMapping(value = "/testRedisTemplate")
    public ServerResponse<String> testRedisTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("111", "hello");
        map.put("222", 000);

        redisTemplate.opsForHash().putAll("hash", map);
        Map<Object, Object> hash = redisTemplate.opsForHash().entries("hash");
        return ServerResponse.createBySuccess(JSON.toJSONString(hash));
    }

}
