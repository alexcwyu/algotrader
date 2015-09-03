package com.unisoft.algotrader.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 7/26/15.
 */
public class DateHelper {

    private static final String YYYYMMDD_FORMAT = "yyyyMMdd";
    private static final String YYMMDD_HHMMSS_FORMAT = "yyyyMMdd HH:mm:ss";

    private static final ThreadLocal<SimpleDateFormat> YYYYMMDD_FFORMATTER = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat(YYYYMMDD_FORMAT);
        }
    };

    private static final ThreadLocal<SimpleDateFormat> YYMMDD_HHMMSS_FORMATTER = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat(YYMMDD_HHMMSS_FORMAT);
        }
    };

    public static Date fromYYYYMMDD(int yyyymmdd){
        try {
            return YYYYMMDD_FFORMATTER.get().parse(Integer.toString(yyyymmdd));
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    public static Date fromYYYYMMDD(String yyyymmdd){
        try {
            return YYYYMMDD_FFORMATTER.get().parse(yyyymmdd);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    public static String formatYYYYMMDDHHMMSS(long date){
        return YYMMDD_HHMMSS_FORMATTER.get().format(new Date(date));
    }

    public static String formatYYYYMMDDHHMMSS(Date date){
        return YYMMDD_HHMMSS_FORMATTER.get().format(date);
    }

}
