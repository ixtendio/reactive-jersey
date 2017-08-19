#!/usr/bin/env bash

SERVER_PORT=8080
DATASET_DIR=./dataset
APP_NAME=reactive-jersey
EXECUTABLE_JAR=$(ls target/*.jar |grep $APP_NAME) \

case "$1" in
    debug)
        DEBUG=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
        ;;
    *)
        DEBUG=
esac

java $DEBUG -Dserver.port=$SERVER_PORT -Ddataset.path="$DATASET_DIR" -DAPP_NAME=$APP_NAME -jar $EXECUTABLE_JAR