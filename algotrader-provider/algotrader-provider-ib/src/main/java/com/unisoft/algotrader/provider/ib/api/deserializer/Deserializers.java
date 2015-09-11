package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.api.event.IBEvent;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.util.Map;

/**
 * Created by alex on 8/2/15.
 */
public class Deserializers {

    private final Map<IncomingMessageId, Deserializer<? extends IBEvent>> deserializerMap = Maps.newHashMap();

    public Deserializers(int serverCurrentVersion, RefDataStore refDataStore){
        init(serverCurrentVersion, refDataStore);
    }

    private void init(int serverCurrentVersion, RefDataStore refDataStore){
        register(new AccountSummaryEndEventDeserializer(serverCurrentVersion));
        register(new AccountSummaryEventDeserializer(serverCurrentVersion));
        register(new AccountUpdateValueEventDeserializer(serverCurrentVersion));
        register(new AccountUpdateTimeEventDeserializer(serverCurrentVersion));
        register(new AccountUpdateValueEndEventDeserializer(serverCurrentVersion));
        register(new BondInstrumentSpecificationEventDeserializer(refDataStore, serverCurrentVersion));
        register(new CommissionReportEventDeserializer(serverCurrentVersion));
        register(new ContractSpecificationEndEventDeserializer(serverCurrentVersion));
        register(new DeltaNeutralValidationEventDeserializer(serverCurrentVersion));
        register(new DisplayGroupListEventDeserializer(serverCurrentVersion));
        register(new DisplayGroupUpdatedEventDeserializer(serverCurrentVersion));
        register(new ExecutionReportEventDeserializer(refDataStore, serverCurrentVersion));
        register(new ExecutionReportEndEventDeserializer(serverCurrentVersion));
        register(new FinancialAdvisorConfigurationEventDeserializer(serverCurrentVersion));
        register(new FundamentalDataEventDeserializer(serverCurrentVersion));
        register(new HistoricalDataEventDeserializer(serverCurrentVersion));
        register(new ContractSpecificationEventDeserializer(refDataStore, serverCurrentVersion));
        register(new ManagedAccountListEventDeserializer(serverCurrentVersion));
        register(new MarketDataTypeEventDeserializer(serverCurrentVersion));
        register(new MarketDepthLevelTwoUpdateEventDeserializer(serverCurrentVersion));
        register(new MarketDepthUpdateEventDeserializer(serverCurrentVersion));
        register(new MarketScannerDataEventDeserializer(refDataStore, serverCurrentVersion));
        register(new MarketScannerValidParametersEventDeserializer(serverCurrentVersion));
        register(new NewsBulletinUpdateEventDeserializer(serverCurrentVersion));
        register(new NextValidOrderIdEventDeserializer(serverCurrentVersion));
        register(new RetrieveOpenOrderEventDeserializer(refDataStore, serverCurrentVersion));
        register(new OrderStatusUpdateEventDeserializer(serverCurrentVersion));
        register(new PortfolioUpdateEventDeserializer(refDataStore, serverCurrentVersion));
        register(new PositionEndEventDeserializer(serverCurrentVersion));
        register(new PositionEventDeserializer(refDataStore, serverCurrentVersion));
        register(new RealTimeBarEventDeserializer(serverCurrentVersion));
        register(new RetrieveOpenOrderEndEventDeserializer(serverCurrentVersion));
        register(new ServerCurrentTimeEventDeserializer(serverCurrentVersion));
        register(new ServerMessageDeserializer(serverCurrentVersion));
        register(new TickEfpEventDeserializer(serverCurrentVersion));
        register(new TickGenericEventDeserializer(serverCurrentVersion));
        register(new TickOptionComputationEventDeserializer(serverCurrentVersion));
        register(new TickPriceEventDeserializer(serverCurrentVersion));
        register(new TickSizeEventDeserializer(serverCurrentVersion));
        register(new TickSnapshotEndEventDeserializer(serverCurrentVersion));
        register(new TickStringEventDeserializer(serverCurrentVersion));
        register(new VerifyCompletedEventDeserializer(serverCurrentVersion));
        register(new VerifyMessageAPIEventDeserializer(serverCurrentVersion));
    }

    protected void register(Deserializer<? extends IBEvent> deserializer){
        if (deserializerMap.containsKey(deserializer.messageId())){
            throw new RuntimeException("Deserializer has already been registered: "+deserializer.messageId());
        }
        deserializerMap.put(deserializer.messageId(), deserializer);
    }

    public Deserializer getDeserializer(final IncomingMessageId messageId) {
        if (deserializerMap.containsKey(messageId)) {
            return deserializerMap.get(messageId);
        }
        throw new IllegalArgumentException("unsupported messageID: "+messageId);
    }

}
