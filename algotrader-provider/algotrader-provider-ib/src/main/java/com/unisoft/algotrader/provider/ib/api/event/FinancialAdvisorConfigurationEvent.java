package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.IBConstants;

/**
 * Created by alex on 8/26/15.
 */
public class FinancialAdvisorConfigurationEvent extends IBEvent<FinancialAdvisorConfigurationEvent>  {

    public final IBConstants.FinancialAdvisorDataType dataTypeValue;
    public final String xml;
    public FinancialAdvisorConfigurationEvent(final IBConstants.FinancialAdvisorDataType dataTypeValue, final String xml){
        this.dataTypeValue = dataTypeValue;
        this.xml = xml;
    }
}