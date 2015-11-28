drop table if exists ref.subroute_new;

create table ref.subroute_new (
  route varchar(100),
  direction varchar(100),
  geom geometry(LineStringM, 32161),
  primary key (route, direction)

);