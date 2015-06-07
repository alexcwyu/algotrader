package com.unisoft.algotrader.core;

/**
 * Created by alex on 5/17/15.
 */
public class Instrument {
    public final String instId;
    public final String exchId;
    public final String ccyId;
    public double factor = 1;
    public double margin = 0;


    public Instrument(String instId, String exchId, String ccyId){
        this.instId = instId;
        this.exchId = exchId;
        this.ccyId = ccyId;
    }


    public static class InstrumentBuilder {
        private String instId;
        private String exchId;
        private String ccyId;

        public InstrumentBuilder setInstId(String instId) {
            this.instId = instId;
            return this;
        }

        public InstrumentBuilder setExchId(String exchId) {
            this.exchId = exchId;
            return this;
        }

        public InstrumentBuilder setCcyId(String ccyId) {
            this.ccyId = ccyId;
            return this;
        }

        public Instrument createInstrument() {
            return new Instrument(instId, exchId, ccyId);
        }
    }
}
