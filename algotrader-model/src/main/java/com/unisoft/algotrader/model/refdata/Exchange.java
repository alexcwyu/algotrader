package com.unisoft.algotrader.model.refdata;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;

/**
 * Created by alex on 5/17/15.
 */
@Table(keyspace = "refdata", name = "exchanges")
public class Exchange{
    @PartitionKey
    @Column(name ="exch_id" )
    private String exchId;
    private String name;

    public static Exchange HKEX = new Exchange("SEHK", "Hong Kong Stock Exchange");
    public static Exchange HKFE = new Exchange("HKFE", "Hong Kong Futures Exchange");
    public static Exchange SEHKNTL = new Exchange("SEHKNTL", "Shanghai-Hong Kong Stock Connect");
    public static Exchange NSE = new Exchange("NSE", "National Stock Exchange of India");

    public static Exchange CHIXJ = new Exchange("CHIXJ", "CHI-X Japan");
    public static Exchange OSE = new Exchange("OSE", "Osaka Securities Exchange");
    public static Exchange TSEJ = new Exchange("TSEJ", "Tokyo Stock Exchange");

    public static Exchange SGX = new Exchange("SGX", "Singapore Exchange");
    public static Exchange KSE = new Exchange("KSE", "Korea Stock Exchange");

    public static Exchange ASX = new Exchange("ASX", "Australian Stock Exchange");

    public static Exchange IDEAL = new Exchange("IDEAL", "IDEAL FX");
    public static Exchange IDEALPRO = new Exchange("IDEALPRO", "IDEALPRO Metals");

    public static Exchange LSE = new Exchange("LSE", "London Stock Exchange");
    public static Exchange SWX = new Exchange("SWX", "Swiss Exchange");
    public static Exchange FWB = new Exchange("FWB", "Frankfurt Stock Exchange");

    public static Exchange CFE = new Exchange("CFE", "CBOE Futures Exchange");
    public static Exchange ECBOT = new Exchange("ECBOT", "CBOT");
    public static Exchange CBOE = new Exchange("CBOE", "Chicago Board Options Exchange");
    public static Exchange CHX = new Exchange("CHX", "Chicago Stock Exchange");
    public static Exchange GLOBEX = new Exchange("GLOBEX", "CME");
    public static Exchange NYBOT = new Exchange("NYBOT", "ICE Futures U.S.");
    public static Exchange ICEUS = new Exchange("ICEUS", "ICE Futures US");
    public static Exchange ISE = new Exchange("ISE", "ISE Options Exchange");
    public static Exchange NASDAQ = new Exchange("NASDAQ", "NASDAQ");
    public static Exchange AMEX = new Exchange("AMEX", "NYSE Amex");
    public static Exchange ARCA = new Exchange("ARCA", "NYSE Arca");
    public static Exchange PSE = new Exchange("PSE", "NYSE Arca");
    public static Exchange NYMEX = new Exchange("NYMEX", "New York Mercantile Exchange");
    public static Exchange NYSE = new Exchange("NYSE", "New York Stock Exchange");

    public static Exchange TSE = new Exchange("TSE", "Toronto Stock Exchange");

    public Exchange(){
    }

    public Exchange(String exchId, String name) {
        this.exchId = exchId;
        this.name = name;
    }

    public String getExchId() {
        return exchId;
    }

    public void setExchId(String exchId) {
        this.exchId = exchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exchange)) return false;
        Exchange exchange = (Exchange) o;
        return Objects.equal(exchId, exchange.exchId) &&
                Objects.equal(name, exchange.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(exchId, name);
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "exchId='" + exchId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
