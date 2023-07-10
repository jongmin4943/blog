package com.min.blog.transactionaleventlistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TempRepository tempRepository;

    @Transactional
    public void publish() {
        log.info("Start publish method");
        Temp saved = tempRepository.save(new Temp());
        applicationEventPublisher.publishEvent(new TestEvent(saved.getId(), saved.getCreatedAt()));
        log.info("End publish method");
    }

}
