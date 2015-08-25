package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.util.Map;

/**
 * Created by alex on 8/2/15.
 */
public class DeserializerFactory {

    private final Map<IncomingMessageId, Deserializer<? extends Event>> deserializerMap = Maps.newHashMap();

    public DeserializerFactory(){
        register(new AccountSummaryEndEventDeserializer());
        register(new AccountSummaryEventDeserializer());
        register(new AccountUpdateEventDeserializer());
        register(new AccountUpdateTimeEventDeserializer());
        register(new AccountUpdateValueEndEventDeserializer());
        register(new BondInstrumentSpecificationEventDeserializer());
        register(new CommissionReportEventDeserializer());
        register(new ContractSpecificationEndEventDeserializer());
        register(new DeltaNeutralValidationEventDeserializer());
        register(new DisplayGroupListEventDeserializer());
        register(new DisplayGroupUpdatedEventDeserializer());
        register(new ExecutionReportDeserializer());
        register(new ExecutionReportEndEventDeserializer());
        register(new FinancialAdvisorConfigurationEventDeserializer());
        register(new FundamentalDataEventDeserializer());
        register(new HistoricalDataEventDeserializer());
        register(new InstrumentSpecificationEventDeserializer());
        register(new ManagedAccountListEventDeserializer());
        register(new MarketDataTypeEventDeserializer());
        register(new MarketDepthL2UpdateDeserializer());
        register(new MarketDepthUpdateDeserializer());
        register(new MarketScannerDataEventDeserializer());
        register(new MarketScannerValidParametersEventDeserializer());
        register(new NewsBulletinUpdateDeserializer());
        register(new NextValidOrderIdEventDeserializer());
        register(new OpenOrderEventDeserializer());
        register(new OrderStatusDeserializer());
        register(new PortfolioUpdateEventDeserializer());
        register(new PositionEndEventDeserializer());
        register(new PositionEventDeserializer());
        register(new RealTimeBarEventDeserializer());
        register(new RetrieveOpenOrderEndEventDeserializer());
        register(new ServerCurrentTimeDeserializer());
        register(new ServerMessageDeserializer());
        register(new TickEFPEventDeserializer());
        register(new TickGenericEventDeserializer());
        register(new TickOptionComputationEventDeserializer());
        register(new TickPriceDeserializer());
        register(new TickSizeDeserializer());
        register(new TickSnapshotEndEventDeserializer());
        register(new TickStringEventDeserializer());
        register(new VerifyCompletedEventDeserializer());
        register(new VerifyMessageAPIEventDeserializer());
    }

    protected void register(Deserializer<? extends Event> deserializer){
        if (deserializerMap.containsKey(deserializer.messageId())){
            throw new RuntimeException("Deserializer has already been registered: "+deserializer.messageId());
        }
        deserializerMap.put(deserializer.messageId(), deserializer);
    }

    public <M extends Event> Deserializer<M> getDeserializer(final IncomingMessageId messageId) {
        if (deserializerMap.containsKey(messageId)) {
            return (Deserializer<M>) deserializerMap.get(messageId);
        }
        throw new IllegalArgumentException("unsupported messageID: "+messageId);
    }

}
