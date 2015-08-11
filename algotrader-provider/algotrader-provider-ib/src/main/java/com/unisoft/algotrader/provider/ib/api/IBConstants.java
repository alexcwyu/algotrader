package com.unisoft.algotrader.provider.ib.api;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alex on 8/3/15.
 */
public class IBConstants {

    public static final String EMPTY = "";
    public static final String UNKNOWN = "UNKNOWN";
    public static final byte[] EMPTY_BYTES = EMPTY.getBytes();
    public static final byte[] UNKNOWN_BYTES = UNKNOWN.getBytes();

    public enum SecType{

        STOCK(Instrument.InstType.Stock, "STK".getBytes()),
        OPTION(Instrument.InstType.Option, "OPT".getBytes()),
        FUTURE(Instrument.InstType.Future, "FUT".getBytes()),
        INDEX(Instrument.InstType.Index, "IND".getBytes()),
        FUTURE_ON_OPTION(Instrument.InstType.FutureOption, "FOP".getBytes()),
        FOREX(Instrument.InstType.FX, "CASH".getBytes()),
        COMBO(Instrument.InstType.Combo, "BAG".getBytes());

        private static Map<Instrument.InstType, byte[]> map = Maps.newHashMap();
        private static Map<byte[], Instrument.InstType> byteArrayMap = Maps.newHashMap();

        private final Instrument.InstType instType;
        private final byte[] bytes;

        SecType(Instrument.InstType instType, byte[] bytes){
            this.instType = instType;
            this.bytes = bytes;
        }

        public Instrument.InstType instType() {
            return instType;
        }

        public byte[] bytes() {
            return bytes;
        }

        static {
            for (SecType secType : SecType.values()) {
                map.put(secType.instType, secType.bytes);
                byteArrayMap.put(secType.bytes, secType.instType);
            }
        }

        public static byte[] convert(Instrument.InstType instType){
            if (instType == null)
                return EMPTY_BYTES;
            if (instType == Instrument.InstType.ETF)
                return STOCK.bytes;
            if (map.containsKey(instType))
                return map.get(instType);
            return UNKNOWN_BYTES;
        }

        public static Instrument.InstType convert(byte[] bytes){
            if (bytes!= null) {
                for (SecType secType : SecType.values()) {
                    if (Arrays.equals(bytes, secType.bytes)) {
                        return secType.instType;
                    }
                }
            }
            return null;
        }
    }

    public enum TIF{

        UNKNOWN(TimeInForce.Undefined, "UNKNOWN".getBytes()),
        DAY(TimeInForce.Day, "DAY".getBytes()),
        GOOD_TILL_CANCEL(TimeInForce.GTC, "GTC".getBytes()),
        IMMEDIATE_OR_CANCEL(TimeInForce.FOK, "IOC".getBytes()),
        GOOD_TILL_DATE(TimeInForce.GoodTillDate, "GTD".getBytes());

        private static Map<TimeInForce, byte[]> map = Maps.newHashMap();
        private static Map<byte[], TimeInForce> byteArrayMap = Maps.newHashMap();

        private final TimeInForce tif;
        private final byte[] bytes;

        TIF(TimeInForce tif, byte[] bytes){
            this.tif = tif;
            this.bytes = bytes;
        }

        public TimeInForce tif() {
            return tif;
        }

        public byte[] bytes() {
            return bytes;
        }

        static {
            for (TIF timeInForce : TIF.values()) {
                map.put(timeInForce.tif, timeInForce.bytes);
                byteArrayMap.put(timeInForce.bytes, timeInForce.tif);
            }
        }

        public static byte[] convert(TimeInForce tif){
            if (tif == null)
                return EMPTY_BYTES;
            if (map.containsKey(tif))
                return map.get(tif);
            return UNKNOWN.bytes;
        }

        public static TimeInForce convert(byte[] bytes){
            if (bytes!= null) {
                for (TIF timeInForce : TIF.values()) {
                    if (Arrays.equals(bytes, timeInForce.bytes)) {
                        return timeInForce.tif;
                    }
                }
            }
            return null;
        }
    }

    public enum OptionRight{

        PUT(Instrument.PutCall.Put, "PUT".getBytes()),
        CALL(Instrument.PutCall.Call, "CALL".getBytes());

        private static Map<Instrument.PutCall, byte[]> map = Maps.newHashMap();
        private static Map<byte[], Instrument.PutCall> byteArrayMap = Maps.newHashMap();

        private final Instrument.PutCall putCall;
        private final byte[] bytes;

        OptionRight(Instrument.PutCall putCall, byte[] bytes){
            this.putCall = putCall;
            this.bytes = bytes;
        }

        public Instrument.PutCall putCall() {
            return putCall;
        }

        public byte[] bytes() {
            return bytes;
        }

