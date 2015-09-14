package com.unisoft.algotrader.provider.ib.api.model.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 8/3/15.
 */
public class IBModelUtils {
    public static final String EMPTY = "";
    public static final String UNKNOWN = "UNKNOWN";
    public static final byte[] EMPTY_BYTES = EMPTY.getBytes();
    public static final byte[] UNKNOWN_BYTES = UNKNOWN.getBytes();

    public static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static Date convertDateTime(String date){
        try {
            return DATETIME_FORMAT.parse(date);
        }
        catch (ParseException e){
            throw new RuntimeException("fail to parse date:"+date, e);
        }
    }

    public static String convertDateTime(long date){
        return DATETIME_FORMAT.format(new Date(date));
    }


    public static Date convertDate(String date){
        try {
            return DATE_FORMAT.parse(date);
        }
        catch (ParseException e){
            throw new RuntimeException("fail to parse date:"+date, e);
        }
    }
}
