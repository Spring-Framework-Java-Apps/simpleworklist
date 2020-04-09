#!/usr/bin/env bash

export PROJECT_PATH=`pwd`

echo STARTED
echo "CLASSPATH $CLASSPATH"
echo "JAVA_HOME $JAVA_HOME"
echo "HOME $HOME"
echo "PROJECT_PATH $PROJECT_PATH"
export CLASSPATH_FILE=$PROJECT_PATH/etc/classpath.txt
export CLASSPATH=
#@rem call mvnw.cmd --batch-mode --log-file=etc/mavenlog1.txt clean install -Pdeveloping -DskipTests=true -Dmaven.javadoc.skip=true -Dmdep.outputFile=etc\classpath.txt
echo "START MAVEN"
./mvnw --batch-mode --log-file=etc/mavenlog1.txt clean install -DskipTests=true -Dmaven.javadoc.skip=true
#@rem call mvnw.cmd --batch-mode --log-file=etc/mavenlog2.txt dependency:unpack-dependencies
./mvnw --batch-mode --log-file=etc/mavenlog3.txt dependency:build-classpath -Dmdep.outputFile=$CLASSPATH_FILE
echo "FINISHED MAVEN"

export MY_CLASSPATH_DEPS=`cat $CLASSPATH_FILE`
export CLASSES_PATH=$PROJECT_PATH/target/classes
export JAVA_RUNTIME_JAR_PATH=$JAVA_HOME/jre/lib/rt.jar

export MY_CLASSPATH=$JAVA_RUNTIME_JAR_PATH:$CLASSES_PATH:$MY_CLASSPATH_DEPS
export CLASSPATH=MY_CLASSPATH

export SERIALVERSIONS_FILE="$PROJECT_PATH/etc/serialversions.txt"

echo "CLASSPATH $CLASSPATH"
cd $CLASSES_PATH
serialver org.woehlke.simpleworklist.language.Language > $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.task.TaskEnergy >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.task.TaskState >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.task.TaskTime >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecoveryStatus >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.register.UserRegistrationStatus >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.account.UserRole >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.common.AuditModel >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.context.Context  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.project.Project  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.task.Task  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.messages.User2UserMessage  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.account.UserAccount  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecovery  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.register.UserRegistration  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.breadcrumb.BreadcrumbItem  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.breadcrumb.Breadcrumb  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.login.LoginForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.context.NewContextForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.messages.User2UserMessageFormBean >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.search.SearchResult >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.account.UserAccountForm  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.context.UserChangeDefaultContextForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.language.UserChangeLanguageForm  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.selfservice.UserChangeNameForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.selfservice.UserChangePasswordForm >> $SERIALVERSIONS_FILE
#serialver org.woehlke.simpleworklist.user.UserDetailsBean >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.register.UserRegistrationForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.UserSessionBean >> $SERIALVERSIONS_FILE
cd $PROJECT_PATH
echo FINISHED
