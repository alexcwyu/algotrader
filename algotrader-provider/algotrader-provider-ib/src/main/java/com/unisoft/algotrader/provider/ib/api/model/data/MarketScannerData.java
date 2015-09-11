package com.unisoft.algotrader.provider.ib.api.model.data;

import com.unisoft.algotrader.provider.ib.api.model.contract.ContractSpecification;

/**
 * Created by alex on 8/19/15.
 */
public class MarketScannerData {

    private final int ranking;
    private final ContractSpecification contractSpecification;
    private final String distance;
    private final String benchmark;
    private final String projection;
    private final String comboLegDescription;

    public MarketScannerData(int ranking, ContractSpecification contractSpecification, String distance, String benchmark, String projection, String comboLegDescription) {

        this.ranking = ranking;
        this.contractSpecification = contractSpecification;
        this.distance = distance;
        this.benchmark = benchmark;
        this.projection = projection;
        this.comboLegDescription = comboLegDescription;
    }

    public int getRanking() {
        return ranking;
    }

    public ContractSpecification getContractSpecification() {
        return contractSpecification;
    }

    public String getDistance() {
        return distance;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public String getProjection() {
        return projection;
    }

    public String getComboLegDescription() {
        return comboLegDescription;
    }

    @Override
    public String toString() {
        return "MarketScannerData{" +
                "ranking=" + ranking +
                ", instrumentSpecification=" + contractSpecification +
                ", distance='" + distance + '\'' +
                ", benchmark='" + benchmark + '\'' +
                ", projection='" + projection + '\'' +
                ", comboLegDescription='" + comboLegDescription + '\'' +
                '}';
    }
}
