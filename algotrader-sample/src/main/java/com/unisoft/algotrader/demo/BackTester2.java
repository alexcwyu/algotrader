package com.unisoft.algotrader.demo;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.lmax.disruptor.multi.MultiEventProcessor;
import com.unisoft.algotrader.config.AppConfig;
import com.unisoft.algotrader.config.DefaultEventBusConfigModule;
import com.unisoft.algotrader.config.SampleAppConfigModule;
import com.unisoft.algotrader.config.ServiceConfigModule;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.Provider;
import com.unisoft.algotrader.provider.ProviderId;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.config.DataServiceConfigModule;
import com.unisoft.algotrader.provider.csv.CSVHistoricalDataStore;
import com.unisoft.algotrader.provider.data.DataService;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.yahoo.YahooHistoricalDataProvider;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.trading.StrategyManager;
import com.unisoft.algotrader.utils.DateHelper;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.unisoft.algotrader.model.refdata.Exchange.HKEX;

/**
 * Created by alex on 11/30/15.
 */
public class BackTester2 {


    public static void main(String [] args){

        Injector injector = Guice.createInjector(new SampleAppConfigModule(), new ServiceConfigModule(), new DefaultEventBusConfigModule(), new DataServiceConfigModule());
        AppConfig appConfig = injector.getInstance(AppConfig.class);

        StrategyManager strategyManager = appConfig.getStrategyManager();
        ProviderManager providerManager = appConfig.getProviderManager();

        //DataService dataService = injector.getInstance(DataService.class);
        RefDataStore refDataStore = injector.getInstance(RefDataStore.class);
        EventBusManager eventBusManager = injector.getInstance(EventBusManager.class);


        CountDownLatch latch = new CountDownLatch(1);

        /**
         * setup strategy....this should be register before....or via service lookup mechanism
         */
        Strategy strategy = new CountDownStrategy(appConfig.getOrderManager(),
                strategyManager.nextStrategyId(),
                Portfolio.DEFAULT_PORTFOLIO_ID, appConfig.getTradingDataStore(), latch, 20);

        strategyManager.register(strategy);

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("0005.HK", HKEX.getExchId());

        /**
         * init backtest config
         */
        BackTestConfig backTestConfig = new BackTestConfig(ProviderId.Yahoo.id,
                strategy.strategyId, DateHelper.fromYYYYMMDD(20100101), DateHelper.fromYYYYMMDD(20160101), instrument.getInstId());

        Provider executionDataProvider = providerManager.getProvider(backTestConfig.executionDataProviderId);
        Provider marketDataProvider = providerManager.getProvider(backTestConfig.marketDataProviderId);
        Strategy backTestStrategy = strategyManager.get(backTestConfig.strategyId);


        /**
         * wire up backtest
         */


        MultiEventProcessor processor = new MultiEventProcessor();
        processor.add(eventBusManager.getMarketDataRB(), null);

        /**
         * subscribe historical data
         */



        for (long instId : backTestConfig.instIds) {

            HistoricalSubscriptionKey subscriptionKey = HistoricalSubscriptionKey.createDailySubscriptionKey(
                    backTestConfig.marketDataProviderId,
                    instId,
                    backTestConfig.fromDate,
                    backTestConfig.toDate);
            CSVHistoricalDataStore provider = (CSVHistoricalDataStore) providerManager.getDataStoreProvider(CSVHistoricalDataStore.PROVIDER_ID);
            provider.subscribeHistoricalData(subscriptionKey);
        }


        System.out.println(marketDataProvider);
    }
}
