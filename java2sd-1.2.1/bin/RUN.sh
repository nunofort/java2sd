
JAVA_HOME=/usr/lib/jvm/java-6-sun
#JAVA_HOME=/usr/lib/jvm/java-1.7.0-openjdk-amd64
#JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.22
#JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64

JAVA_BIN=$JAVA_HOME/bin/java

#OPTION="-view"
#OPTION="-save test.xml -view"
OPTION="-load test.xml -ps"

#$JAVA_BIN -cp "${JAVA_HOME}/lib/tools.jar:." pt/ipbeja/estig/sd/SD -help

$JAVA_BIN -d64 -cp "${JAVA_HOME}/lib/tools.jar:." pt/ipbeja/estig/sd/SD -from pt/ipbeja/estig/test/Test $OPTION 

#$JAVA_BIN -cp "${JAVA_HOME}/lib/tools.jar:." pt/ipbeja/estig/test/Test

