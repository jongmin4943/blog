package com.min.quiz;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IterableTest {

    @Test
    void iterableTest() {
        final MyCollection myCollection = new MyCollection();

        for (String item : myCollection) {
            System.out.println(item);
        }
    }

    static class MyCollection implements Iterable<String> {
        final List<String> stringList = Arrays.asList("a", "b", "c");

        @Override
        public Iterator<String> iterator() {
            return stringList.iterator();
        }
    }
}
