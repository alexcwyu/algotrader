/* Generated SBE (Simple Binary Encoding) message codec */
package com.unisoft.algotrader.model.event.data.sbe;

import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

public class Bar
{
    public static final int BLOCK_LENGTH = 68;
    public static final int TEMPLATE_ID = 3;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final Bar parentMessage = this;
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

    public Bar wrapForEncode(final UnsafeBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = BLOCK_LENGTH;
        this.actingVersion = SCHEMA_VERSION;
        limit(offset + actingBlockLength);

        return this;
    }

    public Bar wrapForDecode(
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

    public Bar symbolId(final long value)
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

    public Bar datetime(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int barSizeId()
    {
        return 3;
    }

    public static String barSizeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static double barSizeNullValue()
    {
        return Double.NaN;
    }

    public static double barSizeMinValue()
    {
        return 4.9E-324d;
    }

    public static double barSizeMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public double barSize()
    {
        return CodecUtil.doubleGet(buffer, offset + 12, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Bar barSize(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 12, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int highId()
    {
        return 4;
    }

    public static String highMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static double highNullValue()
    {
        return Double.NaN;
    }

    public static double highMinValue()
    {
        return 4.9E-324d;
    }

    public static double highMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public double high()
    {
        return CodecUtil.doubleGet(buffer, offset + 20, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Bar high(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 20, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int lowId()
    {
        return 5;
    }

    public static String lowMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static double lowNullValue()
    {
        return Double.NaN;
    }

    public static double lowMinValue()
    {
        return 4.9E-324d;
    }

    public static double lowMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public double low()
    {
        return CodecUtil.doubleGet(buffer, offset + 28, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Bar low(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 28, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int openId()
    {
        return 5;
    }

    public static String openMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static double openNullValue()
    {
        return Double.NaN;
    }

    public static double openMinValue()
    {
        return 4.9E-324d;
    }

    public static double openMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public double open()
    {
        return CodecUtil.doubleGet(buffer, offset + 36, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Bar open(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 36, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int closeId()
    {
        return 5;
    }

    public static String closeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static double closeNullValue()
    {
        return Double.NaN;
    }

    public static double closeMinValue()
    {
        return 4.9E-324d;
    }

    public static double closeMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public double close()
    {
        return CodecUtil.doubleGet(buffer, offset + 44, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Bar close(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 44, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int volumeId()
    {
        return 5;
    }

    public static String volumeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long volumeNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long volumeMinValue()
    {
        return 0x0L;
    }

    public static long volumeMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public long volume()
    {
        return CodecUtil.uint64Get(buffer, offset + 52, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Bar volume(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 52, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int openIntId()
    {
        return 6;
    }

    public static String openIntMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long openIntNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long openIntMinValue()
    {
        return 0x0L;
    }

    public static long openIntMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public long openInt()
    {
        return CodecUtil.uint64Get(buffer, offset + 60, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public Bar openInt(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 60, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
