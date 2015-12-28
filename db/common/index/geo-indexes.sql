CREATE INDEX stop_area_gidx ON stop_area USING GIST (geom);

CREATE INDEX subroute_gix ON subroute USING GIST (geom);

CREATE INDEX stop_gix ON stop USING GIST (geom);
