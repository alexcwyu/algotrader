package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.model.event.EventHandler;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.api.model.bulletin.NewsBulletinType;
import com.unisoft.algotrader.provider.ib.api.model.contract.ContractSpecification;
import com.unisoft.algotrader.provider.ib.api.model.contract.UnderlyingCombo;
import com.unisoft.algotrader.provider.ib.api.model.data.*;
import com.unisoft.algotrader.provider.ib.api.model.execution.CommissionReport;
import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
import com.unisoft.algotrader.provider.ib.api.model.order.OrderExecution;
import com.unisoft.algotrader.provider.ib.api.model.order.OrderStatus;

import java.util.List;

/**
 * Created by alex on 8/26/15.
 */
public interface IBEventHandler extends EventHandler {

    default void onIBEvent(final IBEvent ibEvent){
        ibEvent.on(this);
    }

    default void onAccountSummaryEvent(final AccountSummaryEvent accountSummaryEvent){}

    default void onAccountSummaryEvent(final long requestId, final String account, final String tag, final String value, final String currency){}

    default void onAccountSummaryEndEvent(final AccountSummaryEndEvent accountSummaryEndEvent){}

    default void onAccountSummaryEndEvent(final long requestId){}

    default void onAccountUpdateTimeEvent(final AccountUpdateTimeEvent accountUpdateTimeEvent){}

    default void onAccountUpdateTimeEvent(final String time){}

    default void onAccountUpdateValueEndEvent(final AccountUpdateValueEndEvent accountUpdateValueEndEvent){}

    default void onAccountUpdateValueEndEvent(final String accountName){}

    default void onAccountUpdateValueEvent(final AccountUpdateValueEvent accountUpdateValueEvent){}

    default void onAccountUpdateValueEvent(final String key, final String value, final String currency, final String accountName){}

    default void onBondInstrumentSpecificationEvent(final BondInstrumentSpecificationEvent bondInstrumentSpecificationEvent){}

    default void onBondInstrumentSpecificationEvent(final long requestId, final ContractSpecification contractSpecification){}

    default void onCommissionReportEvent(final CommissionReportEvent commissionReportEvent){}

    default void onCommissionReportEvent(final CommissionReport commissionReport){}

    default void onCompositeTickEvent(final CompositeTickEvent compositeTickEvent){}

    default void onDeltaNeutralValidationEvent(final DeltaNeutralValidationEvent deltaNeutralValidationEvent){}

    default void onDeltaNeutralValidationEvent(final long requestId, final UnderlyingCombo underlyingCombo){}

    default void onDeltaNeutralValidationEvent(final long requestId, final int instId, final double delta, final double price){}

    default void onDisplayGroupListEvent(final DisplayGroupListEvent displayGroupListEvent){}

    default void onDisplayGroupListEvent(final long requestId, final String groups){}

    default void onDisplayGroupUpdatedEvent(final DisplayGroupUpdatedEvent displayGroupUpdatedEvent){}

    default void onDisplayGroupUpdatedEvent(final long requestId, final String contractInfo){}

    default void onExecutionReportEvent(final ExecutionReportEvent executionReportEvent){}

    default void onExecutionReportEvent(final long requestId, final Instrument instrument, final ExecutionReport executionReport){}

    default void onExecutionReportEndEvent(final ExecutionReportEndEvent executionReportEndEvent){}

    default void onExecutionReportEndEvent(final long requestId){}

    default void onFinancialAdvisorConfigurationEvent(final FinancialAdvisorConfigurationEvent financialAdvisorConfigurationEvent){}

    default void onFinancialAdvisorConfigurationEvent(FinancialAdvisorDataType dataTypeValue, String xml){}

    default void onFundamentalDataEvent(final FundamentalDataEvent fundamentalDataEvent){}

    default void onFundamentalDataEvent(final long requestId, final String xml){}

    default void onHistoricalDataEvent(final HistoricalDataEvent historicalDataEvent){}

    default void onHistoricalDataEvent(final long requestId, final Bar bar){}

    default void onHistoricalDataEvent(final long requestId, final String dateTime, final double open, final double high,
                               final double low, final double close, final int volume, final int tradeNumber,
                               final double weightedAveragePrice, final boolean hasGap){}

    default void onHistoricalDataListEvent(final HistoricalDataListEvent historicalDataListEvent){}

    default void onHistoricalDataListEvent(long requestId, List<Bar> bar){}

    default void onInstrumentSpecificationEndEvent(final ContractSpecificationEndEvent contractSpecificationEndEvent){}

    default void onInstrumentSpecificationEndEvent(final long requestId){}

    default void onInstrumentSpecificationEvent(final ContractSpecificationEvent contractSpecificationEvent){}

    default void onInstrumentSpecificationEvent(final long requestId, final ContractSpecification contractSpecification){}

    default void onManagedAccountListEvent(final ManagedAccountListEvent managedAccountListEvent){}

    default void onManagedAccountListEvent(final String commaSeparatedAccountList){}

    default void onMarketDataTypeEvent(final MarketDataTypeEvent marketDataTypeEvent){}

    default void onMarketDataTypeEvent(final long requestId, MarketDataType marketDataType){}

    default void onMarketDepthLevelTwoUpdateEvent(final MarketDepthLevelTwoUpdateEvent marketDepthLevelTwoUpdateEvent){}

    default void onMarketDepthLevelTwoUpdateEvent(final long requestId, final int rowId,
                                      final String marketMakerName, final Operation operation, final BookSide bookSide, final double price, final int size){}

