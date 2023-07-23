package com.min.redisson;

import com.min.redisson.test.TestDecreaseService;
import com.min.redisson.test.TestEntity;
import com.min.redisson.test.TestEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class ConcurrentTest {
    @Autowired
    private TestDecreaseService testDecreaseService;
    @Autowired
    private TestEntityRepository testEntityRepository;


    private final TestEntity entity = TestEntity.create();


    @BeforeEach
    void setUp() {
        testEntityRepository.save(entity);
    }

    @AfterEach
    void tearDown() {
        TestEntity testEntity = testEntityRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(testEntity.getCount()).isZero();
        log.info("entity count : {}", testEntity.getCount());
    }

    @Test
    @DisplayName("재고 차감 테스트 - 동시성 이슈로 실패")
    void testConcurrentRequest() throws InterruptedException {
        ConcurrentTestUtils.execute(
                100,
                () -> testDecreaseService.decrease(entity.getId())
        );
    }

    @Test
    @DisplayName("재고 차감 테스트 - 분산락 적용(annotation)")
    void testConcurrentRequestWithLockAnnotation() throws InterruptedException {
        ConcurrentTestUtils.execute(
                100,
                () -> testDecreaseService.decreaseWithLockAnnotation(entity.getId())
        );
    }

    @Test
    @DisplayName("재고 차감 테스트 - 분산락 적용(redisLockService)")
    void testConcurrentRequestWithLockTemplate() throws InterruptedException {
        ConcurrentTestUtils.execute(
                100,
                () -> testDecreaseService.decreaseWithLockTemplate(entity.getId())
        );
    }
}
