#!/usr/bin/env bash

source setenv.sh

function setupHeroku() {
    heroku login
    heroku ps
}

function runHerokuLocal() {
    heroku ps
    ./mvnw clean install
    heroku local web
    heroku open
}

function runDev() {
    ./mvnw clean install
}

function main() {
    runDev
    #setupHeroku
    #runHerokuLocal
}

main
