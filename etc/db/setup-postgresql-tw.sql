CREATE ROLE tw LOGIN
    PASSWORD 'twpwd'
    SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;
GRANT pg_monitor TO tw;
GRANT pg_read_all_settings TO tw;
GRANT pg_read_all_stats TO tw;
GRANT pg_signal_backend TO tw;
GRANT pg_stat_scan_tables TO tw;

CREATE DATABASE tw
    WITH OWNER = tw
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

