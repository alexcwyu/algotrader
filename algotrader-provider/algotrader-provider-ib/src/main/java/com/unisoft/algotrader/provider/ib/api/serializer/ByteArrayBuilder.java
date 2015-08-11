package com.unisoft.algotrader.provider.ib.api.serializer;

import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;

/**
 * Created by alex on 8/3/15.
 */
public class ByteArrayBuilder {

    private static final byte[] EOL = { 0 };
    private ByteBuffer buf;

    public ByteArrayBuilder(){
        buf = ByteBuffer.allocate(1024);
    }

    public ByteArrayBuilder(int size){
        buf = ByteBuffer.allocate(size);
    }

    public ByteArrayBuilder append(final int i) {
        if (i != Integer.MAX_VALUE) {
            buf.put(Integer.toString(i).getBytes());
        }
        appendEol();
        return this;
    }
    public ByteArrayBuilder append(final long i) {
        if (i != Long.MAX_VALUE) {
            buf.put(Long.toString(i).getBytes());
        }
        appendEol();
        return this;
    }

    public ByteArrayBuilder append(final boolean b) {
        int i = 0;
        if (b) {
            i = 1;
        }
        buf.put(Integer.toString(i).getBytes());
        appendEol();
        return this;
    }

    public ByteArrayBuilder append(final double d) {
        if (d != Double.MAX_VALUE) {
            buf.put(Double.toString(d).getBytes());
        }
        appendEol();
        return this;
    }

    public ByteArrayBuilder append(final String s) {
        if (StringUtils.isNotEmpty(s)) {
            buf.put(s.getBytes());
        }
        appendEol();
        return this;
    }


    public ByteArrayBuilder append(byte [] bytes) {
        if (bytes != null) {
            buf.put(bytes);
        }
        appendEol();
        return this;
    }

    public void appendEol() {
        buf.put(EOL);
    }

    public byte[] toBytes() {
        buf.flip();
        byte[] data = new byte[buf.remaining()];
        buf.get(data);
        return data;

    }

    public void clear(){
        buf.clear();
    }
}
