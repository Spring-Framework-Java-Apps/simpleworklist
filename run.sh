#!/usr/bin/env bash

source etc/setenv.sh

function runDev() {
    ./mvnw
}

function runTest() {
    ./mvnw -B clean dependency:list install --file pom.xml
}

function runGithubTestBuild() {
    #./mvnw -B -DskipTests -DskipIntegrationTests clean dependency:list install --file pom.xml
      ./mvnw -B -DskipTests clean dependency:list install --file pom.xml
}

function setupHeroku() {
    heroku login
    heroku ps -a simpleworklist
}

function buildLikeHeroku() {
    #./mvnw -DskipTests -DskipIntegrationTests clean dependency:list install
   ./mvnw -DskipTests clean dependency:list install
}

function runHerokuLocal() {
    buildLikeHeroku
    heroku ps -a simpleworklist
    heroku local web
    heroku open
}

function main() {
    runGithubTestBuild
    #setupHeroku
    #runHerokuLocal
    #runDev
    #runTest
}

main
