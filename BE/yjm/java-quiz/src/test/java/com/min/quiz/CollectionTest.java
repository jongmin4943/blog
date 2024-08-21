package com.min.quiz;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionTest {

    @Test
    void hashSetAddTest() {
        final Set<Integer> integers = new HashSet<>();

        integers.add(3);
        integers.add(2);
        integers.add(5);
        integers.add(1);
        integers.add(10);

        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }

    @Test
    void hashSetAddTest2() {
        final Set<Person> set = new HashSet<>();

        final Person person1 = new Person("test");
        final Person person2 = new Person("test");

        set.add(person1);
        set.add(person2);

        assertThat(set.iterator().next()).isSameAs(person1);
    }

    private record Person(String name) {

    }

    @Test
    void test() {
    }
}
