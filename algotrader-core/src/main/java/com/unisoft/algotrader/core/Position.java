package com.unisoft.algotrader.core;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataHandler;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.Order;

import java.util.List;

/**
 * Created by alex on 5/25/15.
 */
public class Position implements MarketDataHandler {

    public final Instrument instrument;
    public final Portfolio portfolio;

    public double marketPrice;
    public double qtyBought;
    public double qtySold;
    public double qtySoldShort;

    public double margin;
    public double debt;

    private List<Order> orderList = Lists.newArrayList();
    private int fPnLTransactionIndex = -1;
    private double qtyLeft;

    public Position(Instrument instrument, Portfolio portfolio){
        this.instrument = instrument;
        this.portfolio = portfolio;
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

    public double getValue(){
        if (instrument != null && instrument.factor != 0){
            return price() * amount() * instrument.factor;
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
        this.marketPrice = order.lastPrice;

        int sign = sign(order);

        if (orderList.size() == 0) {
            fPnLTransactionIndex = 0;
            qtyLeft = order.filledQty;
        } else {
            if ((side() == PositionSide.Long && sign < 0) ||
                    (side() == PositionSide.Short && sign > 0)) {
                int index = fPnLTransactionIndex + 1;
                double qty = order.filledQty;

                double totalFilled = 0;

                double qtyFilled = Math.min(qty, qtyLeft);

                totalFilled += qtyLeft;

                Order firstOrder = orderList.get(fPnLTransactionIndex);

                realizedCost += qtyFilled * (order.transactionCost() / order.filledQty
                        + firstOrder.transactionCost() / firstOrder.filledQty);

                pnl += (order.avgPx - firstOrder.avgPx) * qtyFilled * -sign;

                while (qty > totalFilled && index < orderList.size()) {
                    Order nextOrder = orderList.get(index);

                    if (sign(nextOrder) != sign) {

                        qtyFilled = Math.min(qty - totalFilled, nextOrder.filledQty);

                        realizedCost += qtyFilled * (order.transactionCost() / order.filledQty + nextOrder.transactionCost() / nextOrder.filledQty);

                        pnl += (order.avgPx - nextOrder.avgPx) * qtyFilled * -sign;

                        totalFilled += nextOrder.filledQty;
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
        if (instrument.factor != 0)
            pnl *= instrument.factor;

        order.pnl = pnl - order.transactionCost();
        order.realizedPnl = (pnl - realizedCost);

        switch (order.side) {
            case Buy:
            case BuyMinus:
                qtyBought += order.filledQty;
                break;

            case Sell:
            case SellPlus:
                qtySold += order.filledQty;
                break;

            case SellShort:
            case SellShortExempt:
                qtySoldShort += order.filledQty;
                break;
            default:
                throw new UnsupportedOperationException("Transaction Side is not supported : " + order.side);
        }
        orderList.add(order);
    }

//    public void addTranscation(Transaction transaction) {
//        double pnl = 0;
//        double realizedCost = 0;
//
//        double amount = transaction.getAmount();
//
//        int sign = sign(transaction);
//
//        if (transactionList.size() == 0) {
//            fPnLTransactionIndex = 0;
//            qtyLeft = transaction.qty;
//        } else {
//            if ((side() == PositionSide.Long && sign < 0) ||
//                    (side() == PositionSide.Short && sign > 0)) {
//                int index = fPnLTransactionIndex + 1;
//                double qty = transaction.qty;
//
//                double totalFilled = 0;
//
//                double qtyFilled = Math.min(qty, qtyLeft);
//
//                totalFilled += qtyLeft;
//
//                Transaction firstTranscation = transactionList.get(fPnLTransactionIndex);
//
//                realizedCost += qtyFilled * (transaction.transactionCost() / transaction.qty
//                        + firstTranscation.transactionCost() / firstTranscation.qty);
//
//                pnl += (transaction.marketPrice - firstTranscation.marketPrice) * qtyFilled * -sign;
//
//                while (qty > totalFilled && index < transactionList.size()) {
//                    Transaction nextTransaction = transactionList.get(index);
//
//                    if (sign(nextTransaction) != sign) {
//
//                        qtyFilled = Math.min(qty - totalFilled, nextTransaction.qty);
//
//                        realizedCost += qtyFilled * (transaction.transactionCost() / transaction.qty + nextTransaction.transactionCost() / nextTransaction.qty);
//
//                        pnl += (transaction.marketPrice - nextTransaction.marketPrice) * qtyFilled * -sign;
//
//                        totalFilled += nextTransaction.qty;
//                    }
//                    index++;
//                }
//
//                qtyLeft = Math.abs(qty - totalFilled);
//
//                if (qty == totalFilled && index == transactionList.size() || qty > totalFilled)
//                    fPnLTransactionIndex = transactionList.size();
//                else
//                    fPnLTransactionIndex = index - 1;
//
//            }
//
//        }
//        if (instrument.factor != 0)
//            pnl *= instrument.factor;
//
//        transaction.pnl = pnl - transaction.transactionCost();
//        transaction.realizedPnl = (pnl - realizedCost);
//
//        switch (transaction.side) {
//            case Buy:
//            case BuyMinus:
//                qtyBought += transaction.qty;
//                break;
//
//            case Sell:
//            case SellPlus:
//                qtySold += transaction.qty;
//                break;
//
//            case SellShort:
//            case SellShortExempt:
//                qtySoldShort += transaction.qty;
//                break;
//            default:
//                throw new UnsupportedOperationException("Transaction Side is not supported : " + transaction.side);
//        }
//        transactionList.add(transaction);
//    }

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

        pnl += (price - orderList.get(fPnLTransactionIndex).avgPx) - qtyFilled * -sign;

        int index = fPnLTransactionIndex +1;

        while (index < orderList.size()){

            Order order = orderList.get(index);

            pnl += (price - order.avgPx) * qtyFilled * -sign;

            index++;
        }
        if (instrument.factor != 0)
            pnl *= instrument.factor;

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
}
