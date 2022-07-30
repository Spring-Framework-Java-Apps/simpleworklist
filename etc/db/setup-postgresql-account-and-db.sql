CREATE ROLE simpleworklist LOGIN
    PASSWORD 'simpleworklistpwd'
    SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;
GRANT pg_monitor TO simpleworklist;
GRANT pg_read_all_settings TO simpleworklist;
GRANT pg_read_all_stats TO simpleworklist;
GRANT pg_signal_backend TO simpleworklist;
GRANT pg_stat_scan_tables TO simpleworklist;

CREATE ROLE simpleworklistref LOGIN
    PASSWORD 'simpleworklistrefpwd'
    SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;
GRANT pg_monitor TO simpleworklistref;
GRANT pg_read_all_settings TO simpleworklistref;
GRANT pg_read_all_stats TO simpleworklistref;
GRANT pg_signal_backend TO simpleworklistref;
GRANT pg_stat_scan_tables TO simpleworklistref;

CREATE TABLESPACE tablespace_simpleworklist
    OWNER tw
    LOCATION '/opt/postgresql/tablespace_simpleworklist';

CREATE TABLESPACE tablespace_simpleworklist
    OWNER tw
    LOCATION 'C:\tablespace_pg\simpleworklist';

ALTER TABLESPACE tablespace_simpleworklist
    OWNER TO simpleworklist;

CREATE DATABASE simpleworklist
    WITH OWNER = simpleworklist
    ENCODING = 'UTF8'
    TABLESPACE = tablespace_simpleworklist
    CONNECTION LIMIT = -1;

CREATE DATABASE simpleworklistref
    WITH OWNER = simpleworklistref
    ENCODING = 'UTF8'
    TABLESPACE = tablespace_simpleworklist
    CONNECTION LIMIT = -1;
