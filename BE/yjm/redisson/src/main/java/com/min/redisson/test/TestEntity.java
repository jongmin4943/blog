package com.min.redisson.test;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Table(name = "test")
@SequenceGenerator(
        name = "TEST_SEQ_GEN",
        sequenceName = "SEQ_TEST",
        allocationSize = 1
)
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_SEQ_GEN")
    private final Long id;

    private Long count;

    private final LocalDateTime createdAt;

    protected TestEntity() {
        this.id = null;
        this.count = 100L;
        this.createdAt = LocalDateTime.now();
    }

    public static TestEntity create() {
        return new TestEntity();
    }

    public void decrease() {
        this.validateCount();
        this.count -= 1;
    }

    private void validateCount() {
        if (this.count < 1) {
            throw new IllegalArgumentException();
        }
    }
}
