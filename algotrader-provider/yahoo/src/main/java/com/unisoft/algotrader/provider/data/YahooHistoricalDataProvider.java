package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.data.historical.HistoricalDataProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 6/16/15.
 */
public class YahooHistoricalDataProvider implements HistoricalDataProvider {

    //http://www.jarloo.com/google-finance-and-yql/
    //https://www.bigmiketrading.com/brokers-data-feeds/31382-yahoo-finance-historical-daily-data-retrieved-programmatically.html#post403430
//https://www.quantconnect.com/blog/downloading-yahoo-finance-data-with-c/
    private static final String URL = "http://ichart.yahoo.com/table.csv?s=%1$s&a=%2$d&b=%3$d&c=%4$d&d=%5$d&e=%6$d&f=%7$d";

    private static final String YAHOO_CSV_HEADER = "Date,Open,High,Low,Close,Volume,Adj Close";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public void subscribe(SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {
        String url = getURL(subscriptionKey, fromDate, toDate);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()))) {
            String line;

            String header = bufferedReader.readLine();

            assert YAHOO_CSV_HEADER.equals(header);

            while ((line = bufferedReader.readLine()) != null) {
                String [] tokens = line.split(",");
                Bar bar = new Bar();
                bar.instId = subscriptionKey.instId;
                bar.size = SubscriptionKey.DAILY_SIZE;
                bar.dateTime = DATE_FORMAT.parse(tokens[0]).getTime();
                bar.open = Double.parseDouble(tokens[1]);
                bar.high = Double.parseDouble(tokens[2]);
                bar.low = Double.parseDouble(tokens[3]);
                bar.close = Double.parseDouble(tokens[4]);
                bar.volume = Long.parseLong(tokens[5]);

                System.out.println(bar);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String providerId() {
        return "Yahoo";
    }

    @Override
    public boolean connected() {
        return true;
    }


    protected String getURL(SubscriptionKey key, Date fromDate, Date toDate){
            Calendar fromDateCal = Calendar.getInstance();
            fromDateCal.setTime(fromDate);

            Calendar toDateCal = Calendar.getInstance();
            toDateCal.setTime(toDate);

            String url = String.format(URL, key.instId,
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


    public static void main(String [] args) throws Exception{
        YahooHistoricalDataProvider provider = new YahooHistoricalDataProvider();

        provider.subscribe(SubscriptionKey.createDailySubscriptionKey("GOOG"), 20150601, 20150616);
    }

}
