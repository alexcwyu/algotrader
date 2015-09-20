package com.unisoft.algotrader.model.trading;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataHandler;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.Order;

import java.util.List;

/**
 * Created by alex on 5/25/15.
 */
public class Position implements MarketDataHandler {
    private long instId;
    private int portfolioId;

    private double marketPrice;
    private double factor;
    private double qtyBought;
    private double qtySold;
    private double qtySoldShort;

    private double margin;
    private double debt;

    private List<Order> orderList = Lists.newArrayList();
    private int fPnLTransactionIndex = -1;
    private double qtyLeft;

    protected Position(){}

    public Position(long instId, int portfolioId){
        this(instId, portfolioId, 0.0);
    }

    public Position(long instId, int portfolioId, double factor){
        this.instId = instId;
        this.portfolioId = portfolioId;
        this.factor = factor;
    }

    public double getAmount(){
        return qtyBought - qtySold - qtySoldShort;
    }

    public double getQty(){
        return Math.abs(getAmount());
    }

    public PositionSide getSide(){
        if (getAmount() >= 0){
            return PositionSide.Long;
        }
        return PositionSide.Short;
    }

    public int getSign(Order order){
        return order.amount() >0 ? 1: ( order.amount()<0 ? -1 : 0);
    }

    //current liquidation marketPrice to the base portfolio currency
    public double getPrice(){
        //TODO fix to use instrument pricer
        return marketPrice;
    }

    public double getFactor(){
        return factor;
    }

    public double getValue(){
        if (factor != 0.0){
            return getPrice() * getAmount() * factor;
        }
        return getPrice() * getAmount();
    }

    public double getLeverage(){
        if (margin == 0)
            return 0;
        return getValue() / margin;
    }

    public double getAbsValue(){
        return getPrice() * getQty();
    }

    public void add(Order order) {
        double pnl = 0;
        double realizedCost = 0;

        double amount = order.amount();
        this.marketPrice = order.lastPrice();

        int sign = getSign(order);

        if (orderList.size() == 0) {
            fPnLTransactionIndex = 0;
            qtyLeft = order.filledQty();
        } else {
            if ((getSide() == PositionSide.Long && sign < 0) ||
                    (getSide() == PositionSide.Short && sign > 0)) {
                int index = fPnLTransactionIndex + 1;
                double qty = order.filledQty();

                double totalFilled = 0;

                double qtyFilled = Math.min(qty, qtyLeft);

                totalFilled += qtyLeft;

                Order firstOrder = orderList.get(fPnLTransactionIndex);

                realizedCost += qtyFilled * (order.transactionCost() / order.filledQty()
                        + firstOrder.transactionCost() / firstOrder.filledQty());

                pnl += (order.avgPrice() - firstOrder.avgPrice()) * qtyFilled * -sign;

                while (qty > totalFilled && index < orderList.size()) {
                    Order nextOrder = orderList.get(index);

                    if (getSign(nextOrder) != sign) {

                        qtyFilled = Math.min(qty - totalFilled, nextOrder.filledQty());

                        realizedCost += qtyFilled * (order.transactionCost() / order.filledQty() + nextOrder.transactionCost() / nextOrder.filledQty());

                        pnl += (order.avgPrice() - nextOrder.avgPrice()) * qtyFilled * -sign;

                        totalFilled += nextOrder.filledQty();
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
        if (factor != 0)
            pnl *= factor;

        order.pnl(pnl - order.transactionCost());
        order.realizedPnl((pnl - realizedCost));

        switch (order.side) {
            case Buy:
            //case BuyMinus:
                qtyBought += order.filledQty();
                break;

            case Sell:
            //case SellPlus:
                qtySold += order.filledQty();
                break;

            case SellShort:
            //case SellShortExempt:
                qtySoldShort += order.filledQty();
                break;
            default:
                throw new UnsupportedOperationException("Transaction Side is not supported : " + order.side);
        }
        orderList.add(order);
    }

    public double getCashFlow() {
        double cashFlow = 0;
        for (Order order : orderList){
            cashFlow += order.cashFlow();
        }
        return cashFlow;
    }


    public double getNetCashFlow() {
        double netCashFlow = 0;
        for (Order order : orderList){
            netCashFlow += order.netCashFlow();
        }
        return netCashFlow;
    }

    public double getPnl(){
        return getAbsValue() + getCashFlow();
    }

    public double getNetPnl(){
        return getAbsValue() + getNetCashFlow();
    }

    public double getPnlPercent(){
        return getPnl() / orderList.get(0).value();
    }

    public double getNetPnlPercent(){
        return getNetPnl() / orderList.get(0).value();
    }

    public double getUnrealizedPnl(){
        if (getQty() == 0)
            return 0;

        double price = this.marketPrice;

        double pnl = 0;
        int sign = (getSide() == PositionSide.Long) ? -1 : 1;

        double qtyFilled = qtyLeft;

        pnl += (price - orderList.get(fPnLTransactionIndex).avgPrice()) - qtyFilled * -sign;

        int index = fPnLTransactionIndex +1;

        while (index < orderList.size()){

            Order order = orderList.get(index);

            pnl += (price - order.avgPrice()) * qtyFilled * -sign;

            index++;
        }
        if (factor != 0.0)
            pnl *= factor;

        return pnl;

    }

    @Override
    public void onBar(Bar bar) {
        this.marketPrice = bar.close;
    }

    @Override
    public void onQuote(Quote quote) {
        if (getSide() == PositionSide.Long && quote.bid > 0)
            this.marketPrice = quote.bid;
        else if (getSide() == PositionSide.Short && quote.ask > 0)
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
                Objects.equal(orderList, position.orderList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, portfolioId, marketPrice, qtyBought, qtySold, qtySoldShort, margin, debt, orderList, fPnLTransactionIndex, qtyLeft);
    }

    public double debt() {
        return debt;
    }

    public Position debt(double debt) {
        this.debt = debt;
        return this;
    }

    public double factor() {
        return factor;
    }

    public Position factor(double factor) {
        this.factor = factor;
        return this;
    }

    public int fPnLTransactionIndex() {
        return fPnLTransactionIndex;
    }

    public Position fPnLTransactionIndex(int fPnLTransactionIndex) {
        this.fPnLTransactionIndex = fPnLTransactionIndex;
        return this;
    }

    public long instId() {
        return instId;
    }

    public Position instId(long instId) {
        this.instId = instId;
        return this;
    }

    public double margin() {
        return margin;
    }

    public Position margin(double margin) {
        this.margin = margin;
        return this;
    }

    public double marketPrice() {
        return marketPrice;
    }

    public Position marketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
        return this;
    }

    public List<Order> orderList() {
        return orderList;
    }

    public Position orderList(List<Order> orderList) {
        this.orderList = orderList;
        return this;
    }

    public int portfolioId() {
        return portfolioId;
    }

    public Position portfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
        return this;
    }

    public double qtyBought() {
        return qtyBought;
    }

    public Position qtyBought(double qtyBought) {
        this.qtyBought = qtyBought;
        return this;
    }

    public double qtyLeft() {
        return qtyLeft;
    }

    public Position qtyLeft(double qtyLeft) {
        this.qtyLeft = qtyLeft;
        return this;
    }

    public double qtySold() {
        return qtySold;
    }

    public Position qtySold(double qtySold) {
        this.qtySold = qtySold;
        return this;
    }

    public double qtySoldShort() {
        return qtySoldShort;
    }

    public Position qtySoldShort(double qtySoldShort) {
        this.qtySoldShort = qtySoldShort;
        return this;
    }
}
