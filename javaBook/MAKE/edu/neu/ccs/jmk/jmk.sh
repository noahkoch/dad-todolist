#! /bin/sh
# $Id: jmk.sh,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $
# Assumes jar file is in ../lib relative to this file
COMMAND=`command -v "$0"`
JAR=`dirname "$COMMAND"`/../lib/jmk.jar

exec java -jar ${JAR} "$@"
