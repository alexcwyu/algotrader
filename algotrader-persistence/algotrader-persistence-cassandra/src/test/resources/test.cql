drop KEYSPACE complex;

CREATE KEYSPACE IF NOT EXISTS complex WITH replication ={'class':'SimpleStrategy','replication_factor':1};

CREATE TYPE complex.phone (
   type text,
   phoneNumber text,
);

CREATE TYPE complex.address (
   street text,
   city text,
   zipCode int,
   //phones list<frozen<phone>>
);

CREATE TABLE complex.accounts (
   email text PRIMARY KEY,
   name text,
   addr frozen<address>
);