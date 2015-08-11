package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.contract.SecurityType;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarDataType;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarSubscriptionRequest;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarUnsubscriptionRequest;
import ch.aonyx.broker.ib.api.data.historical.*;
import ch.aonyx.broker.ib.api.order.OrderType;
import ch.aonyx.broker.ib.api.util.StringIdUtils;
import com.unisoft.algotrader.model.event.data.DataType;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
                null,
                instrument.getSymbol(IBProvider.PROVIDER_ID),
                instrument.getExchId(IBProvider.PROVIDER_ID),
                instrument.getCcyId());
    }

    public static RealTimeBarSubscriptionRequest createRealTimeBarSubscriptionRequest(SubscriptionKey subscriptionKey, Instrument instrument){
        Contract contract = IBUtils.getContract(instrument);
        return new RealTimeBarSubscriptionRequest(contract, 5, contract.getSecurityType() == SecurityType.FOREX ? RealTimeBarDataType.MID_POINT : RealTimeBarDataType.TRADES, false);
    }

    public static RealTimeBarUnsubscriptionRequest createRealTimeBarUnsubscriptionRequest(Instrument instrument){
        Contract contract = IBUtils.getContract(instrument);
        String subId = StringIdUtils.uniqueIdFromContract(contract);
        return new RealTimeBarUnsubscriptionRequest(subId);
    }

    public static HistoricalDataSubscriptionRequest createHistoricalDataSubscriptionRequest(HistoricalSubscriptionKey subscriptionKey, Instrument instrument){
        Contract contract = IBUtils.getContract(instrument);
        HistoricalDataSubscriptionRequest request = new HistoricalDataSubscriptionRequest(contract,
                IBUtils.convertDate(subscriptionKey.toDate),
                IBUtils.calculateTimeSpan(subscriptionKey.fromDate, subscriptionKey.toDate),
                IBUtils.convertFreq(subscriptionKey.barSize),
                HistoricalDataType.TRADES,
                false,
                DateFormat.YYYYMMDD__HH_MM_SS
        );
        return request;
    }

    public static HistoricalDataType convertHistoricalDataType(DataType dataType){
        if (dataType == DataType.Quote){
            return HistoricalDataType.BID_ASK;
        }
        if (dataType == DataType.Trade){
            return HistoricalDataType.TRADES;
        }
        return HistoricalDataType.MID_POINT;
    }

    public static String convertSecurityType(Instrument.InstType instType){
        switch (instType){
            case ETF:
                return "STK";
            case Future:
                return "FUT";
            case FX:
                return "CASH";
            case Index:
                return "IND";
            case Option:
                return "OPT";
            case Stock:
                return "STK";
            default:
                throw new IllegalArgumentException("unsupported instType:" + instType);
        }
    }

    public static final int SEC_1 = 1;
    public static final int SEC_5 = 5;
    public static final int SEC_15 = 15;
    public static final int SEC_30 = 30;
    public static final int MIN_1 = 60;
    public static final int MIN_2 = 60 * 2;
    public static final int MIN_3 = 60 * 3;
    public static final int MIN_5 = 60 * 5;
    public static final int MIN_15 = 60 * 15;
    public static final int MIN_30 = 60 * 30;
    public static final int HOUR_1 = 60 * 60;
    public static final int DAY_1 = 60 * 60 * 24;


    public static BarSize convertFreq(int freq){
        if(freq <= SEC_1)
            return BarSize.ONE_SECOND;
        else if (freq <= SEC_5)
            return BarSize.FIVE_SECONDS;
        else if (freq <= SEC_15)
            return BarSize.FIFTEEN_SECONDS;
        else if (freq <= SEC_30)
            return BarSize.THIRTY_SECONDS;
        else if (freq <= MIN_1)
            return BarSize.ONE_MINUTE;
        else if (freq <= MIN_2)
            return BarSize.TWO_MINUTES;
        else if (freq <= MIN_3)
            return BarSize.THIRTY_MINUTES;
        else if (freq <= MIN_5)
            return BarSize.FIFTEEN_MINUTES;
        else if (freq <= MIN_15)
            return BarSize.FIFTEEN_MINUTES;
        else if (freq <= MIN_30)
            return BarSize.THIRTY_MINUTES;
        else if (freq <= HOUR_1)
            return BarSize.ONE_HOUR;
        return BarSize.ONE_DAY;
    }

    public static TimeSpan calculateTimeSpan(long fromDate, long toDate){
        long diff = toDate - fromDate;

        Duration duration = Duration.of(diff, ChronoUnit.SECONDS);
        int diffDays = (int)duration.toDays();
        Period p = Period.ofDays(diffDays);
        int years = p.getYears();
        int months = p.getMonths();
        int days = p.getDays();

        if (years > 0){
            return new TimeSpan(years, TimeSpanUnit.YEAR);
        }
        if (months > 0){
            return new TimeSpan(months, TimeSpanUnit.MONTH);
        }
        if (months > 0){
            return new TimeSpan(days, TimeSpanUnit.DAY);
        }
        return new TimeSpan((int)diff, TimeSpanUnit.SECOND);
    }

    public static long convertDate(String date){
        try {
            return FORMAT.parse(date).getTime();
        }
        catch (ParseException e){
            throw new RuntimeException("fail to parse date:"+date, e);
        }
    }

    public static String convertDate(long date){
        return FORMAT.format(new Date(date));
    }

    public static ch.aonyx.broker.ib.api.order.Order convertOrder (Order order){
        ch.aonyx.broker.ib.api.order.Order ibOrder = new ch.aonyx.broker.ib.api.order.Order();
        //ibOrder.setAccountName();

        ibOrder.setAction(null);
        ibOrder.setAllOrNone(order.tif == TimeInForce.FOK);
        ibOrder.setStopPrice(order.stopPrice);
        ibOrder.setLimitPrice(order.limitPrice);
        ibOrder.setOrderType(convertOrderType(order.ordType));
        ibOrder.setTimeInForce(convertTIF(order.tif));

        int quantity = (int)order.ordQty;
        if (order.side == Side.Sell  ||order.side == Side.SellShort ){
            quantity = -quantity;
        }

        ibOrder.setTotalQuantity(quantity);
        ibOrder.setTrailingStopPrice(order.trailingStopExecPrice);

        return ibOrder;
    }

    public static String convertAction(Side side){
        switch (side){
            case Buy:
                return "BUY";
            case Sell:
                return "SELL";
            case SellShort:
                return "SSHORT";
            default:
                throw new IllegalArgumentException("unknown order Action, side ="+side);
        }
    }

    public static ch.aonyx.broker.ib.api.order.TimeInForce convertTIF(TimeInForce timeInForce){
        switch (timeInForce){
            case Day:
                return ch.aonyx.broker.ib.api.order.TimeInForce.DAY;
            case GTC:
                return ch.aonyx.broker.ib.api.order.TimeInForce.GOOD_TILL_CANCEL;
            case IOC:
                return ch.aonyx.broker.ib.api.order.TimeInForce.IMMEDIATE_OR_CANCEL;
            case GoodTillDate:
                return ch.aonyx.broker.ib.api.order.TimeInForce.GOOD_TILL_DATE;
            case Undefined:
                return ch.aonyx.broker.ib.api.order.TimeInForce.UNKNOWN;
            default:
                throw new IllegalArgumentException("unknown TimeInForce ="+timeInForce);
        }
    }

    public static OrderType convertOrderType(OrdType ordType){
        switch (ordType){
            case Market:
                return OrderType.MARKET;
            case Limit:
                return OrderType.LIMIT;
            case Stop:
                return OrderType.STOP;
            case StopLimit:
                return OrderType.STOP_LIMIT;
            case MarketOnClose:
                return OrderType.MARKET_ON_CLOSE;
            case LimitOnClose:
                return OrderType.LIMIT_ON_CLOSE;
            case TrailingStop:
                return OrderType.TRAILING_STOP;
            case MarketWithLeftoverAsLimit:
                return OrderType.MARKET_TO_LIMIT;
            case MIT:
                return OrderType.MARKET_IF_PRICE_TOUCHED;
            case OnClose:
                return OrderType.MARKET_ON_OPEN;
            case Undefined:
                return OrderType.UNKNOWN;
            default:
                throw new IllegalArgumentException("unknown OrdType, OrdType ="+ordType);
        }
    }

    public static String convertPutCall(Instrument.PutCall putCall){
        if (putCall == null){
            return "";
        }
        if (putCall == Instrument.PutCall.Put) {
            return "PUT";
        }
        else {
            return "CALL";
        }
    }
}
