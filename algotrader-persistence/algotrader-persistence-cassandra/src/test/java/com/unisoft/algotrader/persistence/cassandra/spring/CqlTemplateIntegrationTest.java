package com.unisoft.algotrader.persistence.cassandra.spring;

/**
 * Created by alex on 6/28/15.
 */
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.collect.ImmutableSet;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlOperations;

import static org.junit.Assert.assertThat;

public class CqlTemplateIntegrationTest extends BaseIntegrationTest {

    public static final String TIME_BUCKET = "2014-01-01";

    @Autowired
    private CqlOperations cqlTemplate;

    @Test
    public void allowsExecutingCqlStatements() {
        insertEventUsingCqlString();
        insertEventUsingStatementBuildWithQueryBuilder();
        insertEventUsingPreparedStatement();

        ResultSet resultSet1 = cqlTemplate.query("select * from event where type='type2' and bucket='" + TIME_BUCKET + "'");

        assertThat(resultSet1.all().size(), Is.is(2));

        Select select = QueryBuilder.select().from("event").where(QueryBuilder.eq("type", "type1")).and(QueryBuilder.eq("bucket", TIME_BUCKET)).limit(10);
        ResultSet resultSet2 = cqlTemplate.query(select);

        assertThat(resultSet2.all().size(), Is.is(1));
    }

    private void insertEventUsingCqlString() {
        cqlTemplate.execute("insert into event (id, type, bucket, tags) values (" + UUIDs.timeBased() + ", 'type1', '" + TIME_BUCKET + "', {'tag2', 'tag3'})");
    }

    private void insertEventUsingStatementBuildWithQueryBuilder() {
        Insert insertStatement = QueryBuilder.insertInto("event").value("id", UUIDs.timeBased()).value("type", "type2")
                .value("bucket", TIME_BUCKET).value("tags", ImmutableSet.of("tag1"));
        cqlTemplate.execute(insertStatement);
    }

    private void insertEventUsingPreparedStatement() {
        PreparedStatement preparedStatement = cqlTemplate.getSession().prepare("insert into event (id, type, bucket, tags) values (?, ?, ?, ?)");
        Statement insertStatement = preparedStatement.bind(UUIDs.timeBased(), "type2", TIME_BUCKET, ImmutableSet.of("tag1", "tag2"));
        cqlTemplate.execute(insertStatement);
    }
}