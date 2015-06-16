package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.data.historical.HistoricalDataProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 6/16/15.
 */
public class GoogleHistoricalDataProvider implements HistoricalDataProvider {

    private static final String GOOGLE_CSV_HEADER = "\uFEFFDate,Open,High,Low,Close,Volume";
    private static final String URL = "http://www.google.com/finance/historical?q=%1$s&histperiod=daily&startdate=%2$s&enddate=%3$s&output=csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d yyyy");

    private static final SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("d-MMM-yy");


    @Override
    public void subscribe(SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {
        String url = getURL(subscriptionKey, fromDate, toDate);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()))) {
            String line;

            String header = bufferedReader.readLine();

            assert GOOGLE_CSV_HEADER.equals(header);

            while ((line = bufferedReader.readLine()) != null) {
                String [] tokens = line.split(",");
                Bar bar = new Bar();
                bar.instId = subscriptionKey.instId;
                bar.size = SubscriptionKey.DAILY_SIZE;
                bar.dateTime = DATE_FORMAT_2.parse(tokens[0]).getTime();
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
        return "Google";
    }

    @Override
    public boolean connected() {
        return true;
    }


    protected String getURL(SubscriptionKey key, Date fromDate, Date toDate){
        try {
            String fromDateStr = URLEncoder.encode(DATE_FORMAT.format(fromDate), "UTF-8");
            String toDateStr = URLEncoder.encode(DATE_FORMAT.format(toDate), "UTF-8");
            String url = String.format(URL, key.instId, fromDateStr, toDateStr);
            return url;
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }

    //inspired by http://stackoverflow.com/questions/9093000/read-csv-file-from-internet
    //https://www.bigmiketrading.com/brokers-data-feeds/31385-google-finance-historical-daily-data-retrieved-programmatically.html
    //http://trading.cheno.net/downloading-google-intraday-historical-data-with-python/
    //http://www.codeproject.com/Articles/221952/Simple-Csharp-DLL-to-download-data-from-Google-Fin
    //http://www.networkerror.org/component/content/article/1-technical-wootness/44-googles-undocumented-finance-api.html
    //http://www.marketcalls.in/database/google-realtime-intraday-backfill-data.html
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

    public static void main(String [] args) throws Exception{
        GoogleHistoricalDataProvider provider = new GoogleHistoricalDataProvider();

        provider.subscribe(SubscriptionKey.createDailySubscriptionKey("GOOG"), 20150601, 20150616);


    }
}
