package com.min.quiz;

import org.junit.jupiter.api.Test;

public class TryWithResourceTest {
    @Test
    void tryTest() throws Exception {
        AutoClose autoClose = new AutoClose();
        try (autoClose) {
            System.out.println("try");
        } catch (Exception e) {
            System.out.println("catch");
        } finally {
            System.out.println("finally");
            autoClose.close();
        }
    }

    static class AutoClose implements AutoCloseable {

        private int calledCount = 0;

        @Override
        public void close() throws Exception {
            calledCount++;
            System.out.println("AutoClose called count: " + calledCount);
        }
    }
}
