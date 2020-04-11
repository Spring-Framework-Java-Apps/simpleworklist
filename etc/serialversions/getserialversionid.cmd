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
serialver org.woehlke.simpleworklist.application.language.Language > ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.task.TaskEnergy >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.task.TaskState >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.task.TaskTime >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecoveryStatus >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.register.UserRegistrationStatus >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.account.UserRole >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.application.common.AuditModel >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.context.Context  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.project.Project  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.task.Task  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.messages.User2UserMessage  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.account.UserAccount  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecovery  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.register.UserRegistration  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.breadcrumb.BreadcrumbItem  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.breadcrumb.Breadcrumb  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.login.LoginForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.context.NewContextForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.messages.User2UserMessageFormBean >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.searchRequest.SearchResult >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.account.UserAccountForm  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.context.UserChangeDefaultContextForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.application.language.UserChangeLanguageForm  >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.selfservice.UserChangeNameForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.selfservice.UserChangePasswordForm >> ..\..\etc\serialversions.txt
@rem serialver org.woehlke.simpleworklist.user.account.UserDetailsBean >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.user.register.UserRegistrationForm >> ..\..\etc\serialversions.txt
serialver org.woehlke.simpleworklist.session.UserSessionBean >> ..\..\etc\serialversions.txt
cd ..\..
echo FINISHED
