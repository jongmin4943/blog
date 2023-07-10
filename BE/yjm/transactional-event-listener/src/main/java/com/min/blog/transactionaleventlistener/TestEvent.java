package com.min.blog.transactionaleventlistener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record TestEvent(Long id, LocalDateTime createAt) {
    public String detail() {
        return String.format(
                "temp%s saved at %s",
                id,
                createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}
