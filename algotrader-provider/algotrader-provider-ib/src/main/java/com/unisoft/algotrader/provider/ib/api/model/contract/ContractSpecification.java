package com.unisoft.algotrader.provider.ib.api.model.contract;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.provider.ib.api.model.order.OrderType;
import com.unisoft.algotrader.utils.collection.Tuple2;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by alex on 8/19/15.
 */
public class ContractSpecification {


    private static final String EMPTY = "";
    private static final char SEMI_COLON_SEPARATOR = ';';
    private static final char COMMA_SEPARATOR = ',';
    private String bondType = EMPTY;
    private boolean callable;
    private String category = EMPTY;
    private String contractMonth = EMPTY;
    private boolean convertible;
    private double coupon;
    private String couponType = EMPTY;
    private String cusip = EMPTY;
    private String description = EMPTY;
    private String industry = EMPTY;
    private String issueDate = EMPTY;
    private String liquidHours = EMPTY;
    private String longName = EMPTY;
    private String marketName = EMPTY;
    private String maturity = EMPTY;
    private double minimumFluctuation;
    private String nextOptionDate = EMPTY;
    private boolean nextOptionPartial;
    private String nextOptionType = EMPTY;
    private String notes = EMPTY;
    private String validOrderTypes = EMPTY;
    private int priceMagnifier;
    private boolean putable;
    private String ratings = EMPTY;
    private String subcategory = EMPTY;
    private Instrument instrument;
    private String timeZoneId = EMPTY;
    private String tradingClass = EMPTY;
    private String tradingHours = EMPTY;
    private int underlyingContractId;
    private String validExchanges = EMPTY;
    private String economicValueRule = EMPTY;
    private double economicValueMultiplier;
    private List<Tuple2<String, String>> securityIds = Lists.newArrayList();

    private static final Function<String, OrdType> FROM_STRING_TO_ORDER_TYPE_FUNCTION = new Function<String, OrdType>() {
        @Override
        public OrdType apply(final String input) {
            return OrderType.convert(input);
        }
    };
    public String getBondType() {
        return bondType;
    }

    public void setBondType(final String bondType) {
        this.bondType = bondType;
    }

    public boolean isCallable() {
        return callable;
    }

