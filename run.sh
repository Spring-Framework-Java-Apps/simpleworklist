#!/usr/bin/env bash

source src/main/bash/setenv.sh

function composeUp() {
    ./mvnw docker-compose:up
}

function composeDown() {
    ./mvnw docker-compose:down
}

function runTest() {
    ./mvnw -B -DskipTests=false clean dependency:list install --file pom.xml
}

function runGithubTestBuild() {
    ./mvnw -B -DskipTests clean dependency:list install --file pom.xml
}

function setupHeroku() {
    heroku login
    heroku ps -a simpleworklist
}

function buildLikeHerokuWithSite() {
   ./mvnw -DskipTests=true clean dependency:list install site site:deploy
}

function buildLikeHeroku() {
   ./mvnw -DskipTests clean dependency:list install
}

function runHerokuLocal() {
    buildLikeHeroku
    heroku ps -a simpleworklist
    heroku local web
    heroku open
}

function setup() {
   setupHeroku
}

function testing() {
   runTest
}

function runDev() {
    ./mvnw
}

function run() {
    runHerokuLocal
    #runDev
}

function main() {
    ##release
    run
}

main

