#!/bin/sh
#
# Gradle start up script for UN*X
#
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`
APP_HOME="`pwd -P`"
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
JAVA_OPTS=""
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"
warn () { echo "$*"; }
die () { echo; echo "$*"; echo; exit 1; }
# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi
exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain "$@"
