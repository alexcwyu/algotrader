package com.unisoft.algotrader.utils.id;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Created by alex on 9/20/15.
 */
public class AtomicLongIdSupplier implements Supplier<Long> {

    private final AtomicLong counter;

    public AtomicLongIdSupplier(int id){
        counter = new AtomicLong(id);
    }

    public AtomicLongIdSupplier(){
        counter = new AtomicLong(0);
    }

    @Override
    public Long get() {
        return counter.incrementAndGet();
    }
}