    default void onMarketDepthUpdateEvent(final MarketDepthUpdateEvent marketDepthUpdateEvent){}

    default void onMarketDepthUpdateEvent(final long requestId, final int rowId, final Operation operation,
                                    final BookSide bookSide, final double price, final int size){}

    default void onMarketScannerDataEvent(final MarketScannerDataEvent marketScannerDataEvent){}

    default void onMarketScannerDataEvent(final long requestId, final MarketScannerData marketScannerData){}

    default void onMarketScannerDataEvent(final long requestId, final int ranking,
                                         final ContractSpecification contractSpecification, final String distance, final String benchmark,
                                         final String projection, final String comboLegDescription){}

    default void onMarketScannerDataListEvent(final MarketScannerDataListEvent marketScannerDataListEvent){}

    default void onMarketScannerDataListEvent(long requestId, List<MarketScannerData> marketScannerData){}

    default void onMarketScannerValidParametersEvent(final MarketScannerValidParametersEvent marketScannerValidParametersEvent){}

    default void onMarketScannerValidParametersEvent(final String xml){}

    default void onNewsBulletinUpdateEvent(final NewsBulletinUpdateEvent newsBulletinUpdateEvent){}

    default void onNewsBulletinUpdateEvent(final int newsBulletinId, final NewsBulletinType newBulletinTypeValue,
                                     final String message, final String exchange){}

    default void onNextValidOrderIdEvent(final NextValidOrderIdEvent nextValidOrderIdEvent){}

    default void onNextValidOrderIdEvent(final int nextValidOrderId){}

    default void onOrderStatusUpdateEvent(final OrderStatusUpdateEvent orderStatusUpdateEvent){}

    default void onOrderStatusUpdateEvent(final long orderId, final OrderStatus orderStatus, final int filledQuantity,
                              final int remainingQuantity, final double averageFilledPrice, final int permanentId,
                              final int parentOrderId, final double lastFilledPrice, final int clientId, final String heldCause){}


    default void onPositionEvent(final PositionEvent positionEvent){}

    default void onPositionEvent(String account, Instrument instrument, int pos, double avgCost){}

    default void onPositionEndEvent(final PositionEndEvent positionEndEvent){}

    default void onPositionEndEvent(){}

    default void onPortfolioUpdateEvent(final PortfolioUpdateEvent portfolioUpdateEvent){}

    default void onPortfolioUpdateEvent(final Instrument instrument, final int marketPosition, final double marketPrice,
                                final double marketValue, final double averageCost, final double unrealizedProfitAndLoss,
                                final double realizedProfitAndLoss, final String accountName){}

    default void onRealTimeBarEvent(final RealTimeBarEvent realTimeBarEvent){}

    default void onRealTimeBarEvent(final long requestId, final long timestamp, final double open, final double high,
                                   final double low, final double close, final long volume, final double weightedAveragePrice,
                                   final int tradeNumber){}

    default void onRetrieveOpenOrderEndEvent(final RetrieveOpenOrderEndEvent retrieveOpenOrderEndEvent){}

    default void onRetrieveOpenOrderEndEvent(){}

    default void onRetrieveOpenOrderEvent(final RetrieveOpenOrderEvent retrieveOpenOrderEvent){}

    default void onRetrieveOpenOrderEvent(final long orderId, final Instrument instrument,
                                  final Order order, final OrderExecution orderExecution){}

    default void onServerCurrentTimeEvent(final ServerCurrentTimeEvent serverCurrentTimeEvent){}

    default void onServerCurrentTimeEvent(final long timestamp){}

    default void onServerMessageEvent(final ServerMessageEvent serverMessageEvent){}

    default void onServerMessageEvent(final long requestId, final int code, final String message){}

    default void onTickEfpEvent(final TickEfpEvent tickEfpEvent){}

    default void onTickEfpEvent(final long requestId, final TickType tickType, final double basisPoints,
                               final String formattedBasisPoints, final double impliedFuturePrice, final int holdDays,
                               final String futureExpiry, final double dividendImpact, final double dividendToExpiry){}

    default void onTickGenericEvent(final TickGenericEvent tickGenericEvent){}

    default void onTickGenericEvent(final long requestId, final TickType tickType, final double value){}

    default void onTickOptionComputationEvent(final TickOptionComputationEvent tickOptionComputationEvent){}

    default void onTickOptionComputationEvent(final long requestId, final TickType tickType,
                                             final double impliedVolatility, final double delta, final double price, final double presentValueDividend,
                                             final double gamma, final double vega, final double theta, final double underlyingPrice){}

    default void onTickPriceEvent(final TickPriceEvent tickPriceEvent){}

    default void onTickPriceEvent(final long requestId, final TickType tickType, final double price, final boolean autoExecute){}

    default void onTickSizeEvent(final TickSizeEvent tickSizeEvent){}

    default void onTickSizeEvent(final long requestId, final TickType tickType, final int size){}

    default void onTickSnapshotEndEvent(final TickSnapshotEndEvent tickSnapshotEndEvent){}

    default void onTickSnapshotEndEvent(final long requestId){}

    default void onTickStringEvent(final TickStringEvent tickStringEvent){}

    default void onTickStringEvent(final long requestId, final TickType tickType, final String value){}

    default void onVerifyMessageAPIEvent(final VerifyMessageAPIEvent verifyMessageAPIEvent){}

    default void onVerifyMessageAPIEvent(final String apiData){}

    default void onVerifyCompletedEvent(final VerifyCompletedEvent verifyCompletedEvent){}

    default void onVerifyCompletedEvent(final boolean isSuccessful, final String errorText){}
}
