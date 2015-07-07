package com.unisoft.algotrader.model.refdata;


import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;

/**
 * Created by alex on 5/17/15.
 */
@Table(keyspace = "refdata", name = "exchange")
public class Exchange{
    @PartitionKey
    @Column(name ="exch_id" )
    private String exchId;
    private String name;

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

    @ClusteringColumn(value = 0)
    private long businesstime = 0;

    @ClusteringColumn(value = 1)
    private long systemtime = 0;

    private boolean active = true;

    @Column(name ="updated_user" )
    private String updatedUser;

    @Column(name ="updated_reason" )
    private String updatedReason;

    public long getBusinesstime() {
        return businesstime;
    }

    public void setBusinesstime(long businesstime) {
        this.businesstime = businesstime;
    }

    public long getSystemtime() {
        return systemtime;
    }

    public void setSystemtime(long systemtime) {
        this.systemtime = systemtime;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public String getUpdatedReason() {
        return updatedReason;
    }

    public void setUpdatedReason(String updatedReason) {
        this.updatedReason = updatedReason;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
