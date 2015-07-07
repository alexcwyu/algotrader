package com.unisoft.algotrader.model.trading;

import com.datastax.driver.mapping.annotations.Transient;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataHandler;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.refdata.InstrumentManager;
import org.msgpack.annotation.Ignore;

import java.util.List;

/**
 * Created by alex on 5/25/15.
 */
public class Position implements MarketDataHandler {
    private int instId;
    private String portfolioId;

    private double marketPrice;
    private double qtyBought;
    private double qtySold;
    private double qtySoldShort;

    private double margin;
    private double debt;

    private List<Order> orderList = Lists.newArrayList();
    private int fPnLTransactionIndex = -1;
    private double qtyLeft;

    @Ignore
    @Transient
    private Instrument instrument;

    protected Position(){}

    public Position(int instId, String portfolioId){
        this.instId = instId;
        this.portfolioId = portfolioId;
    }

    public double amount(){
        return qtyBought - qtySold - qtySoldShort;
    }

    public double qty(){
        return Math.abs(amount());
    }

    public PositionSide side(){
        if (amount() >= 0){
            return PositionSide.Long;
        }
        return PositionSide.Short;
    }

    public int sign(Order order){
        return order.amount() >0 ? 1: ( order.amount()<0 ? -1 : 0);
    }

    //current liquidation marketPrice to the base portfolio currency
    public double price(){
        //TODO fix to use instrument pricer
        return marketPrice;
    }

    private Instrument instrument() {
        if (instrument == null) {
            instrument = InstrumentManager.INSTANCE.get(instId);
        }
        return instrument;
    }

    public double getValue(){
        Instrument instrument =instrument();
        if (instrument() != null && instrument.getFactor() != 0){
            return price() * amount() * instrument.getFactor();
        }
        return price() * amount();
    }

    public double leverage(){
        if (margin == 0)
            return 0;
        return getValue() / margin;
    }

    public double value(){
        return price() * qty();
    }



    public void add(Order order) {
        double pnl = 0;
        double realizedCost = 0;

        double amount = order.amount();
        this.marketPrice = order.getLastPrice();

        int sign = sign(order);

        if (orderList.size() == 0) {
            fPnLTransactionIndex = 0;
            qtyLeft = order.getFilledQty();
        } else {
            if ((side() == PositionSide.Long && sign < 0) ||
                    (side() == PositionSide.Short && sign > 0)) {
                int index = fPnLTransactionIndex + 1;
                double qty = order.getFilledQty();

                double totalFilled = 0;

                double qtyFilled = Math.min(qty, qtyLeft);

                totalFilled += qtyLeft;

                Order firstOrder = orderList.get(fPnLTransactionIndex);

                realizedCost += qtyFilled * (order.transactionCost() / order.getFilledQty()
                        + firstOrder.transactionCost() / firstOrder.getFilledQty());

                pnl += (order.getAvgPrice() - firstOrder.getAvgPrice()) * qtyFilled * -sign;

                while (qty > totalFilled && index < orderList.size()) {
                    Order nextOrder = orderList.get(index);

                    if (sign(nextOrder) != sign) {

                        qtyFilled = Math.min(qty - totalFilled, nextOrder.getFilledQty());

                        realizedCost += qtyFilled * (order.transactionCost() / order.getFilledQty() + nextOrder.transactionCost() / nextOrder.getFilledQty());

                        pnl += (order.getAvgPrice() - nextOrder.getAvgPrice()) * qtyFilled * -sign;

                        totalFilled += nextOrder.getFilledQty();
                    }
                    index++;
                }

                qtyLeft = Math.abs(qty - totalFilled);

                if (qty == totalFilled && index == orderList.size() || qty > totalFilled)
                    fPnLTransactionIndex = orderList.size();
                else
                    fPnLTransactionIndex = index - 1;

            }

        }
        Instrument instrument =instrument();
        if (instrument.getFactor() != 0)
            pnl *= instrument.getFactor();

        order.setPnl(pnl - order.transactionCost());
        order.setRealizedPnl((pnl - realizedCost));

        switch (order.side) {
            case Buy:
            case BuyMinus:
                qtyBought += order.getFilledQty();
                break;

            case Sell:
            case SellPlus:
                qtySold += order.getFilledQty();
                break;

            case SellShort:
            case SellShortExempt:
                qtySoldShort += order.getFilledQty();
                break;
            default:
                throw new UnsupportedOperationException("Transaction Side is not supported : " + order.side);
        }
        orderList.add(order);
    }

