package com.rickjinny.mark.utils;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis 分布式锁
 */
public class RedisLock {

    // 锁键
    private String lock_key = "redis_lock";

    // 锁过期时间
    private long internalLockLeaseTime = 30000;

    // 获取锁的超时时间
    private long timeout = 999999;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 加锁
     */
    public boolean lock(String id) {
        Jedis jedis = jedisPool.getResource();
        long startTime = System.currentTimeMillis();
        try {
            for (; ; ) {
                // set 命令返回 OK, 则证明获取锁成功
                String lock = jedis.set(lock_key, id);
                if ("OK".equals(lock)) {
                    return true;
                }
                // 否则循序等待，在 timeout 时间仍未获取到锁，则获取失败
                long waitTime = System.currentTimeMillis() - startTime;
                if (waitTime >= timeout) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("分布式锁异常");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();

        }
        return false;
    }
}
