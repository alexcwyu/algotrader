package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.*;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.trading.OrdStatus;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;

import java.util.List;

/**
 * Created by alex on 5/17/15.
 */
@Table(keyspace = "trading", name = "orders")
public class Order<E extends Order<? super E>> extends OrderEvent<E>{

    @PartitionKey(0)
    @Column(name="strategy_id")
    public int strategyId;

    @PartitionKey(1)
    @Column(name="cl_order_id")
    public long clOrderId;

    @Column(name="orig_cl_order_id")
    public long origClOrderId;

    @Column(name="provider_id")
    public int providerId;

    @Column(name="order_id")
    public long orderId = -1;

    @Column(name="portfolio_id")
    public int portfolioId;

    @Column(name="account_id")
    public String accountId;

    @Column(name="inst_id")
    public long instId;

    @Column(name="date_time")
    public long dateTime;

    @Column(name="ord_type")
    @Enumerated
    public OrdType ordType;

    @Column(name="ord_status")
    @Enumerated
    public OrdStatus ordStatus = OrdStatus.New;

    @Column(name="limit_price")
    public double limitPrice;

    @Column(name="stop_price")
    public double stopPrice;

    @Column(name="ord_qty")
    public double ordQty;

    @Column(name="filled_qty")
    public double filledQty = 0;

    @Column(name="avg_price")
    public double avgPrice;

    @Column(name="last_qty")
    public double lastQty;

    @Column(name="last_price")
    public double lastPrice;

    @Column(name="stop_limit_ready")
    public boolean stopLimitReady = false;

    @Column(name="trailing_stop_exec_price")
    public double trailingStopExecPrice;

    @Column(name="tif")
    @Enumerated
    public TimeInForce tif;

    @Column(name="side")
    @Enumerated
    public Side side;

    @Column(name="oca_group")
    public String ocaGroup;

    @Column(name="text")
    public String text;

    @Transient
    public List<ExecutionReport> executionReports = Lists.newArrayList();

    @Transient
    public List<OrderCancelReject> orderCancelRejects = Lists.newArrayList();

    @Column(name="commissions")
    public List<Double> commissions = Lists.newArrayList();

    @Column(name="pnl")
    public double pnl;

    @Column(name="realized_pnl")
    public double realizedPnl;


    public void add(ExecutionReport report){
        //TODO
        filledQty = report.filledQty;
        avgPrice = report.avgPrice;
        ordStatus = report.ordStatus;
        if (report.ordStatus == OrdStatus.PartiallyFilled || report.ordStatus == OrdStatus.Filled){
            lastPrice = report.lastPrice;
            lastQty = report.lastQty;
        }

        executionReports.add(report);

    }

    public void add(OrderCancelReject orderCancelReject){
        ordStatus = orderCancelReject.ordStatus;
        orderCancelRejects.add(orderCancelReject);
    }

