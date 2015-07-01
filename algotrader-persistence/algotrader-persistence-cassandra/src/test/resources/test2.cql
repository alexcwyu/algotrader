drop KEYSPACE complex2;

CREATE KEYSPACE complex2
   WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 3};

CREATE TYPE complex2.phone (
   alias text,
   number text
);

CREATE TYPE complex2.address (
   street text,
   zip_code int,
   phones list<frozen<phone>>
);

CREATE TABLE complex2.users (
   id uuid PRIMARY KEY,
   name text,
   addresses frozen<address>
);