package com.unisoft.algotrader.provider.data;

import com.google.common.base.Objects;
import com.unisoft.algotrader.model.event.data.DataType;

/**
 * Created by alex on 9/22/15.
 */
public class SubscriptionType {

    public final DataType type;

    public final int barSize;

    public SubscriptionType(DataType type, int barSize) {
        this.type = type;
        this.barSize = barSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionType)) return false;
        SubscriptionType that = (SubscriptionType) o;
        return Objects.equal(barSize, that.barSize) &&
                Objects.equal(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, barSize);
    }

    @Override
    public String toString() {
        return "SubscriptionType{" +
                "barSize=" + barSize +
                ", type=" + type +
                '}';
    }
}
