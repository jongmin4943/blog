package com.min.redisson.lock;

public class DistributedLockKeyGenerateException extends RuntimeException {
    public DistributedLockKeyGenerateException() {
        super("key 생성에 실패했습니다.");
    }
}