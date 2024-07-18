--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2024-07-18 16:08:56

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE final_project;
--
-- TOC entry 3322 (class 1262 OID 25020)
-- Name: final_project; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE final_project WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';


\connect final_project

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 3323 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 25021)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    deleted boolean NOT NULL,
    deleted_date timestamp(6) without time zone,
    email character varying(255),
    name character varying(255),
    password character varying(255),
    updated_date timestamp(6) without time zone NOT NULL
);


--
-- TOC entry 3316 (class 0 OID 25021)
-- Dependencies: 214
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users VALUES ('97022c6c-56b5-4498-9d3d-f7fb7a1776b6', '2024-07-17 23:09:09.46', false, NULL, 'user1@gmail.com', 'User1', '$2a$10$zWFxslXTlxrH2X367tcujOxIfNs.tmcJ3l7s4.etdLzoAsrHJ6P02', '2024-07-17 23:09:09.46');
INSERT INTO public.users VALUES ('9651d6d6-1a0a-49cb-8556-6c55ff5e1541', '2024-07-18 16:01:30.362', false, NULL, 'user2@gmail.com', 'User2', '$2a$10$c1HSbYEibnPAgGDxpux3/eK4ggmbD9b9qRAauVi5cxQ5CATBy/GQe', '2024-07-18 16:01:30.362');


--
-- TOC entry 3173 (class 2606 OID 25027)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


-- Completed on 2024-07-18 16:08:56

--
-- PostgreSQL database dump complete
--

