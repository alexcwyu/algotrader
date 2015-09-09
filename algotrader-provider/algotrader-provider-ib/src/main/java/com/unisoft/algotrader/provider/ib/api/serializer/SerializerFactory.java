package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.persistence.RefDataStore;

/**
 * Created by alex on 9/10/15.
 */
public class SerializerFactory {
    private RefDataStore refDataStore;
    private int serverCurrentVersion;

    public SerializerFactory(RefDataStore refDataStore) {
        this.refDataStore = refDataStore;
    }


}
