package com.unisoft.algotrader.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.*;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.ExecutionHandler;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.event.execution.OrderHandler;
import com.unisoft.algotrader.threading.AbstractEventProcessor;
import com.unisoft.algotrader.threading.YieldMultiBufferWaitStrategy;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 5/23/15.
 */
public class Portfolio extends AbstractEventProcessor implements MarketDataHandler, OrderHandler, ExecutionHandler {

    private Account account;

    public final String portfolioId;

    public final Performance performance;

    private final Map<String, Position> positions = Maps.newHashMap();

    private final List<Order> orderList = Lists.newArrayList();

    public Portfolio(String portfolioId, RingBuffer... providers){
        this(portfolioId, new Account("test"), providers);
    }

    public Portfolio(String portfolioId, Account account, RingBuffer... providers){
        super(new YieldMultiBufferWaitStrategy(),  null, providers);
        this.account = account;
        this.portfolioId = portfolioId;
        this.performance = new Performance(this, Clock.CLOCK);

        PortfolioManager.INSTANCE.add(this);
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public void onBar(Bar bar) {
        if (positions.containsKey(bar.instId)){
            positions.get(bar.instId).onBar(bar);
        }
    }

    @Override
    public void onQuote(Quote quote) {
        if (positions.containsKey(quote.instId)){
            positions.get(quote.instId).onQuote(quote);
        }
    }

    @Override
    public void onTrade(Trade trade) {
        if (positions.containsKey(trade.instId)){
            positions.get(trade.instId).onTrade(trade);
        }
    }

    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
    }

    @Override
    public void onOrder(Order order) {
        add(order);
    }

