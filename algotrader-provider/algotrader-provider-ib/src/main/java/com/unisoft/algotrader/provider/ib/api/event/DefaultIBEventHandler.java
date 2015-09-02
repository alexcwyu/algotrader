package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.api.model.*;
import com.unisoft.algotrader.provider.ib.api.model.constants.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by alex on 9/3/15.
 */
public class DefaultIBEventHandler implements IBEventHandler{


    private static final Logger LOG = LogManager.getLogger(DefaultIBEventHandler.class);

    @Override
    public void onAccountSummaryEvent(AccountSummaryEvent accountSummaryEvent) {
        LOG.debug(accountSummaryEvent);
    }

    @Override
    public void onAccountSummaryEvent(int reqId, String account, String tag, String value, String currency) {
        LOG.debug("onAccountSummaryEvent reqId {}, account {}, tag {}, value {}, currency {}", reqId, account, tag, value, currency);
    }

    @Override
    public void onAccountSummaryEndEvent(AccountSummaryEndEvent accountSummaryEndEvent) {
        LOG.debug(accountSummaryEndEvent);
    }

    @Override
    public void onAccountSummaryEndEvent(int reqId) {
        LOG.debug("onAccountSummaryEndEvent reqId {}", reqId);
    }

    @Override
    public void onAccountUpdateTimeEvent(AccountUpdateTimeEvent accountUpdateTimeEvent) {
        LOG.debug(accountUpdateTimeEvent);
    }

    @Override
    public void onAccountUpdateTimeEvent(String time) {
        LOG.debug("onAccountUpdateTimeEvent time {}", time);
    }

    @Override
    public void onAccountUpdateValueEndEvent(AccountUpdateValueEndEvent accountUpdateValueEndEvent) {
        LOG.debug(accountUpdateValueEndEvent);
    }

    @Override
    public void onAccountUpdateValueEndEvent(String accountName) {
        LOG.debug("onAccountUpdateValueEndEvent accountName {}", accountName);
    }

    @Override
    public void onAccountUpdateValueEvent(AccountUpdateValueEvent accountUpdateValueEvent) {
        LOG.debug(accountUpdateValueEvent);
    }

    @Override
    public void onAccountUpdateValueEvent(String key, String value, String currency, String accountName) {
        LOG.debug("onAccountUpdateValueEvent key {}, value {}, currency {}, accountName {}", key, value, currency, accountName);
    }

    @Override
    public void onBondInstrumentSpecificationEvent(BondInstrumentSpecificationEvent bondInstrumentSpecificationEvent) {
        LOG.debug(bondInstrumentSpecificationEvent);
    }

    @Override
    public void onBondInstrumentSpecificationEvent(int requestId, InstrumentSpecification instrumentSpecification) {
        LOG.debug("onBondInstrumentSpecificationEvent requestId {}, contractSpecification {}", requestId, instrumentSpecification);
    }

    @Override
    public void onCommissionReportEvent(CommissionReportEvent commissionReportEvent) {
        LOG.debug(commissionReportEvent);
    }

    @Override
    public void onCommissionReportEvent(CommissionReport commissionReport) {
        LOG.debug("onCommissionReportEvent commissionReport {}", commissionReport);
    }

    @Override
    public void onCompositeTickEvent(CompositeTickEvent compositeTickEvent) {
        LOG.debug(compositeTickEvent);
    }

    @Override
    public void onDeltaNeutralValidationEvent(DeltaNeutralValidationEvent deltaNeutralValidationEvent) {
        LOG.debug(deltaNeutralValidationEvent);
    }

    @Override
    public void onDeltaNeutralValidationEvent(int requestId, UnderlyingCombo underlyingCombo) {
        LOG.debug("onDeltaNeutralValidationEvent requestId {}, underlyingCombo {}", requestId, underlyingCombo);
    }

    @Override
    public void onDeltaNeutralValidationEvent(int requestId, int instId, double delta, double price) {
        LOG.debug("onDeltaNeutralValidationEvent requestId {}, instId {}, delta {}, price {}", requestId, instId, delta, price);
    }

    @Override
    public void onDisplayGroupListEvent(DisplayGroupListEvent displayGroupListEvent) {
        LOG.debug(displayGroupListEvent);
    }

    @Override
    public void onDisplayGroupListEvent(int requestId, String groups) {
        LOG.debug("onDisplayGroupListEvent requestId {}, groups {}", requestId, groups);
    }

