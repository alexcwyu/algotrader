package com.unisoft.algotrader.provider.ib.api.model.data;

/**
 * Created by alex on 8/30/15.
 */
public enum BarSize {

    ONE_SECOND(1, 1, BarSizeUnit.SECONDS),
    FIVE_SECONDS(5, 5, BarSizeUnit.SECONDS),
    FIFTEEN_SECONDS(15, 12, BarSizeUnit.SECONDS),
    THIRTY_SECONDS(30, 30, BarSizeUnit.SECONDS),
    ONE_MINUTE(60, 1, BarSizeUnit.MINUTE),
    TWO_MINUTES(2*60, 2, BarSizeUnit.MINUTES),
    THREE_MINUTES(3*60, 3, BarSizeUnit.MINUTES),
    FIVE_MINUTES(5*60, 5, BarSizeUnit.MINUTES),
    FIFTEEN_MINUTES(15*60, 15, BarSizeUnit.MINUTES),
    THIRTY_MINUTES(30*60, 30, BarSizeUnit.MINUTES),
    ONE_HOUR(60*60, 1, BarSizeUnit.HOUR),
    ONE_DAY(24*60*60, 1, BarSizeUnit.DAY);

    private final int barSize;
    private final int duration;
    private final BarSizeUnit barSizeUnit;

    private BarSize(final int barSize, final int duration, final BarSizeUnit barSizeUnit) {
        this.barSize = barSize;
        this.duration = duration;
        this.barSizeUnit = barSizeUnit;
    }

    public final int getDuration() {
        return duration;
    }

    public final int getBarSizen() {
        return barSize;
    }

    public final BarSizeUnit getBarSizeUnit() {
        return barSizeUnit;
    }

    public final String getFormattedBarSize() {
        return duration + " " + barSizeUnit.getAbbreviation();
    }

    public static String getFormattedBarSize(int barSize){
        if (barSize >= ONE_DAY.barSize){
            return ONE_DAY.getFormattedBarSize();
        }
        else if (barSize >= ONE_HOUR.barSize){
            return ONE_HOUR.getFormattedBarSize();
        }
        else if (barSize >= THIRTY_MINUTES.barSize){
            return THIRTY_MINUTES.getFormattedBarSize();
        }
        else if (barSize >= FIFTEEN_MINUTES.barSize){
            return FIFTEEN_MINUTES.getFormattedBarSize();
        }
        else if (barSize >= FIVE_MINUTES.barSize){
            return FIVE_MINUTES.getFormattedBarSize();
        }
        else if (barSize >= THREE_MINUTES.barSize){
            return THREE_MINUTES.getFormattedBarSize();
        }
        else if (barSize >= TWO_MINUTES.barSize){
            return TWO_MINUTES.getFormattedBarSize();
        }
        else if (barSize >= ONE_MINUTE.barSize){
            return ONE_MINUTE.getFormattedBarSize();
        }
        else if (barSize >= THIRTY_SECONDS.barSize){
            return THIRTY_SECONDS.getFormattedBarSize();
        }
        else if (barSize >= FIFTEEN_SECONDS.barSize){
            return FIFTEEN_SECONDS.getFormattedBarSize();
        }
        else if (barSize >= FIVE_SECONDS.barSize){
            return FIVE_SECONDS.getFormattedBarSize();
        }
        else if (barSize >= ONE_SECOND.barSize){
            return ONE_SECOND.getFormattedBarSize();
        }
        else{
            return FIVE_SECONDS.getFormattedBarSize();
        }
    }
}
