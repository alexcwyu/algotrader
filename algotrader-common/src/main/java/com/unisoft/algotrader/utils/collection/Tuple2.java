package com.unisoft.algotrader.utils.collection;

import com.google.common.base.Objects;

/**
 * Created by alex on 8/19/15.
 */
public class Tuple2<T1, T2> {
    public final T1 v1;
    public final T2 v2;

    public Tuple2(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T1 getV1() {
        return v1;
    }

    public T2 getV2() {
        return v2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple2)) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equal(v1, tuple2.v1) &&
                Objects.equal(v2, tuple2.v2);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(v1, v2);
    }

    @Override
    public String toString() {
        return "Tuple2{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                '}';
    }
}