package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.persistence.RefDataStore;

/**
 * Created by alex on 9/10/15.
 */
public class Serializers {

    private final AccountSummarySubscriptionRequestSerializer accountSummarySubscriptionRequestSerializer;
    private final AccountSummaryUnsubscriptionRequestSerializer accountSummaryUnsubscriptionRequestSerializer;
    private final AccountUpdateSubscriptionRequestSerializer accountUpdateSubscriptionRequestSerializer;
    private final AccountUpdateUnsubscriptionRequestSerializer accountUpdateUnsubscriptionRequestSerializer;
    private final BindNewlyCreatedOpenOrderRequestSerializer bindNewlyCreatedOpenOrderRequestSerializer;
    private final CancelAllOrdersRequestSerializer cancelAllOrdersRequestSerializer;
    private final CancelOrderSerializer cancelOrderSerializer;
    private final ContractSpecificationRequestSerializer contractSpecificationRequestSerializer;
    private final DisplayGroupsQueryRequestSerializer displayGroupsQueryRequestSerializer;
    private final DisplayGroupUpdateRequestSerializer displayGroupUpdateRequestSerializer;
    private final ExecutionReportRequestSerializer executionReportRequestSerializer;
    private final ExerciseOptionRequestSerializer exerciseOptionRequestSerializer;
    private final FAConfigurationRequestSerializer faConfigurationRequestSerializer;
    private final FAReplaceConfigurationRequestSerializer faReplaceConfigurationRequestSerializer;
    private final FundamentalDataSubscriptionRequestSerializer fundamentalDataSubscriptionRequestSerializer;
    private final FundamentalDataUnsubscriptionRequestSerializer fundamentalDataUnsubscriptionRequestSerializer;
    private final GroupEventsSubscriptionRequestSerializer groupEventsSubscriptionRequestSerializer;
    private final GroupEventsUnsubscriptionRequestSerializer groupEventsUnsubscriptionRequestSerializer;
    private final HistoricalMarketDataSubscriptionRequestSerializer historicalMarketDataSubscriptionRequestSerializer;
    private final HistoricalMarketDataUnsubscriptionRequestSerializer historicalMarketDataUnsubscriptionRequestSerializer;
    private final ManagedAccountListRequestSerializer managedAccountListRequestSerializer;
    private final MarketDataSubscriptionRequestSerializer marketDataSubscriptionRequestSerializer;
    private final MarketDataTypeRequestSerializer marketDataTypeRequestSerializer;
    private final MarketDataUnsubscriptionRequestSerializer marketDataUnsubscriptionRequestSerializer;
    private final MarketDepthSubscriptionRequestSerializer marketDepthSubscriptionRequestSerializer;
    private final MarketDepthUnsubscriptionRequestSerializer marketDepthUnsubscriptionRequestSerializer;
    private final MarketScannerSubscriptionRequestSerializer marketScannerSubscriptionRequestSerializer;
    private final MarketScannerUnsubscriptionRequestSerializer marketScannerUnsubscriptionRequestSerializer;
    private final MarketScannerValidParametersRequestSerializer marketScannerValidParametersRequestSerializer;
    private final NewsBulletinSubscriptionRequestSerializer newsBulletinSubscriptionRequestSerializer;
    private final NewsBulletinUnsubscriptionRequestSerializer newsBulletinUnsubscriptionRequestSerializer;
    private final NextValidOrderIdRequestSerializer nextValidOrderIdRequestSerializer;
    private final OptionImpliedVolatilitySubscriptionRequestSerializer optionImpliedVolatilitySubscriptionRequestSerializer;
    private final OptionImpliedVolatilityUnsubscriptionRequestSerializer optionImpliedVolatilityUnsubscriptionRequestSerializer;
    private final OptionPriceSubscriptionRequestSerializer optionPriceSubscriptionRequestSerializer;
    private final OptionPriceUnsubscriptionRequestSerializer optionPriceUnsubscriptionRequestSerializer;
    private final PlaceOrderSerializer placeOrderSerializer;
    private final PositionsCancellationRequestSerializer positionsCancellationRequestSerializer;
    private final PositionsRequestSerializer positionsRequestSerializer;
    private final RealTimeMarketDataSubscriptionRequestSerializer realTimeMarketDataSubscriptionRequestSerializer;
    private final RealTimeMarketDataUnsubscriptionRequestSerializer realTimeMarketDataUnsubscriptionRequestSerializer;
    private final RetrieveAllOpenOrderRequestSerializer retrieveAllOpenOrderRequestSerializer;
    private final RetrieveOpenOrderRequestSerializer retrieveOpenOrderRequestSerializer;
    private final ServerCurrentTimeRequestSerializer serverCurrentTimeRequestSerializer;
    private final ServerLogLevelRequestSerializer serverLogLevelRequestSerializer;
    private final StartAPISerializer startAPISerializer;
    private final VerifyMessageRequestSerializer verifyMessageRequestSerializer;
    private final VerifyRequestSerializer verifyRequestSerializer;

