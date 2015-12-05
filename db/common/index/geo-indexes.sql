--
-- Name: stop_area_gidx; Type: INDEX; Schema: ref; Owner: postgres; Tablespace:
--

CREATE INDEX stop_area_gidx ON stop_area USING gist (geom);


--
-- Name: subroute_new_gix; Type: INDEX; Schema: ref; Owner: postgres; Tablespace:
--

CREATE INDEX subroute_new_gix ON subroute_new USING gist (geom);


--
-- PostgreSQL database dump complete
--
