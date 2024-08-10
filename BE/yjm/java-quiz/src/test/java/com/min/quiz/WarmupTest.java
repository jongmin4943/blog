package com.min.quiz;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WarmupTest {

    @Test
    void isNumberSame1() {
        assertThat(1).isSameAs(1);
    }

    @Test
    void isNumberSame100() {
        assertThat(100).isSameAs(100);
    }

    @Test
    void isNumberSame10000() {
        assertThat(10000).isSameAs(10000);
    }

}