    public void add(Order order){

        Position position = positions.get(order.instId);

        boolean positionOpened = false;
        boolean positionChanged = false;
        boolean positionClosed = false;


        double openMargin = 0;
        double closeMargin = 0;
        double openDebt = 0;
        double closeDebt = 0;

        double orderMargin = order.margin();
        double orderDebt = order.debt();

        if (position == null){
            // open position

            Instrument instrument = InstrumentManager.INSTANCE.get(order.instId);
            position = new Position(instrument, this);
            position.add(order);

            positions.put(order.instId, position);

            // TODO handle margin
            if (orderMargin != 0)
            {
                closeMargin = 0;
                openMargin  = orderMargin;

                closeDebt = 0;
                openDebt  = orderDebt;

                position.margin = orderMargin;
                position.debt   = orderDebt;
            }

            positionOpened = true;
        }
        else{

            // TODO handle margin
            // add to open position
            if(orderMargin != 0){
                if ((position.side() == PositionSide.Long && order.side == Side.Buy)||
                        (position.side() == PositionSide.Short && (order.side == Side.Sell || order.side == Side.SellShort))){
                    closeMargin = 0;
                    openMargin  = orderMargin;

                    closeDebt = 0;
                    openDebt  = orderDebt;

                    position.margin += orderMargin;
                    position.debt   += orderDebt;
                }


                // close or close / open position
                if ((position.side() == PositionSide.Short && order.side == Side.Buy)||
                        (position.side() == PositionSide.Long && (order.side == Side.Sell || order.side == Side.SellShort))){
                    if(position.qty() == order.filledQty){
                        //fully close
                        closeMargin = position.margin;
                        openMargin = 0;

                        closeDebt = position.debt;
                        openDebt = 0;

                        position.margin = 0;
                        position.debt = 0;
                    }
                    else if (position.qty() > order.filledQty){
                        // partially close
                        closeMargin = orderMargin;
                        openMargin = 0;

                        closeDebt = position.debt * order.filledQty / position.qty();
                        openDebt = 0;

                        position.margin -= orderMargin;
                        position.debt -= closeDebt;
                    }
                    else {
                        // close and open
                        double qty = order.filledQty - position.qty();
                        double value = qty * order.avgPx;

                        Instrument instrument = InstrumentManager.INSTANCE.get(order.instId);
                        if (instrument.factor != 0)
                            value *= instrument.factor;

                        closeMargin = position.margin;
                        openMargin = instrument.margin * qty;

                        closeDebt = position.debt;
                        openDebt = value - openMargin;

                        position.margin = openMargin;
                        position.debt = openDebt;
                    }
                }
            }

            position.add(order);

            if (position.qty() == 0){

                //close position
                positions.remove(order.instId);

                positionClosed = true;
            }
        }

        orderList.add(order);

        Currency currency = CurrencyManager.INSTANCE.get(InstrumentManager.INSTANCE.get(order.instId).ccyId);
        AccountTransaction accountTransaction = new AccountTransaction(order.dateTime,
                currency, order.cashFlow() + openDebt - closeDebt, order.text);
        accountTransaction.orderId = order.orderId;
        //accountTransaction.transcationId = transaction.id;
        account.add(accountTransaction);
        performance.valueChanged();

    }

//    public void add(Transaction transaction){
//
//        Position position = positions.get(transaction.instId);
//
//        boolean positionOpened = false;
//        boolean positionChanged = false;
//        boolean positionClosed = false;
//
//
//        double openMargin = 0;
//        double closeMargin = 0;
//        double openDebt = 0;
//        double closeDebt = 0;
//
//        if (position == null){
//            // open position
//
//            Instrument instrument = InstrumentManager.INSTANCE.get(transaction.instId);
//            position = new Position(instrument, this);
//            position.addTranscation(transaction);
//
//            positions.put(transaction.instId, position);
//
//            // TODO handle margin
//            if (transaction.margin() != 0)
//            {
//                closeMargin = 0;
//                openMargin  = transaction.margin();
//
//                closeDebt = 0;
//                openDebt  = transaction.debt();
//
//                position.margin = transaction.margin();
//                position.debt   = transaction.debt();
//            }
//
//            positionOpened = true;
//        }
//        else{
//
//            // TODO handle margin
//            // add to open position
//            if(transaction.margin() != 0){
//                if ((position.side() == PositionSide.Long && transaction.side == Side.Buy)||
//                        (position.side() == PositionSide.Short && (transaction.side == Side.Sell || transaction.side == Side.SellShort))){
//                    closeMargin = 0;
//                    openMargin  = transaction.margin();
//
//                    closeDebt = 0;
//                    openDebt  = transaction.debt();
//
//                    position.margin += transaction.margin();
//                    position.debt   += transaction.debt();
//                }
//
//
//                // close or close / open position
//                if ((position.side() == PositionSide.Short && transaction.side == Side.Buy)||
//                        (position.side() == PositionSide.Long && (transaction.side == Side.Sell || transaction.side == Side.SellShort))){
//                    if(position.qty() == transaction.qty){
//                        //fully close
//                        closeMargin = position.margin;
//                        openMargin = 0;
//
//                        closeDebt = position.debt;
//                        openDebt = 0;
//
//                        position.margin = 0;
//                        position.debt = 0;
//                    }
//                    else if (position.qty() > transaction.qty){
//                            // partially close
//                            closeMargin = transaction.margin();
//                            openMargin = 0;
//
//                            closeDebt = position.debt * transaction.qty / position.qty();
//                            openDebt = 0;
//
//                            position.margin -= transaction.margin();
//                            position.debt -= closeDebt;
//                    }
//                    else {
//                        // close and open
//                        double qty = transaction.qty - position.qty();
//                        double value = qty * transaction.marketPrice;
//
//                        Instrument instrument = InstrumentManager.INSTANCE.get(transaction.instId);
//                        if (instrument.factor != 0)
//                            value *= instrument.factor;
//
//                        closeMargin = position.margin;
//                        openMargin = instrument.margin * qty;
//
//                        closeDebt = position.debt;
//                        openDebt = value - openMargin;
//
//                        position.margin = openMargin;
//                        position.debt = openDebt;
//                    }
//                }
//            }
//
//            position.addTranscation(transaction);
//
//            if (position.qty() == 0){
//
//                //close position
//                //positions.remove(transaction.instId);
//
//                //positionClosed = true;
//            }
//        }
//
//        transactionList.add(transaction);
//
//        AccountTransaction accountTransaction = new AccountTransaction(transaction.dataTime,
//                transaction.currency, transaction.cashFlow() + openDebt - closeDebt, transaction.comment);
//        accountTransaction.orderId = transaction.orderId;
//        accountTransaction.transcationId = transaction.id;
//        account.add(accountTransaction);
//    }


    public double positionValue(){
        return positions.values().stream().mapToDouble(position -> position.getValue()).sum();
    }


    public double accountValue(){
        return account.value();
    }

    public double value(){
        return accountValue() + positionValue();
    }

    public double marginValue(){
        return positions.values().stream().mapToDouble(position -> position.margin).sum();
    }
    public double debtValue(){
        return positions.values().stream().mapToDouble(position -> position.debt).sum();
    }

    public double coreEquity(){
        return accountValue();
    }

    public double totalEquity(){
        return value() - debtValue();
    }

    public double leverage(){
        double margin = marginValue();

        if (margin == 0)
            return 0;
        else
            return value() / margin;
    }

    public double debtEquityRatio()
    {
        double equity = totalEquity();

        if (equity == 0)
            return 0;
        else
            return debtValue() / equity;
    }

    public double cashFlow(){
        return positions.values().stream().mapToDouble(position -> position.cashFlow()).sum();
    }

    public double netCashFlow(){
        return positions.values().stream().mapToDouble(position -> position.netCashFlow()).sum();
    }

}
