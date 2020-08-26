#!/usr/bin/env bash

source src/main/bash/setenv.sh

function composeUp() {
    ./mvnw docker-compose:up
}

function composeDown() {
    ./mvnw docker-compose:down
}

function firstSetup() {
    export JAVA_OPTS=$JAVA_OPTS_RUN_DEFAULT
    showSettings
    ./mvnw dependency:purge-local-repository
    ./mvnw -e -DskipTests=true clean dependency:resolve dependency:resolve-plugins dependency:sources dependency:tree
    ./mvnw -e -DskipTests=true clean package spring-boot:repackage site
}

function setupTravis() {
    export JAVA_OPTS=$JAVA_OPTS_RUN_DEFAULT
    showSettings
    ./mvnw -e -DskipTests=true -B -V install -Dmaven.javadoc.skip=true && \
    ./mvnw -e -DskipTests=true -B -V dependency:purge-local-repository && \
    ./mvnw -e -DskipTests=true -B -V clean && \
    ./mvnw -e -DskipTests=true -B -V dependency:resolve dependency:resolve-plugins dependency:sources && \
    ./mvnw -e -DskipTests=true -B -V dependency:tree && \
    ./mvnw -e -DskipTests=true -B -V clean package spring-boot:repackage && \
    ./mvnw -e -DskipTests=true -B -V site
}

function main() {
    # firstSetup
    setupTravis
}

main
