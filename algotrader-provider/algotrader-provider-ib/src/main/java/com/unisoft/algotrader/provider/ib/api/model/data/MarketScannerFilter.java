package com.unisoft.algotrader.provider.ib.api.model.data;

import ch.aonyx.broker.ib.api.data.scanner.StockType;
import com.google.common.base.Objects;

/**
 * Created by alex on 9/9/15.
 */
public class MarketScannerFilter {

    private static final String EMPTY = "";
    private static final int NO_NUMBER_OF_ROWS_SPECIFIED = -1;
    private double abovePrice = Double.MAX_VALUE;
    private int aboveVolume = Integer.MAX_VALUE;
    private int aboveAverageVolumeOption = Integer.MAX_VALUE;
    private double belowPrice = Double.MAX_VALUE;
    private double aboveCouponRate = Double.MAX_VALUE;
    private double belowCouponRate = Double.MAX_VALUE;
    private String excludeConvertible = EMPTY;
    private String instrument = EMPTY;
    private String locationCode = EMPTY;
    private double aboveMarketCapitalization = Double.MAX_VALUE;
    private double belowMarketCapitalization = Double.MAX_VALUE;
    private String aboveMaturityDate = EMPTY;
    private String belowMaturityDate = EMPTY;
    private String aboveMoodyRating = EMPTY;
    private String belowMoodyRating = EMPTY;
    private int numberOfRows = NO_NUMBER_OF_ROWS_SPECIFIED;
    private String scannerCode = EMPTY;
    private String scannerSettingPairs = EMPTY;
    private String aboveSpRating = EMPTY;
    private String belowSpRating = EMPTY;
    private StockType stockType = StockType.EMPTY;

    public double getAbovePrice() {
        return abovePrice;
    }

    public void setAbovePrice(double abovePrice) {
        this.abovePrice = abovePrice;
    }

    public int getAboveVolume() {
        return aboveVolume;
    }

    public void setAboveVolume(int aboveVolume) {
        this.aboveVolume = aboveVolume;
    }

    public int getAboveAverageVolumeOption() {
        return aboveAverageVolumeOption;
    }

    public void setAboveAverageVolumeOption(int aboveAverageVolumeOption) {
        this.aboveAverageVolumeOption = aboveAverageVolumeOption;
    }

    public double getBelowPrice() {
        return belowPrice;
    }

    public void setBelowPrice(double belowPrice) {
        this.belowPrice = belowPrice;
    }

    public double getAboveCouponRate() {
        return aboveCouponRate;
    }

    public void setAboveCouponRate(double aboveCouponRate) {
        this.aboveCouponRate = aboveCouponRate;
    }

    public double getBelowCouponRate() {
        return belowCouponRate;
    }

    public void setBelowCouponRate(double belowCouponRate) {
        this.belowCouponRate = belowCouponRate;
    }

    public String getExcludeConvertible() {
        return excludeConvertible;
    }

