package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.contract.SecurityType;
import com.unisoft.algotrader.model.refdata.Instrument;
import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;
import uk.co.real_logic.aeron.Subscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by alex on 7/27/15.
 */
public class IBUtils {

    public static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
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

    public static Contract getContract(Instrument instrument){
        return getContract(
                convertSecurityType(instrument.getType()),
                instrument.getSymbol(IBProvider.PROVIDER_ID),
                instrument.getExchId(IBProvider.PROVIDER_ID),
                instrument.getCcyId());
    }

    public static SecurityType convertSecurityType(Instrument.InstType instType){
        switch (instType){
            case ETF:
                return SecurityType.STOCK;
            case Future:
                return SecurityType.FUTURE;
            case FX:
                return SecurityType.FOREX;
            case Index:
                return SecurityType.INDEX;
            case Option:
                return SecurityType.OPTION;
            case Stock:
                return SecurityType.STOCK;
            default:
                throw new IllegalArgumentException("unsupported instType:" + instType);
        }
    }

    public static long convertDate(String date){
        try {
            return FORMAT.parse(date).getTime();
        }
        catch (ParseException e){
            throw new RuntimeException("fail to parse date:"+date, e);
        }
    }
}
