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

--
-- Name: SCHEMA ref; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA ref IS 'Reference Data';


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
-- Name: route_new; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE route_new (
    id character varying(100) NOT NULL,
    "desc" character varying(255),
    color character varying(50),
    gpsenabled boolean,
    sort_order integer,
    priority integer,
    route_group character varying(50)
);


ALTER TABLE route_new OWNER TO postgres;

--
-- Name: schedule; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE schedule (
    route character varying(20) NOT NULL,
    stop_area character varying(20) NOT NULL,
    direction character varying(20) NOT NULL,
    schedule_type character varying(20) NOT NULL,
    stop_time time without time zone NOT NULL,
    error_minutes smallint DEFAULT 0 NOT NULL
);


ALTER TABLE schedule OWNER TO postgres;

--
-- Name: stop; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE stop (
    gid integer,
    routes character varying(254),
    descriptio character varying(500),
    ama_id integer,
    geom public.geometry(Point,4326)
);


ALTER TABLE stop OWNER TO postgres;

--
-- Name: stop_area; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE stop_area (
    id character varying(50) NOT NULL,
    "desc" character varying(100) NOT NULL,
    sort_order smallint DEFAULT 99 NOT NULL,
    lng real,
    lat real,
    geom public.geometry(Point,4326)
);


ALTER TABLE stop_area OWNER TO postgres;

--
-- Name: subroute_new; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE subroute_new (
    route character varying(100) NOT NULL,
    direction character varying(100) NOT NULL,
    geom public.geometry(LineStringM,32161)
);


ALTER TABLE subroute_new OWNER TO postgres;

--
-- Name: subroute_stop; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE subroute_stop (
    route character varying(50) NOT NULL,
    direction character varying(50) NOT NULL,
    stop integer NOT NULL,
    stop_order integer NOT NULL
);


ALTER TABLE subroute_stop OWNER TO postgres;

--
-- Name: vehicle; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE vehicle (
    asset_id integer NOT NULL,
    name character varying(50),
    group_id integer,
    plate character varying(20)
);


ALTER TABLE vehicle OWNER TO postgres;

--
-- Name: vehicle_state; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE vehicle_state (
    asset_id integer,
    last_trail_change timestamp without time zone,
    recent_speeds text,
    avg_speed real,
    within_service_area boolean,
    subroute_m real,
    within_origin boolean,
    trip_id bigint,
    stop_gid integer,
    location_desc text,
    trail public.geometry(LineString,4326),
    last_known_direction character varying(50),
    last_known_route character varying(50)
);


ALTER TABLE vehicle_state OWNER TO postgres;

--
-- Name: vehicle_state_possible_routes; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE vehicle_state_possible_routes (
    route character varying(50) NOT NULL,
    active boolean,
    asset_id integer NOT NULL
);


ALTER TABLE vehicle_state_possible_routes OWNER TO postgres;

--
-- Name: COLUMN vehicle_state_possible_routes.route; Type: COMMENT; Schema: ref; Owner: postgres
--

COMMENT ON COLUMN vehicle_state_possible_routes.route IS 'Route ID string e.g. ''T3''';


--
-- Name: COLUMN vehicle_state_possible_routes.asset_id; Type: COMMENT; Schema: ref; Owner: postgres
--

COMMENT ON COLUMN vehicle_state_possible_routes.asset_id IS 'Vehicle primary key';


--
-- Name: vehicle_state_possible_subroutes; Type: TABLE; Schema: ref; Owner: postgres; Tablespace: 
--

CREATE TABLE vehicle_state_possible_subroutes (
    asset_id integer NOT NULL,
    route character varying(50) NOT NULL,
    direction character varying(50) NOT NULL,
    active boolean
);


ALTER TABLE vehicle_state_possible_subroutes OWNER TO postgres;

--
-- Name: COLUMN vehicle_state_possible_subroutes.asset_id; Type: COMMENT; Schema: ref; Owner: postgres
--

COMMENT ON COLUMN vehicle_state_possible_subroutes.asset_id IS 'Vehicle ID';


--
-- Name: possible_routes_pk; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vehicle_state_possible_routes
    ADD CONSTRAINT possible_routes_pk PRIMARY KEY (asset_id, route);


--
-- Name: possible_subroute_pk; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vehicle_state_possible_subroutes
    ADD CONSTRAINT possible_subroute_pk PRIMARY KEY (asset_id, route, direction);


--
-- Name: route_new_pkey; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY route_new
    ADD CONSTRAINT route_new_pkey PRIMARY KEY (id);


--
-- Name: schedule_pkey; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (route, stop_area, direction, schedule_type, stop_time);


--
-- Name: stop_area_pkey; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY stop_area
    ADD CONSTRAINT stop_area_pkey PRIMARY KEY (id);


--
-- Name: subroute_new_pkey; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY subroute_new
    ADD CONSTRAINT subroute_new_pkey PRIMARY KEY (route, direction);


--
-- Name: subroute_stop_pkey; Type: CONSTRAINT; Schema: ref; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY subroute_stop
    ADD CONSTRAINT subroute_stop_pkey PRIMARY KEY (route, direction, stop);



