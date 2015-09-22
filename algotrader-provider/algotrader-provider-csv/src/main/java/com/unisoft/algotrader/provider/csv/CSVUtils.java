package com.unisoft.algotrader.provider.csv;

import com.unisoft.algotrader.provider.data.SubscriptionKey;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 6/16/15.
 */
public interface CSVUtils {

    //public final static SimpleDateFormat FORMAT= new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat DATETIME_FORMAT= new SimpleDateFormat("yyyyMMddHHmmssZ");

    public final static String BAR_FILENAME_FORMAT = "BAR.%1$s.%2$d.csv";
    public final static String QUOTE_FILENAME_FORMAT = "QUOTE.%1.csv";
    public final static String TRADE_FILENAME_FORMAT = "TRADE.%1.csv";

    public final static String [] BAR_HEADER = {"Date","Open","High","Low","Close","Volume","OpenInt"};
    public final static String [] QUOTE_HEADER = {"Date","bid","ask","bidsize","asksize"};
    public final static String [] TRADE_HEADER = {"Date","price","size"};

    static String formatDate(long datetime){
        Date date = new Date(datetime);
        return DATETIME_FORMAT.format(date);
    }


    static String getFileName(SubscriptionKey key, String symbol){
        switch (key.subscriptionType.type){
            case Bar:
                return String.format(BAR_FILENAME_FORMAT, symbol, key.subscriptionType.barSize);
            case Trade:
                return String.format(TRADE_FILENAME_FORMAT, symbol);
            case Quote:
                return String.format(QUOTE_FILENAME_FORMAT, symbol);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
