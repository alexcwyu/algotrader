DROP KEYSPACE trading;

CREATE KEYSPACE IF NOT EXISTS trading WITH replication ={'class':'SimpleStrategy','replication_factor':1};

USE trading;

CREATE TABLE IF NOT EXISTS account (
  //TODO
);



CREATE TABLE IF NOT EXISTS portfolio (
  //TODO
);

CREATE TABLE IF NOT EXISTS strategy (

);


CREATE TABLE IF NOT EXISTS position (
  //TODO
);

CREATE TABLE IF NOT EXISTS order (
  //TODO
);

CREATE TABLE IF NOT EXISTS executionreport (
  //TODO
);


