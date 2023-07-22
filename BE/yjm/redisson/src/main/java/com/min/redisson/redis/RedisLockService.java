package com.min.redisson.redis;

import com.min.redisson.DistributedLockException;

import java.util.Collection;
import java.util.function.Supplier;

public interface RedisLockService {
    <T> T callWithLock(String lockKey, Supplier<T> supplier, RedisLockTime lockDto) throws DistributedLockException;

    default <T> T callWithLock(String lockKey, Supplier<T> supplier) throws DistributedLockException {
        return this.callWithLock(lockKey, supplier, RedisLockTime.createDefault());
    }

    void callWithLock(String lockKey, Runnable runnable, RedisLockTime lockDto) throws DistributedLockException;

    default void callWithLock(String lockKey, Runnable runnable) throws DistributedLockException {
        this.callWithLock(lockKey, runnable, RedisLockTime.createDefault());
    }

    <T> T callWithMultiLock(Collection<String> lockKeys, Supplier<T> supplier, RedisLockTime lockDto) throws DistributedLockException;

    default <T> T callWithMultiLock(Collection<String> lockKeys, Supplier<T> supplier) throws DistributedLockException {
        return this.callWithMultiLock(lockKeys, supplier, RedisLockTime.createDefault());
    }

    void callWithMultiLock(Collection<String> lockKeys, Runnable runnable, RedisLockTime lockDto) throws DistributedLockException;

    default void callWithMultiLock(Collection<String> lockKeys, Runnable runnable) throws DistributedLockException {
        this.callWithMultiLock(lockKeys, runnable, RedisLockTime.createDefault());
    }
}