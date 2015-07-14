package com.unisoft.algotrader.persistence;

import java.util.function.Supplier;

/**
 * Created by alex on 7/14/15.
 */
public interface IdSupplier<T>  extends Supplier<T> {
    @Override
    default T get() {
        return next();
    }

    T next();
}