    public void setCallable(final boolean callable) {
        this.callable = callable;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getContractMonth() {
        return contractMonth;
    }

    public void setContractMonth(final String contractMonth) {
        this.contractMonth = contractMonth;
    }

    public boolean isConvertible() {
        return convertible;
    }

    public void setConvertible(final boolean convertible) {
        this.convertible = convertible;
    }

    public double getCoupon() {
        return coupon;
    }

    public void setCoupon(final double coupon) {
        this.coupon = coupon;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(final String couponType) {
        this.couponType = couponType;
    }

    public String getCusip() {
        return cusip;
    }

    public void setCusip(final String cusip) {
        this.cusip = cusip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(final String industry) {
        this.industry = industry;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(final String issueDate) {
        this.issueDate = issueDate;
    }

    public String getLiquidHours() {
        return liquidHours;
    }

    public List<String> getLiquidHoursList() {
        if (StringUtils.isNotEmpty(liquidHours)) {
            return Lists.newArrayList(StringUtils.split(liquidHours, SEMI_COLON_SEPARATOR));
        }
        return Lists.newArrayList();
    }

    public void setLiquidHours(final String liquidHours) {
        this.liquidHours = liquidHours;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(final String longName) {
        this.longName = longName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(final String marketName) {
        this.marketName = marketName;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setMaturity(final String maturity) {
        this.maturity = maturity;
    }

    public double getMinimumFluctuation() {
        return minimumFluctuation;
    }

    public void setMinimumFluctuation(final double minimumFluctuation) {
        this.minimumFluctuation = minimumFluctuation;
    }

    public String getNextOptionDate() {
        return nextOptionDate;
    }

    public void setNextOptionDate(final String nextOptionDate) {
        this.nextOptionDate = nextOptionDate;
    }

    public boolean isNextOptionPartial() {
        return nextOptionPartial;
    }

    public void setNextOptionPartial(final boolean nextOptionPartial) {
        this.nextOptionPartial = nextOptionPartial;
    }

    public String getNextOptionType() {
        return nextOptionType;
    }

    public void setNextOptionType(final String nextOptionType) {
        this.nextOptionType = nextOptionType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public String getValidOrderTypes() {
        return validOrderTypes;
    }

    public List<OrdType> getValidOrderTypesList() {
        if (StringUtils.isNotEmpty(validOrderTypes)) {
            final List<String> orderTypeList = Lists.newArrayList(StringUtils.split(validOrderTypes, COMMA_SEPARATOR));
            return Lists.transform(orderTypeList, FROM_STRING_TO_ORDER_TYPE_FUNCTION);
        }
        return Lists.newArrayList();
    }

    public void setValidOrderTypes(final String validOrderTypes) {
        this.validOrderTypes = validOrderTypes;
    }

    public int getPriceMagnifier() {
        return priceMagnifier;
    }

    public void setPriceMagnifier(final int priceMagnifier) {
        this.priceMagnifier = priceMagnifier;
    }

    public boolean isPutable() {
        return putable;
    }

    public void setPutable(final boolean putable) {
        this.putable = putable;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(final String ratings) {
        this.ratings = ratings;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(final String subcategory) {
        this.subcategory = subcategory;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(final Instrument instrument) {
        this.instrument = instrument;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(final String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getTradingClass() {
        return tradingClass;
    }

    public void setTradingClass(final String tradingClass) {
        this.tradingClass = tradingClass;
    }

    public String getTradingHours() {
        return tradingHours;
    }

    public List<String> getTradingHoursList() {
        if (StringUtils.isNotEmpty(tradingHours)) {
            return Lists.newArrayList(StringUtils.split(tradingHours, SEMI_COLON_SEPARATOR));
        }
        return Lists.newArrayList();
    }

    public void setTradingHours(final String tradingHours) {
        this.tradingHours = tradingHours;
    }

    public int getUnderlyingContractId() {
        return underlyingContractId;
    }

    public void setUnderlyingContractId(final int underlyingContractId) {
        this.underlyingContractId = underlyingContractId;
    }

    public String getValidExchanges() {
        return validExchanges;
    }

    public List<String> getValidExchangesList() {
        if (StringUtils.isNotEmpty(validExchanges)) {
            return Lists.newArrayList(StringUtils.split(validExchanges, COMMA_SEPARATOR));
        }
        return Lists.newArrayList();
    }

    public void setValidExchanges(final String validExchanges) {
        this.validExchanges = validExchanges;
    }

    public String getEconomicValueRule() {
        return economicValueRule;
    }

    public void setEconomicValueRule(final String economicValueRule) {
        this.economicValueRule = economicValueRule;
    }

    public double getEconomicValueMultiplier() {
        return economicValueMultiplier;
    }

    public void setEconomicValueMultiplier(final double economicValueMultiplier) {
        this.economicValueMultiplier = economicValueMultiplier;
    }

    public List<Tuple2<String, String>> getSecurityIds() {
        return securityIds;
    }

    public void setSecurityIds(final List<Tuple2<String, String>> securityIds) {
        this.securityIds = securityIds;
    }

    @Override
    public String toString() {
        return "InstrumentSpecification{" +
                "bondType='" + bondType + '\'' +
                ", callable=" + callable +
                ", category='" + category + '\'' +
                ", contractMonth='" + contractMonth + '\'' +
                ", convertible=" + convertible +
                ", coupon=" + coupon +
                ", couponType='" + couponType + '\'' +
                ", cusip='" + cusip + '\'' +
                ", description='" + description + '\'' +
                ", industry='" + industry + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", liquidHours='" + liquidHours + '\'' +
                ", longName='" + longName + '\'' +
                ", marketName='" + marketName + '\'' +
                ", maturity='" + maturity + '\'' +
                ", minimumFluctuation=" + minimumFluctuation +
                ", nextOptionDate='" + nextOptionDate + '\'' +
                ", nextOptionPartial=" + nextOptionPartial +
                ", nextOptionType='" + nextOptionType + '\'' +
                ", notes='" + notes + '\'' +
                ", validOrderTypes='" + validOrderTypes + '\'' +
                ", priceMagnifier=" + priceMagnifier +
                ", putable=" + putable +
                ", ratings='" + ratings + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", instrument=" + instrument +
                ", timeZoneId='" + timeZoneId + '\'' +
                ", tradingClass='" + tradingClass + '\'' +
                ", tradingHours='" + tradingHours + '\'' +
                ", underlyingContractId=" + underlyingContractId +
                ", validExchanges='" + validExchanges + '\'' +
                ", economicValueRule='" + economicValueRule + '\'' +
                ", economicValueMultiplier=" + economicValueMultiplier +
                ", securityIds=" + securityIds +
                '}';
    }
}
