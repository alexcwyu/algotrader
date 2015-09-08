package com.unisoft.algotrader.provider.ib.api.model.order;

import com.google.common.base.Objects;

/**
 * Created by alex on 9/9/15.
 */
public class ComboLeg {
    private static final String EMPTY = "";
    private OrderAction orderOrderAction;
    private int contractId;
    private String designatedLocation = EMPTY;
    private String exchange = EMPTY;
    private int exemptionCode = -1;
    private OpenCloseComboLeg openClose = OpenCloseComboLeg.SAME;
    private int ratio;
    private ShortSaleSlotInstitutional shortSaleSlotValue = ShortSaleSlotInstitutional.INAPPLICABLE;


    public OrderAction getOrderOrderAction() {
        return orderOrderAction;
    }

    public void setOrderOrderAction(OrderAction orderOrderAction) {
        this.orderOrderAction = orderOrderAction;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getDesignatedLocation() {
        return designatedLocation;
    }

    public void setDesignatedLocation(String designatedLocation) {
        this.designatedLocation = designatedLocation;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getExemptionCode() {
        return exemptionCode;
    }

    public void setExemptionCode(int exemptionCode) {
        this.exemptionCode = exemptionCode;
    }

    public OpenCloseComboLeg getOpenClose() {
        return openClose;
    }

    public void setOpenClose(OpenCloseComboLeg openClose) {
        this.openClose = openClose;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public ShortSaleSlotInstitutional getShortSaleSlotValue() {
        return shortSaleSlotValue;
    }

    public void setShortSaleSlotValue(ShortSaleSlotInstitutional shortSaleSlotValue) {
        this.shortSaleSlotValue = shortSaleSlotValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComboLeg)) return false;
        ComboLeg comboLeg = (ComboLeg) o;
        return Objects.equal(contractId, comboLeg.contractId) &&
                Objects.equal(exemptionCode, comboLeg.exemptionCode) &&
                Objects.equal(ratio, comboLeg.ratio) &&
                Objects.equal(orderOrderAction, comboLeg.orderOrderAction) &&
                Objects.equal(designatedLocation, comboLeg.designatedLocation) &&
                Objects.equal(exchange, comboLeg.exchange) &&
                Objects.equal(openClose, comboLeg.openClose) &&
                Objects.equal(shortSaleSlotValue, comboLeg.shortSaleSlotValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderOrderAction, contractId, designatedLocation, exchange, exemptionCode, openClose, ratio, shortSaleSlotValue);
    }

    @Override
    public String toString() {
        return "ComboLeg{" +
                "orderAction=" + orderOrderAction +
                ", contractId=" + contractId +
                ", designatedLocation='" + designatedLocation + '\'' +
                ", exchange='" + exchange + '\'' +
                ", exemptionCode=" + exemptionCode +
                ", openClose=" + openClose +
                ", ratio=" + ratio +
                ", shortSaleSlotValue=" + shortSaleSlotValue +
                '}';
    }
}
