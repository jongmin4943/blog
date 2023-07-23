package com.min.redisson.lock;

public class DistributedLockException extends RuntimeException {
    public DistributedLockException(final String message) {
        super(message);
    }
    public DistributedLockException(final String message, final Throwable cause) {
        super(message, cause);
    }

}