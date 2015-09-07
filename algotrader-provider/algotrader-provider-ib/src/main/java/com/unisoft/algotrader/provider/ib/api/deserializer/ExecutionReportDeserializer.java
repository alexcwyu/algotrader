package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.model.constants.IBSide;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.constants.SecType;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;


/**
 * Created by alex on 8/13/15.
 */
public class ExecutionReportDeserializer extends Deserializer {


    public ExecutionReportDeserializer(){
        super(IncomingMessageId.EXECUTION_REPORT);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        int requestId = (version >= 7) ? readInt(inputStream) : -1;
        final int orderId = readInt(inputStream);
        final Instrument instrument = consumeInstrument(version, inputStream, ibProvider);
        final ExecutionReport executionReport = consumeExecutionReport(version, inputStream, orderId);
        ibProvider.onExecutionReportEvent(requestId, instrument, executionReport);
    }

    protected Instrument consumeInstrument(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int instId = (version >= 5)? InputStreamUtils.readInt(inputStream) : 0;
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = readDouble(inputStream);
        final Instrument.PutCall putCall = OptionRight.convert(readString(inputStream));
        final String multiplier = (version >= 9)? readString(inputStream) : null;
        final String exchange = readString(inputStream);
        final String ccyCode = readString(inputStream);
        final String localSymbol = readString(inputStream);
        final String tradingClass = (version >= 10)? readString(inputStream) : null;

        Instrument instrument = ibProvider.getRefDataStore().getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, exchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrumnet symbol=" + symbol +", primaryExchange="+exchange);
        }

        return instrument;
    }

    protected ExecutionReport consumeExecutionReport(final int version, final InputStream inputStream, final int extOrderId){
        ExecutionReport executionReport = new ExecutionReport();
        executionReport.setExtOrderId(extOrderId);
        //TODO string to execID mapping?
        executionReport.setExecId(Long.parseLong(readString(inputStream)));
        String time = readString(inputStream);
        String account = readString(inputStream);
        String exchange = readString(inputStream);
        executionReport.setSide(IBSide.convert(readString(inputStream)));
        executionReport.setLastQty(readInt(inputStream));
        executionReport.setLastPrice(readDouble(inputStream));
        if (version >= 2) {
            int permanentId = readInt(inputStream);
        }
        if (version >= 3) {
            int clientId = readInt(inputStream);
        }
        if (version >= 4) {
            int liquidation = readInt(inputStream);
        }
        if (version >= 6) {
            executionReport.setFilledQty(readInt(inputStream));
            executionReport.setAvgPrice(readDouble(inputStream));
        }
        if (version >= 8) {
            String orderRef = readString(inputStream);
        }
        if (version >= 9) {
            String economicValueRule = readString(inputStream);
            double economicValueMultiplier = readDouble(inputStream);
        }
        return executionReport;
    }
}