CREATE KEYSPACE events WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
USE events;
CREATE TABLE event (
  type text,
  bucket text,
  id timeuuid,
  tags set<text>,
  PRIMARY KEY ((type, bucket), id)
) WITH CLUSTERING ORDER BY (id DESC);