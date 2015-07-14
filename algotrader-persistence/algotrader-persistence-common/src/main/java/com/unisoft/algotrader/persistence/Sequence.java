package com.unisoft.algotrader.persistence;

import java.util.stream.LongStream;

/**
 * Created by alex on 7/14/15.
 */
public class Sequence {
    private long start;
    private long end;

    public Sequence(){
    }

    public Sequence(long start, long end){
        this.start = start;
        this.end = end;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public LongStream stream(){
        return LongStream.range(start, end);
    }
}
