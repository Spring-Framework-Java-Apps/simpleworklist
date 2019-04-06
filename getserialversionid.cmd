@echo OFF
echo STARTED
echo %CLASSPATH%
echo %JAVA_HOME%
echo %HOME%
set CLASSPATH=
@rem call mvnw.cmd --batch-mode --log-file=etc/mavenlog1.txt clean install -Pdeveloping -DskipTests=true -Dmaven.javadoc.skip=true -Dmdep.outputFile=etc\classpath.txt
echo START MAVEN
call mvnw.cmd --batch-mode --log-file=etc/mavenlog1.txt clean install -DskipTests=true -Dmaven.javadoc.skip=true
@rem call mvnw.cmd --batch-mode --log-file=etc/mavenlog2.txt dependency:unpack-dependencies
call mvnw.cmd --batch-mode --log-file=etc/mavenlog3.txt dependency:build-classpath -Dmdep.outputFile=etc\classpath.txt
echo FINISHED MAVEN
set /P MY_CLASSPATH_DEPS= < etc\classpath.txt
set MY_CLASSPATH_APP=%JAVA_HOME%\jre\lib\rt.jar;%CD%\target\classes
set CLASSPATH=%MY_CLASSPATH_APP%;%MY_CLASSPATH_DEPS%
echo %CLASSPATH%
cd target\classes
serialver org.woehlke.simpleworklist.model.LoginFormBean > ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.NewContextFormBean >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.RegisterFormBean >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.SearchResult >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserAccountFormBean  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserChangeDefaultContextFormBean >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserChangeLanguageFormBean  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserChangeNameFormBean >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserChangePasswordFormBean >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserSessionBean >> ..\..\etc\serialversions.txt
@rem serialver org.springframework.security.core.userdetails.UserDetails >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserDetailsBean >> ..\..\etc\serialversions.txt
cd ..\..
echo FINISHED
