package com.unisoft.algotrader.demo;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.unisoft.algotrader.config.AppConfig;
import com.unisoft.algotrader.config.SampleConfigModule;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.provider.execution.simulation.SimulationExecutor;
import com.unisoft.algotrader.trading.PortfolioProcessor;
import com.unisoft.algotrader.trading.Strategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by alex on 6/7/15.
 */
public class StrategyRunner {
    private static final Logger LOG = LogManager.getLogger(StrategyRunner.class);

    private final AppConfig appConfig;
    private final Account account;
    private final PortfolioProcessor portfolioProcessor;
    private final Strategy strategy;
    private final Instrument instrument;
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    public StrategyRunner(AppConfig appConfig, Account account, PortfolioProcessor portfolioProcessor, Strategy strategy, Instrument instrument){
        this.appConfig = appConfig;
        this.account = account;
        this.portfolioProcessor = portfolioProcessor;
        this.strategy = strategy;
        this.instrument = instrument;
    }

    public void run(){

        SimulationExecutor simulationExecutor = new SimulationExecutor(appConfig);
        appConfig.getProviderManager().addExecutionProvider(simulationExecutor);
        DataPublisher dataPublisher = new DataPublisher(appConfig.getEventBusManager().getMarketDataRB());

//        executor.submit(strategy);
//        executor.submit(portfolioProcessor);
//        executor.submit(simulationExecutor);
//        executor.submit(appConfig.getInstrumentDataManager());



        executor.submit(()->{

            for (int i =0; i< 100; i ++){
                dataPublisher.publishTrade(instrument.getInstId(), System.currentTimeMillis(), 100+i, 1000);
                try {
                    Thread.sleep(1000);
                }catch(Exception e){

                }
            }
        });
    }


    public static void main(String [] args) throws Exception{


        Injector injector = Guice.createInjector(new SampleConfigModule());
        AppConfig appConfig = injector.getInstance(AppConfig.class);


        Account account = Account.TEST_USD_ACCOUNT;
        Portfolio portfolio = new Portfolio(1, account.accountId());
        appConfig.getTradingDataStore().savePortfolio(portfolio);

        PortfolioProcessor portfolioProcessor = new PortfolioProcessor(appConfig, portfolio, account);

        Instrument testInstrument = SampleEventFactory.TEST_HKD_INSTRUMENT;

        BuyAndHoldStrategy strategy = new BuyAndHoldStrategy(appConfig, portfolio);
        appConfig.getStrategyManager().register(strategy);

        StrategyRunner strategyRunner = new StrategyRunner(appConfig, account, portfolioProcessor, strategy, testInstrument);
        strategyRunner.run();
    }
}
