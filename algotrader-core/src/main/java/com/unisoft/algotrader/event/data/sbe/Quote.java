/* Generated SBE (Simple Binary Encoding) message codec */
package com.unisoft.algotrader.event.data.sbe;

import uk.co.real_logic.sbe.codec.java.*;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

public class Quote
{
    public static final int BLOCK_LENGTH = 36;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final Quote parentMessage = this;
    private UnsafeBuffer buffer;
    private int offset;
    private int limit;
    private int actingBlockLength;
    private int actingVersion;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public int offset()
    {
        return offset;
    }

    public Quote wrapForEncode(final UnsafeBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = BLOCK_LENGTH;
        this.actingVersion = SCHEMA_VERSION;
        limit(offset + actingBlockLength);

        return this;
    }

    public Quote wrapForDecode(
        final UnsafeBuffer buffer, final int offset, final int actingBlockLength, final int actingVersion)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public int size()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        buffer.checkLimit(limit);
        this.limit = limit;
    }

    public static int symbolIdId()
    {
        return 1;
    }

    public static String symbolIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long symbolIdNullValue()
    {
        return 4294967294L;
    }

    public static long symbolIdMinValue()
    {
        return 0L;
    }

    public static long symbolIdMaxValue()
    {
        return 4294967293L;
    }

    public long symbolId()
    {
        return CodecUtil.uint32Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Quote symbolId(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int datetimeId()
    {
        return 2;
    }

    public static String datetimeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long datetimeNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long datetimeMinValue()
    {
        return 0x0L;
    }

    public static long datetimeMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public long datetime()
    {
        return CodecUtil.uint64Get(buffer, offset + 4, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Quote datetime(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int bidId()
    {
        return 3;
    }

    public static String bidMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static double bidNullValue()
    {
        return Double.NaN;
    }

    public static double bidMinValue()
    {
        return 4.9E-324d;
    }

    public static double bidMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public double bid()
    {
        return CodecUtil.doubleGet(buffer, offset + 12, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Quote bid(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 12, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int askId()
    {
        return 4;
    }

    public static String askMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static double askNullValue()
    {
        return Double.NaN;
    }

    public static double askMinValue()
    {
        return 4.9E-324d;
    }

    public static double askMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public double ask()
    {
        return CodecUtil.doubleGet(buffer, offset + 20, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Quote ask(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 20, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int bidVolId()
    {
        return 5;
    }

    public static String bidVolMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long bidVolNullValue()
    {
        return 4294967294L;
    }

    public static long bidVolMinValue()
    {
        return 0L;
    }

    public static long bidVolMaxValue()
    {
        return 4294967293L;
    }

    public long bidVol()
    {
        return CodecUtil.uint32Get(buffer, offset + 28, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Quote bidVol(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 28, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int askVolId()
    {
        return 6;
    }

    public static String askVolMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long askVolNullValue()
    {
        return 4294967294L;
    }

    public static long askVolMinValue()
    {
        return 0L;
    }

    public static long askVolMaxValue()
    {
        return 4294967293L;
    }

    public long askVol()
    {
        return CodecUtil.uint32Get(buffer, offset + 32, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Quote askVol(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 32, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
