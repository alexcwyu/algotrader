package com.unisoft.algotrader.core;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.unisoft.algotrader.core.id.InstId;

import java.util.Map;

/**
 * Created by alex on 5/17/15.
 */
public class Instrument {

    public final InstId instId;
    public final String ccyId;
    public Map<String, InstId> altIds = Maps.newHashMap();
    public double factor = 1;
    public double margin = 0;


    public Instrument(InstId instId, String ccyId){
        this.instId = instId;
        this.ccyId = ccyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrument)) return false;
        Instrument that = (Instrument) o;
        return Objects.equal(factor, that.factor) &&
                Objects.equal(margin, that.margin) &&
                Objects.equal(instId, that.instId) &&
                Objects.equal(ccyId, that.ccyId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, ccyId, factor, margin);
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "instId=" + instId +
                ", ccyId='" + ccyId + '\'' +
                ", factor=" + factor +
                ", margin=" + margin +
                '}';
    }

    public void addAltId(String providerId, InstId instId){
        this.altIds.put(providerId, instId);
    }

    public InstId getAltId(String providerId){
        return this.altIds.get(providerId);
    }
}
