package com.unisoft.algotrader.model.refdata;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;

/**
 * Created by alex on 6/29/15.
 */
public abstract class RefData {

    @ClusteringColumn(value = 1)
    @Column(name ="business_time" )
    private long businessTime = 0;

    @ClusteringColumn(value = 2)
    @Column(name ="system_time" )
    private long systemTime = 0;

    private boolean active = true;

    @Column(name ="updated_user" )
    private String updatedUser;

    @Column(name ="updated_reason" )
    private String updatedReason;


    public RefData(){
        this(0,0);
    }

    public RefData(long businessTime, long systemTime){
        this.businessTime = businessTime;
        this.systemTime = systemTime;
    }


    public long getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(long businessTime) {
        this.businessTime = businessTime;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(long systemTime) {
        this.systemTime = systemTime;
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
