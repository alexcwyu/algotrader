CREATE KEYSPACE mykeyspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
USE mykeyspace;
CREATE TABLE person (
  id text,
  firstname text,
  lastname text,
  email text,
  phone text,
  age int,
  PRIMARY KEY (id)
);

