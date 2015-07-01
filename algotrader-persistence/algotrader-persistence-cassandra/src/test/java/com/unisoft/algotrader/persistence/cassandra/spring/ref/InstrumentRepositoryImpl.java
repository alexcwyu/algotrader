package com.unisoft.algotrader.persistence.cassandra.spring.ref;

import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.schemabuilder.UDTType;
import com.unisoft.algotrader.core.Currency;
import com.unisoft.algotrader.core.Instrument;
import org.springframework.cassandra.core.PreparedStatementBinder;
import org.springframework.cassandra.core.PreparedStatementCreator;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.data.cassandra.core.CassandraOperations;

import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;
import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;

/**
 * Created by alex on 6/30/15.
 */
public class InstrumentRepositoryImpl implements InstrumentRepositoryCustom {

    public static final String INST_ID = "inst_id";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String SYMBOL = "symbol";

    public static final String EXCH_ID = "exch_id";
    public static final String CCY_ID = "ccy_id";

    public static final String UND_INST_ID = "und_inst_id";
    public static final String FACTOR = "factor";
    public static final String EXPIRY_DATE = "expiry_date";
    public static final String STRIKE = "strike";
    public static final String PUT_CALL = "put_call";
    public static final String MARGIN = "margin";

    public static final String ALT_SYMBOLS = "alt_symbols";
    public static final String ALT_EXCHIDS = "alt_exchids";
    //public static final String CLASSIFICATION = "classifications";


    public static final String GROUP = "group";
    public static final String SECTOR = "sector";

    public static final String BUS_TIME = "bus_time";
    public static final String SYS_TIME = "sys_time";


    public static final String KS  = "refdata";
    public static final String TABLE  = "instrument";

    public static final String UDT_INST_ID  = "inst_id";
    public static final String UDT_INST_CLASSIF  = "inst_classification";


    public static final RegularStatement insertStatement = insertInto(KS, TABLE)
            .value(INST_ID, bindMarker())
            .value(TYPE, bindMarker())
            .value(NAME, bindMarker())
            .value(SYMBOL, bindMarker())
            .value(EXCH_ID, bindMarker())
            .value(CCY_ID, bindMarker())
            .value(UND_INST_ID, bindMarker())
            .value(FACTOR, bindMarker())
            .value(EXPIRY_DATE, bindMarker())
            .value(STRIKE, bindMarker())
            .value(PUT_CALL, bindMarker())
            .value(MARGIN, bindMarker())
            .value(SECTOR, bindMarker())
            .value(GROUP, bindMarker())
            .value(ALT_SYMBOLS, bindMarker())
            .value(ALT_EXCHIDS, bindMarker())
            ;

    public static final RegularStatement selectInStatement = select().from(KS, TABLE).where(in(INST_ID, bindMarker()));

    private final PreparedStatement insertPreparedStatement;
    private final PreparedStatementCreator selectInPreparedStatement = session -> session.prepare(selectInStatement);
    private final CassandraOperations cassandraOperations;
    private final RowMapper<Instrument> mapper = new InstrumentRowMapper();
//    private final UserType instIdUDT;
//    private final UserType instClassificationUDT;
    @Inject
    InstrumentRepositoryImpl(CassandraOperations cassandraOperations) {
        this.cassandraOperations = cassandraOperations;
        Session  session = cassandraOperations.getSession();
        //instIdUDT = session.getCluster().getMetadata().getKeyspace(KS).getUserType(UDT_INST_ID);
        //instClassificationUDT = session.getCluster().getMetadata().getKeyspace(KS).getUserType(UDT_INST_CLASSIF);

        insertPreparedStatement = session.prepare(insertStatement);
    }


