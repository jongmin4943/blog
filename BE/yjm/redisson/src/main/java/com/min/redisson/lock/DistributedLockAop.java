package com.min.redisson.lock;

import com.min.redisson.redis.RedisLockService;
import com.min.redisson.redis.RedisLockTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private final RedisLockService redisLockService;

    @Pointcut("@annotation(com.min.redisson.lock.DistributedLock)")
    private void distributeLock() {
    }

    @Around("distributeLock()")
    public Object executeWithLock(final ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();
        final String[] parameterNames = methodSignature.getParameterNames();
        final Object[] args = joinPoint.getArgs();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        final List<String> keys =
                DistributedLockKeyGenerator.generateKeys(
                        distributedLock.keyPrefix(),
                        distributedLock.dynamicKey(),
                        parameterNames,
                        args
                );
        final RedisLockTime redisLockTime = RedisLockTime.from(distributedLock);

        try {
            return this.proceed(joinPoint, keys, redisLockTime);
        } catch (ExecuteContextException e) {
            throw e.getCause();
        }
    }

    private Object proceed(final ProceedingJoinPoint joinPoint, final List<String> keys, final RedisLockTime redisLockTime) {
        Supplier<Object> executeContext = this.createExecuteContext(joinPoint);
        if (keys.size() == 1) {
            return redisLockService.callWithLock(keys.get(0), executeContext, redisLockTime);
        }
        return redisLockService.callWithMultiLock(keys, executeContext, redisLockTime);
    }

    private Supplier<Object> createExecuteContext(final ProceedingJoinPoint joinPoint) throws ExecuteContextException {
        return () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new ExecuteContextException(e);
            }
        };
    }

    public static class ExecuteContextException extends RuntimeException {
        public ExecuteContextException(final Throwable cause) {
            super(cause);
        }
    }

}