    @Override
    public void onDisplayGroupUpdatedEvent(DisplayGroupUpdatedEvent displayGroupUpdatedEvent) {
        LOG.debug(displayGroupUpdatedEvent);
    }

    @Override
    public void onDisplayGroupUpdatedEvent(int requestId, String contractInfo) {
        LOG.debug("onDisplayGroupUpdatedEvent requestId {}, contractInfo {}", requestId, contractInfo);
    }

    @Override
    public void onExecutionReportEvent(ExecutionReportEvent executionReportEvent) {
        LOG.debug(executionReportEvent);
    }

    @Override
    public void onExecutionReportEvent(int requestId, Instrument instrument, ExecutionReport executionReport) {
        LOG.debug("onExecutionReportEvent requestId {}, instrument {}, executionReport {}", requestId, instrument, executionReport);
    }

    @Override
    public void onExecutionReportEndEvent(ExecutionReportEndEvent executionReportEndEvent) {
        LOG.debug(executionReportEndEvent);
    }

    @Override
    public void onExecutionReportEndEvent(int requestId) {
        LOG.debug("onExecutionReportEndEvent requestId {}", requestId);
    }

    @Override
    public void onFinancialAdvisorConfigurationEvent(FinancialAdvisorConfigurationEvent financialAdvisorConfigurationEvent) {
        LOG.debug(financialAdvisorConfigurationEvent);
    }

    @Override
    public void onFinancialAdvisorConfigurationEvent(FinancialAdvisorDataType dataTypeValue, String xml) {
        LOG.debug("onFinancialAdvisorConfigurationEvent dataTypeValue {}, xml {}", dataTypeValue, xml);
    }

    @Override
    public void onFundamentalDataEvent(FundamentalDataEvent fundamentalDataEvent) {
        LOG.debug(fundamentalDataEvent);
    }

    @Override
    public void onFundamentalDataEvent(int requestId, String xml) {
        LOG.debug("onFundamentalDataEvent requestId {}, xml {}", requestId, xml);
    }

    @Override
    public void onHistoricalDataEvent(HistoricalDataEvent historicalDataEvent) {
        LOG.debug(historicalDataEvent);
    }

    @Override
    public void onHistoricalDataEvent(int requestId, Bar bar) {
        LOG.debug("onHistoricalDataEvent requestId {}, bar {}", requestId, bar);
    }

    @Override
    public void onHistoricalDataEvent(int requestId, String dateTime, double open, double high, double low, double close, int volume, int tradeNumber, double weightedAveragePrice, boolean hasGap) {
        LOG.debug("onHistoricalDataEvent requestId {}, dateTime {}, open {}, high {}, low {}, close {}, volume {}, tradeNumber {}, weightedAveragePrice {}, hasGap {}",
                requestId, dateTime, open, high, low, close, volume, tradeNumber, weightedAveragePrice, hasGap);
    }

    @Override
    public void onHistoricalDataListEvent(HistoricalDataListEvent historicalDataListEvent) {
        LOG.debug(historicalDataListEvent);
    }

    @Override
    public void onHistoricalDataListEvent(int requestId, List<Bar> bars) {
        LOG.debug("onHistoricalDataListEvent requestId {}, bars {}", requestId, bars);
    }

    @Override
    public void onInstrumentSpecificationEndEvent(InstrumentSpecificationEndEvent instrumentSpecificationEndEvent) {
        LOG.debug(instrumentSpecificationEndEvent);
    }

    @Override
    public void onInstrumentSpecificationEndEvent(int requestId) {
        LOG.debug("onInstrumentSpecificationEndEvent requestId {}", requestId);
    }

    @Override
    public void onInstrumentSpecificationEvent(InstrumentSpecificationEvent instrumentSpecificationEvent) {
        LOG.debug(instrumentSpecificationEvent);
    }

    @Override
    public void onInstrumentSpecificationEvent(int requestId, InstrumentSpecification instrumentSpecification) {
        LOG.debug("onInstrumentSpecificationEvent requestId {}, instrumentSpecification {}", requestId, instrumentSpecification);
    }

    @Override
    public void onManagedAccountListEvent(ManagedAccountListEvent managedAccountListEvent) {
        LOG.debug(managedAccountListEvent);
    }

