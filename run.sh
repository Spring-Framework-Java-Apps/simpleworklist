#!/usr/bin/env bash

source etc/setenv.sh

function runDev() {
    ./mvnw
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

function build() {
    buildLikeHerokuWithSite
    #buildLikeHeroku
    #runGithubTestBuild
}

function testing() {
   runTest
}

function run() {
    #runHerokuLocal
    runDev
}

function main() {
    build
    #run
}

main