    @Override
    public String toString() {
        return "Order{" +
                "accountId='" + accountId + '\'' +
                ", clOrderId=" + clOrderId +
                ", orderId=" + orderId +
                ", origClOrderId=" + origClOrderId +
                ", instId=" + instId +
                ", dateTime=" + dateTime +
                ", ordType=" + ordType +
                ", ordStatus=" + ordStatus +
                ", limitPrice=" + limitPrice +
                ", stopPrice=" + stopPrice +
                ", ordQty=" + ordQty +
                ", filledQty=" + filledQty +
                ", avgPrice=" + avgPrice +
                ", lastQty=" + lastQty +
                ", lastPrice=" + lastPrice +
                ", stopLimitReady=" + stopLimitReady +
                ", trailingStopExecPrice=" + trailingStopExecPrice +
                ", tif=" + tif +
                ", side=" + side +
                ", providerId='" + providerId + '\'' +
                ", portfolioId='" + portfolioId + '\'' +
                ", strategyId='" + strategyId + '\'' +
                ", text='" + text + '\'' +
                ", ocaGroup='" + ocaGroup + '\'' +
                ", executionReports=" + executionReports +
                ", orderCancelRejects=" + orderCancelRejects +
                ", commissions=" + commissions +
                ", pnl=" + pnl +
                ", realizedPnl=" + realizedPnl +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order<?> order = (Order<?>) o;
        return Objects.equal(clOrderId, order.clOrderId) &&
                Objects.equal(strategyId, order.strategyId) &&
                Objects.equal(orderId, order.orderId) &&
                Objects.equal(origClOrderId, order.origClOrderId) &&
                Objects.equal(providerId, order.providerId) &&
                Objects.equal(portfolioId, order.portfolioId) &&
                Objects.equal(accountId, order.accountId) &&
                Objects.equal(instId, order.instId) &&
                Objects.equal(dateTime, order.dateTime) &&
                Objects.equal(limitPrice, order.limitPrice) &&
                Objects.equal(stopPrice, order.stopPrice) &&
                Objects.equal(ordQty, order.ordQty) &&
                Objects.equal(filledQty, order.filledQty) &&
                Objects.equal(avgPrice, order.avgPrice) &&
                Objects.equal(lastQty, order.lastQty) &&
                Objects.equal(lastPrice, order.lastPrice) &&
                Objects.equal(stopLimitReady, order.stopLimitReady) &&
                Objects.equal(trailingStopExecPrice, order.trailingStopExecPrice) &&
                Objects.equal(pnl, order.pnl) &&
                Objects.equal(realizedPnl, order.realizedPnl) &&
                Objects.equal(ordType, order.ordType) &&
                Objects.equal(ordStatus, order.ordStatus) &&
                Objects.equal(tif, order.tif) &&
                Objects.equal(side, order.side) &&
                Objects.equal(text, order.text) &&
                Objects.equal(ocaGroup, order.ocaGroup) &&
                Objects.equal(executionReports, order.executionReports) &&
                Objects.equal(orderCancelRejects, order.orderCancelRejects) &&
                Objects.equal(commissions, order.commissions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clOrderId, strategyId, orderId, origClOrderId, providerId, portfolioId, accountId, instId, dateTime, ordType, ordStatus, limitPrice, stopPrice, ordQty, filledQty, avgPrice, lastQty, lastPrice, stopLimitReady, trailingStopExecPrice, tif, side, text, ocaGroup, executionReports, orderCancelRejects, commissions, pnl, realizedPnl);
    }

    @Override
    public void on(OrderEventHandler handler) {
        handler.onNewOrderRequest(this);
    }

    public boolean isDone(){
        return ordStatus == OrdStatus.Filled || ordStatus == OrdStatus.Cancelled || ordStatus == OrdStatus.Rejected;
    }

    public double leaveQty(){
        return ordQty - filledQty;
    }

    public boolean isActive(){
        return ordStatus == OrdStatus.New ||
                ordStatus == OrdStatus.PendingNew ||
                ordStatus == OrdStatus.PartiallyFilled ||
                ordStatus == OrdStatus.Replaced;
    }

    public double amount(){
        switch (side){
            case Buy:
            //case BuyMinus:
                return filledQty;
            case Sell:
            //case SellPlus:
            case SellShort:
            //case SellShortExempt:
                return -filledQty;
            default:
                throw new UnsupportedOperationException("");
        }
    }

    public double value(){
        return avgPrice * amount();
    }


    public double transactionCost(){
        double cost = 0.0;
        for (double commission : commissions){
            cost += commission;
        }
        return cost;
    }


    public double netCashFlow(){
        return -value();
    }


    public double cashFlow(){
        return netCashFlow() - transactionCost();
    }
//    public double margin(){
//        double margin = InstrumentManager.INSTANCE.get(instId).getMargin() * filledQty;
//        return margin;
//    }
//
//    public double debt(){
//        double margin = margin();
//        if (margin == 0){
//            return 0;
//        }
//        return value()-margin;
//    }


    public String accountId() {
        return accountId;
    }

    public Order accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public double avgPrice() {
        return avgPrice;
    }

