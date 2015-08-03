package com.unisoft.algotrader.provider.ib.api.message;

import ch.aonyx.broker.ib.api.util.RequestBuilder;
import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by alex on 8/3/15.
 */
public class ByteArrayBuilder {

    private static final byte[] EOL = { 0 };
    private byte[] bytes = new byte[] {};

    public ByteArrayBuilder append(final int i) {
        if (i != Integer.MAX_VALUE) {
            bytes = Bytes.concat(bytes, String.valueOf(i).getBytes());
        }
        appendEol();
        return this;
    }
    public ByteArrayBuilder append(final long i) {
        if (i != Long.MAX_VALUE) {
            bytes = Bytes.concat(bytes, String.valueOf(i).getBytes());
        }
        appendEol();
        return this;
    }

    public ByteArrayBuilder append(final boolean b) {
        int i = 0;
        if (b) {
            i = 1;
        }
        bytes = Bytes.concat(bytes, String.valueOf(i).getBytes());
        appendEol();
        return this;
    }

    public ByteArrayBuilder append(final double d) {
        if (d != Double.MAX_VALUE) {
            bytes = Bytes.concat(bytes, String.valueOf(d).getBytes());
        }
        appendEol();
        return this;
    }

    public ByteArrayBuilder append(final String s) {
        if (StringUtils.isNotEmpty(s)) {
            bytes = Bytes.concat(bytes, s.getBytes());
        }
        appendEol();
        return this;
    }

    private void appendEol() {
        bytes = Bytes.concat(bytes, EOL);
    }

    public byte[] toBytes() {
        return bytes;
    }
}