        static {
            for (OptionRight optionRight : OptionRight.values()) {
                map.put(optionRight.putCall, optionRight.bytes);
                byteArrayMap.put(optionRight.bytes, optionRight.putCall);
            }
        }

        public static byte[] convert(Instrument.PutCall putCall){
            if (putCall == null)
                return EMPTY_BYTES;
            if (map.containsKey(putCall))
                return map.get(putCall);
            return UNKNOWN_BYTES;
        }

        public static Instrument.PutCall convert(byte[] bytes){
            if (bytes!= null) {
                for (OptionRight optionRight : OptionRight.values()) {
                    if (Arrays.equals(bytes, optionRight.bytes)) {
                        return optionRight.putCall;
                    }
                }
            }
            return null;
        }
    }

    public enum OrderStatus{

        UNKNOWN("UNKNOWN"),
        PENDING_SUBMIT("PendingSubmit"),
        PENDING_CANCEL("PendingCancel"),
        PRE_SUBMITTED("PreSubmitted"),
        SUBMITTED("Submitted"),
        CANCELLED("Cancelled"),
        FILLED("Filled"),
        INACTIVE("Inactive"),
        EMPTY("");

        private final String label;
        private static final Map<String, OrderStatus> MAP;

        private OrderStatus(final String label) {
            this.label = label;
        }

        static {
            MAP = Maps.newHashMap();
            for (final OrderStatus orderStatus : values()) {
                MAP.put(orderStatus.label.toUpperCase(), orderStatus);
            }
        }
        public static final OrderStatus fromLabel(final String label) {
            if (StringUtils.isBlank(label)) {
                return EMPTY;
            }
            final String labelUpperCase = label.toUpperCase();
            if (MAP.containsKey(labelUpperCase)) {
                return MAP.get(labelUpperCase);
            }
            return UNKNOWN;
        }
    }

    public enum OrderType{

        MARKET(OrdType.Market, "MARKET".getBytes()),
        LIMIT(OrdType.Limit, "LIMIT".getBytes()),
        STOP(OrdType.Stop, "STOP".getBytes()),
        STOP_LIMIT(OrdType.StopLimit, "STOP_LIMIT".getBytes()),
        MARKET_ON_CLOSE(OrdType.MarketOnClose, "MARKET_ON_CLOSE".getBytes()),
        LIMIT_ON_CLOSE(OrdType.LimitOnClose, "LIMIT_ON_CLOSE".getBytes()),
        TRAILING_STOP(OrdType.TrailingStop, "TRAILING_STOP".getBytes()),
        MARKET_TO_LIMIT(OrdType.MarketWithLeftoverAsLimit, "MARKET_TO_LIMIT".getBytes()),
        MARKET_IF_PRICE_TOUCHED(OrdType.MIT, "MARKET_IF_PRICE_TOUCHED".getBytes()),
        MARKET_ON_OPEN(OrdType.OnClose, "MARKET_ON_OPEN".getBytes()),
        UNKNOWN(OrdType.Undefined, "UNKNOWN".getBytes());

        private static Map<OrdType, byte[]> ordTypeMap = Maps.newHashMap();
        private static Map<byte[], OrdType> byteArrayMap = Maps.newHashMap();

        private final OrdType ordType;
        private final byte[] bytes;

        OrderType(OrdType ordType, byte[] bytes){
            this.ordType = ordType;
            this.bytes = bytes;
        }

        public OrdType ordType() {
            return ordType;
        }

        public byte[] bytes() {
            return bytes;
        }

        static {
            for (OrderType orderType : OrderType.values()) {
                ordTypeMap.put(orderType.ordType, orderType.bytes);
                byteArrayMap.put(orderType.bytes, orderType.ordType);
            }
        }

        public static byte[] convert(OrdType ordType){
            if (ordType == null)
                return EMPTY_BYTES;
            if (ordTypeMap.containsKey(ordType))
                return ordTypeMap.get(ordType);
            return UNKNOWN.bytes;
        }

        public static OrdType convert(byte[] bytes){
            if (bytes!= null) {
                for (OrderType orderType : OrderType.values()) {
                    if (Arrays.equals(bytes, orderType.bytes)) {
                        return orderType.ordType;
                    }
                }
            }
            return OrdType.Undefined;
        }
    }

    public enum Action{

        BUY(Side.Buy, "BUY".getBytes()),
        SELL(Side.Sell, "SELL".getBytes()),
        SSHORT(Side.SellShort, "SSHORT".getBytes()),
        UNKNOWN(Side.Undefined, "UNKNOWN".getBytes());

        private static Map<Side, byte[]> sideMap = Maps.newHashMap();
        private static Map<byte[], Side> byteArrayMap = Maps.newHashMap();

        private final Side side;
        private final byte[] bytes;