    public double cashFlow() {
        double cashFlow = 0;
        for (Order order : orderList){
            cashFlow += order.cashFlow();
        }
        return cashFlow;
    }


    public double netCashFlow() {
        double netCashFlow = 0;
        for (Order order : orderList){
            netCashFlow += order.netCashFlow();
        }
        return netCashFlow;
    }

    public double pnl(){
        return value() + cashFlow();
    }

    public double netPnl(){
        return value() + netCashFlow();
    }

    public double pnlPercent(){
        return pnl() / orderList.get(0).value();
    }

    public double netPnlPercent(){
        return netPnl() / orderList.get(0).value();
    }

    public double unrealizedPnl(){
        if (qty() == 0)
            return 0;

        double price = this.marketPrice;

        double pnl = 0;
        int sign = (side() == PositionSide.Long) ? -1 : 1;

        double qtyFilled = qtyLeft;

        pnl += (price - orderList.get(fPnLTransactionIndex).getAvgPrice()) - qtyFilled * -sign;

        int index = fPnLTransactionIndex +1;

        while (index < orderList.size()){

            Order order = orderList.get(index);

            pnl += (price - order.getAvgPrice()) * qtyFilled * -sign;

            index++;
        }
        Instrument instrument =instrument();
        if (instrument.getFactor() != 0)
            pnl *= instrument.getFactor();

        return pnl;

    }

    @Override
    public void onBar(Bar bar) {
        this.marketPrice = bar.close;
    }

    @Override
    public void onQuote(Quote quote) {
        if (side() == PositionSide.Long && quote.bid > 0)
            this.marketPrice = quote.bid;
        else if (side() == PositionSide.Short && quote.ask > 0)
            this.marketPrice = quote.ask;
    }

    @Override
    public void onTrade(Trade trade) {
        this.marketPrice = trade.price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return Objects.equal(instId, position.instId) &&
                Objects.equal(marketPrice, position.marketPrice) &&
                Objects.equal(qtyBought, position.qtyBought) &&
                Objects.equal(qtySold, position.qtySold) &&
                Objects.equal(qtySoldShort, position.qtySoldShort) &&
                Objects.equal(margin, position.margin) &&
                Objects.equal(debt, position.debt) &&
                Objects.equal(fPnLTransactionIndex, position.fPnLTransactionIndex) &&
                Objects.equal(qtyLeft, position.qtyLeft) &&
                Objects.equal(portfolioId, position.portfolioId) &&
                Objects.equal(orderList, position.orderList) &&
                Objects.equal(instrument, position.instrument);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, portfolioId, marketPrice, qtyBought, qtySold, qtySoldShort, margin, debt, orderList, fPnLTransactionIndex, qtyLeft, instrument);
    }

    public int getInstId() {
        return instId;
    }

    public void setInstId(int instId) {
        this.instId = instId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getQtyBought() {
        return qtyBought;
    }

    public void setQtyBought(double qtyBought) {
        this.qtyBought = qtyBought;
    }

    public double getQtySold() {
        return qtySold;
    }

    public void setQtySold(double qtySold) {
        this.qtySold = qtySold;
    }

    public double getQtySoldShort() {
        return qtySoldShort;
    }

    public void setQtySoldShort(double qtySoldShort) {
        this.qtySoldShort = qtySoldShort;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public int getfPnLTransactionIndex() {
        return fPnLTransactionIndex;
    }

    public void setfPnLTransactionIndex(int fPnLTransactionIndex) {
        this.fPnLTransactionIndex = fPnLTransactionIndex;
    }

    public double getQtyLeft() {
        return qtyLeft;
    }

    public void setQtyLeft(double qtyLeft) {
        this.qtyLeft = qtyLeft;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

}
