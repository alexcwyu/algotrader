package com.unisoft.algotrader.persistence;


import java.util.Optional;
import java.util.function.Function;
/**
 * Created by alex on 7/14/15.
 */
public interface GetSequenceRange extends Function<Integer, Sequence> {
    @Override
    default Sequence apply(Integer size) {
        return next(size);
    }

    Sequence next(Integer size);

    Optional<Sequence> latest();
}