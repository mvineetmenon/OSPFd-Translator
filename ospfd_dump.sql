--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'SQL_ASCII';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: authentication_type; Type: TYPE; Schema: public; Owner: ospf
--

CREATE TYPE authentication_type AS ENUM (
    'PASSWORD',
    'MD5'
);


ALTER TYPE public.authentication_type OWNER TO ospf;

--
-- Name: network_type; Type: TYPE; Schema: public; Owner: ospf
--

CREATE TYPE network_type AS ENUM (
    'BROADCAST',
    'NON_BROADCAST',
    'POINT_TO_POINT',
    'POINT_TO_MULTIPOINT'
);


ALTER TYPE public.network_type OWNER TO ospf;

--
-- Name: permitordeny; Type: TYPE; Schema: public; Owner: ospf
--

CREATE TYPE permitordeny AS ENUM (
    'permit',
    'deny'
);


ALTER TYPE public.permitordeny OWNER TO ospf;

--
-- Name: route_types; Type: TYPE; Schema: public; Owner: ospf
--

CREATE TYPE route_types AS ENUM (
    'STATIC',
    'CONNECTED',
    'BGP'
);


ALTER TYPE public.route_types OWNER TO ospf;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: areas; Type: TABLE; Schema: public; Owner: ospf; Tablespace: 
--

CREATE TABLE areas (
    id integer NOT NULL,
    area_id inet,
    is_stub boolean,
    ospf_authentication_id integer,
    net_address cidr,
    enable boolean,
    is_no_summary boolean,
    auth_type authentication_type
);


ALTER TABLE public.areas OWNER TO ospf;

--
-- Name: areas_id_seq; Type: SEQUENCE; Schema: public; Owner: ospf
--

CREATE SEQUENCE areas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.areas_id_seq OWNER TO ospf;

--
-- Name: areas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ospf
--

ALTER SEQUENCE areas_id_seq OWNED BY areas.id;


--
-- Name: ospf_authentications; Type: TABLE; Schema: public; Owner: ospf; Tablespace: 
--

CREATE TABLE ospf_authentications (
    id integer NOT NULL,
    key_id integer,
    auth_type authentication_type
);


ALTER TABLE public.ospf_authentications OWNER TO ospf;

--
-- Name: ospf_authentications_id_seq; Type: SEQUENCE; Schema: public; Owner: ospf
--

CREATE SEQUENCE ospf_authentications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ospf_authentications_id_seq OWNER TO ospf;

--
-- Name: ospf_authentications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ospf
--

ALTER SEQUENCE ospf_authentications_id_seq OWNED BY ospf_authentications.id;


--
-- Name: ospf_interfaces; Type: TABLE; Schema: public; Owner: ospf; Tablespace: 
--

CREATE TABLE ospf_interfaces (
    cost integer DEFAULT 1,
    dead_interval integer DEFAULT 40,
    hello_interval integer DEFAULT 10,
    priority integer DEFAULT 1,
    retransmit_interval integer DEFAULT 5,
    interface_name character varying NOT NULL,
    network_type network_type DEFAULT 'BROADCAST'::network_type,
    enable boolean,
    is_passive boolean,
    transmit_delay integer DEFAULT 1,
    ospf_area_id inet,
    ospf_authentication_key character varying(16),
    interface_id integer
);


ALTER TABLE public.ospf_interfaces OWNER TO ospf;

--
-- Name: ospf_router; Type: TABLE; Schema: public; Owner: ospf; Tablespace: 
--

CREATE TABLE ospf_router (
    router_id inet,
    spf_delay_msec integer,
    spf_init_holdtime_msec integer,
    spf_max_holdtime_msec integer,
    is_enable boolean,
    passive_interfaces character varying[]
);


ALTER TABLE public.ospf_router OWNER TO ospf;

--
-- Name: route_maps; Type: TABLE; Schema: public; Owner: ospf; Tablespace: 
--

CREATE TABLE route_maps (
    id integer NOT NULL,
    route_map_name character varying NOT NULL,
    action permitordeny NOT NULL,
    orders integer NOT NULL,
    match_param character varying NOT NULL,
    match_value character varying NOT NULL,
    action_param character varying NOT NULL,
    action_value character varying NOT NULL,
    exit_action character varying
);


ALTER TABLE public.route_maps OWNER TO ospf;

--
-- Name: route_maps_id_seq; Type: SEQUENCE; Schema: public; Owner: ospf
--

CREATE SEQUENCE route_maps_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.route_maps_id_seq OWNER TO ospf;

--
-- Name: route_maps_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ospf
--

ALTER SEQUENCE route_maps_id_seq OWNED BY route_maps.id;


--
-- Name: routes; Type: TABLE; Schema: public; Owner: ospf; Tablespace: 
--

