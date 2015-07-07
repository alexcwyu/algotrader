DROP KEYSPACE refdata;

CREATE KEYSPACE IF NOT EXISTS refdata WITH replication ={'class':'SimpleStrategy','replication_factor':1};

USE refdata;

CREATE TABLE IF NOT EXISTS currency (
  ccy_id text,
  businesstime bigint,
  systemtime bigint,
  active boolean,
  updated_user text,
  updated_reason text,
  name text,
  PRIMARY KEY (ccy_id, businesstime, systemtime)
)WITH CLUSTERING ORDER BY (businesstime DESC, systemtime DESC);

CREATE TABLE IF NOT EXISTS exchange (
  exch_id text,
  businesstime bigint,
  systemtime bigint,
  active boolean,
  updated_user text,
  updated_reason text,
  name text,
  PRIMARY KEY (exch_id, businesstime, systemtime)
)WITH CLUSTERING ORDER BY (businesstime DESC, systemtime DESC);

//CREATE TYPE inst_classification (
//   group text,
//   sector text,
//);

//CREATE TYPE inst_id (
//   symbol text,
//   exch_id text,
//);


//CREATE TABLE IF NOT EXISTS instrument (
//  instId int,
//  businessTime bigint,
//  systemTime bigint,
//  type int,
//  symbol text,
//  exchId text,
//  ccyId text,
//  factor double,
//  expiry_date timestamp,
//  strike double,
//  put_call int,
//  alt_ids map<text, frozen<inst_id>>,
//  classifications map<text, frozen<inst_classification>>,
//  PRIMARY KEY (instId, businessTime, systemTime)
//)WITH CLUSTERING ORDER BY (businessTime DESC, systemTime DESC);

CREATE TABLE IF NOT EXISTS instrument (
  inst_id int,
  type text,
  name text,
  symbol text,
  exch_id text,
  ccy_id text,
  und_inst_id int,
  factor double,
  expiry_date timestamp,
  strike double,
  put_call text,
  margin double,
  sector text,
  group text,
  alt_symbols map<text, text>,
  alt_exchids map<text, text>,
  PRIMARY KEY (inst_id)
);


