package com.unisoft.algotrader.provider.ib.api;

import ch.aonyx.broker.ib.api.io.IOStreamException;
import org.apache.commons.lang3.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static ch.aonyx.broker.ib.api.ClientMessageCode.INPUT_OUTPUT_STREAM_EXCEPTION;

/**
 * Created by alex on 8/2/15.
 */
public class InputStreamUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputStreamUtils.class);
    private static final StrBuilder BUILDER = new StrBuilder();

    public static final int readInt(final InputStream inputStream) {
        final String string = readString(inputStream);
        return string == null ? 0 : Integer.parseInt(string);
    }

    public static final String readString(final InputStream inputStream) {
        BUILDER.clear();
        while (true) {
            final byte c = readByte(inputStream);
            if (c == 0) {
                break;
            }
            BUILDER.append((char) c);
        }
        final String s = BUILDER.toString();
        return s.length() == 0 ? null : s;
    }

    private static byte readByte(final InputStream inputStream) {
        try {
            final byte c = (byte) inputStream.read();
            return c;
        } catch (final IOException e) {
            final String detailedMessage = "problem reading byte";
            LOGGER.error("{}: ", detailedMessage, e);
            throw new IOStreamException(INPUT_OUTPUT_STREAM_EXCEPTION, detailedMessage, e);
        }
    }

    public static final boolean readBoolean(final InputStream inputStream) {
        final String string = readString(inputStream);
        return string == null ? false : (Integer.parseInt(string) != 0);
    }

    public static final long readLong(final InputStream inputStream) {
        final String string = readString(inputStream);
        return string == null ? 0 : Long.parseLong(string);
    }

    public static final double readDouble(final InputStream inputStream) {
        final String string = readString(inputStream);
        return string == null ? 0 : Double.parseDouble(string);
    }

    public static final int readIntMax(final InputStream inputStream) {
        final String string = readString(inputStream);
        return string == null ? Integer.MAX_VALUE : Integer.parseInt(string);
    }

    public static final double readDoubleMax(final InputStream inputStream) {
        final String string = readString(inputStream);
        return string == null ? Double.MAX_VALUE : Double.parseDouble(string);
    }
}