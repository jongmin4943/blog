package com.min.redisson;

import com.min.redisson.lock.DistributedLock;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @DistributedLock(keyPrefix = "test")
    public void test() {
        System.out.println("test");
    }

    @DistributedLock(keyPrefix = "testException")
    public void testWithNullPointerException(final String test) {
        System.out.println("testException: " + test);
        throw new NullPointerException();
    }

    @DistributedLock(keyPrefix = "testDynamicKey", dynamicKey = "#test")
    public void testDynamicKey(final String test) {
        System.out.println("testDynamicKey: " + test);
    }

    @DistributedLock(keyPrefix = "test", dynamicKey = "#test.toUpperCase()")
    public void testDynamicKeyMethod(final String test) {
        System.out.println("testDynamicKeyMethod: " + test);
    }

    @DistributedLock(keyPrefix = "test", dynamicKey = "#person.getFullName()")
    public void testDynamicKeyMethod(final Person person) {
        System.out.println("testDynamicKeyMethod: " + person.getFullName());
    }

    @DistributedLock(keyPrefix = "test", dynamicKey = {"#person.getFullName()", "#test"})
    public void testDynamicKeyMethod(final Person person, final String test) {
        System.out.println("testDynamicKeyMethod: " + person.getFullName() + test);
    }
}