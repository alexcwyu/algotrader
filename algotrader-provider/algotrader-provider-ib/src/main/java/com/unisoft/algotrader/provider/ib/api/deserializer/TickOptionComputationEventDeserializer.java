package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class TickOptionComputationEventDeserializer extends Deserializer {

    private static final int NOT_YET_COMPUTED_0 = 0;
    private static final int NOT_YET_COMPUTED_1 = 1;
    private static final int VERSION = 6;

    public TickOptionComputationEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.TICK_OPTION_COMPUTATION, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double impliedVolatility = getComputedValue0(inputStream);
        final double delta = getComputedValue1(inputStream);
        double price = Double.MAX_VALUE;
        double presentValueDividend = Double.MAX_VALUE;
        double gamma = Double.MAX_VALUE;
        double vega = Double.MAX_VALUE;
        double theta = Double.MAX_VALUE;
        double underlyingPrice = Double.MAX_VALUE;
        if ((getVersion() >= VERSION) || (tickType == IBConstants.TickType.MODEL_OPTION_COMPUTATION.getValue())) {
            price = getComputedValue0(inputStream);
            presentValueDividend = getComputedValue0(inputStream);
        }
        if (getVersion() >= VERSION) {
            gamma = getComputedValue1(inputStream);
            vega = getComputedValue1(inputStream);
            theta = getComputedValue1(inputStream);
            underlyingPrice = getComputedValue0(inputStream);
        }

        ibSession.onTickOptionComputationEvent(requestId, tickType,
        impliedVolatility, delta, price, presentValueDividend,
        gamma, vega, theta, underlyingPrice);
    }


    private double getComputedValue0(final InputStream inputStream) {
        double value = readDouble(inputStream);
        if (value < NOT_YET_COMPUTED_0) {
            value = Double.MAX_VALUE;
        }
        return value;
    }

    private double getComputedValue1(final InputStream inputStream) {
        double value = readDouble(inputStream);
        if (Math.abs(value) > NOT_YET_COMPUTED_1) {
            value = Double.MAX_VALUE;
        }
        return value;
    }
}