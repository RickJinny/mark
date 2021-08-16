package com.rick.test.service.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService_01 {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String timeout = "500";

    private static final String prefix = "redis_lock_";

    public static final String liveTime = "100000";

    public static final String defaultError = "The Lock has been occupied.";

    public static final ConcurrentHashMap<Long, String> threadKeyMap = new ConcurrentHashMap<>();

    /**
     * 设置值
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时时间
     * @return
     */
    private boolean add(String key, String value, Long timeout) {
        Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                Boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
                if (success) {
                    connection.pExpire(serializer.serialize(key), timeout);
                }
                return success;
            }
        });
        return result;
    }

    /**
     * Redis 删除值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    private boolean del(String key, String value) {
        Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                String val = serializer.deserialize(connection.get(serializer.serialize(key)));
                if (!value.equals(val)) {
                    return false;
                }
                connection.del(serializer.serialize(key));
                return true;
            }
        });
        return result;
    }

    /**
     * 加锁，失败的话抛出异常
     */
    public void lock(String key) {
        key = getLockKey(key);
        long id = Thread.currentThread().getId();

        if (threadKeyMap.containsKey(id)) {
            throw new RuntimeException("Current thread has acquired the lock.");
        }

        try {
            String uuid = UUID.randomUUID().toString();
            threadKeyMap.put(id, uuid);
            boolean success = add(key, uuid, 100L);
            if (!success) {
                throw new RuntimeException("加锁失败");
            }
        } catch (Exception e) {
            threadKeyMap.remove(id);
            throw e;
        }
    }

    /**
     * 尝试加锁，返回加锁结果
     *
     * @param key
     * @return
     */
    public boolean tryLock(String key) {
        return tryLock(key, 0L);
    }

    /**
     * 尝试固定时间内加锁，返回加锁结果，单位毫秒
     *
     * @param key
     * @param time
     * @return
     */
    private boolean tryLock(String key, long time) {
        String lockKey = getLockKey(key);
        long id = Thread.currentThread().getId();
        String uuid = UUID.randomUUID().toString();
        long timeout = Long.parseLong(liveTime);
        // 判断是当前线程是否持有锁，如果当前线程已经在 threadKeyMap 中，说明当前线程已经持有锁。
        if (threadKeyMap.containsKey(id)) {
            return false;
        }

        try {
            // 将当前线程的 id 作为 key, 放入到 threadKeyMap 中
            threadKeyMap.put(id, uuid);
            // 给 lockKey 加锁，并设置超时时间为 timeout
            boolean success = add(lockKey, uuid, timeout);
            if (!success) {
                long max = timeout;
                long min = 50;
                if (time < min) {
                    if (time > 0) {
                        TimeUnit.MILLISECONDS.sleep(time);
                        success = add(lockKey, uuid, timeout);
                    }
                } else {
                    max = Math.min(time, max);
                    while (max > 0) {
                        TimeUnit.MILLISECONDS.sleep(min);
                        success = add(lockKey, uuid, timeout);
                        if (success) {
                            break;
                        }
                        max = max - min;
                        min = 2 * min;
                        min = Math.min(max, min);
                    }
                }
            }
            return success;
        } catch (Exception e) {
            threadKeyMap.remove(id);
            return false;
        }
    }

    /**
     * 释放锁
     * @param key 键
     */
    public void unLock(String key) {
        key = getLockKey(key);
        long id = Thread.currentThread().getId();
        if (!threadKeyMap.containsKey(id)) {
            return;
        }

        try {
            String uuid = threadKeyMap.get(id);
            del(key, uuid);
        } catch (Exception e) {
            threadKeyMap.remove(id);
        }
    }
    
    private String getLockKey(String key) {
        return "prefix-" + key;
    }
}
