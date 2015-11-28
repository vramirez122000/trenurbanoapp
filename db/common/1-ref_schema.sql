--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: ref; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA ref;


ALTER SCHEMA ref OWNER TO postgres;

SET search_path = ref, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: configuration; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE configuration (
    assets_hash character varying(100)
);


ALTER TABLE configuration OWNER TO postgres;

--
-- Name: geofence; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE geofence (
    gid integer,
    type character varying(20),
    name character varying(50),
    avl_id integer,
    geom public.geometry(MultiPolygon,4326)
);


ALTER TABLE geofence OWNER TO postgres;

--
-- Name: route; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE route (
    gid integer,
    name character varying,
    gpsenabled boolean,
    color character varying,
    sort_order integer,
    priority integer,
    geom public.geometry,
    route_group character varying(50),
    full_name character varying(200)
);


ALTER TABLE route OWNER TO postgres;

--
-- Name: stop; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE stop (
    gid integer,
    routes character varying(254),
    descriptio character varying(254),
    ama_id integer,
    geom public.geometry(Point,4326)
);


ALTER TABLE stop OWNER TO postgres;

--
-- Name: stop_area; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE stop_area (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    geom public.geometry(Point,4326) NOT NULL,
    full_name character varying(100)
);


ALTER TABLE stop_area OWNER TO postgres;

--
-- Name: stop_route; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE stop_route (
    route_gid integer,
    stop_gid integer
);


ALTER TABLE stop_route OWNER TO postgres;

--
-- Name: stop_subroute; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE stop_subroute (
    stop_index integer,
    subroute_gid integer,
    stop_gid integer
);


ALTER TABLE stop_subroute OWNER TO postgres;

--
-- Name: subroute; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE subroute (
    gid character varying,
    name character varying,
    dest character varying,
    route_gid character varying(254),
    dest_id character varying,
    next_id character varying,
    origin_id character varying,
    geom public.geometry(LineString,32161),
    geom_with_m public.geometry
);


ALTER TABLE subroute OWNER TO postgres;

--
-- Name: trainschedule; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE trainschedule (
    id integer NOT NULL,
    route character varying(20) NOT NULL,
    station character varying(20) NOT NULL,
    direction character varying(20) NOT NULL,
    schedule_type character varying(20) NOT NULL,
    arrival time without time zone NOT NULL
);


ALTER TABLE trainschedule OWNER TO postgres;

--
-- Name: trainschedule_id_seq; Type: SEQUENCE; Schema: ref; Owner: postgres
--

CREATE SEQUENCE trainschedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE trainschedule_id_seq OWNER TO postgres;

--
-- Name: trainschedule_id_seq; Type: SEQUENCE OWNED BY; Schema: ref; Owner: postgres
--

ALTER SEQUENCE trainschedule_id_seq OWNED BY trainschedule.id;


--
-- Name: vehicle; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE vehicle (
    asset_id integer NOT NULL,
    name character varying(50),
    group_id integer
);


ALTER TABLE vehicle OWNER TO postgres;

--
-- Name: vehicle_state; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE vehicle_state (
    asset_id integer,
    last_known_subroute_id integer,
    last_known_route_geofence_id integer,
    last_trail_change timestamp without time zone,
    recent_speeds text,
    avg_speed real,
    within_service_area boolean,
    subroute_m real,
    within_origin boolean,
    trip_id bigint,
    stop_gid integer,
    location_desc text,
    cardinal_dir character varying(20),
    trail public.geometry(LineString,4326)
);


ALTER TABLE vehicle_state OWNER TO postgres;

--
-- Name: vehicle_state_possible_geofence_routes; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE vehicle_state_possible_geofence_routes (
    asset_id integer,
    active boolean,
    geofence_gid integer
);


ALTER TABLE vehicle_state_possible_geofence_routes OWNER TO postgres;

--
-- Name: vehicle_state_possible_subroutes; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE vehicle_state_possible_subroutes (
    asset_id integer,
    active boolean,
    subroute_gid integer
);


ALTER TABLE vehicle_state_possible_subroutes OWNER TO postgres;

--
-- Name: id; Type: DEFAULT; Schema: ref; Owner: postgres
--

ALTER TABLE ONLY trainschedule ALTER COLUMN id SET DEFAULT nextval('trainschedule_id_seq'::regclass);


--
-- Name: trainschedule_pkey; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY trainschedule
    ADD CONSTRAINT trainschedule_pkey PRIMARY KEY (id);


--
-- Name: vehicle_pkey; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vehicle
    ADD CONSTRAINT vehicle_pkey PRIMARY KEY (asset_id);


--
-- Name: idx_route_name; Type: INDEX; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_route_name ON route USING btree (name);


--
-- Name: idx_stop_area_name; Type: INDEX; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX idx_stop_area_name ON stop_area USING btree (name);


--
-- Name: idx_trainschedule_dest; Type: INDEX; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_trainschedule_dest ON trainschedule USING btree (direction);


--
-- Name: idx_trainschedule_station; Type: INDEX; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_trainschedule_station ON trainschedule USING btree (station);


--
-- Name: poss_geofence_route_asset_id_idx; Type: INDEX; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE INDEX poss_geofence_route_asset_id_idx ON vehicle_state_possible_geofence_routes USING btree (asset_id);


--
-- Name: poss_subroute_asset_id_idx; Type: INDEX; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE INDEX poss_subroute_asset_id_idx ON vehicle_state_possible_subroutes USING btree (asset_id);


--
-- PostgreSQL database dump complete
--

