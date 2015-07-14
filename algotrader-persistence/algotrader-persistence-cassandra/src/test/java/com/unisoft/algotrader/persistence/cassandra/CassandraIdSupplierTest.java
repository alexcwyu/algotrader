package com.unisoft.algotrader.persistence.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
/**
 * Created by alex on 7/14/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class CassandraIdSupplierTest {

    @Mock Session session;
    @Mock Row row;
    @Mock List<Row> rows;
    @Mock ResultSet resultSet;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CassandraIdSupplier ids;

    @Before
    public void setUp() throws Exception {
        when(session.execute(org.mockito.Matchers.<Statement>any())).thenReturn(resultSet);
        when(resultSet.one()).thenReturn(row);
        when(row.getLong("next_id")).thenReturn(1L, 2L, 3L);

        ids = new CassandraIdSupplier(session, "test");
    }

    @Test
    public void should_return_one_on_first_start() throws Exception {
        assertThat(ids.next(), is(equalTo(1L)));
    }

    @Test
    public void should_increment_id() throws Exception {
        assertThat(ids.next(), is(equalTo(1L)));
        assertThat(ids.next(), is(equalTo(2L)));
        assertThat(ids.next(), is(equalTo(3L)));
    }
}
