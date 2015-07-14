package com.unisoft.algotrader.trading;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataHandler;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.ExecutionHandler;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.event.execution.OrderHandler;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.refdata.InstrumentManager;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 7/1/15.
 */
public class PortfolioProcessor extends MultiEventProcessor implements MarketDataHandler, OrderHandler, ExecutionHandler {

    private static final Logger LOG = LogManager.getLogger(Portfolio.class);

    private final Portfolio portfolio;
    private final Account account;
    private final RefDataStore refDataStore;
    private final Clock clock;

    public PortfolioProcessor(Portfolio portfolio, Account account, RefDataStore refDataStore, Clock clock, RingBuffer... providers) {
        super(new NoWaitStrategy(), null, providers);
        this.portfolio = portfolio;
        this.account = account;
        this.refDataStore = refDataStore;
        this.clock = clock;
    }


    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public void onBar(Bar bar) {
        if (portfolio.getPositions().containsKey(bar.instId)){
            portfolio.getPositions().get(bar.instId).onBar(bar);
        }
    }

    @Override
    public void onQuote(Quote quote) {
        if (portfolio.getPositions().containsKey(quote.instId)){
            portfolio.getPositions().get(quote.instId).onQuote(quote);
        }
    }

    @Override
    public void onTrade(Trade trade) {
        if (portfolio.getPositions().containsKey(trade.instId)){
            portfolio.getPositions().get(trade.instId).onTrade(trade);
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

        double newDebt = addOrderToPosition(order);

        Currency currency = refDataStore.getCurrency(InstrumentManager.INSTANCE.get(order.getInstId()).getCcyId());
        AccountTransaction accountTransaction = new AccountTransaction(order.getOrderId(), order.getDateTime(),
                currency, order.cashFlow() + newDebt, order.getText());
        account.add(accountTransaction);

        portfolio.getPerformance().valueChanged(clock.now(), coreEquity(), totalEquity());

    }

    public void updatePerformance(long dateTime){
        portfolio.getPerformance().valueChanged(dateTime, coreEquity(), totalEquity());
    }

    public void updatePerformance(){
        updatePerformance(clock.now());
    }

    public void reconstruct(){
        for(Order order : portfolio.getOrderList()){
            addOrderToPosition(order);
        }
    }


    protected double calcOrderMargin(Order order, Instrument instrument){
        double margin = instrument.getMargin() * order.getFilledQty();
        return margin;
    }

    protected double calcOrderDebt(Order order, Instrument instrument){
        double margin = calcOrderMargin(order, instrument);
        if (margin == 0){
            return 0;
        }
        return order.value()-margin;
    }

    private double addOrderToPosition(Order order){
        Position position = portfolio.getPosition(order.getInstId());

        boolean positionOpened = false;
        boolean positionChanged = false;
        boolean positionClosed = false;


        double openMargin = 0;
        double closeMargin = 0;
        double openDebt = 0;
        double closeDebt = 0;

        Instrument instrument = InstrumentManager.INSTANCE.get(order.getInstId());

        double orderMargin = calcOrderMargin(order, instrument);
        double orderDebt = calcOrderDebt(order, instrument);

        if (position == null){
            // open position
            position = new Position(order.getInstId(), portfolio.getPortfolioId(), instrument.getFactor());
            position.add(order);

            position = portfolio.addPosition(order.getInstId(), position);

            // TODO handle margin
            if (orderMargin != 0)
            {
                closeMargin = 0;
                openMargin  = orderMargin;

                closeDebt = 0;
                openDebt  = orderDebt;

                position.setMargin(orderMargin);
                position.setDebt(orderDebt);
            }

            positionOpened = true;
        }
        else{

            // TODO handle margin
            // add to open position
            if(orderMargin != 0){
                if ((position.getSide() == PositionSide.Long && order.side == Side.Buy)||
                        (position.getSide() == PositionSide.Short && (order.side == Side.Sell || order.side == Side.SellShort))){
                    closeMargin = 0;
                    openMargin  = orderMargin;

                    closeDebt = 0;
                    openDebt  = orderDebt;

                    position.setMargin(position.getMargin() + orderMargin);
                    position.setDebt(position.getDebt() + closeDebt);
                }


                // close or close / open position
                if ((position.getSide() == PositionSide.Short && order.side == Side.Buy)||
                        (position.getSide() == PositionSide.Long && (order.side == Side.Sell || order.side == Side.SellShort))){
                    if(position.getQty() == order.getFilledQty()){
                        //fully close
                        closeMargin = position.getMargin();
                        openMargin = 0;

                        closeDebt = position.getDebt();
                        openDebt = 0;

                        position.setMargin(0);
                        position.setDebt(0);
                    }
                    else if (position.getQty() > order.getFilledQty()){
                        // partially close
                        closeMargin = orderMargin;
                        openMargin = 0;

                        closeDebt = position.getDebt() * order.getFilledQty() / position.getQty();
                        openDebt = 0;

                        position.setMargin(position.getMargin() - orderMargin);
                        position.setDebt(position.getDebt() - closeDebt);
                    }
                    else {
                        // close and open
                        double qty = order.getFilledQty() - position.getQty();
                        double value = qty * order.getAvgPrice();

                        if (instrument.getFactor() != 0)
                            value *= instrument.getFactor();

                        closeMargin = position.getMargin();
                        openMargin = instrument.getMargin() * qty;

                        closeDebt = position.getDebt();
                        openDebt = value - openMargin;

                        position.setMargin(openMargin);
                        position.setDebt(openDebt);
                    }
                }
            }

            position.add(order);

            if (position.getQty() == 0){

                //close position
                portfolio.removePosition(order.getInstId());

                positionClosed = true;
            }
        }

        portfolio.addOrder(order);

        return openDebt - closeDebt;
    }

    public double positionValue(){

        return portfolio.getPositions().values().stream().mapToDouble(position -> position.getValue()).sum();
    }


    public double accountValue(){
        double val = 0;

        for(AccountPosition position : account.getAccountPositions().values()) {
            val += CurrencyConverter.convert(position.getValue(), refDataStore.getCurrency(position.getCcyId()), refDataStore.getCurrency(account.getCcyId()));
        }
        return val;
    }

    public double value(){
        return accountValue() + positionValue();
    }

    public double marginValue(){
        return portfolio.getPositions().values().stream().mapToDouble(position -> position.getMargin()).sum();
    }
    public double debtValue(){
        return portfolio.getPositions().values().stream().mapToDouble(position -> position.getDebt()).sum();
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
        return portfolio.getPositions().values().stream().mapToDouble(position -> position.getCashFlow()).sum();
    }

    public double netCashFlow(){
        return portfolio.getPositions().values().stream().mapToDouble(position -> position.getNetCashFlow()).sum();
    }

}
