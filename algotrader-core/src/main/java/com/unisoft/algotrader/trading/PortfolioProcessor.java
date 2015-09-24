package com.unisoft.algotrader.trading;

import com.unisoft.algotrader.config.AppConfig;
import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataHandler;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.ExecutionEventHandler;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.event.execution.OrderEventHandler;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.persistence.RefDataStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 7/1/15.
 */
public class PortfolioProcessor implements MarketDataHandler, OrderEventHandler, ExecutionEventHandler {

    private static final Logger LOG = LogManager.getLogger(Portfolio.class);

    private final Portfolio portfolio;
    private final Account account;
    private final RefDataStore refDataStore;
    private final Clock clock;

    public PortfolioProcessor(AppConfig appConfig, Portfolio portfolio, Account account){
        this(portfolio, account, appConfig.getRefDataStore(), appConfig.getClock());
    }

    public PortfolioProcessor(Portfolio portfolio, Account account, RefDataStore refDataStore, Clock clock) {
        this.portfolio = portfolio;
        this.account = account;
        this.refDataStore = refDataStore;
        this.clock = clock;
    }


    public int portfolioId(){
        return portfolio.portfolioId();
    }

    public Portfolio portfolio(){
        return portfolio;
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public void onBar(Bar bar) {
        if (portfolio.positions().containsKey(bar.instId)){
            portfolio.positions().get(bar.instId).onBar(bar);
        }
    }

    @Override
    public void onQuote(Quote quote) {
        if (portfolio.positions().containsKey(quote.instId)){
            portfolio.positions().get(quote.instId).onQuote(quote);
        }
    }

    @Override
    public void onTrade(Trade trade) {
        if (portfolio.positions().containsKey(trade.instId)){
            portfolio.positions().get(trade.instId).onTrade(trade);
        }
    }


    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
    }

    @Override
    public void onNewOrderRequest(Order order) {
        add(order);
    }
    @Override
    public void onOrderUpdateRequest(Order order) {
        //TODO
    }

    @Override
    public void onOrderCancelRequest(Order order) {
        //TODO
    }

    public void add(Order order){

        double newDebt = addOrderToPosition(order);

        Currency currency = refDataStore.getCurrency(refDataStore.getInstrument(order.instId()).getCcyId());
        AccountTransaction accountTransaction = new AccountTransaction(order.clOrderId(), order.dateTime(),
                currency, order.cashFlow() + newDebt, order.text());
        account.add(accountTransaction);

        portfolio.performance().valueChanged(clock.now(), coreEquity(), totalEquity());

    }

    public void updatePerformance(long dateTime){
        portfolio.performance().valueChanged(dateTime, coreEquity(), totalEquity());
    }

    public void updatePerformance(){
        updatePerformance(clock.now());
    }

    public void reconstruct(){
        for(Order order : portfolio.orderList()){
            addOrderToPosition(order);
        }
    }


    protected double calcOrderMargin(Order order, Instrument instrument){
        double margin = instrument.getMargin() * order.filledQty();
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
        Position position = portfolio.getPosition(order.instId());

        boolean positionOpened = false;
        boolean positionChanged = false;
        boolean positionClosed = false;


        double openMargin = 0;
        double closeMargin = 0;
        double openDebt = 0;
        double closeDebt = 0;

        Instrument instrument = refDataStore.getInstrument(order.instId());

        double orderMargin = calcOrderMargin(order, instrument);
        double orderDebt = calcOrderDebt(order, instrument);

        if (position == null){
            // open position
            position = new Position(order.instId(), portfolio.portfolioId(), instrument.getFactor());
            position.add(order);

            position = portfolio.addPosition(order.instId(), position);

            // TODO handle margin
            if (orderMargin != 0)
            {
                closeMargin = 0;
                openMargin  = orderMargin;

                closeDebt = 0;
                openDebt  = orderDebt;

                position.margin(orderMargin);
                position.debt(orderDebt);
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

                    position.margin(position.margin() + orderMargin);
                    position.debt(position.debt() + closeDebt);
                }


                // close or close / open position
                if ((position.getSide() == PositionSide.Short && order.side == Side.Buy)||
                        (position.getSide() == PositionSide.Long && (order.side == Side.Sell || order.side == Side.SellShort))){
                    if(position.getQty() == order.filledQty()){
                        //fully close
                        closeMargin = position.margin();
                        openMargin = 0;

                        closeDebt = position.debt();
                        openDebt = 0;

                        position.margin(0);
                        position.debt(0);
                    }
                    else if (position.getQty() > order.filledQty()){
                        // partially close
                        closeMargin = orderMargin;
                        openMargin = 0;

                        closeDebt = position.debt() * order.filledQty() / position.getQty();
                        openDebt = 0;

                        position.margin(position.margin() - orderMargin);
                        position.debt(position.debt() - closeDebt);
                    }
                    else {
                        // close and open
                        double qty = order.filledQty() - position.getQty();
                        double value = qty * order.avgPrice();

                        if (instrument.getFactor() != 0)
                            value *= instrument.getFactor();

                        closeMargin = position.margin();
                        openMargin = instrument.getMargin() * qty;

                        closeDebt = position.debt();
                        openDebt = value - openMargin;

                        position.margin(openMargin);
                        position.debt(openDebt);
                    }
                }
            }

            position.add(order);

            if (position.getQty() == 0){

                //close position
                portfolio.removePosition(order.instId());

                positionClosed = true;
            }
        }

        portfolio.addOrder(order);

        return openDebt - closeDebt;
    }

    public double positionValue(){

        return portfolio.positions().values().stream().mapToDouble(position -> position.getValue()).sum();
    }


    public double accountValue(){
        double val = 0;

        for(AccountPosition position : account.accountPositions().values()) {
            val += CurrencyConverter.convert(position.value(), refDataStore.getCurrency(position.ccyId()), refDataStore.getCurrency(account.ccyId()));
        }
        return val;
    }

    public double value(){
        return accountValue() + positionValue();
    }

    public double marginValue(){
        return portfolio.positions().values().stream().mapToDouble(position -> position.margin()).sum();
    }
    public double debtValue(){
        return portfolio.positions().values().stream().mapToDouble(position -> position.debt()).sum();
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
        return portfolio.positions().values().stream().mapToDouble(position -> position.getCashFlow()).sum();
    }

    public double netCashFlow(){
        return portfolio.positions().values().stream().mapToDouble(position -> position.getNetCashFlow()).sum();
    }

}
