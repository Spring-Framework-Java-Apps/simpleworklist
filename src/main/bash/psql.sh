#!/usr/bin/env bash

echo "simpleworklistpwd"
psql -h localhost -U simpleworklist -p 5432 -d simpleworklist
