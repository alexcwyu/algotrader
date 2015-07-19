package com.unisoft.algotrader.provider.yahoo;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.HistoricalDataProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.Subscriber;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 6/16/15.
 *
 * http://www.jarloo.com/google-finance-and-yql/
 * https://www.bigmiketrading.com/brokers-data-feeds/31382-yahoo-finance-historical-daily-data-retrieved-programmatically.html#post403430
 * https://www.quantconnect.com/blog/downloading-yahoo-finance-data-with-c/
 */
@Singleton
public class YahooHistoricalDataProvider implements HistoricalDataProvider {

    public static final String PROVIDER_ID = "Yahoo";

    private static final Logger LOG = LogManager.getLogger(YahooHistoricalDataProvider.class);

    private static final String URL = "http://ichart.yahoo.com/table.csv?s=%1$s&a=%2$d&b=%3$d&c=%4$d&d=%5$d&e=%6$d&f=%7$d";

    private static final String YAHOO_CSV_HEADER = "Date,Open,High,Low,Close,Volume,Adj Close";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final RefDataStore refDataStore;

    @Inject
    public YahooHistoricalDataProvider(RefDataStore refDataStore){
        this.refDataStore = refDataStore;
    }

    private List<String> loadData(HistoricalSubscriptionKey subscriptionKey){
        List<String> data = Lists.newArrayList();
        String url = getURL(subscriptionKey, subscriptionKey.fromDate, subscriptionKey.toDate);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()))) {
            String line;
            String header = bufferedReader.readLine();
            assert YAHOO_CSV_HEADER.equals(header);


            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return data;
    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey, Subscriber subscriber) {
        List<String> data = loadData(subscriptionKey);

        String line;
        try {
            //reverse the data, as the yahoo'csv store latest on the top
            for (int i = data.size() - 1; i >= 0; i--) {
                line = data.get(i);
                String[] tokens = line.split(",");

                subscriber.marketDataEventBus.publishBar(subscriptionKey.instId, SubscriptionKey.DAILY_SIZE, DATE_FORMAT.parse(tokens[0]).getTime(),
                        Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Long.parseLong(tokens[5]), 0);
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return true;
    }


    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {

        List<String> data = loadData(subscriptionKey);
        List<MarketDataContainer> list = Lists.newArrayList();

        String line;
        try {
            for (int i = data.size() - 1; i >= 0; i--) {
                line = data.get(i);
                String[] tokens = line.split(",");

                MarketDataContainer container = new MarketDataContainer();
                container.setBar(subscriptionKey.instId, SubscriptionKey.DAILY_SIZE, DATE_FORMAT.parse(tokens[0]).getTime(),
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
            Calendar fromDateCal = Calendar.getInstance();
            fromDateCal.setTime(new Date(fromDate));

            Calendar toDateCal = Calendar.getInstance();
            toDateCal.setTime(new Date(toDate));

            Instrument instrument = refDataStore.getInstrument(key.instId);
            String url = String.format(URL, instrument.getSymbol(),
                    fromDateCal.get(Calendar.MONTH), fromDateCal.get(Calendar.DATE), fromDateCal.get(Calendar.YEAR),
                    toDateCal.get(Calendar.MONTH), toDateCal.get(Calendar.DATE), toDateCal.get(Calendar.YEAR));
            return url;
    }




    public static void getYQLURL2() throws Exception{

        String PREFIX = "http://query.yahooapis.com/v1/public/yql?q=";
        String QUERY = "select * from yahoo.finance.historicaldata where symbol = \"%1$s\" and startDate = \"%2$s\" and endDate = \"%3$s\"";
        String SUFFIX = "&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        Date startDate = YYYYMMDD_FORMAT.parse("20140401");
        Date endDate = YYYYMMDD_FORMAT.parse("20140415");

        String fromDateStr = URLEncoder.encode(DATE_FORMAT.format(startDate), "UTF-8");
        String toDateStr = URLEncoder.encode(DATE_FORMAT.format(endDate), "UTF-8");
        String query = String.format(QUERY, "^GSPC", fromDateStr, toDateStr);
        query =query.replaceAll(" ", "%20");

        String URL =PREFIX+query+SUFFIX;

        System.out.println(URL);

        String prefix = "http://query.yahooapis.com/v1/public/yql?q=";
        String query2 = "select * from yahoo.finance.historicaldata where symbol = \"^GSPC\" and startDate = \"2014-04-01\" and endDate = \"2014-04-15\"";
        String suffix = "&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        String s = prefix + query2.replaceAll(" ", "%20") + suffix;

        System.out.println(s);
    }

}
