package com.unisoft.algotrader.persistence.cassandra.objectmapper;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * Created by alex on 6/28/15.
 */
public class ObjectMapperTester {

    public static void test1(){

        SimpleClient client = new SimpleClient();
        client.connect("127.0.0.1");
        MappingManager manager = new MappingManager(client.getSession());
        Mapper<Account> accountMappermapper = manager.mapper(Account.class);
//        UDTMapper<Address> addressUDTMapper = new MappingManager(client.getSession())
//                .udtMapper(Address.class);


        //Phone phone = new Phone(Phone.Type.Home, "707-555-3537");
        //List<Phone> phones = new ArrayList<Phone>();
        //phones.add(phone);
        Address address = new Address("25800 Arnold Drive", "Sonoma", 95476);
        Account account = new Account("John Doe", "jd@example.com", address);
        accountMappermapper.save(account);


        Account account2 = new Account("Peter Pan", "pp@example.com", address);
        accountMappermapper.save(account2);

        Account account3 = new Account("Jon Lee", "jl@example.com", address);
        accountMappermapper.save(account3);

        Account whose = accountMappermapper.get("jd@example.com");
        System.out.println("Account name: " + whose.getName());
        Account whose2 = accountMappermapper.get("jl@example.com");
        System.out.println("Account name: " + whose2.getName());

        AccountAccessor accessor = manager.createAccessor(AccountAccessor.class);
        Account a = accessor.byEmail("pp@example.com");

            System.out.println(a);


    }

    public static void test2(){
        SimpleClient client = new SimpleClient();
        client.connect("127.0.0.1");
        PreparedStatement insertUserPreparedStatement
                = client.getSession().prepare("INSERT INTO complex2.users (id, name, addresses) VALUES (?, ?, ?);");
        PreparedStatement selectUserPreparedStatement
                = client.getSession().prepare("SELECT * FROM complex2.users WHERE id = ?;");

        UserType addressUDT = client.getSession().getCluster()
                .getMetadata().getKeyspace("complex2").getUserType("address");
        UserType phoneUDT = client.getSession().getCluster()
                .getMetadata().getKeyspace("complex2").getUserType("phone");

        UDTValue phone1 = phoneUDT.newValue()
                .setString("alias", "home")
                .setString("number", "1-707-555-1234");
        UDTValue phone2 = phoneUDT.newValue()
                .setString("alias", "work")
                .setString("number", "1-800-555-9876");

        UDTValue address = addressUDT.newValue()
                .setString("street", "123 Arnold Drive")
                .setInt("zip_code", 95476)
                .setList("phones", ImmutableList.of(phone1, phone2));

//        Map<String, UDTValue> addresses = new HashMap<String, UDTValue>();
//        addresses.put("Work", address);

        UUID userId = UUID.fromString("fbdf82ed-0063-4796-9c7c-a3d4f47b4b25");
        client.getSession().execute(insertUserPreparedStatement.bind(userId, "G. Binary", address));


        UUID userId2 = UUID.fromString("fbdf82ed-0064-4796-9c7c-a3d4f47b4b25");
        client.getSession().execute(insertUserPreparedStatement.bind(userId2, "Alex", address));

        UUID userId3 = UUID.fromString("fbdf82ef-0064-4796-9c7c-a3d4f47b4b25");
        client.getSession().execute(insertUserPreparedStatement.bind(userId3, "Paul", address));
//
//
//        Row row = client.getSession().execute(selectUserPreparedStatement.bind(userId)).one();
//        UDTValue addr = row.getUDTValue("addresses");
//        System.out.println("Zip: " + addr.getInt("zip_code"));




    }
    @Accessor
    interface AccountAccessor {

        @Query("SELECT * FROM complex.accounts where email = :email")
        Account byEmail(@Param("email") String email);
    }

    public static void main(String [] args){
        test1();
    }
}
