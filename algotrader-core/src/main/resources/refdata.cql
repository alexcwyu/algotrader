DROP KEYSPACE refdata;

CREATE KEYSPACE IF NOT EXISTS refdata WITH replication ={'class':'SimpleStrategy','replication_factor':1};

USE refdata;

CREATE TABLE IF NOT EXISTS counters (
    entity_class text,
    next_id counter,
    PRIMARY KEY (entity_class)
);


CREATE TABLE IF NOT EXISTS currencies (
  ccy_id text,
  active boolean,
  updated_user text,
  updated_reason text,
  name text,
  PRIMARY KEY (ccy_id)
);

CREATE TABLE IF NOT EXISTS exchanges (
  exch_id text,
  active boolean,
  updated_user text,
  updated_reason text,
  name text,
  PRIMARY KEY (exch_id)
);

CREATE TABLE IF NOT EXISTS instruments (
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


