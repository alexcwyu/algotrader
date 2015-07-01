// table to store historical stock quote data

CREATE KEYSPACE IF NOT EXISTS marketdata WITH replication ={'class':'SimpleStrategy','replication_factor':1};

USE marketdata;

CREATE TABLE IF NOT EXISTS bar (
  instid varchar, // stock symbol
  barsize int, //bar size
  datetime timestamp, // timestamp of bar
  open double, // open price
  high double, // high price
  low double, // low price
  close double, // close price
  volume bigint, // volume
  openint bigint, // openInt
  PRIMARY KEY (instid, barsize, datetime) // primary key
);


CREATE TABLE IF NOT EXISTS quote(
  instid varchar, // stock symbol
  datetime timestamp, // timestamp of quote
  bid double, // bid
  ask double, // ask
  bidsize int, // bid size
  asksize int, // ask size
  PRIMARY KEY (instid, datetime) // primary key
);

CREATE TABLE IF NOT EXISTS trade(
  instid varchar, // stock symbol
  datetime timestamp, // timestamp of trade
  price double, // price
  size int, // size
  PRIMARY KEY (instid, datetime) // primary key
);