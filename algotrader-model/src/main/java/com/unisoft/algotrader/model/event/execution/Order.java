package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.trading.OrdStatus;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;

import java.util.List;

/**
 * Created by alex on 5/17/15.
 */
@Table(keyspace = "trading", name = "orders")
public class Order<E extends Order<? super E>> implements Event<OrderHandler, E> {

    @PartitionKey
    @Column(name="cl_order_id")
    public long clOrderId;

    @Column(name="order_id")
    public long orderId = -1;

    @Column(name="orig_cl_order_id")
    public long origClOrderId;

    @Column(name="inst_id")
    public long instId;

    @Column(name="date_time")
    public long dateTime;

    @Column(name="ord_type")
    public OrdType ordType;

    @Column(name="ord_status")
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

    public TimeInForce tif;

    public Side side;

    @Column(name="exec_provider_id")
    public String execProviderId;

    @Column(name="portfolio_id")
    public String portfolioId;

    @Column(name="account")
    public String account;

    @Column(name="strategy_id")
    public String strategyId;

    @Column(name="text")
    public String text;

    @Column(name="oca_group")
    public String ocaGroup;

    @Transient
    public List<ExecutionReport> executionReports = Lists.newArrayList();

    @Transient
    public List<OrderCancelReject> orderCancelRejects = Lists.newArrayList();

    public List<Double> commissions = Lists.newArrayList();

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
                "account='" + account + '\'' +
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
                ", execProviderId='" + execProviderId + '\'' +
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
                Objects.equal(orderId, order.orderId) &&
                Objects.equal(origClOrderId, order.origClOrderId) &&
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
                Objects.equal(execProviderId, order.execProviderId) &&
                Objects.equal(portfolioId, order.portfolioId) &&
                Objects.equal(account, order.account) &&
                Objects.equal(strategyId, order.strategyId) &&
                Objects.equal(text, order.text) &&
                Objects.equal(ocaGroup, order.ocaGroup) &&
                Objects.equal(executionReports, order.executionReports) &&
                Objects.equal(orderCancelRejects, order.orderCancelRejects) &&
                Objects.equal(commissions, order.commissions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clOrderId, orderId, origClOrderId, instId, dateTime, ordType, ordStatus, limitPrice, stopPrice, ordQty, filledQty, avgPrice, lastQty, lastPrice, stopLimitReady, trailingStopExecPrice, tif, side, execProviderId, portfolioId, account, strategyId, text, ocaGroup, executionReports, orderCancelRejects, commissions, pnl, realizedPnl);
    }

    public static final EventFactory<Order> FACTORY = new EventFactory(){
        @Override
        public Order newInstance() {
            return new Order();
        }
    };

    @Override
    public void on(OrderHandler handler) {
        handler.onNewOrderRequest(this);
    }

    @Override
    public void reset() {

    }

    @Override
    public void copy(E event) {

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


    public String account() {
        return account;
    }

    public void account(String account) {
        this.account = account;
    }

    public double avgPrice() {
        return avgPrice;
    }

    public void avgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public long clOrderId() {
        return clOrderId;
    }

    public void clOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
    }

    public List<Double> commissions() {
        return commissions;
    }

    public void commissions(List<Double> commissions) {
        this.commissions = commissions;
    }

    public long dateTime() {
        return dateTime;
    }

    public void dateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String execProviderId() {
        return execProviderId;
    }

    public void execProviderId(String execProviderId) {
        this.execProviderId = execProviderId;
    }

    public List<ExecutionReport> executionReports() {
        return executionReports;
    }

    public void executionReports(List<ExecutionReport> executionReports) {
        this.executionReports = executionReports;
    }

    public double filledQty() {
        return filledQty;
    }

    public void filledQty(double filledQty) {
        this.filledQty = filledQty;
    }

    public long instId() {
        return instId;
    }

    public void instId(long instId) {
        this.instId = instId;
    }

    public double lastPrice() {
        return lastPrice;
    }

    public void lastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double lastQty() {
        return lastQty;
    }

    public void lastQty(double lastQty) {
        this.lastQty = lastQty;
    }

    public double limitPrice() {
        return limitPrice;
    }

    public void limitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public List<OrderCancelReject> orderCancelRejects() {
        return orderCancelRejects;
    }

    public void orderCancelRejects(List<OrderCancelReject> orderCancelRejects) {
        this.orderCancelRejects = orderCancelRejects;
    }

    public long orderId() {
        return orderId;
    }

    public void orderId(long orderId) {
        this.orderId = orderId;
    }

    public double ordQty() {
        return ordQty;
    }

    public void ordQty(double ordQty) {
        this.ordQty = ordQty;
    }

    public OrdStatus ordStatus() {
        return ordStatus;
    }

    public void ordStatus(OrdStatus ordStatus) {
        this.ordStatus = ordStatus;
    }

    public OrdType ordType() {
        return ordType;
    }

    public void ordType(OrdType ordType) {
        this.ordType = ordType;
    }

    public long origClOrderId() {
        return origClOrderId;
    }

    public void origClOrderId(long origClOrderId) {
        this.origClOrderId = origClOrderId;
    }

    public double pnl() {
        return pnl;
    }

    public void pnl(double pnl) {
        this.pnl = pnl;
    }

    public String portfolioId() {
        return portfolioId;
    }

    public void portfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public double realizedPnl() {
        return realizedPnl;
    }

    public void realizedPnl(double realizedPnl) {
        this.realizedPnl = realizedPnl;
    }

    public Side side() {
        return side;
    }

    public void side(Side side) {
        this.side = side;
    }

    public boolean stopLimitReady() {
        return stopLimitReady;
    }

    public void stopLimitReady(boolean stopLimitReady) {
        this.stopLimitReady = stopLimitReady;
    }

    public double stopPrice() {
        return stopPrice;
    }

    public void stopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String strategyId() {
        return strategyId;
    }

    public void strategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String text() {
        return text;
    }

    public void text(String text) {
        this.text = text;
    }

    public TimeInForce tif() {
        return tif;
    }

    public void tif(TimeInForce tif) {
        this.tif = tif;
    }

    public double trailingStopExecPrice() {
        return trailingStopExecPrice;
    }

    public void trailingStopExecPrice(double trailingStopExecPrice) {
        this.trailingStopExecPrice = trailingStopExecPrice;
    }

    public String ocaGroup() {
        return ocaGroup;
    }

    public void ocaGroup(String ocaGroup) {
        this.ocaGroup = ocaGroup;
    }

    public void replaceOrder(OrderCancelReplaceRequest req){
        this.origClOrderId = this.clOrderId;

        if (req.clOrderId > 0){
            this.clOrderId = clOrderId;
        }

        if (req.instId >0)
            this.instId = instId;

        if (req.dateTime >0)
            this.dateTime = dateTime;

        if (req.ordType != null)
            this.ordType = req.ordType;

        if(req.limitPrice >0)
            this.limitPrice = req.limitPrice;

        if(req.stopPrice >0)
            this.stopPrice = req.stopPrice;

        if(req.ordQty > 0)
            this.ordQty = req.ordQty;

        if(req.tif != null)
            this.tif = req.tif;

        if(req.side != null)
            this.side = req.side;

        if(req.account != null)
            this.account = req.account;

    }
}
