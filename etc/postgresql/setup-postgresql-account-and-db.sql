-- Role: simpleworklist

-- DROP ROLE simpleworklist;

CREATE ROLE simpleworklist LOGIN
    ENCRYPTED PASSWORD 'md5010a71cb83ab0724fd85c338e2e87614'
    SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;
GRANT pg_monitor TO simpleworklist;
GRANT pg_read_all_settings TO simpleworklist;
GRANT pg_read_all_stats TO simpleworklist;
GRANT pg_signal_backend TO simpleworklist;
GRANT pg_stat_scan_tables TO simpleworklist;


-- Database: simpleworklist

-- DROP DATABASE simpleworklist;

CREATE DATABASE simpleworklist
    WITH OWNER = simpleworklist
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    CONNECTION LIMIT = -1;

