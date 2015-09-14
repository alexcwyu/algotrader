package com.unisoft.algotrader.provider.ib.api.model.execution;

import com.google.common.base.Objects;
import com.unisoft.algotrader.provider.ib.api.model.contract.SecType;
import com.unisoft.algotrader.provider.ib.api.model.order.OrderAction;

/**
 * Created by alex on 9/9/15.
 */
public class ExecutionReportFilter {

    private static final String EMPTY = "";
    private String accountNumber = EMPTY;
    private int clientId;
    private String exchange = EMPTY;
    private SecType securityType = null;
    private OrderAction orderAction = null;
    private String symbol = EMPTY;
    private String time = EMPTY;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public SecType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecType securityType) {
        this.securityType = securityType;
    }

    public OrderAction getOrderAction() {
        return orderAction;
    }

    public void setOrderAction(OrderAction orderAction) {
        this.orderAction = orderAction;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionReportFilter)) return false;
        ExecutionReportFilter that = (ExecutionReportFilter) o;
        return Objects.equal(clientId, that.clientId) &&
                Objects.equal(accountNumber, that.accountNumber) &&
                Objects.equal(exchange, that.exchange) &&
                Objects.equal(securityType, that.securityType) &&
                Objects.equal(orderAction, that.orderAction) &&
                Objects.equal(symbol, that.symbol) &&
                Objects.equal(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber, clientId, exchange, securityType, orderAction, symbol, time);
    }

    @Override
    public String toString() {
        return "ExecutionReportFilter{" +
                "accountNumber='" + accountNumber + '\'' +
                ", clientId=" + clientId +
                ", exchange='" + exchange + '\'' +
                ", securityType=" + securityType +
                ", orderAction=" + orderAction +
                ", symbol='" + symbol + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