    public Order avgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
        return this;
    }

    public long clOrderId() {
        return clOrderId;
    }

    public Order clOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
        return this;
    }

    public List<Double> commissions() {
        return commissions;
    }

    public Order commissions(List<Double> commissions) {
        this.commissions = commissions;
        return this;
    }

    public long dateTime() {
        return dateTime;
    }

    public Order dateTime(long dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public List<ExecutionReport> executionReports() {
        return executionReports;
    }

    public Order executionReports(List<ExecutionReport> executionReports) {
        this.executionReports = executionReports;
        return this;
    }

    public double filledQty() {
        return filledQty;
    }

    public Order filledQty(double filledQty) {
        this.filledQty = filledQty;
        return this;
    }

    public long instId() {
        return instId;
    }

    public Order instId(long instId) {
        this.instId = instId;
        return this;
    }

    public double lastPrice() {
        return lastPrice;
    }

    public Order lastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
        return this;
    }

    public double lastQty() {
        return lastQty;
    }

    public Order lastQty(double lastQty) {
        this.lastQty = lastQty;
        return this;
    }

    public double limitPrice() {
        return limitPrice;
    }

    public Order limitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
        return this;
    }

    public String ocaGroup() {
        return ocaGroup;
    }

    public Order ocaGroup(String ocaGroup) {
        this.ocaGroup = ocaGroup;
        return this;
    }

    public List<OrderCancelReject> orderCancelRejects() {
        return orderCancelRejects;
    }

    public Order orderCancelRejects(List<OrderCancelReject> orderCancelRejects) {
        this.orderCancelRejects = orderCancelRejects;
        return this;
    }

    public long orderId() {
        return orderId;
    }

    public Order orderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    public double ordQty() {
        return ordQty;
    }

    public Order ordQty(double ordQty) {
        this.ordQty = ordQty;
        return this;
    }

    public OrdStatus ordStatus() {
        return ordStatus;
    }

    public Order ordStatus(OrdStatus ordStatus) {
        this.ordStatus = ordStatus;
        return this;
    }

    public OrdType ordType() {
        return ordType;
    }

    public Order ordType(OrdType ordType) {
        this.ordType = ordType;
        return this;
    }

    public long origClOrderId() {
        return origClOrderId;
    }

    public Order origClOrderId(long origClOrderId) {
        this.origClOrderId = origClOrderId;
        return this;
    }

    public double pnl() {
        return pnl;
    }

    public Order pnl(double pnl) {
        this.pnl = pnl;
        return this;
    }

    public int portfolioId() {
        return portfolioId;
    }

    public Order portfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
        return this;
    }

    public int providerId() {
        return providerId;
    }

    public Order providerId(int providerId) {
        this.providerId = providerId;
        return this;
    }

    public double realizedPnl() {
        return realizedPnl;
    }

    public Order realizedPnl(double realizedPnl) {
        this.realizedPnl = realizedPnl;
        return this;
    }

    public Side side() {
        return side;
    }

    public Order side(Side side) {
        this.side = side;
        return this;
    }

    public boolean stopLimitReady() {
        return stopLimitReady;
    }

    public Order stopLimitReady(boolean stopLimitReady) {
        this.stopLimitReady = stopLimitReady;
        return this;
    }

    public double stopPrice() {
        return stopPrice;
    }

    public Order stopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
        return this;
    }

    public int strategyId() {
        return strategyId;
    }

    public Order strategyId(int strategyId) {
        this.strategyId = strategyId;
        return this;
    }

    public String text() {
        return text;
    }

    public Order text(String text) {
        this.text = text;
        return this;
    }

    public TimeInForce tif() {
        return tif;
    }

    public Order tif(TimeInForce tif) {
        this.tif = tif;
        return this;
    }

    public double trailingStopExecPrice() {
        return trailingStopExecPrice;
    }

    public Order trailingStopExecPrice(double trailingStopExecPrice) {
        this.trailingStopExecPrice = trailingStopExecPrice;
        return this;
    }
}
