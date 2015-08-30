package com.unisoft.algotrader.provider.ib.api.model.constants;

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

    public static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public static long convertDate(String date){
        try {
            return FORMAT.parse(date).getTime();
        }
        catch (ParseException e){
            throw new RuntimeException("fail to parse date:"+date, e);
        }
    }

    public static String convertDate(long date){
        return FORMAT.format(new Date(date));
    }
}
