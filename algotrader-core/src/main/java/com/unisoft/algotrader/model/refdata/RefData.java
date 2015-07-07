package com.unisoft.algotrader.model.refdata;

/**
 * Created by alex on 6/29/15.
 */
public interface RefData {

    public long getBusinesstime();

    public void setBusinesstime(long businesstime);

    public long getSystemtime();

    public void setSystemtime(long systemtime);

    public String getUpdatedUser();

    public void setUpdatedUser(String updatedUser);

    public String getUpdatedReason();

    public void setUpdatedReason(String updatedReason);

    public boolean isActive();

    public void setActive(boolean active);
}
