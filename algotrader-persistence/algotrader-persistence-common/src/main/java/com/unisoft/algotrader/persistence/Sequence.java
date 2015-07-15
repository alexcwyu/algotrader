package com.unisoft.algotrader.persistence;

import com.google.common.base.Preconditions;

import java.util.function.Predicate;
import java.util.stream.LongStream;

/**
 * Created by alex on 7/14/15.
 */
public class Sequence implements Predicate<Long>, Comparable<Sequence> {

    private final long start;
    private final long end;
    private final long limit;

    public Sequence(long end) {
        this(1, end, -1);
    }

    public Sequence(long start, long end, long limit) {
        this.end = end;
        this.start = start;
        this.limit = limit;
        validate();
    }

    public long end() {
        return end;
    }

    public long start() {
        return start;
    }

    public long limit() {
        return limit;
    }

    public int size() {
        return (int) (end - start + 1);
    }

    public LongStream stream() {
        return LongStream.rangeClosed(start, end);
    }

    public boolean limitBreached() {
        return limit > 0 && end > limit;
    }

    public Sequence validate() {
        Preconditions.checkArgument(start <= end, this + " is not valid");
        Preconditions.checkArgument(end > 0, "End should be greater than zero");
        Preconditions.checkArgument(start > 0, "Start should be greater than zero");
        return this;
    }

    public boolean nextOf(Sequence other) {
        return this.start() - other.end() == 1;
    }

    public Sequence next(long size) {
        return new Sequence(end + 1, end + size, limit);
    }

    @Override
    public boolean test(Long value) {
        return value != null && contains(value);
    }

    public boolean contains(long value) {
        return value >= start && value < end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sequence that = (Sequence) o;
        return end == that.end && start == that.start;
    }

    @Override
    public int hashCode() {
        int result = (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (end ^ (end >>> 32));
        return result;
    }

    @Override
    public int compareTo(Sequence other) {
        return (start - other.start) > 0 ? 1 : -1;
    }

    @Override
    public String toString() {
        return String.format("[%d..%d]", start, end);
    }
}