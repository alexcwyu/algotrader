package com.unisoft.algotrader.provider.ib.neoib;

import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.contract.SecurityType;

/**
 * Created by alex on 6/24/15.
 */
public class ContractUtil {
    public static Contract getForexContract(final String symbol) {
        final Contract contract = new Contract();
        contract.setCurrencyCode("USD");
        contract.setExchange("IDEALPRO");
        contract.setSecurityType(SecurityType.FOREX);
        contract.setSymbol(symbol);
        return contract;
    }


    public static Contract getContract(SecurityType type, String symbol, String exchange, String currency){
        final Contract contract = new Contract();
        contract.setCurrencyCode(currency);
        contract.setExchange(exchange);
        contract.setSecurityType(type);
        contract.setSymbol(symbol);
        return contract;
    }

    public static Contract getStockContract(String symbol, String exchange, String currency){
        return getContract(SecurityType.STOCK, symbol, exchange, currency);
    }
}
