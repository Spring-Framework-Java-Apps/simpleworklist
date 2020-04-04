#!/usr/bin/env bash

source setenv.sh

function setupHeroku() {
    heroku login
    heroku ps -a simpleworklist
}

function runHerokuLocal() {
    heroku ps -a simpleworklist
    ./mvnw clean install
    heroku local web
    heroku open
}

function runDev() {
    ./mvnw clean install
}

function runGithubTestBuild() {
    ./mvnw -B package --file pom.xml
}

function main() {
    runDev
    #setupHeroku
    #runHerokuLocal
}

main
