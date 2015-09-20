package com.unisoft.algotrader.utils.id;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created by alex on 9/20/15.
 */
public class AtomicIntIdSupplier implements Supplier<Integer> {

    private final AtomicInteger counter;

    public AtomicIntIdSupplier(int id){
        counter = new AtomicInteger(id);
    }

    public AtomicIntIdSupplier(){
        counter = new AtomicInteger(0);
    }

    @Override
    public Integer get() {
        return counter.incrementAndGet();
    }
}
