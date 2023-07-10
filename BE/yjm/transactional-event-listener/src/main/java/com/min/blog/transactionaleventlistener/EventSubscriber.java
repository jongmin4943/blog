package com.min.blog.transactionaleventlistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class EventSubscriber {
    @Order(4)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommitSubscribeNo4(TestEvent event) {
        log.info("NO.4 AFTER COMMIT " + event.detail());
    }
    @Order(3)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommitSubscribeNo3(TestEvent event) {
        log.info("NO.3 AFTER COMMIT " + event.detail());
    }
    @Order(1)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletionSubscribeNo1(TestEvent event) {
        log.info("NO.1 AFTER COMPLETION " + event.detail());
    }
    @Order(2)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletionSubscribeNo2(TestEvent event) {
        log.info("NO.2 AFTER COMPLETION " + event.detail());
    }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollbackSubscribe(TestEvent event) {
        log.info("AFTER ROLLBACK " + event.detail());
    }

    @Order(5)
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommitSubscribe(TestEvent event) {
        log.info("BEFORE COMMIT " + event.detail());
    }

}
