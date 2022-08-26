#!/usr/bin/env bash

#source src/main/bash/setenv.sh
source ~/.bash_aliases_simpleworklist

function composeUp() {
    ./mvnw docker-compose:up
}

function composeDown() {
    ./mvnw docker-compose:down
}

function showSettings() {
    echo $JAVA_HOME
}

function firstSetup() {
    export JAVA_OPTS=$JAVA_OPTS_RUN_DEFAULT
    showSettings
    ./mvnw dependency:purge-local-repository
    ./mvnw -e -DskipTests=true clean dependency:resolve dependency:resolve-plugins dependency:sources dependency:tree
    ./mvnw -e -DskipTests=true clean package spring-boot:repackage site site:deploy
}

function setupTravis() {
    export JAVA_OPTS=$JAVA_OPTS_RUN_DEFAULT
    showSettings
    ./mvnw -e -DskipTests=true -B -V install -Dmaven.javadoc.skip=true && \
    ./mvnw -e -DskipTests=true -B -V dependency:purge-local-repository && \
    ./mvnw -e -DskipTests=true -B -V clean install && \
    ./mvnw -e -DskipTests=true -B -V dependency:tree && \
    ./mvnw -e -DskipTests=true -B -V dependency:resolve dependency:resolve-plugins dependency:sources && \
    ./mvnw -e -DskipTests=true -B -V clean package spring-boot:repackage && \
    ./mvnw -e -DskipTests=true -B -V site site:deploy
}

function setupTravis_tmp() {
    export JAVA_OPTS=$JAVA_OPTS_RUN_DEFAULT
    showSettings
    ./mvnw -e -DskipTests=true -B -V install -Dmaven.javadoc.skip=true && \
    ./mvnw -e -DskipTests=true -B -V dependency:purge-local-repository && \
    ./mvnw -e -DskipTests=true -B -V clean install && \
    ./mvnw -e -DskipTests=true -B -V dependency:tree && \
    ./mvnw -e -DskipTests=true -B -V dependency:resolve dependency:resolve-plugins dependency:sources && \
    ./mvnw -e -DskipTests=true -B -V clean package spring-boot:repackage && \
    ./mvnw -e -DskipTests=true -B -V site site:deploy
}

function buildJar() {
    export JAVA_OPTS=$JAVA_OPTS_RUN_DEFAULT
    showSettings
    ./mvnw dependency:purge-local-repository
    ./mvnw -e -DskipTests=true clean install dependency:tree spring-boot:repackage
}

function boot_run() {
    ./mvnw clean install spring-boot:run
}

function main() {
    # firstSetup
    setupTravis
    #buildJar
    #boot_run
}

# JAVA_OPTS=-XX:+UseContainerSupport -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8

main
