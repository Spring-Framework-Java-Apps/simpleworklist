#!/usr/bin/env bash

echo "simpleworklistpwd"
psql -h localhost -U simpleworklist -p 5433 -d simpleworklist
