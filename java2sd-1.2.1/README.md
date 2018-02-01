
             JAVA to SEQUENCE DIAGRAM 
             ========================
 
JAVA Program to Sequence Diagram Generator. This tool tries to help on analysing and understanding the behavior of the code when developping JAVA applications by generating the respective Sequence Diagram automatically at program execution.


Requirements: 
-------------

JDK 6 (JDK 5,may not full compatible) 
http://java.sun.com/javase/6/docs/api/index.html

pic2plot (for nice diagram visualization) 
http://www.gnu.org/software/plotutils/

sequence.pic (needed if using pic2plot) 
(UMLGraph PIC Macros file to easily design sequence diagrams) 
( is included on the package, the modified version for this tool )
!!! You should not use the UMLGraph original file. !!!
!!! The program will not recognize some modified and new pic macros !!! 

Optional:
---------

sequence tool (a simple java tool to visualize sequence diagrams)
http://www.zanthan.com/itymbi/archives/cat_sequence.html

INSTALL:
--------
Extract the compressed ZIP file, the package directory will be created.

Linux:

unzip Java2SD-x.y.z.zip 
cd Java2SD 

Windows:

install winzip
click on file and extract to a good location folder 

Usefull files: 
--------------

README               This file
CHANGES              Changes in versions
SD.ini               SequenceDiagram Configuration File 

trace.log            Trace Specific Log File
SD.log               SequenceDiagram Log File (change in SD.ini) 

sequence.pic         UMLGraph PIC Macros for Sequence Diagrams


Quick Testing:
--------------

cd src

make clean

make install

make test

make help

You can use COMPILE.sh and RUN.sh scripts too for testing purposes.

Running by hand: 
----------------

Examples

linux:
cd bin

set CLASS_PATH variable 
Add tools.jar package library 
export CLASS_PATH="/usr/lib/jvm/java-6-sun/lib/tools.jar:."
or give it on java command such as:
-classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."

- help:
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:." 
 pt/ipbeja/estig/sd/SD -help

(included test example)
- run simple test application and save diagram to XML format file :
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from pt/ipbeja/estig/test/Test -save SD.xml 

- run simple test application from "class.method" and save diagram 
  to XML format file :
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from pt/ipbeja/estig/test/Test -save SD.xml
 -start "Sub.Sub"  

- Load Trace Information from TraceFile source and save diagram to 
 XML format file :
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from program.log -save SD.xml  
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from program.log -start "1 Sub.Sub" -save SD.xml 

- Save Diagram to PIC format, then to Postscript and show it with the viewer 
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -load SD.xml -ps 

NOTE: The output images should be written to file SD.format

Running from JAR: 
-----------------

cd bin

java -jar SD.jar -help
(use included simple Test Application) 
java -jar SD.jar -from pt/ipbeja/estig/test/Test -save SD.xml 
java -jar SD.jar -start "2 Sub.Sub" -from pt/ipbeja/estig/test/Test
 -save SD.xml 
 
java -jar SD.jar -load SD.xml -view
java -jar SD.jar -load SD.xml -pic  
java -jar SD.jar -load SD.xml -ps 
 

HOW TO USE EXTERNAL JAVA PROGRAMS:
----------------------------------

inside a defined package like our Test example: 
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from "-classpath \".\" pt/ipbeja/estig/test/Test"
 -save SD.xml -ps 

codes without a package like our external examples:

#compile
#cd examples
#javac TestDispenser.java

cd ../bin

# View diagram on Postscript format without saving it 
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from "-classpath \"../examples/dispenserMachine\"
 TestDispenser" -ps 

# View diagram on Postscript format saving it first 
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from "-classpath \"../examples/ExpressionTree\"
 TreeEvaluationTest" -save SD.xml -ps 

# View diagram on Postscript format saving and loading it first 
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from "-classpath \"../examples/operacoesArray\"
 IntArrayShowCaseTest" -save SD.xml -load SD.xml -ps 

java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -from "-classpath \"../examples/operacoesArrayList\"
 IntArrayListShowCaseTest" -save SD.xml -load SD.xml -ps 

# View diagram on Postscript format loaded from file, without running the code 
java -classpath "/usr/lib/jvm/java-6-sun/lib/tools.jar:."
 pt/ipbeja/estig/sd/SD -load SD.xml -ps 

Making JAR:
-----------
cd bin
sh ../jar_build.sh



TRACE FILE FORMAT (possible instructions): 
------------------------------------------
 CALL object = new Constructor(...
 CALL object.method...
 RET value

/************************************************************************
* NOTE:
* Return Arrows from Contructors shouldn't appear on diagram
* if return_constructor is not set or set to false on configuration file
* Return Arrows with value Void shouldn't appear on diagram
* if return_void is not set or set to false on configuration file
* Return Arrows with "PROGRAM EXIT" messages should appear, 
* if program exited without passing from program start 
*************************************************************************/

Known issues:
-------------

Ubuntu linux: :  
<<
Exception in thread "main" java.lang.Error: Target VM failed to initialize: VM initialization failed for: /usr/lib/jvm/java-6-sun-1.6.0.22/jre/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=laptop:47208,suspend=y pt/ipbeja/estig/test/Test
>>
VM at init go check the machine hostname (laptop), if the address is not correct in /etc/hosts and available it simply exit with this error.


Contact: 
--------

nunofort@gmail.com

