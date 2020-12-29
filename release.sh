#!/usr/bin/env bash

#source src/main/bash/setenv.sh

function releaseMe(){
    export JAVA_OPTS=$JAVA_OPTS_RUN_DEFAULT
    ./mvnw -e -DskipTests=true -B -V dependency:purge-local-repository
    ./mvnw -e -DskipTests=true -B -V clean
    ./mvnw -e -DskipTests=true -B -V release:clean
    ./mvnw -e -DskipTests=true -B -V release:prepare && ./mvnw -e -DskipTests=true -B -V release:perform
}

releaseMe
