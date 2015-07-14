package com.unisoft.algotrader.persistence;

import java.util.UUID;
import java.util.function.Supplier;

import com.datastax.driver.core.utils.UUIDs;
/**
 * Created by alex on 7/14/15.
 */
public class ObjectIdSupplier implements Supplier<UUID> {

    /**
     * @deprecated should be injected as Supplier<UUID>
     */
    @Deprecated
    public static UUID generate() {
        return UUIDs.timeBased();
    }

    @Override
    public UUID get() {
        return UUIDs.timeBased();
    }
}

