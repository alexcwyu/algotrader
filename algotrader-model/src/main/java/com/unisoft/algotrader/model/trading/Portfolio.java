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


    public Portfolio(){
    }

    public Portfolio(String portfolioId, String accountName){
        this.accountName = accountName;
        this.portfolioId = portfolioId;
        this.performance = new Performance();
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


    public Position getPosition(int instId){
        return positions.get(instId);
    }

    public Position addPosition(int instId, Position position){
        return positions.putIfAbsent(instId, position);
    }

    public Position removePosition(int instId){
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
                Objects.equal(accountName, portfolio.accountName) &&
                Objects.equal(performance, portfolio.performance) &&
                Objects.equal(positions, portfolio.positions) &&
                Objects.equal(orderList, portfolio.orderList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(portfolioId, accountName, performance, positions, orderList);
    }
}
