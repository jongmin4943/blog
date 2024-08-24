package com.min.quiz;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class StreamTest {
    @Test
    void streamTest() {
        Stream.of(1, 2, 3)
                .map(n -> {
                    System.out.println("map");
                    return n;
                })
                .filter(n -> {
                    System.out.println("filter");
                    return true;
                })
                .forEach(n -> {
                    System.out.println("forEach");
                });
    }

    @Test
    void streamTestWithLimit() {
        Stream.of(1, 2, 3)
                .map(n -> {
                    System.out.println("map");
                    return n;
                })
                .limit(2)
                .filter(n -> {
                    System.out.println("filter");
                    return true;
                })
                .forEach(n -> {
                    System.out.println("forEach");
                });
    }

    @Test
    void streamTestWithSorted() {
        Stream.of(1, 2, 3)
                .map(n -> {
                    System.out.println("map");
                    return n;
                })
                .limit(2)
                .filter(n -> {
                    System.out.println("filter");
                    return true;
                })
                .sorted()
                .forEach(n -> {
                    System.out.println("forEach");
                });
    }
}
