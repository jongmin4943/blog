package com.min.redisson;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTestUtils {
    public static void execute(final int numberOfThreads, final Runnable task) throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        final CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    task.run();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }
}
