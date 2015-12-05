CREATE schema logs;


CREATE TABLE logs.client_location_log (
  stamp timestamp without time zone,
  accuracy real,
  type character varying(20)
);

select AddGeometryColumn('logs', 'client_location_log', 'location', 4326, 'Point', 2);

CREATE TABLE logs.trip_log (
  trip_id bigint,
  route varchar(50),
  direction varchar(50),
  asset_id integer,
  stamp timestamp without time zone,
  stop_gid integer
);

create sequence logs.trip_id_seq;
