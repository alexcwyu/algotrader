package com.unisoft.algotrader.event.execution;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.core.*;
import com.unisoft.algotrader.event.Event;

import java.util.List;

/**
 * Created by alex on 5/17/15.
 */
public class Order<E extends Order<? super E>> implements Event<OrderHandler, E>  {

    public long orderId;
    public String instId;
    public long dateTime;

    public OrdType ordType;
    public double limitPrice;
    public double stopPx;
    public double ordQty;

    public double filledQty = 0;
    public double avgPx;

    public double lastQty;
    public double lastPrice;

    public boolean stopLimitReady = false;
    public double trailingStopExecPrice;

    public TimeInForce tif;
    public Side side;

    public String execProviderId;
    public String portfolioId;
    public String strategyId;

    public String text;

    public OrdStatus ordStatus = OrdStatus.New;

    public List<ExecutionReport> executionReports = Lists.newArrayList();

    public List<Commission> commissions = Lists.newArrayList();
    public double pnl;
    public double realizedPnl;


    public void add(ExecutionReport report){
        filledQty = report.filledQty;
        avgPx = report.avgPx;
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
                "orderId=" + orderId +
                ", instId='" + instId + '\'' +
                ", dateTime=" + dateTime +
                ", ordType=" + ordType +
                ", limitPrice=" + limitPrice +
                ", ordQty=" + ordQty +
                ", filledQty=" + filledQty +
                ", stopPx=" + stopPx +
                ", avgPx=" + avgPx +
                ", stopLimitReady=" + stopLimitReady +
                ", trailingStopExecPrice=" + trailingStopExecPrice +
                ", tif=" + tif +
                ", side=" + side +
                ", execProviderId='" + execProviderId + '\'' +
                ", portfolioId='" + portfolioId + '\'' +
                ", strategyId='" + strategyId + '\'' +
                ", text='" + text + '\'' +
                ", ordStatus=" + ordStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order<?> order = (Order<?>) o;
        return Objects.equal(orderId, order.orderId) &&
                Objects.equal(dateTime, order.dateTime) &&
                Objects.equal(limitPrice, order.limitPrice) &&
                Objects.equal(ordQty, order.ordQty) &&
                Objects.equal(filledQty, order.filledQty) &&
                Objects.equal(stopPx, order.stopPx) &&
                Objects.equal(avgPx, order.avgPx) &&
                Objects.equal(stopLimitReady, order.stopLimitReady) &&
                Objects.equal(trailingStopExecPrice, order.trailingStopExecPrice) &&
                Objects.equal(instId, order.instId) &&
                Objects.equal(ordType, order.ordType) &&
                Objects.equal(tif, order.tif) &&
                Objects.equal(side, order.side) &&
                Objects.equal(execProviderId, order.execProviderId) &&
                Objects.equal(portfolioId, order.portfolioId) &&
                Objects.equal(strategyId, order.strategyId) &&
                Objects.equal(text, order.text) &&
                Objects.equal(ordStatus, order.ordStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId, instId, dateTime, ordType, limitPrice, ordQty, filledQty, stopPx, avgPx, stopLimitReady, trailingStopExecPrice, tif, side, execProviderId, portfolioId, strategyId, text, ordStatus);
    }


    public static final EventFactory<Order> FACTORY = new EventFactory(){
        @Override
        public Order newInstance() {
            return new Order();
        }
    };

    @Override
    public void on(OrderHandler handler) {
        handler.onOrder(this);
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
            case BuyMinus:
                return filledQty;
            case Sell:
            case SellPlus:
            case SellShort:
            case SellShortExempt:
                return -filledQty;
            default:
                throw new UnsupportedOperationException("");
        }
    }

    public double value(){
        return avgPx * amount();
    }


    public double transactionCost(){
        double cost = 0.0;
        for (Commission commission : commissions){
            cost += commission.apply(this);
        }
        return cost;
    }


    public double netCashFlow(){
        return -value();
    }


    public double cashFlow(){
        return netCashFlow() - transactionCost();
    }
    public double margin(){
        double margin = InstrumentManager.INSTANCE.get(instId).margin * filledQty;
        return margin;
    }

    public double debt(){
        double margin = margin();
        if (margin == 0){
            return 0;
        }
        return value()-margin;
    }

}
