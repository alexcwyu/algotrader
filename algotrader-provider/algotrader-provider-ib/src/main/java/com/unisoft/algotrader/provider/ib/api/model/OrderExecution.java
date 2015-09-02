package com.unisoft.algotrader.provider.ib.api.model;

import com.google.common.base.Objects;
import com.unisoft.algotrader.provider.ib.api.model.constants.OrderStatus;

/**
 * Created by alex on 8/26/15.
 */
public class OrderExecution {

    private static final String EMTPY = "";
    private double commission;
    private String commissionCurrencyCode = EMTPY;
    private String equityWithLoan = EMTPY;
    private String initialMargin = EMTPY;
    private String maintenanceMargin = EMTPY;
    private double maxCommission;
    private double minCommission;
    private OrderStatus orderStatus = OrderStatus.EMPTY;
    private String warningText = EMTPY;

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getCommissionCurrencyCode() {
        return commissionCurrencyCode;
    }

    public void setCommissionCurrencyCode(String commissionCurrencyCode) {
        this.commissionCurrencyCode = commissionCurrencyCode;
    }

    public String getEquityWithLoan() {
        return equityWithLoan;
    }

    public void setEquityWithLoan(String equityWithLoan) {
        this.equityWithLoan = equityWithLoan;
    }

    public String getInitialMargin() {
        return initialMargin;
    }

    public void setInitialMargin(String initialMargin) {
        this.initialMargin = initialMargin;
    }

    public String getMaintenanceMargin() {
        return maintenanceMargin;
    }

    public void setMaintenanceMargin(String maintenanceMargin) {
        this.maintenanceMargin = maintenanceMargin;
    }

    public double getMaxCommission() {
        return maxCommission;
    }

    public void setMaxCommission(double maxCommission) {
        this.maxCommission = maxCommission;
    }

    public double getMinCommission() {
        return minCommission;
    }

    public void setMinCommission(double minCommission) {
        this.minCommission = minCommission;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getWarningText() {
        return warningText;
    }

    public void setWarningText(String warningText) {
        this.warningText = warningText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderExecution)) return false;
        OrderExecution that = (OrderExecution) o;
        return Objects.equal(commission, that.commission) &&
                Objects.equal(maxCommission, that.maxCommission) &&
                Objects.equal(minCommission, that.minCommission) &&
                Objects.equal(commissionCurrencyCode, that.commissionCurrencyCode) &&
                Objects.equal(equityWithLoan, that.equityWithLoan) &&
                Objects.equal(initialMargin, that.initialMargin) &&
                Objects.equal(maintenanceMargin, that.maintenanceMargin) &&
                Objects.equal(orderStatus, that.orderStatus) &&
                Objects.equal(warningText, that.warningText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commission, commissionCurrencyCode, equityWithLoan, initialMargin, maintenanceMargin, maxCommission, minCommission, orderStatus, warningText);
    }

    @Override
    public String toString() {
        return "OrderExecution{" +
                "commission=" + commission +
                ", commissionCurrencyCode='" + commissionCurrencyCode + '\'' +
                ", equityWithLoan='" + equityWithLoan + '\'' +
                ", initialMargin='" + initialMargin + '\'' +
                ", maintenanceMargin='" + maintenanceMargin + '\'' +
                ", maxCommission=" + maxCommission +
                ", minCommission=" + minCommission +
                ", orderStatus=" + orderStatus +
                ", warningText='" + warningText + '\'' +
                "} " + super.toString();
    }
}
