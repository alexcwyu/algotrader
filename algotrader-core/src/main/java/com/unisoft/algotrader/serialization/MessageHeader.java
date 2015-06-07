package com.unisoft.algotrader.serialization;

import com.google.common.base.Objects;

/**
 * Created by alex on 5/15/15.
 */
public class MessageHeader {
    public int msgId;
    public int typeId;
    public int version;
    public int length;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageHeader that = (MessageHeader) o;
        return Objects.equal(msgId, that.msgId) &&
                Objects.equal(typeId, that.typeId) &&
                Objects.equal(version, that.version) &&
                Objects.equal(length, that.length);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(msgId, typeId, version, length);
    }
}
