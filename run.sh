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
    heroku local web
    heroku open
}

function main() {
    runGithubTestBuild
    #runDev
    #setupHeroku
    #runHerokuLocal
}

main
