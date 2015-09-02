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
    @Column(name="order_id")
    public long orderId;

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

    @Column(name="strategy_id")
    public String strategyId;

    public String text;

    @Transient
    public List<ExecutionReport> executionReports = Lists.newArrayList();

    public List<Double> commissions = Lists.newArrayList();

    public double pnl;

    @Column(name="realized_pnl")
    public double realizedPnl;


    public void add(ExecutionReport report){
        filledQty = report.filledQty;
        avgPrice = report.avgPrice;
        ordStatus = report.ordStatus;
        if (report.ordStatus == OrdStatus.PartiallyFilled || report.ordStatus == OrdStatus.Filled){
            lastPrice = report.lastPrice;
            lastQty = report.lastQty;
        }

        executionReports.add(report);

    }

    @Override
    public String toString() {
        return "Order{" +
                "getOrderId=" + orderId +
                ", instId='" + instId + '\'' +
                ", dateTime=" + dateTime +
                ", ordType=" + ordType +
                ", limitPrice=" + limitPrice +
                ", ordQty=" + ordQty +
                ", filledQty=" + filledQty +
                ", stopPrice=" + stopPrice +
                ", avgPrice=" + avgPrice +
                ", stopLimitReady=" + stopLimitReady +
                ", trailingStopExecPrice=" + trailingStopExecPrice +
                ", tif=" + tif +
                ", side=" + side +
                ", execProviderId='" + execProviderId + '\'' +
                ", portfolioId='" + portfolioId + '\'' +
                ", strategyId='" + strategyId + '\'' +
                ", getText='" + text + '\'' +
                ", ordStatus=" + ordStatus +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order<?> order = (Order<?>) o;
        return Objects.equal(orderId, order.orderId) &&
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
                Objects.equal(strategyId, order.strategyId) &&
                Objects.equal(text, order.text) &&
                Objects.equal(executionReports, order.executionReports) &&
                Objects.equal(commissions, order.commissions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId, instId, dateTime, ordType, ordStatus, limitPrice, stopPrice, ordQty, filledQty, avgPrice, lastQty, lastPrice, stopLimitReady, trailingStopExecPrice, tif, side, execProviderId, portfolioId, strategyId, text, executionReports, commissions, pnl, realizedPnl);
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


    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getInstId() {
        return instId;
    }

    public void setInstId(long instId) {
        this.instId = instId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public OrdType getOrdType() {
        return ordType;
    }

    public void setOrdType(OrdType ordType) {
        this.ordType = ordType;
    }

    public OrdStatus getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(OrdStatus ordStatus) {
        this.ordStatus = ordStatus;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public double getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
    }

    public double getOrdQty() {
        return ordQty;
    }

    public void setOrdQty(double ordQty) {
        this.ordQty = ordQty;
    }

    public double getFilledQty() {
        return filledQty;
    }

    public void setFilledQty(double filledQty) {
        this.filledQty = filledQty;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getLastQty() {
        return lastQty;
    }

    public void setLastQty(double lastQty) {
        this.lastQty = lastQty;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public boolean isStopLimitReady() {
        return stopLimitReady;
    }

    public void setStopLimitReady(boolean stopLimitReady) {
        this.stopLimitReady = stopLimitReady;
    }

    public double getTrailingStopExecPrice() {
        return trailingStopExecPrice;
    }

    public void setTrailingStopExecPrice(double trailingStopExecPrice) {
        this.trailingStopExecPrice = trailingStopExecPrice;
    }

    public TimeInForce getTif() {
        return tif;
    }

    public void setTif(TimeInForce tif) {
        this.tif = tif;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public String getExecProviderId() {
        return execProviderId;
    }

    public void setExecProviderId(String execProviderId) {
        this.execProviderId = execProviderId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ExecutionReport> getExecutionReports() {
        return executionReports;
    }

    public void setExecutionReports(List<ExecutionReport> executionReports) {
        this.executionReports = executionReports;
    }

    public List<Double> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<Double> commissions) {
        this.commissions = commissions;
    }

    public double getPnl() {
        return pnl;
    }

    public void setPnl(double pnl) {
        this.pnl = pnl;
    }

    public double getRealizedPnl() {
        return realizedPnl;
    }

    public void setRealizedPnl(double realizedPnl) {
        this.realizedPnl = realizedPnl;
    }
}
