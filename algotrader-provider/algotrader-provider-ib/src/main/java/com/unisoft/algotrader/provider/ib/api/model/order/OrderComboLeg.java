package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.base.Objects;

/**
 * Created by alex on 9/9/15.
 */
public class OrderComboLeg {

    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderComboLeg)) return false;
        OrderComboLeg that = (OrderComboLeg) o;
        return Objects.equal(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(price);
    }

    @Override
    public String toString() {
        return "OrderComboLeg{" +
                "price=" + price +
                '}';
    }
}
