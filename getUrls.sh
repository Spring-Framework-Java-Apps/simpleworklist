#!/usr/bin/env bash

ROOT_PATH_PROJECT=`pwd`
ROOT_PATH_SRC="$ROOT_PATH_PROJECT/src/main/java/org/woehlke/simpleworklist"

SUBDIRS="context error project search task taskstate testdata"
SUBDIRS_USER="login messages register resetpassword selfservice "

OUTPUT="etc/urls01.txt"
OUTPUT2="etc/urls02.txt"

echo "$OUTPUT" > $OUTPUT
echo "---------------------------------------------------------------------" >> $OUTPUT
date >> $OUTPUT
echo "---------------------------------------------------------------------" >> $OUTPUT
for i in $SUBDIRS ; do
    echo "$ROOT_PATH_SRC/$i" >> $OUTPUT
    echo "---------------------------------------------------------------------" >> $OUTPUT
    grep -n "RequestMapping" $ROOT_PATH_SRC/$i/*Controller*.java | grep -v "import org.springframework.web.bind.annotation.RequestMapping" >> $OUTPUT
done
echo "---------------------------------------------------------------------" >> $OUTPUT
for i in $SUBDIRS_USER ; do
    echo "$ROOT_PATH_SRC/user/$i" >> $OUTPUT
    echo "---------------------------------------------------------------------" >> $OUTPUT
    grep -n "RequestMapping" $ROOT_PATH_SRC/user/$i/*Controller*.java | grep -v "import org.springframework.web.bind.annotation.RequestMapping" >> $OUTPUT
done
echo "---------------------------------------------------------------------" >> $OUTPUT

cat $OUTPUT | sed "s/\/home\/tw\/IdeaProjects\/Spring-Framework-Java-Apps\/simpleworklist\/src\/main\/java\/org\/woehlke\/simpleworklist//g" | tr -s " " | cut -d" " -f2-4 > $OUTPUT2
cat $OUTPUT2