    public void setExcludeConvertible(String excludeConvertible) {
        this.excludeConvertible = excludeConvertible;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public double getAboveMarketCapitalization() {
        return aboveMarketCapitalization;
    }

    public void setAboveMarketCapitalization(double aboveMarketCapitalization) {
        this.aboveMarketCapitalization = aboveMarketCapitalization;
    }

    public double getBelowMarketCapitalization() {
        return belowMarketCapitalization;
    }

    public void setBelowMarketCapitalization(double belowMarketCapitalization) {
        this.belowMarketCapitalization = belowMarketCapitalization;
    }

    public String getAboveMaturityDate() {
        return aboveMaturityDate;
    }

    public void setAboveMaturityDate(String aboveMaturityDate) {
        this.aboveMaturityDate = aboveMaturityDate;
    }

    public String getBelowMaturityDate() {
        return belowMaturityDate;
    }

    public void setBelowMaturityDate(String belowMaturityDate) {
        this.belowMaturityDate = belowMaturityDate;
    }

    public String getAboveMoodyRating() {
        return aboveMoodyRating;
    }

    public void setAboveMoodyRating(String aboveMoodyRating) {
        this.aboveMoodyRating = aboveMoodyRating;
    }

    public String getBelowMoodyRating() {
        return belowMoodyRating;
    }

    public void setBelowMoodyRating(String belowMoodyRating) {
        this.belowMoodyRating = belowMoodyRating;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public String getScannerCode() {
        return scannerCode;
    }

    public void setScannerCode(String scannerCode) {
        this.scannerCode = scannerCode;
    }

    public String getScannerSettingPairs() {
        return scannerSettingPairs;
    }

    public void setScannerSettingPairs(String scannerSettingPairs) {
        this.scannerSettingPairs = scannerSettingPairs;
    }

    public String getAboveSpRating() {
        return aboveSpRating;
    }

    public void setAboveSpRating(String aboveSpRating) {
        this.aboveSpRating = aboveSpRating;
    }

    public String getBelowSpRating() {
        return belowSpRating;
    }

    public void setBelowSpRating(String belowSpRating) {
        this.belowSpRating = belowSpRating;
    }

    public StockType getStockType() {
        return stockType;
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketScannerFilter)) return false;
        MarketScannerFilter that = (MarketScannerFilter) o;
        return Objects.equal(abovePrice, that.abovePrice) &&
                Objects.equal(aboveVolume, that.aboveVolume) &&
                Objects.equal(aboveAverageVolumeOption, that.aboveAverageVolumeOption) &&
                Objects.equal(belowPrice, that.belowPrice) &&
                Objects.equal(aboveCouponRate, that.aboveCouponRate) &&
                Objects.equal(belowCouponRate, that.belowCouponRate) &&
                Objects.equal(aboveMarketCapitalization, that.aboveMarketCapitalization) &&
                Objects.equal(belowMarketCapitalization, that.belowMarketCapitalization) &&
                Objects.equal(numberOfRows, that.numberOfRows) &&
                Objects.equal(excludeConvertible, that.excludeConvertible) &&
                Objects.equal(instrument, that.instrument) &&
                Objects.equal(locationCode, that.locationCode) &&
                Objects.equal(aboveMaturityDate, that.aboveMaturityDate) &&
                Objects.equal(belowMaturityDate, that.belowMaturityDate) &&
                Objects.equal(aboveMoodyRating, that.aboveMoodyRating) &&
                Objects.equal(belowMoodyRating, that.belowMoodyRating) &&
                Objects.equal(scannerCode, that.scannerCode) &&
                Objects.equal(scannerSettingPairs, that.scannerSettingPairs) &&
                Objects.equal(aboveSpRating, that.aboveSpRating) &&
                Objects.equal(belowSpRating, that.belowSpRating) &&
                Objects.equal(stockType, that.stockType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(abovePrice, aboveVolume, aboveAverageVolumeOption, belowPrice, aboveCouponRate, belowCouponRate, excludeConvertible, instrument, locationCode, aboveMarketCapitalization, belowMarketCapitalization, aboveMaturityDate, belowMaturityDate, aboveMoodyRating, belowMoodyRating, numberOfRows, scannerCode, scannerSettingPairs, aboveSpRating, belowSpRating, stockType);
    }

    @Override
    public String toString() {
        return "MarketScannerFilter{" +
                "abovePrice=" + abovePrice +
                ", aboveVolume=" + aboveVolume +
                ", aboveAverageVolumeOption=" + aboveAverageVolumeOption +
                ", belowPrice=" + belowPrice +
                ", aboveCouponRate=" + aboveCouponRate +
                ", belowCouponRate=" + belowCouponRate +
                ", excludeConvertible='" + excludeConvertible + '\'' +
                ", instrument='" + instrument + '\'' +
                ", locationCode='" + locationCode + '\'' +
                ", aboveMarketCapitalization=" + aboveMarketCapitalization +
                ", belowMarketCapitalization=" + belowMarketCapitalization +
                ", aboveMaturityDate='" + aboveMaturityDate + '\'' +
                ", belowMaturityDate='" + belowMaturityDate + '\'' +
                ", aboveMoodyRating='" + aboveMoodyRating + '\'' +
                ", belowMoodyRating='" + belowMoodyRating + '\'' +
                ", numberOfRows=" + numberOfRows +
                ", scannerCode='" + scannerCode + '\'' +
                ", scannerSettingPairs='" + scannerSettingPairs + '\'' +
                ", aboveSpRating='" + aboveSpRating + '\'' +
                ", belowSpRating='" + belowSpRating + '\'' +
                ", stockType=" + stockType +
                '}';
    }
}
