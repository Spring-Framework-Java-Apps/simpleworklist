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






CREATE ROLE petclinic_jakartaee LOGIN
    PASSWORD 'petclinic_jakartaeepwd'
    SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;
GRANT pg_monitor TO petclinic_jakartaee;
GRANT pg_read_all_settings TO petclinic_jakartaee;
GRANT pg_read_all_stats TO petclinic_jakartaee;
GRANT pg_signal_backend TO petclinic_jakartaee;
GRANT pg_stat_scan_tables TO petclinic_jakartaee;


-- Database: petclinic_jakartaee

-- DROP DATABASE petclinic_jakartaee;
-- CONNECTION LIMIT = -1;

CREATE TABLESPACE tablespace_petclinic_jakartae
    OWNER tw
    LOCATION '/opt/postgresql/tablespace_petclinic_jakartae';

ALTER TABLESPACE tablespace_petclinic_jakartae
    OWNER TO simpleworklist;

CREATE DATABASE petclinic_jakartaee
    WITH OWNER = petclinic_jakartaee
    ENCODING = 'UTF8'
    TABLESPACE = tablespace_petclinic_jakartae
    CONNECTION LIMIT = -1;


