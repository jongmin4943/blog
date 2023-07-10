package com.min.blog.transactionaleventlistener;

import org.springframework.data.repository.Repository;

public interface TempRepository extends Repository<Temp, Long> {
    Temp save(Temp entity);
}
