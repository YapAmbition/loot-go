#!/bin/bash

CUR_DIR=`dirname "$0"`

MAIN_CLASS=com.nikfce.Application
MEM_OPTS="-Xms512m -Xmx512m"
GC_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200"
CLASS_PATH="$CUR_DIR/conf/:$CUR_DIR/conf/scene/:$CUR_DIR/conf/looter:$CUR_DIR/lib/*:$CLASS_PATH"

echo "nohup exec java -server $MEM_OPTS $GC_OPTS -classpath $CLASS_PATH $MAIN_CLASS &"
exec nohup java -server $MEM_OPTS $GC_OPTS -classpath $CLASS_PATH $MAIN_CLASS &