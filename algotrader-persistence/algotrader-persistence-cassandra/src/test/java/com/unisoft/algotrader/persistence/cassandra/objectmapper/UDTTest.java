package com.unisoft.algotrader.persistence.cassandra.objectmapper;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.UDTMapper;
import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;

import java.util.Date;
/**
 * Created by alex on 7/2/15.
 */
public class UDTTest {

    public static void main(String[] args) throws Exception {

        try (Cluster cluster = Cluster.builder().withProtocolVersion(ProtocolVersion.V3).addContactPoint("localhost").build(); Session umvKeyspace = cluster.connect("umv")) {

            UDTMapper<Entitlement> mapper = new MappingManager(umvKeyspace).udtMapper(Entitlement.class);

            ResultSet execute = umvKeyspace.execute("select * from household_entitlement");

            for (Row row : execute) {
                UDTValue entitlement = row.getUDTValue("entitlement");
                System.out.println(entitlement);
                System.out.println(mapper.fromUDT(entitlement));
            }
        }
    }

    @UDT(name = "entitlement", keyspace = "umv")
    public static class Entitlement {

        @Field(name = "name")
        private String name;

        @Field(name = "start_date")
        private Date startDate;

        @Field(name = "end_date")
        private Date endDate;

        public String getName() {
            return name;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        @Override
        public String toString() {
            return "Entitlement{" +
                    "name='" + name + '\'' +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    "} " + super.toString();
        }
    }
}
