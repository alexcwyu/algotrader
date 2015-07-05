package com.unisoft.algotrader.core;

import com.datastax.driver.mapping.annotations.*;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.event.execution.Order;
import org.msgpack.annotation.Ignore;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 5/23/15.
 */
@Table(keyspace = "trading", name = "portfolios")
public class Portfolio{

    @PartitionKey
    @Column(name ="portfolio_id")
    private String portfolioId;

    @Column(name ="account_name")
    private String accountName;

    @Frozen
    private Performance performance;

    @Ignore
    @Transient
    private Map<Integer, Position> positions = Maps.newHashMap();

    @Ignore
    @Transient
    private List<Order> orderList = Lists.newArrayList();

    @Ignore
    @Transient
    private Account account;

    protected Portfolio(){
    }

    public Portfolio(String portfolioId){
        this(portfolioId, AccountManager.DEFAULT_ACCOUNT.getName());
    }

    public Portfolio(String portfolioId, String accountName){
        this.accountName = accountName;
        this.portfolioId = portfolioId;
        this.performance = new Performance(this, Clock.CLOCK);
    }

    public Performance getPerformance(){
        return performance;
    }

    public Map<Integer, Position> getPositions(){
        return positions;
    }

    public List<Order> getOrderList(){
        return orderList;
    }

    private Account getAccount(){
        if (account == null){
            account = AccountManager.INSTANCE.get(accountName);
        }
        return account;
    }

    public void add(Order order){

        double newDebt = addOrderToPosition(order);

        Currency currency = CurrencyManager.INSTANCE.get(InstrumentManager.INSTANCE.get(order.instId).ccyId);
        AccountTransaction accountTransaction = new AccountTransaction(order.orderId, order.dateTime,
                currency, order.cashFlow() + newDebt, order.text);
        getAccount().add(accountTransaction);
        performance.valueChanged();

    }

    public void reconstruct(){
        for(Order order : orderList){
            addOrderToPosition(order);
        }
    }

    private double addOrderToPosition(Order order){
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
            position = new Position(order.instId, portfolioId);
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
                position.debt = orderDebt;
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
                        double value = qty * order.avgPrice;

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

        return openDebt - closeDebt;
    }

    public double positionValue(){
        return positions.values().stream().mapToDouble(position -> position.getValue()).sum();
    }


    public double accountValue(){
        return getAccount().value();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Portfolio)) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equal(portfolioId, portfolio.portfolioId) &&
                Objects.equal(accountName, portfolio.accountName) &&
                Objects.equal(performance, portfolio.performance) &&
                Objects.equal(positions, portfolio.positions) &&
                Objects.equal(orderList, portfolio.orderList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(portfolioId, accountName, performance, positions, orderList);
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }


    public void setPositions(Map<Integer, Position> positions) {
        this.positions = positions;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
