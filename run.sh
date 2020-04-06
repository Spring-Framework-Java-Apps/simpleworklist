#!/usr/bin/env bash

source etc/setenv.sh

function runDev() {
    ./mvnw
}

function runTest() {
    ./mvnw -B -DskipTests=false  clean dependency:list install --file pom.xml
}

function runGithubTestBuild() {
    ./mvnw -B -DskipTests clean dependency:list install --file pom.xml
}

function setupHeroku() {
    heroku login
    heroku ps -a simpleworklist
}

function buildLikeHeroku() {
   ./mvnw -DskipTests clean dependency:list install site site:deploy
}

function runHerokuLocal() {
    buildLikeHeroku
    heroku ps -a simpleworklist
    heroku local web
    heroku open
}

function main() {
    #runGithubTestBuild
    #setupHeroku
    buildLikeHeroku
    #runHerokuLocal
    #runDev
    #runTest
}

main
