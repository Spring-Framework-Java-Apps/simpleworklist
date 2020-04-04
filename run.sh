#!/usr/bin/env bash

source setenv.sh



function runDev() {
    ./mvnw
}

function runGithubTestBuild() {
    ./mvnw -B package --file pom.xml
}


function setupHeroku() {
    heroku login
    heroku ps -a simpleworklist
}

function buildLikeHeroku() {
   ./mvnw -DskipTests clean dependency:list install
}

function runHerokuLocal() {
    buildLikeHeroku
    heroku ps -a simpleworklist
    ./mvnw clean install
    heroku local web
    heroku open
}

function main() {
    #runDev
    #setupHeroku
    runHerokuLocal
}

main
