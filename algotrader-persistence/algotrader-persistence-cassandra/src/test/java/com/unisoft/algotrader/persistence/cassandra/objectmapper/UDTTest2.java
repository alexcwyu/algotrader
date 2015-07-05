package com.unisoft.algotrader.persistence.cassandra.objectmapper;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.UDTMapper;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.core.*;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;

/**
 * Created by alex on 7/2/15.
 */
public class UDTTest2 {

    public static com.unisoft.algotrader.core.Account saveAccount(Session tradingSession){

        UDTMapper<AccountTransaction> acctTransMapper = new MappingManager(tradingSession).udtMapper(AccountTransaction.class);
        UDTMapper<AccountPosition> acctPosMapper = new MappingManager(tradingSession).udtMapper(AccountPosition.class);
        Mapper<com.unisoft.algotrader.core.Account> mapper = new MappingManager(tradingSession).mapper(com.unisoft.algotrader.core.Account.class);

        com.unisoft.algotrader.core.Account account = new com.unisoft.algotrader.core.Account("Test", "", Currency.USD, 100000);
        account.add(new AccountTransaction(System.currentTimeMillis(), Currency.USD, 500, ""));


        mapper.save(account);

        return account;

//
//            Map<String, UDTValue> positionUDT = new HashMap();
//            Map<String, List<UDTValue>> transactionUDT = new HashMap();
//
//            for (AccountPosition position : account.getAccountPositions().values()){
//                positionUDT.put(position.getCcyId(), acctPosMapper.toUDT(position));
//                List<UDTValue> t = new ArrayList<>();
//                transactionUDT.put(position.getCcyId(), t);
//                for (AccountTransaction transaction : position.getAccountTransactions()){
//                    t.add(acctTransMapper.toUDT(transaction));
//                }
//            }
//
//            PreparedStatement insertUserPreparedStatement
//                    = tradingSession.prepare("INSERT INTO trading.account (name, description, ccy_id, positions) VALUES (?, ?, ?, ?);");
//
//            BoundStatement bs = insertUserPreparedStatement.bind(account.getName(), account.getDescription(), account.getCcyId(), positionUDT);
//
//            ResultSet execute = tradingSession.execute(bs);
    }


    public static void saveAccount(Session tradingSession, com.unisoft.algotrader.core.Account account){
        Mapper<com.unisoft.algotrader.core.Account> mapper = new MappingManager(tradingSession).mapper(com.unisoft.algotrader.core.Account.class);
        mapper.save(account);
    }

    public static void savePortfolio(Session tradingSession, Portfolio portfolio){
        Mapper<Portfolio> mapper = new MappingManager(tradingSession).mapper(Portfolio.class);
        mapper.save(portfolio);
    }


    public static void saveExecutionReport(Session tradingSession, ExecutionReport er){
        Mapper<ExecutionReport> mapper = new MappingManager(tradingSession).mapper(ExecutionReport.class);
        mapper.save(er);
    }


    public static void saveOrder(Session tradingSession, Order order){
        Mapper<Order> mapper = new MappingManager(tradingSession).mapper(Order.class);
        mapper.save(order);
    }

    public static void main(String[] args) throws Exception {
        try (Cluster cluster = Cluster.builder().withProtocolVersion(ProtocolVersion.V3).addContactPoint("localhost").build();
             Session tradingSession = cluster.connect("trading")) {

            com.unisoft.algotrader.core.Account account = AccountManager.DEFAULT_ACCOUNT;

            Portfolio portfolio = SampleEventFactory.createPortfolio("TestPortfolio", account.getName());
            PortfolioManager.INSTANCE.add(portfolio);

            Clock.CLOCK.setDateTime(System.currentTimeMillis());
            Order order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.instId, Side.Buy, OrdType.Limit, 9000, 98, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
            ExecutionReport executionReport = SampleEventFactory.createExecutionReport(order);
            order.add(executionReport);
            portfolio.add(order);

            Clock.CLOCK.setDateTime(System.currentTimeMillis());
            Order order2 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.instId, Side.Sell, OrdType.Limit, 10000, 108, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
            ExecutionReport executionReport2= SampleEventFactory.createExecutionReport(order2);
            order2.add(executionReport2);
            portfolio.add(order2);

            Clock.CLOCK.setDateTime(System.currentTimeMillis());
            Order order3 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.instId, Side.Buy, OrdType.Limit, 1000, 88, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
            ExecutionReport executionReport3 = SampleEventFactory.createExecutionReport(order3);
            order3.add(executionReport3);
            portfolio.add(order3);

            saveOrder(tradingSession, order);
            saveOrder(tradingSession, order2);
            saveOrder(tradingSession, order3);

            saveExecutionReport(tradingSession, executionReport);
            saveExecutionReport(tradingSession, executionReport2);
            saveExecutionReport(tradingSession, executionReport3);

            savePortfolio(tradingSession, portfolio);
            saveAccount(tradingSession, account);
        }

    }

}
