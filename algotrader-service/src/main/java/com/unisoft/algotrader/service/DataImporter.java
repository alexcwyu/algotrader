package com.unisoft.algotrader.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.unisoft.algotrader.config.DefaultEventBusConfigModule;
import com.unisoft.algotrader.config.SampleAppConfigModule;
import com.unisoft.algotrader.config.ServiceConfigModule;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.event.RingBufferMarketDataEventBus;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.*;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.config.DataServiceConfigModule;
import com.unisoft.algotrader.provider.csv.CSVHistoricalDataStore;
import com.unisoft.algotrader.provider.data.DataService;
import com.unisoft.algotrader.provider.data.DataStoreProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.yahoo.YahooHistoricalDataProvider;
import com.unisoft.algotrader.utils.DateHelper;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.unisoft.algotrader.model.refdata.Exchange.HKEX;
/**
 * Created by alex on 7/19/15.
 */
public class DataImporter extends MultiEventProcessor implements MarketDataHandler {
    private final ProviderManager providerManager;
    private final DataService dataService;
    private final RingBufferMarketDataEventBus rb;

    private DataStoreProvider provider;

    public DataImporter(ProviderManager providerManager, DataService dataService, RingBuffer<MarketDataContainer> marketDataRB){
        super(new NoWaitStrategy(),  null, marketDataRB);
        this.providerManager = providerManager;
        this.dataService = dataService;
        this.rb = new RingBufferMarketDataEventBus(marketDataRB);

    }
    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public void onBar(Bar bar) {
        if(provider != null){
            provider.onBar(bar);
        }
    }

    @Override
    public void onQuote(Quote quote) {
        if(provider != null){
            provider.onQuote(quote);
        }
    }

    @Override
    public void onTrade(Trade trade) {
        if(provider != null){
            provider.onTrade(trade);
        }
    }

    public boolean importData(HistoricalSubscriptionKey subscriptionKey){
        provider = providerManager.getDataStoreProvider(CSVHistoricalDataStore.PROVIDER_ID);
        return dataService.subscribeHistoricalData(subscriptionKey);
    }

    public static void main(String [] args){

        final ExecutorService executor = Executors.newFixedThreadPool(2, DaemonThreadFactory.INSTANCE);


        Injector injector = Guice.createInjector(new SampleAppConfigModule(), new ServiceConfigModule(), new DefaultEventBusConfigModule(), new DataServiceConfigModule());
        ProviderManager providerManager = injector.getInstance(ProviderManager.class);
        DataService dataService = injector.getInstance(DataService.class);
        RefDataStore refDataStore = injector.getInstance(RefDataStore.class);
        EventBusManager eventBusManager = injector.getInstance(EventBusManager.class);
        DataImporter importer = new DataImporter(providerManager, dataService, eventBusManager.marketDataRB);

        executor.submit(importer);

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("0005.HK", HKEX.getExchId());
        Date fromDate = DateHelper.fromYYYYMMDD(20000101);
        Date toDate = Calendar.getInstance().getTime();
        HistoricalSubscriptionKey subscriptionKey = HistoricalSubscriptionKey.createDailySubscriptionKey(YahooHistoricalDataProvider.PROVIDER_ID, instrument.getInstId(), fromDate, toDate);

        boolean result = importer.importData(subscriptionKey);


        CSVHistoricalDataStore provider = (CSVHistoricalDataStore)providerManager.getDataStoreProvider(CSVHistoricalDataStore.PROVIDER_ID);

        List<MarketDataContainer> data = provider.loadHistoricalData(subscriptionKey);

        System.out.println(data.size());

        data.stream().map(marketDataContainer -> marketDataContainer.bar).forEach(System.out::println);

        executor.shutdownNow();
    }

}
