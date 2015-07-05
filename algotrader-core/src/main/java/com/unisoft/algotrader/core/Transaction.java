package com.unisoft.algotrader.core;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;

import java.util.List;

/**
 * Created by alex on 5/25/15.
 */
@Deprecated
public class Transaction {

    public long id;
    public long dataTime;
    public int instId;
    public long orderId;
    public long execId;
    public String strategyId;

    public Side side;
    public double price;
    public double qty;
    public String comment;
    public Currency currency;

    public List<Double> commissions = Lists.newArrayList();

    public double pnl;
    public double realizedPnl;

    public Transaction(Order order){
        this.dataTime = order.dateTime;
        this.instId = order.instId;
        this.orderId = order.orderId;
        this.strategyId = order.strategyId;
        this.side = order.side;
        this.price = order.avgPrice;
        this.qty = order.filledQty;
        this.comment = order.text;

        this.currency = CurrencyManager.INSTANCE.get(InstrumentManager.INSTANCE.get(order.instId).ccyId);
    }

    public Transaction(ExecutionReport executionReport){
        this.dataTime = executionReport.transactionTime;
        this.instId = executionReport.instId;
        this.orderId = executionReport.orderId;
        this.execId = execId;

        this.side = executionReport.side;
        this.price = executionReport.avgPrice;
        this.qty = executionReport.filledQty;
        this.comment = executionReport.text;

        this.currency = CurrencyManager.INSTANCE.get(InstrumentManager.INSTANCE.get(executionReport.instId).ccyId);
    }

    public double getAmount(){
        switch (side){
            case Buy:
            case BuyMinus:
                return qty;
            case Sell:
            case SellPlus:
            case SellShort:
            case SellShortExempt:
                return -qty;
            default:
                throw new UnsupportedOperationException("");
        }
    }

    public double value(){
        return price * getAmount();
    }

    public double netCashFlow(){
        return -value();
    }


    public double transactionCost(){
        double cost = 0.0;
        for (double commission : commissions){
            cost += commission;
        }
        return cost;
    }

    public double cashFlow(){
        return netCashFlow() - transactionCost();
    }

    public double margin(){
        double margin = InstrumentManager.INSTANCE.get(instId).margin * qty;
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
