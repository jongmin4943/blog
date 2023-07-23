package com.min.redisson.test;

import com.min.redisson.lock.DistributedLock;
import com.min.redisson.redis.RedisLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestDecreaseService {
    private final TestEntityRepository testEntityRepository;
    private final RedisLockService redisLockService;
    @Transactional
    public void decrease(final Long testId) {
        final TestEntity entity = this.getEntity(testId);
        entity.decrease();
    }

    @DistributedLock(keyPrefix = "test", dynamicKey = "#testId")
    public void decreaseWithLockAnnotation(final Long testId) {
        final TestEntity entity = this.getEntity(testId);
        entity.decrease();
    }

    public void decreaseWithLockTemplate(final Long testId) {
        redisLockService.callWithLock("test:"+testId , ()->{
            final TestEntity entity = this.getEntity(testId);
            entity.decrease();
        });
    }

    public TestEntity getEntity(final Long testId) {
        return testEntityRepository.findById(testId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
