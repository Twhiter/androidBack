package com.springtest.demo.businessEntity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class PaySemaphorePool {


    private static volatile PaySemaphorePool pool = null;

    public static synchronized PaySemaphorePool getInstance() {
        if (pool == null)
            pool = new PaySemaphorePool();
        return pool;
    }


    private PaySemaphorePool() {
    }


    private final Cache<Integer, PaySemaphore> cache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(4, TimeUnit.MINUTES).build();


    public PaySemaphore get(int sessionId) {
        return cache.getIfPresent(sessionId);
    }


    public void add(PaySemaphore paySemaphore) {
        cache.put(paySemaphore.sessionId, paySemaphore);
    }


    public boolean exist(int sessionId) {
        return get(sessionId) != null;
    }
}
