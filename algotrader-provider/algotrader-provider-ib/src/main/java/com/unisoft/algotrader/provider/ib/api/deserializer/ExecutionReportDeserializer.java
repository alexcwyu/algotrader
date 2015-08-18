package com.unisoft.algotrader.provider.ib.api.deserializer;

import ch.aonyx.broker.ib.api.execution.Side;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.InputStreamUtils;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;


/**
 * Created by alex on 8/13/15.
 */
public class ExecutionReportDeserializer extends Deserializer {

    private final RefDataStore refDataStore;

    public ExecutionReportDeserializer(int serverCurrentVersion, RefDataStore refDataStore){
        super(IncomingMessageId.EXECUTION_REPORT, serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        int requestId = -1;
        if (getVersion() >= 7) {
            requestId = readInt(inputStream);
        }
        final int orderId = readInt(inputStream);
        final Instrument instrument = parseInstrument(inputStream);
        final ExecutionReport executionReport = consumeExecutionReport(inputStream, orderId);
        ibSession.onExecutionReport(requestId, executionReport);
    }

    protected Instrument parseInstrument(final InputStream inputStream) {
        final int instId = (getVersion() >= 5)? InputStreamUtils.readInt(inputStream) : 0;
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = IBConstants.SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = readDouble(inputStream);
        final Instrument.PutCall putCall = IBConstants.OptionRight.convert(readString(inputStream));
        final String multiplier = (getVersion() >= 9)? readString(inputStream) : null;
        final String exchange = readString(inputStream);
        final String ccyCode = readString(inputStream);
        final String localSymbol = readString(inputStream);
        final String tradingClass = (getVersion() >= 10)? readString(inputStream) : null;

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrumnet symbol=" + symbol +", primaryExchange="+exchange);
        }

        return instrument;
    }

    protected ExecutionReport consumeExecutionReport(final InputStream inputStream, final int orderId){
        ExecutionReport executionReport = new ExecutionReport();
        executionReport.setOrderId(orderId);
        //TODO string to execID mapping?
        executionReport.setExecId(Long.parseLong(readString(inputStream)));
        String time = readString(inputStream);
        String account = readString(inputStream);
        String exchange = readString(inputStream);
        executionReport.setSide(IBConstants.IBSide.convert(readString(inputStream)));
        executionReport.setLastQty(readInt(inputStream));
        executionReport.setLastPrice(readDouble(inputStream));
        if (getVersion() >= 2) {
            int permanentId = readInt(inputStream);
        }
        if (getVersion() >= 3) {
            int clientId = readInt(inputStream);
        }
        if (getVersion() >= 4) {
            int liquidation = readInt(inputStream);
        }
        if (getVersion() >= 6) {
            executionReport.setFilledQty(readInt(inputStream));
            executionReport.setAvgPrice(readDouble(inputStream));
        }
        if (getVersion() >= 8) {
            String orderRef = readString(inputStream);
        }
        if (getVersion() >= 9) {
            String economicValueRule = readString(inputStream);
            double economicValueMultiplier = readDouble(inputStream);
        }
        return executionReport;
    }
}