    @Override
    public void onManagedAccountListEvent(String commaSeparatedAccountList) {
        LOG.debug("onManagedAccountListEvent commaSeparatedAccountList {}", commaSeparatedAccountList);
    }

    @Override
    public void onMarketDataTypeEvent(MarketDataTypeEvent marketDataTypeEvent) {
        LOG.debug(marketDataTypeEvent);
    }

    @Override
    public void onMarketDataTypeEvent(int requestId, MarketDataType marketDataType) {
        LOG.debug("onMarketDataTypeEvent requestId {}, marketDataType {}", requestId, marketDataType);
    }

    @Override
    public void onMarketDepthLevelTwoUpdateEvent(MarketDepthLevelTwoUpdateEvent marketDepthLevelTwoUpdateEvent) {
        LOG.debug(marketDepthLevelTwoUpdateEvent);
    }

    @Override
    public void onMarketDepthLevelTwoUpdateEvent(int requestId, int rowId, String marketMakerName, Operation operation, BookSide bookSide, double price, int size) {
        LOG.debug("onMarketDepthLevelTwoUpdateEvent requestId {}, rowId {}, marketMakerName {}, operation {}, bookSide {}, price {}, size {}",
                requestId, rowId, marketMakerName, operation, bookSide, price, size);
    }

    @Override
    public void onMarketDepthUpdateEvent(MarketDepthUpdateEvent marketDepthUpdateEvent) {
        LOG.debug(marketDepthUpdateEvent);
    }

    @Override
    public void onMarketDepthUpdateEvent(int requestId, int rowId, Operation operation, BookSide bookSide, double price, int size) {
        LOG.debug("onMarketDepthUpdateEvent requestId {}, rowId {}, operation {}, bookSide {}, price {}, size {}",
                requestId, rowId, operation, bookSide, price, size);
    }

    @Override
    public void onMarketScannerDataEvent(MarketScannerDataEvent marketScannerDataEvent) {
        LOG.debug(marketScannerDataEvent);
    }

    @Override
    public void onMarketScannerDataEvent(int requestId, MarketScannerData marketScannerData) {
        LOG.debug("onMarketScannerDataEvent requestId {}, marketScannerData {}", requestId, marketScannerData);
    }

    @Override
    public void onMarketScannerDataEvent(int requestId, int ranking, InstrumentSpecification instrumentSpecification, String distance, String benchmark, String projection, String comboLegDescription) {
        LOG.debug("onMarketScannerDataEvent requestId {}, ranking {}, instrumentSpecification {}, distance {}, benchmark {}, projection {}, comboLegDescription {}",
                requestId, ranking, instrumentSpecification, distance, benchmark, projection, comboLegDescription);
    }

    @Override
    public void onMarketScannerDataListEvent(MarketScannerDataListEvent marketScannerDataListEvent) {
        LOG.debug(marketScannerDataListEvent);
    }

    @Override
    public void onMarketScannerDataListEvent(int requestId, List<MarketScannerData> marketScannerData) {
        LOG.debug("onMarketScannerDataListEvent requestId {}, marketScannerData {}", requestId, marketScannerData);
    }

    @Override
    public void onMarketScannerValidParametersEvent(MarketScannerValidParametersEvent marketScannerValidParametersEvent) {
        LOG.debug(marketScannerValidParametersEvent);
    }

    @Override
    public void onMarketScannerValidParametersEvent(String xml) {
        LOG.debug("onMarketScannerValidParametersEvent xml {}", xml);
    }

    @Override
    public void onNewsBulletinUpdateEvent(NewsBulletinUpdateEvent newsBulletinUpdateEvent) {
        LOG.debug(newsBulletinUpdateEvent);
    }

    @Override
    public void onNewsBulletinUpdateEvent(int newsBulletinId, NewsBulletinType newBulletinTypeValue, String message, String exchange) {
        LOG.debug("onNewsBulletinUpdateEvent newsBulletinId {}, newBulletinTypeValue {}, message {}, exchange {}",
                newsBulletinId, newBulletinTypeValue, message, exchange);
    }

    @Override
    public void onNextValidOrderIdEvent(NextValidOrderIdEvent nextValidOrderIdEvent) {
        LOG.debug(nextValidOrderIdEvent);
    }

    @Override
    public void onNextValidOrderIdEvent(int nextValidOrderId) {
        LOG.debug("onNextValidOrderIdEvent nextValidOrderId {}", nextValidOrderId);
    }

