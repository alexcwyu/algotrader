package com.unisoft.algotrader.provider.ib.api.model;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.Currency;

/**
 * Created by alex on 8/21/15.
 */
public class CommissionReport {

    private static final String DATE_PATTERN = "yyyyMMdd";
    private double commission;
    private String currencyCode;
    private String executionId;
    private double realizedProfitAndLoss;
    private double yield;
    private int yieldRedemptionDate;

    public double getCommission() {
        return commission;
    }

    public void setCommission(final double commission) {
        this.commission = commission;
    }

    public String getCurrencyCode() {
        return StringUtils.trimToNull(currencyCode);
    }

    public Currency getCurrency() {
        return Currency.getInstance(currencyCode);
    }

    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setCurrency(final Currency currency) {
        currencyCode = currency.getCurrencyCode();
    }

    public String getExecutionId() {
        return StringUtils.trimToNull(executionId);
    }

    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }

    public double getRealizedProfitAndLoss() {
        return realizedProfitAndLoss;
    }

    public void setRealizedProfitAndLoss(final double realizedProfitAndLoss) {
        this.realizedProfitAndLoss = realizedProfitAndLoss;
    }

    public double getYield() {
        return yield;
    }

    public void setYield(final double yield) {
        this.yield = yield;
    }

    public int getYieldRedemptionDate() {
        return yieldRedemptionDate;
    }

    public DateTime getYieldRedemptionDateTime() {
        try {
            return new DateTime(DateUtils.parseDate(String.valueOf(yieldRedemptionDate), DATE_PATTERN).getTime());
        } catch (final ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void setYieldRedemptionDate(final int yieldRedemptionDate) {
        this.yieldRedemptionDate = yieldRedemptionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommissionReport)) return false;
        CommissionReport that = (CommissionReport) o;
        return Objects.equal(commission, that.commission) &&
                Objects.equal(realizedProfitAndLoss, that.realizedProfitAndLoss) &&
                Objects.equal(yield, that.yield) &&
                Objects.equal(yieldRedemptionDate, that.yieldRedemptionDate) &&
                Objects.equal(currencyCode, that.currencyCode) &&
                Objects.equal(executionId, that.executionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commission, currencyCode, executionId, realizedProfitAndLoss, yield, yieldRedemptionDate);
    }

    @Override
    public String toString() {
        return "CommissionReport{" +
                "commission=" + commission +
                ", currencyCode='" + currencyCode + '\'' +
                ", executionId='" + executionId + '\'' +
                ", realizedProfitAndLoss=" + realizedProfitAndLoss +
                ", yield=" + yield +
                ", yieldRedemptionDate=" + yieldRedemptionDate +
                '}';
    }
}
