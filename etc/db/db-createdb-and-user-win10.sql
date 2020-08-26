
CREATE USER simpleworklist WITH PASSWORD 'simpleworklistpwd' SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;

GRANT pg_monitor TO simpleworklist;
GRANT pg_read_all_settings TO simpleworklist;
GRANT pg_read_all_stats TO simpleworklist;
GRANT pg_signal_backend TO simpleworklist;
GRANT pg_stat_scan_tables TO simpleworklist;

CREATE DATABASE simpleworklist WITH OWNER simpleworklist ENCODING 'UTF8' LC_COLLATE 'de-DE' LC_CTYPE 'de-DE';
CREATE DATABASE simpleworklist_default WITH OWNER simpleworklist ENCODING 'UTF8' LC_COLLATE 'de-DE' LC_CTYPE 'de-DE';
CREATE DATABASE simpleworklist_branch_master WITH OWNER simpleworklist ENCODING 'UTF8' LC_COLLATE 'de-DE' LC_CTYPE 'de-DE';
CREATE DATABASE simpleworklist_testing WITH OWNER simpleworklist ENCODING 'UTF8' LC_COLLATE 'de-DE' LC_CTYPE 'de-DE';
CREATE DATABASE simpleworklist_qa WITH OWNER simpleworklist ENCODING 'UTF8' LC_COLLATE 'de-DE' LC_CTYPE 'de-DE';
CREATE DATABASE simpleworklist_heroku WITH OWNER simpleworklist ENCODING 'UTF8' LC_COLLATE 'de-DE' LC_CTYPE 'de-DE';

GRANT ALL ON DATABASE simpleworklist TO simpleworklist WITH GRANT OPTION;
GRANT ALL ON DATABASE simpleworklist_default TO simpleworklist WITH GRANT OPTION;
GRANT ALL ON DATABASE simpleworklist_branch_master TO simpleworklist WITH GRANT OPTION;
GRANT ALL ON DATABASE simpleworklist_testing TO simpleworklist WITH GRANT OPTION;
GRANT ALL ON DATABASE simpleworklist_qa TO simpleworklist WITH GRANT OPTION;
GRANT ALL ON DATABASE simpleworklist_heroku TO simpleworklist WITH GRANT OPTION;




