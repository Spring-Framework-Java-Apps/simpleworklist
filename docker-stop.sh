#!/usr/bin/env bash

./mvnw docker-compose:down

systemctl start postgresql
