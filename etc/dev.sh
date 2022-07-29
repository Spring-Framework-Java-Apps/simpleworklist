#!/usr/bin/env bash

#source src/main/bash/setenv.sh
source ~/.bash_aliases_simpleworklist

./mvnw clean install spring-boot:repackage

sudo cp -f target/simpleworklist.war /var/lib/tomcat9/webapps/
