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
serialver org.woehlke.simpleworklist.oodm.enumerations.Language > ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.task.TaskEnergy >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.task.TaskState >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.task.TaskTime >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.enumerations.UserPasswordRecoveryStatus >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.enumerations.UserRegistrationStatus >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.enumerations.UserRole >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.entities.impl.AuditModel >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.entities.Context  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.entities.Project  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.task.Task  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.entities.User2UserMessage  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.entities.UserAccount  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.entities.UserPasswordRecovery  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.oodm.entities.UserRegistration  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.breadcrumb.BreadcrumbItem  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.breadcrumb.Breadcrumb  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.LoginForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.NewContextForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.NewUser2UserMessage >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.SearchResult >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.UserAccountForm  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.UserChangeDefaultContextForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.UserChangeLanguageForm  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.UserChangeNameForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.UserChangePasswordForm >> ..\..\etc\serialversions.txt
@rem serialver org.woehlke.simpleworklist.model.beans.UserDetailsBean >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.UserRegistrationForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.model.beans.UserSessionBean >> ..\..\etc\serialversions.txt
cd ..\..
echo FINISHED
