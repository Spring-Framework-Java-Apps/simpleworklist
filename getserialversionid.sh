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
serialver org.woehlke.simpleworklist.oodm.enumerations.Language > $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.task.TaskEnergy >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.task.TaskState >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.task.TaskTime >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.enumerations.UserPasswordRecoveryStatus >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.enumerations.UserRegistrationStatus >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.enumerations.UserRole >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.entities.impl.AuditModel >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.entities.Context  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.entities.Project  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.task.Task  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.entities.User2UserMessage  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.entities.UserAccount  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.entities.UserPasswordRecovery  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.oodm.entities.UserRegistration  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.breadcrumb.BreadcrumbItem  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.breadcrumb.Breadcrumb  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.user.login.LoginForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.NewContextForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.NewUser2UserMessage >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.SearchResult >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.UserAccountForm  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.UserChangeDefaultContextForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.UserChangeLanguageForm  >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.UserChangeNameForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.UserChangePasswordForm >> $SERIALVERSIONS_FILE
#serialver org.woehlke.simpleworklist.model.beans.UserDetailsBean >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.UserRegistrationForm >> $SERIALVERSIONS_FILE
serialver org.woehlke.simpleworklist.model.beans.UserSessionBean >> $SERIALVERSIONS_FILE
cd $PROJECT_PATH
echo FINISHED
