package com.min.redisson.lock;

import com.min.redisson.redis.RedisLockTime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * Lock 의 key 값 설정
     */
    String keyPrefix();

    /**
     * 메서드의 파라미터값을 Key 에 추가로 붙일때 사용한다. Spring Expression Language (SpEL) 을 사용한다.
     * @see <a href="https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html">SpEl 문서</a>
     */
    String[] dynamicKey() default {};

    /**
     * 락 획득을 위해 waitTime 만큼 대기한다 (default - 5s)
     */
    long waitTime() default RedisLockTime.DEFAULT_WAIT_TIME;

    /**
     * 락을 획득한 이후 leaseTime 이 지나면 락을 해제한다 (default - 3s)
     */
    long leaseTime() default RedisLockTime.DEFAULT_LEASE_TIME;

}