package com.unisoft.algotrader.provider.ib.api.model.order;

/**
 * Created by alex on 8/30/15.
 */
public interface Origin {
    // Constants
    int CUSTOMER = 0;
    int FIRM = 1;
    char OPT_UNKNOWN = '?';
    char OPT_BROKER_DEALER = 'b';
    char OPT_CUSTOMER = 'c';
    char OPT_FIRM = 'f';
    char OPT_ISEMM = 'm';
    char OPT_FARMM = 'n';
    char OPT_SPECIALIST = 'y';
    int AUCTION_MATCH = 1;
    int AUCTION_IMPROVEMENT = 2;
    int AUCTION_TRANSPARENT = 3;
}
