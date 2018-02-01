
rm -f com/sun/tools/example/trace/*.class
rm -f pt/ipbeja/estig/sd/*.class
rm -f pt/ipbeja/estig/test/*.class

JAVA_HOME=/usr/lib/jvm/java-6-sun
#JAVA_HOME=/usr/lib/jvm/java-1.7.0-openjdk-amd64
#JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.22
#JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64

JAVA_BIN=$JAVA_HOME/bin/javac

$JAVA_BIN -g -Xlint:unchecked -cp "${JAVA_HOME}/lib/tools.jar:." pt/ipbeja/estig/test/Test.java com/sun/tools/example/trace/Trace.java pt/ipbeja/estig/sd/SD.java
$JAVA_BIN -Xlint:unchecked pt/ipbeja/estig/sd/Invoke.java

