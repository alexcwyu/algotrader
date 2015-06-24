package com.unisoft.algotrader.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataHandler;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.ExecutionHandler;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.event.execution.OrderHandler;
import com.unisoft.algotrader.threading.MultiEventProcessor;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 5/23/15.
 */
public class Portfolio extends MultiEventProcessor implements MarketDataHandler, OrderHandler, ExecutionHandler {

    private static final Logger LOG = LogManager.getLogger(Portfolio.class);
    private Account account;

    public final String portfolioId;

    public final Performance performance;

    private final Map<Integer, Position> positions = Maps.newHashMap();

    private final List<Order> orderList = Lists.newArrayList();

    public Portfolio(String portfolioId, RingBuffer... providers){
        this(portfolioId, new Account("test"), providers);
    }

    public Portfolio(String portfolioId, Account account, RingBuffer... providers){
        super(new NoWaitStrategy(),  null, providers);
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
        logStatus();
    }

    @Override
    public void onQuote(Quote quote) {
        if (positions.containsKey(quote.instId)){
            positions.get(quote.instId).onQuote(quote);
        }
        logStatus();
    }

    @Override
    public void onTrade(Trade trade) {
        if (positions.containsKey(trade.instId)){
            positions.get(trade.instId).onTrade(trade);
        }
        logStatus();
    }


    private void logStatus(){
        LOG.info("positionValue={}, value={}, cashFlow={}, netCashFlow={}, totalEquity={}",
                positionValue(), value(), cashFlow(), netCashFlow(), totalEquity());
    }
    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
    }

    @Override
    public void onOrder(Order order) {
        add(order);
        logStatus();
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
        //accountTransaction.transcationId = transaction.instId;
        account.add(accountTransaction);
        performance.valueChanged();

    }

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
