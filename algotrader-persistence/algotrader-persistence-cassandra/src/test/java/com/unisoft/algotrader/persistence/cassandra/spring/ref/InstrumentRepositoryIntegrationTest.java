package com.unisoft.algotrader.persistence.cassandra.spring.ref;

/**
 * Created by alex on 6/28/15.
 */

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.google.common.collect.ImmutableSet;
import com.unisoft.algotrader.core.Instrument;
import com.unisoft.algotrader.persistence.cassandra.spring.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class InstrumentRepositoryIntegrationTest extends BaseIntegrationTest {

    public static final String TIME_BUCKET = "2014-01-01";

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Test
    public void repositoryStoresAndRetrievesEvents() {
        Instrument inst1 = new Instrument(1, Instrument.InstType.Stock, "0005.HK", "HKEX", "HKD");
        inst1.addAltSymbol("IB", "1");
        inst1.addAltExchId("IB", "SEHK");
        inst1.addAltExchId("Esignal", "SEHK");
        Instrument inst2 = new Instrument(2, Instrument.InstType.Stock, "0959.HK", "HKEX", "HKD");
        instrumentRepository.save(ImmutableSet.of(inst1, inst2));

        Iterable<Instrument> instruments = instrumentRepository.findAll();

        assertThat(instruments, hasItem(inst1));
        assertThat(instruments, hasItem(inst2));
    }

    @Test
    public void repositoryDeletesStoredEvents() {
        Instrument inst1 = new Instrument(1, Instrument.InstType.Stock, "0005.HK", "HKEX", "HKD");
        Instrument inst2 = new Instrument(2, Instrument.InstType.Stock, "0959.HK", "HKEX", "HKD");
        instrumentRepository.save(ImmutableSet.of(inst1, inst2));

        instrumentRepository.delete(inst1);
        instrumentRepository.delete(inst2);

        Iterable<Instrument> instruments = instrumentRepository.findAll();

        assertThat(instruments, not(hasItem(inst1)));
        assertThat(instruments, not(hasItem(inst2)));
    }


    public static void main(String [] args)throws Exception{
        Cluster cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .build();
        Session session = cluster.connect();
        PreparedStatement insertPreparedStatement = session.prepare("INSERT INTO refdata.instrument (inst_id, type, name, symbol, exch_id, ccy_id, und_inst_id, factor, expiry_date, strike, put_call, margin, sector, group, alt_symbols, alt_exchids) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        PreparedStatement selectPreparedStatement = session.prepare("SELECT * FROM refdata.instrument WHERE inst_id = ?;");

//        UserType instIDUDT = session.getCluster().getMetadata().getKeyspace("refdata").getUserType("inst_id");
//        UserType instClassificationUDT = session.getCluster().getMetadata().getKeyspace("refdata").getUserType("inst_classification");

        Instrument inst1 = new Instrument(1, Instrument.InstType.Stock, "0005.HK", "HKEX", "HKD");
        inst1.addAltSymbol("IB", "1");
        inst1.addAltExchId("IB", "SEHK");
        inst1.addAltExchId("Esignal", "SEHK");

        inst1.sector = "Finance";
        inst1.sector = "Banking";

//        Map<String, UDTValue> altIds = new HashMap();
//
//        for (Map.Entry<String, Instrument.InstId> entry : inst1.altIds.entrySet()) {
//            altIds.put(entry.getKey(), instIDUDT.newValue()
//                    .setString("symbol", entry.getValue().symbol)
//                    .setString("exch_id", entry.getValue().exchId));
//        }
//
//        Map<String, UDTValue> classification = new HashMap();
//        for (Map.Entry<String, Instrument.Classification> entry : inst1.classifications.entrySet()) {
//            classification.put(entry.getKey(), instClassificationUDT.newValue()
//                    .setString("group", entry.getValue().group)
//                    .setString("sector", entry.getValue().sector));
//        }

        session.execute(insertPreparedStatement.bind(
                inst1.instId,
                inst1.type.name(),
                inst1.name,
                inst1.symbol,
                inst1.exchId,
                inst1.ccyId,
                inst1.underlyingInstId,
                inst1.factor,
                inst1.expiryDate,
                inst1.strike,
                inst1.putCall!=null ? inst1.putCall.name():null,
                inst1.margin,
                inst1.sector,
                inst1.group,
                inst1.altSymbols,
                inst1.altExchIds));

    }
}