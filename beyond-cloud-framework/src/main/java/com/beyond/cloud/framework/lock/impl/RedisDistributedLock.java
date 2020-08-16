package com.beyond.cloud.framework.lock.impl;

import com.beyond.cloud.framework.lock.DistributedLock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDistributedLock implements DistributedLock {

    private final StringRedisTemplate redisTemplate;


    private static final String LUA_LOCK_SCRIPT = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then  return redis.call('expire',KEYS[1],ARGV[2])  else return 0 end";
    private static final String LUA_UNLOCK_SCRIPT = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    public RedisDistributedLock(final StringRedisTemplate redisTemplate) {this.redisTemplate = redisTemplate;}

    @Override
    public boolean lock(final String key, final String requestId, final int expires) {
        final DefaultRedisScript<Long> lockScript = new DefaultRedisScript<>(LUA_LOCK_SCRIPT, Long.class);
        Long result = redisTemplate.execute(lockScript, Collections.singletonList(key), requestId, String.valueOf(expires));
        return Optional.ofNullable(result).orElse(-1L) == 1;
    }

    @Override
    public boolean releaseLock(final String key, final String requestId) {
        DefaultRedisScript<Long> longDefaultRedisScript = new DefaultRedisScript<>(LUA_UNLOCK_SCRIPT, Long.class);
        Long result = redisTemplate.execute(longDefaultRedisScript, Collections.singletonList(key), requestId);
        return Optional.ofNullable(result).orElse(-1L) == 1;
    }

}