CREATE TABLE routes (
    id integer NOT NULL,
    is_enable boolean,
    source_protocol_type route_types,
    metric integer,
    route_map_id integer,
    redistribute_type integer,
    CONSTRAINT routes_redistribute_type_check CHECK (((redistribute_type = 1) OR (redistribute_type = 2)))
);


ALTER TABLE public.routes OWNER TO ospf;

--
-- Name: routes_id_seq; Type: SEQUENCE; Schema: public; Owner: ospf
--

CREATE SEQUENCE routes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.routes_id_seq OWNER TO ospf;

--
-- Name: routes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ospf
--

ALTER SEQUENCE routes_id_seq OWNED BY routes.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ospf
--

ALTER TABLE ONLY areas ALTER COLUMN id SET DEFAULT nextval('areas_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ospf
--

ALTER TABLE ONLY ospf_authentications ALTER COLUMN id SET DEFAULT nextval('ospf_authentications_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ospf
--

ALTER TABLE ONLY route_maps ALTER COLUMN id SET DEFAULT nextval('route_maps_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ospf
--

ALTER TABLE ONLY routes ALTER COLUMN id SET DEFAULT nextval('routes_id_seq'::regclass);


--
-- Data for Name: areas; Type: TABLE DATA; Schema: public; Owner: ospf
--

COPY areas (id, area_id, is_stub, ospf_authentication_id, net_address, enable, is_no_summary, auth_type) FROM stdin;
2	0.0.0.0	t	1	172.16.121.0/24	t	f	MD5
\.


--
-- Name: areas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ospf
--

SELECT pg_catalog.setval('areas_id_seq', 2, true);


--
-- Data for Name: ospf_authentications; Type: TABLE DATA; Schema: public; Owner: ospf
--

COPY ospf_authentications (id, key_id, auth_type) FROM stdin;
1	1	MD5
2	2	PASSWORD
\.


--
-- Name: ospf_authentications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ospf
--

SELECT pg_catalog.setval('ospf_authentications_id_seq', 2, true);


--
-- Data for Name: ospf_interfaces; Type: TABLE DATA; Schema: public; Owner: ospf
--

COPY ospf_interfaces (cost, dead_interval, hello_interval, priority, retransmit_interval, interface_name, network_type, enable, is_passive, transmit_delay, ospf_area_id, ospf_authentication_key, interface_id) FROM stdin;
1	40	10	1	5	eth0	BROADCAST	t	f	1	0.0.0.0	sdfaswsfsdf	\N
\.


--
-- Data for Name: ospf_router; Type: TABLE DATA; Schema: public; Owner: ospf
--

COPY ospf_router (router_id, spf_delay_msec, spf_init_holdtime_msec, spf_max_holdtime_msec, is_enable, passive_interfaces) FROM stdin;
0.0.0.1	200	400	1000	t	{eth0,lo0}
\.


--
-- Data for Name: route_maps; Type: TABLE DATA; Schema: public; Owner: ospf
--

COPY route_maps (id, route_map_name, action, orders, match_param, match_value, action_param, action_value, exit_action) FROM stdin;
\.


--
-- Name: route_maps_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ospf
--

SELECT pg_catalog.setval('route_maps_id_seq', 1, false);


--
-- Data for Name: routes; Type: TABLE DATA; Schema: public; Owner: ospf
--

COPY routes (id, is_enable, source_protocol_type, metric, route_map_id, redistribute_type) FROM stdin;
2	t	STATIC	1	\N	\N
1	t	CONNECTED	11	\N	1
\.


--
-- Name: routes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ospf
--

SELECT pg_catalog.setval('routes_id_seq', 2, true);


--
-- Name: areas_pkey; Type: CONSTRAINT; Schema: public; Owner: ospf; Tablespace: 
--

ALTER TABLE ONLY areas
    ADD CONSTRAINT areas_pkey PRIMARY KEY (id);


--
-- Name: ospf_authentications_key_key; Type: CONSTRAINT; Schema: public; Owner: ospf; Tablespace: 
--

ALTER TABLE ONLY ospf_authentications
    ADD CONSTRAINT ospf_authentications_key_key UNIQUE (key_id);


--
-- Name: ospf_authentications_pkey; Type: CONSTRAINT; Schema: public; Owner: ospf; Tablespace: 
--

ALTER TABLE ONLY ospf_authentications
    ADD CONSTRAINT ospf_authentications_pkey PRIMARY KEY (id);


--
-- Name: route_maps_pkey; Type: CONSTRAINT; Schema: public; Owner: ospf; Tablespace: 
--

ALTER TABLE ONLY route_maps
    ADD CONSTRAINT route_maps_pkey PRIMARY KEY (id);


--
-- Name: routes_route_map_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: ospf
--

ALTER TABLE ONLY routes
    ADD CONSTRAINT routes_route_map_id_fkey FOREIGN KEY (route_map_id) REFERENCES route_maps(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

