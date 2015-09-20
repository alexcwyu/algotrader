package com.unisoft.algotrader.trading;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.data.MDSide;
import com.unisoft.algotrader.model.event.data.MarketDepth;
import com.unisoft.algotrader.model.event.data.Quote;

import java.util.List;

/**
 * Created by alex on 9/17/15.
 */
public class OrderBook {

    public static class Entry {
        public long dateTime;
        public double price;
        public int size;

        public Entry(long dateTime, double price, int size) {
            this.dateTime = dateTime;
            this.price = price;
            this.size = size;
        }
    }

    public final long instId;
    public List<Entry> bidList = Lists.newArrayList();
    public List<Entry> askList = Lists.newArrayList();

    public OrderBook(long instId){
        this.instId = instId;
    }

    public void add(MarketDepth marketDepth) {
        List<Entry> list = marketDepth.side == MDSide.Bid ? bidList : marketDepth.side == MDSide.Ask ? askList : null;
        if (list != null) {
            switch (marketDepth.operation) {
                case Insert:
                    if (marketDepth.position == -1) {
                        int index = -1;
                        switch (marketDepth.side) {
                            case Bid:
                                index = 0;
                                while (index < list.size()) {
                                    if (marketDepth.price > list.get(index).price)
                                        break;
                                    index++;
                                }
                                break;
                            case Ask:
                                index = list.size();
                                while (index > 0) {
                                    if (marketDepth.price > list.get(index - 1).price)
                                        break;
                                    index--;
                                }
                                break;

                        }
                        list.add(index, new Entry(marketDepth.dateTime, marketDepth.price, marketDepth.size));
                    } else {
                        list.add(marketDepth.position, new Entry(marketDepth.dateTime, marketDepth.price, marketDepth.size));
                    }
                    break;
                case Update:
                    if (marketDepth.position >= 0) {
                        if (marketDepth.position < list.size()) {
                            Entry entry = list.get(marketDepth.position);
                            entry.dateTime = marketDepth.dateTime;
                            entry.size = marketDepth.size;
                            if (marketDepth.price > 0) {
                                entry.price = marketDepth.price;
                            }
                        }
                    }
                    break;
                case Delete:
                    if (marketDepth.position >= 0) {
                        list.remove(marketDepth.position);
                    }
                    break;
                case Reset:
                    list.clear();
                    break;
            }
        }
    }

    public Quote getQuote(int level) {
        long dateTime = -1;
        double bid = 0;
        double ask = 0;
        int bidSize = 0;
        int askSize = 0;
        if (level < bidList.size()) {
            Entry entry = bidList.get(level);
            bid = entry.price;
            bidSize = entry.size;
            if (entry.dateTime > dateTime) {
                dateTime = entry.dateTime;
            }
        }

        if (level < askList.size()) {
            Entry entry = askList.get(level);
            ask = entry.price;
            askSize = entry.size;
            if (entry.dateTime > dateTime) {
                dateTime = entry.dateTime;
            }
        }
        return new Quote(instId, dateTime, bid, ask, bidSize, askSize);
    }

    public double getBidVolume() {
        return bidList.stream().mapToInt(e -> e.size).sum();
    }

    public double getAskVolume() {
        return askList.stream().mapToInt(e -> e.size).sum();
    }

    public double getAvgBidPrice() {
        double price = 0;
        double size = 0;
        for (Entry entry : bidList) {
            price += entry.price * entry.size;
            size += entry.size;
        }
        return (size > 0) ? price / size : 0;
    }


    public double getAvgAskPrice() {
        double price = 0;
        double size = 0;
        for (Entry entry : askList) {
            price += entry.price * entry.size;
            size += entry.size;
        }
        return (size > 0) ? price / size : 0;
    }

    public double getAvgBidPrice(double qty) {
        double avgPrice = 0;
        double leaveQty = qty;

        for (Entry entry : bidList) {
            if (leaveQty >= entry.size){
                avgPrice += entry.price * entry.size;
                leaveQty -= entry.size;
            }
            else{
                avgPrice += entry.price * leaveQty;
                leaveQty = 0;
            }
            if (leaveQty <=0)
                break;
        }

        if (leaveQty >= 0)
            avgPrice += bidList.get(bidList.size()-1).price * leaveQty;

        avgPrice /= qty;
        return avgPrice;
    }


    public double getAvgAskPrice(double qty) {
        double avgPrice = 0;
        double leaveQty = qty;

        for (Entry entry : askList) {
            if (leaveQty >= entry.size){
                avgPrice += entry.price * entry.size;
                leaveQty -= entry.size;
            }
            else{
                avgPrice += entry.price * leaveQty;
                leaveQty = 0;
            }
            if (leaveQty <=0)
                break;
        }

        if (leaveQty >= 0)
            avgPrice += askList.get(askList.size()-1).price * leaveQty;

        avgPrice /= qty;
        return avgPrice;
    }

    public void clear() {
        bidList.clear();
        askList.clear();
    }
}
