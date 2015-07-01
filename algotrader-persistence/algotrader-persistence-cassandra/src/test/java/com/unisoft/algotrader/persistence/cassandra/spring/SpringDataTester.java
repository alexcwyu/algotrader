package com.unisoft.algotrader.persistence.cassandra.spring;

/**
 * Created by alex on 6/28/15.
 */


import java.net.InetAddress;
import java.net.UnknownHostException;

import com.datastax.driver.core.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

public class SpringDataTester {


    private static final Logger LOG = LogManager.getLogger(SpringDataTester.class);

    private static Cluster cluster;
    private static Session session;

    public static void main(String [] args){
        try{

            cluster = Cluster.builder().addContactPoints("127.0.0.1").build();

            session = cluster.connect("mykeyspace");

            CassandraOperations cassandraOps = new CassandraTemplate(session);

            cassandraOps.insert(new Person("1234567890", "David", "Tang", "david.tang@gmail.com", "97354625", 40));

            Select s = QueryBuilder.select().from("person");
            s.where(QueryBuilder.eq("id", "1234567890"));

            ResultSet rs = cassandraOps.query(s);
            Person p = cassandraOps.selectOne(s, Person.class);
            LOG.info(p.getId());
            //LOG.info(cassandraOps.queryForObject(s, Person.class).getId());

            cassandraOps.truncate("person");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