    public Serializers(int serverCurrentVersion, RefDataStore refDataStore) {
        accountSummarySubscriptionRequestSerializer = new AccountSummarySubscriptionRequestSerializer(serverCurrentVersion);
        accountSummaryUnsubscriptionRequestSerializer = new AccountSummaryUnsubscriptionRequestSerializer(serverCurrentVersion);
        accountUpdateSubscriptionRequestSerializer = new AccountUpdateSubscriptionRequestSerializer(serverCurrentVersion);
        accountUpdateUnsubscriptionRequestSerializer = new AccountUpdateUnsubscriptionRequestSerializer(serverCurrentVersion);
        bindNewlyCreatedOpenOrderRequestSerializer = new BindNewlyCreatedOpenOrderRequestSerializer(serverCurrentVersion);
        cancelAllOrdersRequestSerializer = new CancelAllOrdersRequestSerializer(serverCurrentVersion);
        cancelOrderSerializer = new CancelOrderSerializer(serverCurrentVersion);
        contractSpecificationRequestSerializer = new ContractSpecificationRequestSerializer(refDataStore, serverCurrentVersion);
        displayGroupsQueryRequestSerializer = new DisplayGroupsQueryRequestSerializer(serverCurrentVersion);
        displayGroupUpdateRequestSerializer = new DisplayGroupUpdateRequestSerializer(serverCurrentVersion);
        executionReportRequestSerializer = new ExecutionReportRequestSerializer(serverCurrentVersion);
        exerciseOptionRequestSerializer = new ExerciseOptionRequestSerializer(refDataStore, serverCurrentVersion);
        faConfigurationRequestSerializer = new FAConfigurationRequestSerializer(serverCurrentVersion);
        faReplaceConfigurationRequestSerializer = new FAReplaceConfigurationRequestSerializer(serverCurrentVersion);
        fundamentalDataSubscriptionRequestSerializer = new FundamentalDataSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        fundamentalDataUnsubscriptionRequestSerializer = new FundamentalDataUnsubscriptionRequestSerializer(serverCurrentVersion);
        groupEventsSubscriptionRequestSerializer = new GroupEventsSubscriptionRequestSerializer(serverCurrentVersion);
        groupEventsUnsubscriptionRequestSerializer = new GroupEventsUnsubscriptionRequestSerializer(serverCurrentVersion);
        historicalMarketDataSubscriptionRequestSerializer = new HistoricalMarketDataSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        historicalMarketDataUnsubscriptionRequestSerializer = new HistoricalMarketDataUnsubscriptionRequestSerializer(serverCurrentVersion);
        managedAccountListRequestSerializer = new ManagedAccountListRequestSerializer(serverCurrentVersion);
        marketDataSubscriptionRequestSerializer = new MarketDataSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        marketDataTypeRequestSerializer = new MarketDataTypeRequestSerializer(serverCurrentVersion);
        marketDataUnsubscriptionRequestSerializer = new MarketDataUnsubscriptionRequestSerializer(serverCurrentVersion);
        marketDepthSubscriptionRequestSerializer = new MarketDepthSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        marketDepthUnsubscriptionRequestSerializer = new MarketDepthUnsubscriptionRequestSerializer(serverCurrentVersion);
        marketScannerSubscriptionRequestSerializer = new MarketScannerSubscriptionRequestSerializer(serverCurrentVersion);
        marketScannerUnsubscriptionRequestSerializer = new MarketScannerUnsubscriptionRequestSerializer(serverCurrentVersion);
        marketScannerValidParametersRequestSerializer = new MarketScannerValidParametersRequestSerializer(serverCurrentVersion);
        newsBulletinSubscriptionRequestSerializer = new NewsBulletinSubscriptionRequestSerializer(serverCurrentVersion);
        newsBulletinUnsubscriptionRequestSerializer = new NewsBulletinUnsubscriptionRequestSerializer(serverCurrentVersion);
        nextValidOrderIdRequestSerializer = new NextValidOrderIdRequestSerializer(serverCurrentVersion);
        optionImpliedVolatilitySubscriptionRequestSerializer = new OptionImpliedVolatilitySubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        optionImpliedVolatilityUnsubscriptionRequestSerializer = new OptionImpliedVolatilityUnsubscriptionRequestSerializer(serverCurrentVersion);
        optionPriceSubscriptionRequestSerializer = new OptionPriceSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        optionPriceUnsubscriptionRequestSerializer = new OptionPriceUnsubscriptionRequestSerializer(serverCurrentVersion);
        placeOrderSerializer = new PlaceOrderSerializer(refDataStore, serverCurrentVersion);
        positionsCancellationRequestSerializer = new PositionsCancellationRequestSerializer(serverCurrentVersion);
        positionsRequestSerializer = new PositionsRequestSerializer(serverCurrentVersion);
        realTimeMarketDataSubscriptionRequestSerializer = new RealTimeMarketDataSubscriptionRequestSerializer(refDataStore, serverCurrentVersion);
        realTimeMarketDataUnsubscriptionRequestSerializer = new RealTimeMarketDataUnsubscriptionRequestSerializer(serverCurrentVersion);
        retrieveAllOpenOrderRequestSerializer = new RetrieveAllOpenOrderRequestSerializer(serverCurrentVersion);
        retrieveOpenOrderRequestSerializer = new RetrieveOpenOrderRequestSerializer(serverCurrentVersion);
        serverCurrentTimeRequestSerializer = new ServerCurrentTimeRequestSerializer(serverCurrentVersion);
        serverLogLevelRequestSerializer = new ServerLogLevelRequestSerializer(serverCurrentVersion);
        startAPISerializer = new StartAPISerializer(serverCurrentVersion);
        verifyMessageRequestSerializer = new VerifyMessageRequestSerializer(serverCurrentVersion);
        verifyRequestSerializer = new VerifyRequestSerializer(serverCurrentVersion);
    }

