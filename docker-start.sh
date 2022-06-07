#!/usr/bin/env bash

systemctl stop postgresql

./mvnw docker-compose:up