    @Override
    public void onOrderStatusUpdateEvent(OrderStatusUpdateEvent orderStatusUpdateEvent) {
        LOG.debug(orderStatusUpdateEvent);
    }

    @Override
    public void onOrderStatusUpdateEvent(int orderId, OrderStatus orderStatus, int filledQuantity, int remainingQuantity, double averageFilledPrice, int permanentId, int parentOrderId, double lastFilledPrice, int clientId, String heldCause) {
        LOG.debug("onOrderStatusUpdateEvent orderId {}, orderStatus {}, filledQuantity {}, remainingQuantity {}, averageFilledPrice {}, permanentId {}, parentOrderId {}, lastFilledPrice {}, clientId {}, heldCause {}",
                orderId, orderStatus, filledQuantity, remainingQuantity, averageFilledPrice, permanentId, parentOrderId, lastFilledPrice, clientId, heldCause);
    }

    @Override
    public void onPositionEvent(PositionEvent positionEvent) {
        LOG.debug(positionEvent);
    }

    @Override
    public void onPositionEvent(String account, Instrument instrument, int pos, double avgCost) {
        LOG.debug("onPositionEvent account {}, instrument {}, pos {}, avgCost {}", account, instrument, pos, avgCost);
    }

    @Override
    public void onPositionEndEvent(PositionEndEvent positionEndEvent) {
        LOG.debug(positionEndEvent);
    }

    @Override
    public void onPositionEndEvent() {
        LOG.debug("onPositionEndEvent");
    }

    @Override
    public void onPortfolioUpdateEvent(PortfolioUpdateEvent portfolioUpdateEvent) {
        LOG.debug(portfolioUpdateEvent);
    }

    @Override
    public void onPortfolioUpdateEvent(Instrument instrument, int marketPosition, double marketPrice, double marketValue, double averageCost, double unrealizedProfitAndLoss, double realizedProfitAndLoss, String accountName) {
        LOG.debug("onPortfolioUpdateEvent instrument {}, marketPosition {}, marketPrice {}, marketValue {}, averageCost {}, unrealizedProfitAndLoss {}, realizedProfitAndLoss {}, accountName {}",
                instrument, marketPosition, marketPrice, marketValue, averageCost, unrealizedProfitAndLoss, realizedProfitAndLoss, accountName);
    }

    @Override
    public void onRealTimeBarEvent(RealTimeBarEvent realTimeBarEvent) {
        LOG.debug(realTimeBarEvent);
    }

    @Override
    public void onRealTimeBarEvent(int requestId, long timestamp, double open, double high, double low, double close, long volume, double weightedAveragePrice, int tradeNumber) {
        LOG.debug("onRealTimeBarEvent requestId {}, timestamp {}, open {}, high {}, low {}, close {}, volume {}, weightedAveragePrice {}, tradeNumber {}",
                requestId, timestamp, open, high, low, close, volume, weightedAveragePrice, tradeNumber);
    }

    @Override
    public void onRetrieveOpenOrderEndEvent(RetrieveOpenOrderEndEvent retrieveOpenOrderEndEvent) {
        LOG.debug(retrieveOpenOrderEndEvent);
    }

    @Override
    public void onRetrieveOpenOrderEndEvent() {
        LOG.debug("onRetrieveOpenOrderEndEvent");
    }

    @Override
    public void onRetrieveOpenOrderEvent(RetrieveOpenOrderEvent retrieveOpenOrderEvent) {
        LOG.debug("onRetrieveOpenOrderEvent retrieveOpenOrderEvent {}", retrieveOpenOrderEvent);
    }

    @Override
    public void onRetrieveOpenOrderEvent(long orderId, Instrument instrument, Order order, OrderExecution orderExecution) {
        LOG.debug("onRetrieveOpenOrderEvent orderId {}, instrument {}, order {}, orderExecution {}", orderId, instrument, order, orderExecution);
    }

    @Override
    public void onServerCurrentTimeEvent(ServerCurrentTimeEvent serverCurrentTimeEvent) {
        LOG.debug(serverCurrentTimeEvent);
    }

    @Override
    public void onServerCurrentTimeEvent(long timestamp) {
        LOG.debug("onServerCurrentTimeEvent timestamp {}", timestamp);
    }

