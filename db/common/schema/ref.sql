--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.6
-- Dumped by pg_dump version 9.6.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: ref; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA ref;


--
-- Name: SCHEMA ref; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA ref IS 'Reference Data';


SET search_path = ref, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: configuration; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE configuration (
    assets_hash text
);


--
-- Name: route; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE route (
    id text NOT NULL,
    code text,
    "desc" text,
    color text,
    gpsenabled boolean,
    sort_order integer,
    priority integer,
    route_group text,
    foreign_id text
);


--
-- Name: schedule; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE schedule (
    route text NOT NULL,
    stop_area text NOT NULL,
    direction text NOT NULL,
    schedule_type text NOT NULL,
    stop_time time without time zone NOT NULL,
    error_minutes smallint DEFAULT 0 NOT NULL
);


--
-- Name: stop; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE stop (
    gid integer,
    routes text,
    descriptio text,
    ama_id integer,
    geom public.geometry(Point,4326)
);


--
-- Name: stop_area; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE stop_area (
    id text NOT NULL,
    "desc" text NOT NULL,
    sort_order smallint DEFAULT 99 NOT NULL,
    lng real,
    lat real,
    geom public.geometry(Point,4326)
);


--
-- Name: subroute; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE subroute (
    route text NOT NULL,
    direction text NOT NULL,
    geom public.geometry(LineStringM,32161)
);


--
-- Name: subroute_stop; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE subroute_stop (
    route text NOT NULL,
    direction text NOT NULL,
    stop integer NOT NULL,
    stop_order integer NOT NULL
);


--
-- Name: vehicle; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE vehicle (
    asset_id integer NOT NULL,
    name text,
    group_id integer,
    plate text,
    routes text[]
);


--
-- Name: vehicle_state; Type: TABLE; Schema: ref; Owner: -
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
    azimuth real,
    location_desc text,
    trail public.geometry(LineString,4326),
    last_known_direction text,
    last_known_route text
);


--
-- Name: vehicle_state_possible_routes; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE vehicle_state_possible_routes (
    route text NOT NULL,
    active boolean,
    asset_id integer NOT NULL
);


--
-- Name: COLUMN vehicle_state_possible_routes.route; Type: COMMENT; Schema: ref; Owner: -
--

COMMENT ON COLUMN vehicle_state_possible_routes.route IS 'Route ID string e.g. ''T3''';


--
-- Name: COLUMN vehicle_state_possible_routes.asset_id; Type: COMMENT; Schema: ref; Owner: -
--

COMMENT ON COLUMN vehicle_state_possible_routes.asset_id IS 'Vehicle primary key';


--
-- Name: vehicle_state_possible_subroutes; Type: TABLE; Schema: ref; Owner: -
--

CREATE TABLE vehicle_state_possible_subroutes (
    asset_id integer NOT NULL,
    route text NOT NULL,
    direction text NOT NULL,
    active boolean
);


--
-- Name: COLUMN vehicle_state_possible_subroutes.asset_id; Type: COMMENT; Schema: ref; Owner: -
--

COMMENT ON COLUMN vehicle_state_possible_subroutes.asset_id IS 'Vehicle ID';


--
-- Name: vehicle_state_possible_routes possible_routes_pk; Type: CONSTRAINT; Schema: ref; Owner: -
--

ALTER TABLE ONLY vehicle_state_possible_routes
    ADD CONSTRAINT possible_routes_pk PRIMARY KEY (asset_id, route);


--
-- Name: vehicle_state_possible_subroutes possible_subroute_pk; Type: CONSTRAINT; Schema: ref; Owner: -
--

ALTER TABLE ONLY vehicle_state_possible_subroutes
    ADD CONSTRAINT possible_subroute_pk PRIMARY KEY (asset_id, route, direction);


--
-- Name: route route_pkey; Type: CONSTRAINT; Schema: ref; Owner: -
--

ALTER TABLE ONLY route
    ADD CONSTRAINT route_pkey PRIMARY KEY (id);


--
-- Name: schedule schedule_pkey; Type: CONSTRAINT; Schema: ref; Owner: -
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (route, stop_area, direction, schedule_type, stop_time);


--
-- Name: stop_area stop_area_pkey; Type: CONSTRAINT; Schema: ref; Owner: -
--

ALTER TABLE ONLY stop_area
    ADD CONSTRAINT stop_area_pkey PRIMARY KEY (id);


--
-- Name: subroute subroute_pkey; Type: CONSTRAINT; Schema: ref; Owner: -
--

ALTER TABLE ONLY subroute
    ADD CONSTRAINT subroute_pkey PRIMARY KEY (route, direction);


--
-- Name: subroute_stop subroute_stop_pkey; Type: CONSTRAINT; Schema: ref; Owner: -
--

ALTER TABLE ONLY subroute_stop
    ADD CONSTRAINT subroute_stop_pkey PRIMARY KEY (route, direction, stop);



