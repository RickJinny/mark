package com.rickjinny.mark.utils.lock;

import com.rickjinny.mark.utils.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    @Autowired
    private RedisClient redisClient;

    private long sleepTime = 100;
    /**
     * 直接使用setnx + expire方式获取分布式锁
     * 非原子性
     */
    public boolean lockSetNx(String key, String value, int timeout) {
        Long result = redisClient.setnx(key, value);
        // result = 1时，设置成功，否则设置失败
        if (result == 1L) {
            return redisClient.expire(key, timeout) == 1L;
        } else {
            return false;
        }
    }

    /**
     * 使用Lua脚本，脚本中使用setnex+expire命令进行加锁操作
     */
    public boolean LockWithLua(String key, String UniqueId, int seconds) {
        String lua_scripts = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then" +
                "redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end";
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        keys.add(key);
        values.add(UniqueId);
        values.add(String.valueOf(seconds));
        Object result = RedisClient.getJedis().eval(lua_scripts, keys, values);
        //判断是否成功
        return result.equals(1L);
    }

    /**
     * 在Redis的2.6.12及以后中,使用 set key value [NX] [EX] 命令
     */
    public static boolean lock(String key, String value, int timeout, TimeUnit timeUnit) {
        long seconds = timeUnit.toSeconds(timeout);
        return "OK".equals(RedisClient.getJedis().set(key, value, "NX", "EX", seconds));
    }

    /**
     * 自定义获取锁的超时时间
     */
    public boolean lockWithWaitTime(String key, String value, int timeout,
                                    long waitTime, TimeUnit timeUnit) throws InterruptedException {
        long seconds = timeUnit.toSeconds(timeout);
        while (waitTime >= 0) {
            String result = RedisClient.getJedis().set(key, value, "nx", "ex", seconds);
            if ("OK".equals(result)) {
                return true;
            }
            waitTime -= sleepTime;
            Thread.sleep(sleepTime);
        }
        return false;
    }

    /**
     * 错误的解锁方法—直接删除key
     *
     * @param key
     */
    public void unlockWithDel(String key) {
        RedisClient.getJedis().del(key);
    }

    /**
     * 使用Lua脚本进行解锁操纵，解锁的时候验证value值
     */
    public static boolean unlock(String key, String value) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                "return redis.call('del',KEYS[1]) else return 0 end";
        return RedisClient.getJedis().eval(luaScript, Collections.singletonList(key), Collections.singletonList(value)).equals(1L);
    }
}