    @Override
    public void onServerMessageEvent(ServerMessageEvent serverMessageEvent) {
        LOG.debug(serverMessageEvent);
    }

    @Override
    public void onServerMessageEvent(int requestId, int code, String message) {
        LOG.debug("onServerMessageEvent requestId {}, code {}, message {}", requestId, code, message);
    }

    @Override
    public void onTickEfpEvent(TickEfpEvent tickEfpEvent) {
        LOG.debug(tickEfpEvent);
    }

    @Override
    public void onTickEfpEvent(int requestId, TickType tickType, double basisPoints, String formattedBasisPoints, double impliedFuturePrice, int holdDays, String futureExpiry, double dividendImpact, double dividendToExpiry) {
        LOG.debug("onTickEfpEvent requestId {}, tickType {}, basisPoints {}, formattedBasisPoints {}, impliedFuturePrice {}, holdDays {}, futureExpiry {}, dividendImpact {}, dividendToExpiry {}",
                requestId, tickType, basisPoints, formattedBasisPoints, impliedFuturePrice, holdDays, futureExpiry, dividendImpact, dividendToExpiry);
    }

    @Override
    public void onTickGenericEvent(TickGenericEvent tickGenericEvent) {
        LOG.debug(tickGenericEvent);
    }

    @Override
    public void onTickGenericEvent(int requestId, TickType tickType, double value) {
        LOG.debug("onTickGenericEvent requestId {}, tickType {}, value {}", requestId, tickType, value);
    }

    @Override
    public void onTickOptionComputationEvent(TickOptionComputationEvent tickOptionComputationEvent) {
        LOG.debug(tickOptionComputationEvent);
    }

    @Override
    public void onTickOptionComputationEvent(int requestId, TickType tickType, double impliedVolatility, double delta, double price, double presentValueDividend, double gamma, double vega, double theta, double underlyingPrice) {
        LOG.debug("onTickOptionComputationEvent requestId {}, tickType {}, impliedVolatility {}, delta {}, price {}, presentValueDividend {}, gamma {}, vega {}, theta {}, underlyingPrice {}",
                requestId, tickType, impliedVolatility, delta, price, presentValueDividend, gamma, vega, theta, underlyingPrice);
    }

    @Override
    public void onTickPriceEvent(TickPriceEvent tickPriceEvent) {
        LOG.debug(tickPriceEvent);
    }

    @Override
    public void onTickPriceEvent(int requestId, TickType tickType, double price, int autoExecute) {
        LOG.debug("onTickPriceEvent requestId {}, tickType {}, price {}, autoExecute {}", requestId, tickType, price, autoExecute);
    }

    @Override
    public void onTickSizeEvent(TickSizeEvent tickSizeEvent) {
        LOG.debug(tickSizeEvent);
    }

    @Override
    public void onTickSizeEvent(int requestId, TickType tickType, int size) {
        LOG.debug("onTickSizeEvent requestId {}, tickType {}, size {}", requestId, tickType, size);
    }

    @Override
    public void onTickSnapshotEndEvent(TickSnapshotEndEvent tickSnapshotEndEvent) {
        LOG.debug(tickSnapshotEndEvent);
    }

    @Override
    public void onTickSnapshotEndEvent(int requestId) {
        LOG.debug("onTickSnapshotEndEvent requestId {}", requestId);
    }

    @Override
    public void onTickStringEvent(TickStringEvent tickStringEvent) {
        LOG.debug(tickStringEvent);
    }

    @Override
    public void onTickStringEvent(int requestId, TickType tickType, String value) {
        LOG.debug("onTickStringEvent requestId {}, tickType {}, value {}", requestId, tickType, value);
    }

    @Override
    public void onVerifyMessageAPIEvent(VerifyMessageAPIEvent verifyMessageAPIEvent) {
        LOG.debug(verifyMessageAPIEvent);
    }

    @Override
    public void onVerifyMessageAPIEvent(String apiData) {
        LOG.debug("onVerifyMessageAPIEvent apiData {}", apiData);
    }

    @Override
    public void onVerifyCompletedEvent(VerifyCompletedEvent verifyCompletedEvent) {
        LOG.debug(verifyCompletedEvent);
    }

    @Override
    public void onVerifyCompletedEvent(boolean isSuccessful, String errorText) {
        LOG.debug("onVerifyCompletedEvent isSuccessful {}, errorText {}", isSuccessful, errorText);
    }
}
