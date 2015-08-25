package com.unisoft.algotrader.provider.ib.api.model;

import com.google.common.base.Objects;

/**
 * Created by alex on 8/26/15.
 */
public class UnderlyingCombo {

    private int instId;
    private double delta;
    private double price;

    public int getInstId() {
        return instId;
    }

    public void setInstId(int instId) {
        this.instId = instId;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnderlyingCombo)) return false;
        UnderlyingCombo that = (UnderlyingCombo) o;
        return Objects.equal(instId, that.instId) &&
                Objects.equal(delta, that.delta) &&
                Objects.equal(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, delta, price);
    }

    @Override
    public String toString() {
        return "UnderlyingCombo{" +
                "instId=" + instId +
                ", delta=" + delta +
                ", price=" + price +
                '}';
    }
}