    public AccountSummarySubscriptionRequestSerializer accountSummarySubscriptionRequestSerializer() {
        return accountSummarySubscriptionRequestSerializer;
    }

    public AccountSummaryUnsubscriptionRequestSerializer accountSummaryUnsubscriptionRequestSerializer() {
        return accountSummaryUnsubscriptionRequestSerializer;
    }

    public AccountUpdateSubscriptionRequestSerializer accountUpdateSubscriptionRequestSerializer() {
        return accountUpdateSubscriptionRequestSerializer;
    }

    public AccountUpdateUnsubscriptionRequestSerializer accountUpdateUnsubscriptionRequestSerializer() {
        return accountUpdateUnsubscriptionRequestSerializer;
    }

    public BindNewlyCreatedOpenOrderRequestSerializer bindNewlyCreatedOpenOrderRequestSerializer() {
        return bindNewlyCreatedOpenOrderRequestSerializer;
    }

    public CancelAllOrdersRequestSerializer cancelAllOrdersRequestSerializer() {
        return cancelAllOrdersRequestSerializer;
    }

    public CancelOrderSerializer cancelOrderSerializer() {
        return cancelOrderSerializer;
    }

    public ContractSpecificationRequestSerializer contractSpecificationRequestSerializer() {
        return contractSpecificationRequestSerializer;
    }

    public DisplayGroupsQueryRequestSerializer displayGroupsQueryRequestSerializer() {
        return displayGroupsQueryRequestSerializer;
    }

    public DisplayGroupUpdateRequestSerializer displayGroupUpdateRequestSerializer() {
        return displayGroupUpdateRequestSerializer;
    }

    public ExecutionReportRequestSerializer executionReportRequestSerializer() {
        return executionReportRequestSerializer;
    }

    public ExerciseOptionRequestSerializer exerciseOptionRequestSerializer() {
        return exerciseOptionRequestSerializer;
    }

    public FAConfigurationRequestSerializer faConfigurationRequestSerializer() {
        return faConfigurationRequestSerializer;
    }

    public FAReplaceConfigurationRequestSerializer faReplaceConfigurationRequestSerializer() {
        return faReplaceConfigurationRequestSerializer;
    }

    public FundamentalDataSubscriptionRequestSerializer fundamentalDataSubscriptionRequestSerializer() {
        return fundamentalDataSubscriptionRequestSerializer;
    }

    public FundamentalDataUnsubscriptionRequestSerializer fundamentalDataUnsubscriptionRequestSerializer() {
        return fundamentalDataUnsubscriptionRequestSerializer;
    }

    public GroupEventsSubscriptionRequestSerializer groupEventsSubscriptionRequestSerializer() {
        return groupEventsSubscriptionRequestSerializer;
    }

    public GroupEventsUnsubscriptionRequestSerializer groupEventsUnsubscriptionRequestSerializer() {
        return groupEventsUnsubscriptionRequestSerializer;
    }

    public HistoricalMarketDataSubscriptionRequestSerializer historicalMarketDataSubscriptionRequestSerializer() {
        return historicalMarketDataSubscriptionRequestSerializer;
    }

