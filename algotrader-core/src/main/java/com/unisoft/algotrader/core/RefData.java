package com.unisoft.algotrader.core;

import com.datastax.driver.mapping.annotations.ClusteringColumn;

/**
 * Created by alex on 6/29/15.
 */
public abstract class RefData {

    @ClusteringColumn(value = 1)
    private long businesstime;

    @ClusteringColumn(value = 2)
    private long systemtime;

    private String updatedUser;

    private String updatedReason;

    private boolean active = true;

    public RefData(){
        this(System.currentTimeMillis(), System.currentTimeMillis());
    }

    public RefData(long businesstime, long systemtime){
        this.businesstime = businesstime;
        this.systemtime = systemtime;
    }


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
