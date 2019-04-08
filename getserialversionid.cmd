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
serialver org.woehlke.simpleworklist.entities.entities.impl.AuditModel > ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.enumerations.Language >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.enumerations.TaskEnergy >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.enumerations.TaskState >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.enumerations.TaskStateView >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.enumerations.TaskTime >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.enumerations.UserPasswordRecoveryStatus >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.enumerations.UserRegistrationStatus >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.enumerations.UserRole >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.Context  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.Project  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.Task  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.User2UserMessage  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.UserAccount  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.UserPasswordRecovery  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.entities.UserRegistration  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.BreadcrumbItem  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.Breadcrumb  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.LoginForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.NewContextForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserRegistrationForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.SearchResult >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserAccountForm  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserChangeDefaultContextForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserChangeLanguageForm  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserChangeNameForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserChangePasswordForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.UserSessionBean >> ..\..\etc\serialversions.txt
@rem serialver org.woehlke.simpleworklist.model.UserDetailsBean >> ..\..\etc\serialversions.txt
cd ..\..
echo FINISHED
