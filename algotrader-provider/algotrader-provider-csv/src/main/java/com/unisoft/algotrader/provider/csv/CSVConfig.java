package com.unisoft.algotrader.provider.csv;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.refdata.InstrumentManager;
import com.unisoft.algotrader.provider.SubscriptionKey;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 6/16/15.
 */
public interface CSVConfig {

    public final static SimpleDateFormat FORMAT= new SimpleDateFormat("yyyy-MM-dd");
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


    static String getFileName(SubscriptionKey key){
        Instrument instrument = InstrumentManager.INSTANCE.get(key.instId);
        switch (key.type){
            case Bar:
                return String.format(BAR_FILENAME_FORMAT, instrument.getSymbol(), key.barSize);
            case Trade:
                return String.format(TRADE_FILENAME_FORMAT, instrument.getSymbol());
            case Quote:
                return String.format(QUOTE_FILENAME_FORMAT, instrument.getSymbol());
            default:
                throw new UnsupportedOperationException();
        }
    }
}
