package com.min.redisson.redis;

import com.min.redisson.lock.DistributedLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockServiceImpl implements RedisLockService {
    private static final String REDIS_LOCK_PREFIX = "REDISSON_LOCK:";
    private final RedissonClient redissonClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public <T> T callWithLock(final String lockKey, final Supplier<T> supplier, final RedisLockTime lockDto) throws DistributedLockException {
        List<String> keys = this.generateKeys(Collections.singletonList(lockKey));
        final RLock lock = redissonClient.getLock(keys.get(0));
        return this.execute(supplier, lockDto, keys, lock);
    }

    @Override
    @Transactional
    public void callWithLock(final String lockKey, final Runnable runnable, final RedisLockTime lockDto) throws DistributedLockException {
        List<String> keys = this.generateKeys(Collections.singletonList(lockKey));
        final RLock lock = redissonClient.getLock(keys.get(0));
        this.execute(runnable, lockDto, keys, lock);
    }

    @Override
    @Transactional
    public <T> T callWithMultiLock(final Collection<String> lockKeys, final Supplier<T> supplier, final RedisLockTime lockDto) {
        final List<String> keys = generateKeys(lockKeys);
        final RLock multiLock = redissonClient.getMultiLock(keys.stream().map(redissonClient::getLock).toArray(RLock[]::new));
        return this.execute(supplier, lockDto, keys, multiLock);
    }

    @Override
    @Transactional
    public void callWithMultiLock(final Collection<String> lockKeys, final Runnable runnable, final RedisLockTime lockDto) {
        final List<String> keys = generateKeys(lockKeys);
        final RLock multiLock = redissonClient.getMultiLock(keys.stream().map(redissonClient::getLock).toArray(RLock[]::new));
        this.execute(runnable, lockDto, keys, multiLock);
    }

    private <T> T execute(final Supplier<T> supplier, final RedisLockTime lockDto, final List<String> keys, final RLock lock) {
        try {
            log.debug("{} - lock 획득 시도", keys);
            if (lock.tryLock(lockDto.getWaitTime(), lockDto.getLeaseTime(), lockDto.getTimeUnit())) {
                log.debug("{} - lock 획득 성공", keys);
                return supplier.get();
            }
            throw new InterruptedException();
        } catch (InterruptedException e) {
            log.error("{} - lock 획득 실패", keys);
            throw new DistributedLockException("요청이 너무 많아 처리에 실패했습니다. 재시도 해주세요.", e);
        } finally {
            applicationEventPublisher.publishEvent(new RedisLockEvent(keys, lock));
        }
    }

    private void execute(final Runnable runnable, final RedisLockTime lockDto, final List<String> keys, final RLock lock) {
        try {
            log.debug("{} - lock 획득 시도", keys);
            if (lock.tryLock(lockDto.getWaitTime(), lockDto.getLeaseTime(), lockDto.getTimeUnit())) {
                log.debug("{} - lock 획득 성공", keys);
                runnable.run();
                return;
            }
            throw new InterruptedException();
        } catch (InterruptedException e) {
            log.error("{} - lock 획득 실패", keys);
            throw new DistributedLockException("요청이 너무 많아 처리에 실패했습니다. 재시도 해주세요.", e);
        } finally {
            applicationEventPublisher.publishEvent(new RedisLockEvent(keys, lock));
        }
    }

    private List<String> generateKeys(final Collection<String> lockKeys) {
        return lockKeys.stream().map(key -> REDIS_LOCK_PREFIX + key).toList();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void subscribeUnlock(final RedisLockEvent lockEvent) {
        try {
            lockEvent.unlock();
            log.debug("{} - lock 해제 성공", lockEvent.keys());
        } catch (IllegalMonitorStateException e) {
            log.warn("{} - 이미 해제된 lock 입니다.", lockEvent.keys);
        }
    }

    private record RedisLockEvent(List<String> keys, RLock lock) {
        public void unlock() {
            this.lock.unlock();
        }
    }
}
