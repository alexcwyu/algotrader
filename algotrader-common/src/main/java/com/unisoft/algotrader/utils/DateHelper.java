package com.unisoft.algotrader.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 7/26/15.
 */
public class DateHelper {

    public final static SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static Date fromYYYYMMDD(int yyyymmdd){
        try {
            return YYYYMMDD_FORMAT.parse(Integer.toString(yyyymmdd));
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    public static Date fromYYYYMMDD(String yyyymmdd){
        try {
            return YYYYMMDD_FORMAT.parse(yyyymmdd);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

}
