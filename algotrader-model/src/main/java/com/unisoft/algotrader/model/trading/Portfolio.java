package com.unisoft.algotrader.model.trading;

import com.datastax.driver.mapping.annotations.*;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.execution.Order;
import org.msgpack.annotation.Ignore;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 5/23/15.
 */
@Table(keyspace = "trading", name = "portfolios")
public class Portfolio{

    public static final int TEST_USD_PORTFOLIO_ID = -1;
    public static final int TEST_HKD_PORTFOLIO_ID = -2;
    public static final Portfolio TEST_USD_PORTFOLIO = new Portfolio(Portfolio.TEST_USD_PORTFOLIO_ID, Account.TEST_USD_ACCOUNT_ID);
    public static final Portfolio TEST_HKD_PORTFOLIO = new Portfolio(Portfolio.TEST_HKD_PORTFOLIO_ID, Account.TEST_HKD_ACCOUNT_ID);


    @PartitionKey
    @Column(name ="portfolio_id")
    private int portfolioId;

    @Column(name ="account_id")
    private String accountId;

    @Frozen
    private Performance performance;

    @Ignore
    @Transient
    private Map<Long, Position> positions = Maps.newHashMap();

    @Ignore
    @Transient
    private List<Order> orderList = Lists.newArrayList();

    public Portfolio(){
    }

    public Portfolio(int portfolioId, String accountId){
        this.accountId = accountId;
        this.portfolioId = portfolioId;
        this.performance = new Performance();
    }

    public String accountId() {
        return accountId;
    }

    public Portfolio accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public List<Order> orderList() {
        return orderList;
    }

    public Portfolio orderList(List<Order> orderList) {
        this.orderList = orderList;
        return this;
    }

    public Performance performance() {
        return performance;
    }

    public Portfolio performance(Performance performance) {
        this.performance = performance;
        return this;
    }

    public int portfolioId() {
        return portfolioId;
    }

    public Portfolio portfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
        return this;
    }

    public Map<Long, Position> positions() {
        return positions;
    }

    public Portfolio positions(Map<Long, Position> positions) {
        this.positions = positions;
        return this;
    }

    public Position getPosition(long instId){
        return positions.get(instId);
    }

    public Position addPosition(long instId, Position position){
        return positions.putIfAbsent(instId, position);
    }

    public Position removePosition(long instId){
        return positions.remove(instId);
    }

    public boolean addOrder(Order order){
        return this.orderList.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Portfolio)) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equal(portfolioId, portfolio.portfolioId) &&
                Objects.equal(accountId, portfolio.accountId) &&
                Objects.equal(performance, portfolio.performance) &&
                Objects.equal(positions, portfolio.positions) &&
                Objects.equal(orderList, portfolio.orderList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(portfolioId, accountId, performance, positions, orderList);
    }
}