        Action(Side side, byte[] bytes){
            this.side = side;
            this.bytes = bytes;
        }

        public Side getSide() {
            return side;
        }

        public byte[] getBytes() {
            return bytes;
        }

        static {
            for (Action action : Action.values()) {
                sideMap.put(action.side, action.bytes);
                byteArrayMap.put(action.bytes, action.side);
            }
        }

        public static byte[] convert(Side side){
            if (side == null)
                return EMPTY_BYTES;
            if (sideMap.containsKey(side))
                return sideMap.get(side);
            return UNKNOWN.bytes;
        }

        public static Side convert(byte[] bytes){
            if (bytes!= null) {
                for (Action action : Action.values()) {
                    if (Arrays.equals(bytes, action.bytes)) {
                        return action.side;
                    }
                }
            }
            return Side.Undefined;
        }


    }

    public enum RealTimeBarDataType{
        UNKNOWN("UNKNOWN"), EMPTY(""), TRADES("TRADES"), BID("BID"), ASK("ASK"), MID_POINT("MIDPOINT");

        private final byte[] bytes;

        RealTimeBarDataType(String type){
            this.bytes = type.getBytes();
        }

        public byte[] getBytes() {
            return bytes;
        }
    }

    public enum ReturnedTickTypeFilter {

        UNKNOWN(-1), OPTION_VOLUME(100), OPTION_OPEN_INTEREST(101), HISTORICAL_VOLATILITY(104), OPTION_IMPLIED_VOLATILITY(
                106), INDEX_FUTURE_PREMIUM(162), MISCELLANEOUS_STATS(165), MARK_PRICE(221), AUCTION_VALUES(225),
        REAL_TIME_VOLUME(233), SHORTABLE(236), INVENTORY(256), FUNDAMENTAL_RATIOS(258),
        REAL_TIME_HISTORICAL_VOLATILITY(411);

        private final int id;
        private static final Map<Integer, ReturnedTickTypeFilter> MAP;

        static {
            MAP = Maps.newHashMap();
            for (final ReturnedTickTypeFilter filterReturnedTickType : values()) {
                MAP.put(filterReturnedTickType.getId(), filterReturnedTickType);
            }
        }

        private ReturnedTickTypeFilter(final int id) {
            this.id = id;
        }

        public final int getId() {
            return id;
        }

        public static final ReturnedTickTypeFilter fromId(final int id) {
            if (MAP.containsKey(id)) {
                return MAP.get(id);
            }
            return UNKNOWN;
        }
    }


    public interface BarSize{

        int SEC_1  = 1;
        int SEC_5  = 2;
        int SEC_15 = 3;
        int SEC_30 = 4;
        int MIN_1  = 5;
        int MIN_2  = 6;
        int MIN_5  = 7;
        int MIN_15 = 8;
        int MIN_30 = 9;
        int HOUR_1 = 10;
        int DAY_1  = 11;
    }

    public interface TickType{
        // Constants
        int BID_SIZE		= 0;
        int BID			= 1;
        int ASK			= 2;
        int ASK_SIZE		= 3;
        int LAST			= 4;
        int LAST_SIZE	= 5;
        int HIGH			= 6;
        int LOW			= 7;
        int VOLUME		= 8; // not defined
        int CLOSE		= 9; // last day close
    }

    public interface ErrorCode{
        // Constants
        int ORDER_INVALID_ID         = 106;
        int ORDER_INCOMPLETE_ORDER   = 107;
        int ORDER_PRICE_OUT_OF_RANGE = 109;
        int ORDER_INVALID_PRICE      = 110;

        int HISTORICAL_MARKET_DATA_SERVICE_ERROR = 162;

        int NO_SECURITY_DEFINITION = 200;

        int MARKET_DEPTH_DATA_RESET = 317;

        int SERVER_ERROR_VALIDATING_API_REQUEST = 321;
    }

    public interface FADataType{
        // Constants
        int GROUPS          = 1;
        int PROFILES        = 2;
        int ACCOUNT_ALIASES = 3;
    }

    public interface QuoteSide{
        // Constants
        int ASK = 0;
        int BID = 1;
    }

    public interface Origin{
        // Constants
        int  CUSTOMER            = 0;
        int  FIRM                = 1;
        char OPT_UNKNOWN         = '?';
        char OPT_BROKER_DEALER   = 'b';
        char OPT_CUSTOMER        = 'c';
        char OPT_FIRM            = 'f';
        char OPT_ISEMM           = 'm';
        char OPT_FARMM           = 'n';
        char OPT_SPECIALIST      = 'y';
        int  AUCTION_MATCH       = 1;
        int  AUCTION_IMPROVEMENT = 2;
        int  AUCTION_TRANSPARENT = 3;
    }
}
