@echo off

call etc\setenv.cmd

goto :main

:main
goto :runmaven
rem call :runGithubTestBuild
rem call :runHerokuLocal
rem call :buildLikeHerokuWithSite
rem call :runTest
goto :end


:runMaven
mvnw
goto :end

:runTest
mvnw -B -DskipTests=false clean dependency:list install --file pom.xml
goto :end

:runGithubTestBuild
mvnw -B -DskipTests clean dependency:list install --file pom.xml
goto :end

:buildLikeHerokuWithSite
mvnw -DskipTests=true clean dependency:list install site site:deploy
goto :end

:runHerokuLocal	
rem heroku login
heroku ps -a simpleworklist
mvnw -DskipTests clean dependency:list install
heroku local web
heroku open
goto :end

:end
