package com.min.blog.transactionaleventlistener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(false)
class TransactionalEventListenerApplicationTests {

	@Autowired
	private EventPublisher eventPublisher;

	@Test
	void test() {
		eventPublisher.publish();
	}
}
