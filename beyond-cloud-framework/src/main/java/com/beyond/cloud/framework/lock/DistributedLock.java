package com.beyond.cloud.framework.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    /**
     * 获取锁
     *
     * @param key     锁 key
     * @param requestId 请求 id
     * @param expires 失效时间 ms
     * @return 获取锁是否成功
     */
    boolean lock(String key, String requestId, int expires);

    /**
     * 释放锁
     *
     * @param key 锁 key
     * @param requestId 请求 id
     * @return 成功释放 true
     */
    boolean releaseLock(String key, String requestId);

}
