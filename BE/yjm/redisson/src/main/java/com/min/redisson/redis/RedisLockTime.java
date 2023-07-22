package com.min.redisson.redis;

import com.min.redisson.DistributedLock;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * waitTime - 락 임대 시간 (default - 3s) 락을 획득한 이후 leaseTime 이 지나면 락을 해제한다 <br/>
 * leaseTime - 락을 기다리는 시간 (default - 5s) 락 획득을 위해 waitTime 만큼 대기한다
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class RedisLockTime {
    public static final long DEFAULT_WAIT_TIME = 5L;
    public static final long DEFAULT_LEASE_TIME = 3L;

    @Builder.Default
    private final long waitTime = DEFAULT_WAIT_TIME;
    @Builder.Default
    private final long leaseTime = DEFAULT_LEASE_TIME;
    @Builder.Default
    private final TimeUnit timeUnit = TimeUnit.SECONDS;

    public static RedisLockTime createDefault() {
        return RedisLockTime.builder().build();
    }

    public static RedisLockTime from(final DistributedLock distributedLock) {
        return RedisLockTime.builder()
                .waitTime(distributedLock.waitTime())
                .leaseTime(distributedLock.leaseTime())
                .build();
    }
}