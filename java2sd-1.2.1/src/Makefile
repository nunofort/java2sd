
all: usage 

JAVA_HOME=/usr/lib/jvm/java-6-sun
JAVA_C=${JAVA_HOME}/bin/javac
JAVA_VM=${JAVA_HOME}/bin/java
JAVA_OPTIONS=-classpath "${JAVA_HOME}/lib/tools.jar:." 
JAVAC_OPTIONS=-g -Xlint:unchecked -cp "${JAVA_HOME}/lib/tools.jar:." 
PROGRAM_CLASS=pt/ipbeja/estig/sd/SD
TEST_CLASS=pt/ipbeja/estig/test/Test
#PROGRAM_OPTIONS=-save SD.xml -load SD.xml -ps -seq 
PROGRAM_OPTIONS=-save SD.xml -ps
#PROGRAM_OPTIONS=-save SD.xml -ps -cp "./pt/ipbeja/estig/test:." -call "pt.ipbeja.estig.test.Add(int::15,,int::20);getResult();" 
#PROGRAM_OPTIONS=-save SD.xml -ps -call "pt.ipbeja.estig.test.Add(int::15,,int::20);getResult();" 
#PROGRAM_OPTIONS=-save SD.xml -view
#PROGRAM_OPTIONS=-save SD.xml -seq 
#PROGRAM_OPTIONS=-view -svg 
#PROGRAM_OPTIONS=-load SD.xml -view 

#INVOKE Feature Class in STAND ALONE
#java pt/ipbeja/estig/sd/Invoke "pt.ipbeja.estig.test.Add(int::15,,int::20);getResult()"

usage:
	@echo "make [test|ExpressionTree|dispenserMachine|operacoesArray|operacoesArrayList|sd" 

help:
	@echo "Help Screen"
	${JAVA_VM} ${JAVA_OPTIONS} ${PROGRAM_CLASS} -help 

install:
	@echo "Compiling..."
	${JAVA_C} ${JAVAC_OPTIONS} pt/ipbeja/estig/sd/SD.java com/sun/tools/example/trace/Trace.java pt/ipbeja/estig/test/Test.java
	${JAVA_C} ${JAVAC_OPTIONS} pt/ipbeja/estig/sd/Invoke.java
	@cp pt/ipbeja/estig/sd/*.class ../bin/pt/ipbeja/estig/sd/
	@cp pt/ipbeja/estig/test/*.class ../bin/pt/ipbeja/estig/test/
	@cp com/sun/tools/example/trace/*.class ../bin/com/sun/tools/example/trace/
 
clean:
	@echo "Cleaning Directories..."
	@rm -f com/sun/tools/example/trace/*.class
	@rm -f pt/ipbeja/estig/test/*.class
	@rm -f pt/ipbeja/estig/sd/*.class

test:
	@echo "Example Test..."
	$(JAVA_VM) ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from ${TEST_CLASS} ${PROGRAM_OPTIONS} 

	#java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from pt/ipbeja/estig/test/Test -start "2 Sub.Sub" ${PROGRAM_OPTIONS} 
	#java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from pt/ipbeja/estig/test/Test -start "1 Add.Add" ${PROGRAM_OPTIONS} 
	#java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from pt/ipbeja/estig/test/Test -start "3 Operation.calc" ${PROGRAM_OPTIONS} 
	#java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from program.log -start "1 Sub.Sub" ${PROGRAM_OPTIONS} 
	#java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from program.log ${PROGRAM_OPTIONS} 

        #java -jar SD.jar -from pt/ipbeja/estig/test/Test ${PROGRAM_OPTIONS}
sd:
	@echo "Example of our SD Application..."
	${JAVA_VM} ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from ${PROGRAM_CLASS} ${PROGRAM_OPTIONS} 
	#${JAVA_VM} ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from ${PROGRAM_CLASS} -load SD.xml -ps 
	#${JAVA_VM} ${JAVA_OPTIONS} ${PROGRAM_CLASS} -start "1 SD.loadXML" -from "pt/ipbeja/estig/sd/SD -load SD1.xml" ${PROGRAM_OPTIONS} 
	#java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -start "1 SD.init" -from "pt/ipbeja/estig/sd/SD -from pt/ipbeja/estig/test/Test" ${PROGRAM_OPTIONS} 
	#java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -start "1 SD.saveXML" -from "pt/ipbeja/estig/sd/SD -save SD1.xml" ${PROGRAM_OPTIONS} 
	#java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -start "1 SD.insertCallArrow" -from "pt/ipbeja/estig/sd/SD -from pt/ipbeja/estig/test/Test" ${PROGRAM_OPTIONS} 

ExpressionTree:
	@echo "Example ExpressionTree..."
	java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from "-classpath \"../examples/ExpressionTree\" TreeEvaluationTest" ${PROGRAM_OPTIONS}	

dispenserMachine:
	@echo "Example dispenserMachine..."
	java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from "-classpath \"../examples/dispenserMachine\" TestDispenser" ${PROGRAM_OPTIONS}

operacoesArrayList:
	@echo "Example operacoesArrayList..."
	java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from "-classpath \"../examples/operacoesArrayList\" IntArrayListShowCaseTest" ${PROGRAM_OPTIONS}

operacoesArray:
	@echo "Example operacoesArray..."
	java ${JAVA_OPTIONS} ${PROGRAM_CLASS} -from "-classpath \"../examples/operacoesArray\" IntArrayShowCaseTest" ${PROGRAM_OPTIONS}