    private BoundStatement bindInsert(Instrument instrument){
        BoundStatement boundStatement =
                insertPreparedStatement.bind();
        boundStatement.setInt(INST_ID, instrument.instId);
        boundStatement.setString(TYPE, instrument.type.name());
        boundStatement.setString(NAME, instrument.name);
        boundStatement.setString(SYMBOL, instrument.symbol);
        boundStatement.setString(EXCH_ID, instrument.exchId);
        boundStatement.setString(CCY_ID, instrument.ccyId);
        boundStatement.setInt(UND_INST_ID, instrument.underlyingInstId);
        boundStatement.setDouble(FACTOR, instrument.factor);
        if (instrument.expiryDate !=null) {
            boundStatement.setDate(EXPIRY_DATE, instrument.expiryDate);
        }
        else{
            boundStatement.setToNull(EXPIRY_DATE);
        }
        boundStatement.setDouble(STRIKE, instrument.strike);
        if (instrument.putCall != null){
            boundStatement.setString(PUT_CALL, instrument.putCall.name());
        }
        else{
            boundStatement.setToNull(PUT_CALL);
        }
        boundStatement.setDouble(MARGIN, instrument.margin);

//        Map<String, UDTValue> altIds = new HashMap();
//        for (Map.Entry<String, Instrument.InstId> entry : instrument.altIds.entrySet()) {
//            altIds.put(entry.getKey(), instIdUDT.newValue()
//                    .setString(SYMBOL, entry.getValue().symbol)
//                    .setString(EXCH_ID, entry.getValue().exchId));
//        }
//
//        Map<String, UDTValue> classification = new HashMap();
//        for (Map.Entry<String, Instrument.Classification> entry : instrument.classifications.entrySet()) {
//            classification.put(entry.getKey(), instClassificationUDT.newValue()
//                    .setString(GROUP, entry.getValue().group)
//                    .setString(SECTOR, entry.getValue().sector));
//        }
//        boundStatement.setMap(ALT_IDS, altIds);
//        boundStatement.setMap(CLASSIFICATION, classification);

        boundStatement.setString(SECTOR, instrument.sector);
        boundStatement.setString(GROUP, instrument.group);
        boundStatement.setMap(ALT_SYMBOLS, instrument.altSymbols);
        boundStatement.setMap(ALT_EXCHIDS, instrument.altExchIds);

        return boundStatement;
    }

    public Instrument save(Instrument instrument){
        BoundStatement boundStatement = bindInsert(instrument);
        cassandraOperations.execute(boundStatement);
        return instrument;
    }

    public Iterable<Instrument> save(Iterable<Instrument> instruments){
        BatchStatement batch = new BatchStatement();
        for (Instrument instrument : instruments){
            batch.add(bindInsert(instrument));
        }
        cassandraOperations.execute(batch);

        return instruments;
    }

    public Instrument save(Instrument instrument, long busTime){
        return save(instrument);
    }

    @Override
    public Instrument findOne(Integer s) {
        Instrument instrument = cassandraOperations.queryForObject(select().from(KS, TABLE).where(eq(INST_ID, s)).limit(1), mapper);
        return instrument;
    }

    @Override
    public Iterable<Instrument> findAll() {
        return cassandraOperations.query(select().from(KS, TABLE), mapper);
    }

    @Override
    public Iterable<Instrument> findAll(Iterable<Integer> ids) {
        return cassandraOperations.query(selectInPreparedStatement, ps -> ps.bind(ids), mapper);
    }

    public static class InstrumentRowMapper implements RowMapper<Instrument>{
        @Override
        public Instrument mapRow(Row row, int rowNum) throws DriverException {
            Instrument instrument = new Instrument(
                    row.getInt(INST_ID),
                    Instrument.InstType.valueOf(row.getString(TYPE)),
                    row.getString(NAME),
                    row.getString(SYMBOL),
                    row.getString(EXCH_ID),
                    row.getString(CCY_ID)
            );
            instrument.underlyingInstId = row.getInt(UND_INST_ID);
            instrument.factor = row.getDouble(FACTOR);
            if (!row.isNull(EXPIRY_DATE))
                instrument.expiryDate = row.getDate(EXPIRY_DATE);

            instrument.strike = row.getDouble(STRIKE);
            if (!row.isNull(PUT_CALL))
                instrument.putCall = Instrument.PutCall.valueOf(row.getString(PUT_CALL));

            instrument.margin = row.getDouble(MARGIN);

//            Map<String, UDTValue> rawAltIds = row.getMap(ALT_IDS, String.class, UDTValue.class);
//            //Map<String, Instrument.InstId> altIds = new HashMap();
//
//            for (Map.Entry<String, UDTValue> entry: rawAltIds.entrySet()){
//                //altIds.put(entry.getKey(), new Instrument.InstId(entry.getValue().getString(SYMBOL), entry.getValue().getString(EXCH_ID)));
//                instrument.addAltId(entry.getKey(), new Instrument.InstId(entry.getValue().getString(SYMBOL), entry.getValue().getString(EXCH_ID)));
//            }
//
//            Map<String, UDTValue> rawClassifications = row.getMap(CLASSIFICATION, String.class, UDTValue.class);
//            //Map<String, Instrument.Classification> classifications = new HashMap();
//
//            for (Map.Entry<String, UDTValue> entry: rawClassifications.entrySet()){
//                //classifications.put(entry.getKey(), new Instrument.Classification(entry.getValue().getString(GROUP), entry.getValue().getString(SECTOR)));
//                instrument.addClassification(entry.getKey(), new Instrument.Classification(entry.getValue().getString(GROUP), entry.getValue().getString(SECTOR)));
//            }

            instrument.sector = row.getString(SECTOR);
            instrument.group = row.getString(GROUP);
            instrument.altSymbols = row.getMap(ALT_SYMBOLS, String.class, String.class);
            instrument.altExchIds = row.getMap(ALT_EXCHIDS, String.class, String.class);

            return instrument;
        }
    }
}
