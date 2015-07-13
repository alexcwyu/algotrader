package com.unisoft.algotrader.utils.networking.aeron;

import com.unisoft.algotrader.utils.networking.DataHandler;
import uk.co.real_logic.aeron.common.RateReporter;
import uk.co.real_logic.aeron.samples.SamplesUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 4/19/15.
 */
public class AeronUtils {

    public static RateReporter simpleRateReporter() {
        return new RateReporter(TimeUnit.SECONDS.toNanos(1), SamplesUtil::printRate);
    }

    public static DataHandler rateReporterHandler(final RateReporter reporter)
    {
        return (data) -> reporter.onMessage(1, data.length);
    }
}
