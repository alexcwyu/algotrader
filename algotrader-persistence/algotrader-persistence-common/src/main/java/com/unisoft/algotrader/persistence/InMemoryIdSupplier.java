package com.unisoft.algotrader.persistence;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by alex on 7/14/15.
 */
public class InMemoryIdSupplier implements IdSupplier<Long> {
    private static AtomicLong ids = new AtomicLong(0);

    @Override
    public Long next() {
        return ids.incrementAndGet();
    }


}
