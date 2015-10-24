
CREATE TABLE logs.client_location_log (
  stamp timestamp without time zone,
  accuracy real,
  type character varying(20)
);

select AddGeometryColumn('logs', 'client_location_log', 'location', 4326, 'Point', 2);

CREATE TABLE logs.trip_log (
  trip_id bigint,
  subroute_gid integer,
  asset_id integer,
  stamp timestamp without time zone,
  stop_gid integer
);
