package com.unisoft.algotrader.provider.google;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.bus.MarketDataEventBus;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.AbstractHistoricalDataProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 6/16/15.
 *
 * inspired by http://stackoverflow.com/questions/9093000/read-csv-file-from-internet
 * https://www.bigmiketrading.com/brokers-data-feeds/31385-google-finance-historical-daily-data-retrieved-programmatically.html
 * http://trading.cheno.net/downloading-google-intraday-historical-data-with-python/
 * http://www.codeproject.com/Articles/221952/Simple-Csharp-DLL-to-download-data-from-Google-Fin
 * http://www.networkerror.org/component/content/article/1-technical-wootness/44-googles-undocumented-finance-api.html
 * http://www.marketcalls.in/database/google-realtime-intraday-backfill-data.html
 */
@Singleton
public class GoogleHistoricalDataProvider extends AbstractHistoricalDataProvider {

    public static final String PROVIDER_ID = "Google";

    private static final Logger LOG = LogManager.getLogger(GoogleHistoricalDataProvider.class);

    private static final String GOOGLE_CSV_HEADER = "\uFEFFDate,Open,High,Low,Close,Volume";
    private static final String URL = "http://www.google.com/finance/historical?q=%1$s&histperiod=daily&startdate=%2$s&enddate=%3$s&output=csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d yyyy");
    private static final SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("d-MMM-yy");

    private final RefDataStore refDataStore;
    private final MarketDataEventBus marketDataEventBus;

    @Inject
    public GoogleHistoricalDataProvider(ProviderManager providerManager, RefDataStore refDataStore, MarketDataEventBus marketDataEventBus){
        super(providerManager);
        this.refDataStore = refDataStore;
        this.marketDataEventBus = marketDataEventBus;
    }

    private List<String> loadData(HistoricalSubscriptionKey subscriptionKey) {
        String url = getURL(subscriptionKey, subscriptionKey.fromDate, subscriptionKey.toDate);
        List<String> data = Lists.newArrayList();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()))) {
            String line;

            String header = bufferedReader.readLine();

            assert GOOGLE_CSV_HEADER.equals(header);

            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        try {
            List<String> data = loadData(subscriptionKey);
            String line;
            //reverse the data, as the google'csv store latest on the top
            for (int i = data.size() -1; i>=0; i--){
                line = data.get(i);
                String [] tokens = line.split(",");

                marketDataEventBus.publishBar(subscriptionKey.instId, SubscriptionKey.DAILY_SIZE, DATE_FORMAT_2.parse(tokens[0]).getTime(),
                        Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]),Double.parseDouble(tokens[3]),Double.parseDouble(tokens[4]),Long.parseLong(tokens[5]), 0);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        List<MarketDataContainer> list = Lists.newArrayList();
        try {
            List<String> data = loadData(subscriptionKey);
            String line;
            //reverse the data, as the google'csv store latest on the top
            for (int i = data.size() -1; i>=0; i--){
                line = data.get(i);
                String [] tokens = line.split(",");

                MarketDataContainer container = new MarketDataContainer();
                container.setBar(subscriptionKey.instId, SubscriptionKey.DAILY_SIZE, DATE_FORMAT_2.parse(tokens[0]).getTime(),
                        Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Long.parseLong(tokens[5]), 0);

                list.add(container);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public String providerId() {
        return PROVIDER_ID;
    }

    @Override
    public boolean connected() {
        return true;
    }


    protected String getURL(SubscriptionKey key, long fromDate, long toDate){
        try {

            Instrument instrument = refDataStore.getInstrument(key.instId);

            String fromDateStr = URLEncoder.encode(DATE_FORMAT.format(new Date(fromDate)), "UTF-8");
            String toDateStr = URLEncoder.encode(DATE_FORMAT.format(new Date(toDate)), "UTF-8");
            String url = String.format(URL, instrument.getSymbol(), fromDateStr, toDateStr);
            return url;
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }

    public static void sample_query1() {
        try {
            URL url = new URL("http://www.google.com/finance/historical?q=GOOG&histperiod=daily&startdate=Apr+1+2014&enddate=Apr+15+2014&output=csv");
            URLConnection urlConn = url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(urlConn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sample_query2(){
        try {
            URL url = new URL("https://www.google.com/finance/getprices?q=.INX&x=INDEXSP&i=86400&p=12d&f=d,c,v,k,o,h,l&df=cpct&auto=0&ei=Ef6XUYDfCqSTiAKEMg");
            URLConnection urlConn = url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(urlConn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
