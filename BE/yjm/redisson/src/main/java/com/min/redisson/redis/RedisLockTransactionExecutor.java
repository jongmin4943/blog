package com.min.redisson.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
@Transactional
@Slf4j
public class RedisLockTransactionExecutor {

    public <T> T execute(final Supplier<T> supplier) {
        return supplier.get();
    }

    public void execute(final Runnable runnable) {
        runnable.run();
    }

}
