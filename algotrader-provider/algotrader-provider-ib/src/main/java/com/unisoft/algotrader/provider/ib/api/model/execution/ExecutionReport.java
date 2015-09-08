package com.unisoft.algotrader.provider.ib.api.model.execution;

import com.google.common.base.Objects;
import com.unisoft.algotrader.model.trading.Side;

/**
 * Created by alex on 9/9/15.
 */
public class ExecutionReport {


    private static final String EMPTY = "";
    private String accountNumber = EMPTY;
    private double averageFilledPrice;
    private int clientId;
    private int cumulativeQuantity;
    private String exchange = EMPTY;
    private String executionId = EMPTY;
    private int liquidation;
    private int orderId;
    private String orderRef = EMPTY;
    private int permanentId;
    private double filledPrice;
    private int filledQuantity;
    private Side side = null;
    private String time = EMPTY;
    private String economicValueRule = EMPTY;
    private double economicValueMultiplier;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAverageFilledPrice() {
        return averageFilledPrice;
    }

    public void setAverageFilledPrice(double averageFilledPrice) {
        this.averageFilledPrice = averageFilledPrice;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getCumulativeQuantity() {
        return cumulativeQuantity;
    }

    public void setCumulativeQuantity(int cumulativeQuantity) {
        this.cumulativeQuantity = cumulativeQuantity;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public int getLiquidation() {
        return liquidation;
    }

    public void setLiquidation(int liquidation) {
        this.liquidation = liquidation;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public int getPermanentId() {
        return permanentId;
    }

    public void setPermanentId(int permanentId) {
        this.permanentId = permanentId;
    }

    public double getFilledPrice() {
        return filledPrice;
    }

    public void setFilledPrice(double filledPrice) {
        this.filledPrice = filledPrice;
    }

    public int getFilledQuantity() {
        return filledQuantity;
    }

    public void setFilledQuantity(int filledQuantity) {
        this.filledQuantity = filledQuantity;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEconomicValueRule() {
        return economicValueRule;
    }

    public void setEconomicValueRule(String economicValueRule) {
        this.economicValueRule = economicValueRule;
    }

    public double getEconomicValueMultiplier() {
        return economicValueMultiplier;
    }

    public void setEconomicValueMultiplier(double economicValueMultiplier) {
        this.economicValueMultiplier = economicValueMultiplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionReport)) return false;
        ExecutionReport that = (ExecutionReport) o;
        return Objects.equal(averageFilledPrice, that.averageFilledPrice) &&
                Objects.equal(clientId, that.clientId) &&
                Objects.equal(cumulativeQuantity, that.cumulativeQuantity) &&
                Objects.equal(liquidation, that.liquidation) &&
                Objects.equal(orderId, that.orderId) &&
                Objects.equal(permanentId, that.permanentId) &&
                Objects.equal(filledPrice, that.filledPrice) &&
                Objects.equal(filledQuantity, that.filledQuantity) &&
                Objects.equal(economicValueMultiplier, that.economicValueMultiplier) &&
                Objects.equal(accountNumber, that.accountNumber) &&
                Objects.equal(exchange, that.exchange) &&
                Objects.equal(executionId, that.executionId) &&
                Objects.equal(orderRef, that.orderRef) &&
                Objects.equal(side, that.side) &&
                Objects.equal(time, that.time) &&
                Objects.equal(economicValueRule, that.economicValueRule);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber, averageFilledPrice, clientId, cumulativeQuantity, exchange, executionId, liquidation, orderId, orderRef, permanentId, filledPrice, filledQuantity, side, time, economicValueRule, economicValueMultiplier);
    }

    @Override
    public String toString() {
        return "ExecutionReport{" +
                "accountNumber='" + accountNumber + '\'' +
                ", averageFilledPrice=" + averageFilledPrice +
                ", clientId=" + clientId +
                ", cumulativeQuantity=" + cumulativeQuantity +
                ", exchange='" + exchange + '\'' +
                ", executionId='" + executionId + '\'' +
                ", liquidation=" + liquidation +
                ", orderId=" + orderId +
                ", orderRef='" + orderRef + '\'' +
                ", permanentId=" + permanentId +
                ", filledPrice=" + filledPrice +
                ", filledQuantity=" + filledQuantity +
                ", side=" + side +
                ", time='" + time + '\'' +
                ", economicValueRule='" + economicValueRule + '\'' +
                ", economicValueMultiplier=" + economicValueMultiplier +
                '}';
    }
}
