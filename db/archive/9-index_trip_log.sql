-- THIS INDEX IS NOT USED AS OF 2015-12-05


SET search_path = logs, pg_catalog;

--
-- Name: trip_log_stop_gid_idx; Type: INDEX; Schema: logs; Owner: postgres; Tablespace:
--

CREATE INDEX trip_log_stop_gid_idx ON trip_log USING btree (stop_gid);


--
-- Name: trip_log_subroute_gid_idx; Type: INDEX; Schema: logs; Owner: postgres; Tablespace:
--

CREATE INDEX trip_log_subroute_gid_idx ON trip_log USING btree (subroute_gid);


--
-- Name: trip_log_trip_id_idx; Type: INDEX; Schema: logs; Owner: postgres; Tablespace:
--

CREATE INDEX trip_log_trip_id_idx ON trip_log USING btree (trip_id);