    public HistoricalMarketDataUnsubscriptionRequestSerializer historicalMarketDataUnsubscriptionRequestSerializer() {
        return historicalMarketDataUnsubscriptionRequestSerializer;
    }

    public ManagedAccountListRequestSerializer managedAccountListRequestSerializer() {
        return managedAccountListRequestSerializer;
    }

    public MarketDataSubscriptionRequestSerializer marketDataSubscriptionRequestSerializer() {
        return marketDataSubscriptionRequestSerializer;
    }

    public MarketDataTypeRequestSerializer marketDataTypeRequestSerializer() {
        return marketDataTypeRequestSerializer;
    }

    public MarketDataUnsubscriptionRequestSerializer marketDataUnsubscriptionRequestSerializer() {
        return marketDataUnsubscriptionRequestSerializer;
    }

    public MarketDepthSubscriptionRequestSerializer marketDepthSubscriptionRequestSerializer() {
        return marketDepthSubscriptionRequestSerializer;
    }

    public MarketDepthUnsubscriptionRequestSerializer marketDepthUnsubscriptionRequestSerializer() {
        return marketDepthUnsubscriptionRequestSerializer;
    }

    public MarketScannerSubscriptionRequestSerializer marketScannerSubscriptionRequestSerializer() {
        return marketScannerSubscriptionRequestSerializer;
    }

    public MarketScannerUnsubscriptionRequestSerializer marketScannerUnsubscriptionRequestSerializer() {
        return marketScannerUnsubscriptionRequestSerializer;
    }

    public MarketScannerValidParametersRequestSerializer marketScannerValidParametersRequestSerializer() {
        return marketScannerValidParametersRequestSerializer;
    }

    public NewsBulletinSubscriptionRequestSerializer newsBulletinSubscriptionRequestSerializer() {
        return newsBulletinSubscriptionRequestSerializer;
    }

    public NewsBulletinUnsubscriptionRequestSerializer newsBulletinUnsubscriptionRequestSerializer() {
        return newsBulletinUnsubscriptionRequestSerializer;
    }

    public NextValidOrderIdRequestSerializer nextValidOrderIdRequestSerializer() {
        return nextValidOrderIdRequestSerializer;
    }

    public OptionImpliedVolatilitySubscriptionRequestSerializer optionImpliedVolatilitySubscriptionRequestSerializer() {
        return optionImpliedVolatilitySubscriptionRequestSerializer;
    }

    public OptionImpliedVolatilityUnsubscriptionRequestSerializer optionImpliedVolatilityUnsubscriptionRequestSerializer() {
        return optionImpliedVolatilityUnsubscriptionRequestSerializer;
    }

    public OptionPriceSubscriptionRequestSerializer optionPriceSubscriptionRequestSerializer() {
        return optionPriceSubscriptionRequestSerializer;
    }

    public OptionPriceUnsubscriptionRequestSerializer optionPriceUnsubscriptionRequestSerializer() {
        return optionPriceUnsubscriptionRequestSerializer;
    }

    public PlaceOrderSerializer placeOrderSerializer() {
        return placeOrderSerializer;
    }

    public PositionsCancellationRequestSerializer positionsCancellationRequestSerializer() {
        return positionsCancellationRequestSerializer;
    }

    public PositionsRequestSerializer positionsRequestSerializer() {
        return positionsRequestSerializer;
    }

    public RealTimeMarketDataSubscriptionRequestSerializer realTimeMarketDataSubscriptionRequestSerializer() {
        return realTimeMarketDataSubscriptionRequestSerializer;
    }

    public RealTimeMarketDataUnsubscriptionRequestSerializer realTimeMarketDataUnsubscriptionRequestSerializer() {
        return realTimeMarketDataUnsubscriptionRequestSerializer;
    }

    public RetrieveAllOpenOrderRequestSerializer retrieveAllOpenOrderRequestSerializer() {
        return retrieveAllOpenOrderRequestSerializer;
    }

    public RetrieveOpenOrderRequestSerializer retrieveOpenOrderRequestSerializer() {
        return retrieveOpenOrderRequestSerializer;
    }

    public ServerCurrentTimeRequestSerializer serverCurrentTimeRequestSerializer() {
        return serverCurrentTimeRequestSerializer;
    }

    public ServerLogLevelRequestSerializer serverLogLevelRequestSerializer() {
        return serverLogLevelRequestSerializer;
    }

    public StartAPISerializer startAPISerializer() {
        return startAPISerializer;
    }

    public VerifyMessageRequestSerializer verifyMessageRequestSerializer() {
        return verifyMessageRequestSerializer;
    }

    public VerifyRequestSerializer verifyRequestSerializer() {
        return verifyRequestSerializer;
    }
}
