// pacote java onde esta inserida esta classe
package pt.ipbeja.estig.sd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

//import com.sun.jdi.Location;
//import com.sun.tools.example.trace.EventThread;


import pt.ipbeja.estig.sd.SD;
import pt.ipbeja.estig.sd.ObjectData;
//import pt.ipbeja.estig.sd.Arrow;
import pt.ipbeja.estig.sd.CallArrow;
import pt.ipbeja.estig.sd.ReturnArrow;
import pt.ipbeja.estig.sd.TraceSource;

/**
 * This Class creates a Sequence Diagram from a simple Trace File 
 * for easily make a diagram without code
 * (possibility of using a simple trace file generated from any source)
 * 
 * @version 1.0
 * @author Nuno Fortes
 *
 * TRACE FILE FORMAT (possible instructions) :
 * -------------------------------------------
 * CALL object = new Constructor(...
 * CALL object.method...
 * RET value
 * 
 */
public class TraceFile extends TraceSource {

  private int lineNumber = 0;
  
  public TraceFile(SD sd, String filename, String from) {
    
    // Initialize the class properties
    this.init(sd, from);
    
    // Load trace file
    this.loadFile(filename);
  }

  /**
   * Load Trace File
   * @param filename
   *        String File Name
   */
  public void loadFile(String filename) {
    try
    {
      try {
        // Verificar logo se o ficheiro existe
        FileInputStream fis = new FileInputStream(filename);
        fis.close();
      } catch (IOException e) {
        System.err.println("File not Found : "+filename);
        System.exit(1);
      }
      BufferedReader reader = new BufferedReader( new FileReader(filename) );
      String line = "";
      String arrowType = "";
      do {
	      line = reader.readLine();	
        if ((line != null) && (line.length() > 4)) {
        if (line.substring(0,4).equals("CALL")) {
	         this.methodEntry( line.substring(5) );
           arrowType = line.substring(0,4);
        } else if (line.substring(0,3).equals("RET")) {
	         this.methodExit( line.substring(4) );
           arrowType = line.substring(0,3);
        }
        this.lineNumber++;
        }
        //System.out.println("READ "+line);
      } 
      while (((line != null) && (line.length() > 0)) && 
             ((arrowType.equals("CALL")) || (arrowType.equals("RET")))); 
      reader.close();
    }
    catch (IOException e)
    {
      System.out.println("ERROR: " + e.toString());
    }
  }

  /**
   * Check if the ObjId String is found on ObjectData Stack
   * @param objId
   *        String which represents the ID of the Object
   * @return ObjectData
   *         Reference for the Object found
   */
  private ObjectData foundObject(String objId)
  {
    for (int i = 0; i < this.objTrace.size(); i++)
    {
      if (this.objTrace.get(i).getObjId().equals(objId))
          return this.objTrace.get(i);
    }
    return null;
  }
  
  /**
   * Process Method Entry
   * @param label
   *        Arrow Text Information
   */
  private void methodEntry(String label) {
 
    //System.out.println("-> Adding CallArrow... "+label);
    String className = "";
    String methodName = "";
    String objId = "";
    
    ObjectData sender = this.objTrace.lastElement();
    
    String senderObject = sender.getObjId();
    String senderClass = sender.getClassName();
    
    //ObjectData receiver = sender;
    ObjectData receiver = new ObjectData();
    
    String[] tmp1 = label.split("\\(");
    String args = tmp1[1].trim();
    String[] tmp2 = tmp1[0].split("=");
    if (tmp2.length > 1) {
      // POSSIBLE OBJECT CREATION
      objId = tmp2[0].trim();
      label = tmp2[1].trim();
      String[] tmp3 = label.split(" ");
      
      if (tmp3[0].substring(0,3).equals("new")) {
        // NEW CLASS OBJECT CREATION
        methodName = tmp3[1].trim();
        className = methodName;
        receiver.setObjId(objId);
        receiver.setId( Integer.parseInt( objId.substring(3) ) );
        receiver.setClassName(className);
      }
    } else {
      // OBJECT.METHOD CALL
      String[] tmp3 = tmp2[0].trim().split("\\.");
      if (tmp3.length > 1) {
        objId = tmp3[0].trim();
        methodName = tmp3[1].trim();
        receiver = this.foundObject(objId);
        if (receiver == null) receiver = sender;
        className = receiver.getClassName();
        label = methodName;
      }
    }
    label += "("+args;

    String receiverObject = receiver.getObjId();
    String receiverClass = receiver.getClassName();
    
    String extrainfo = "[line "+this.lineNumber+"]";

    long millitime = System.currentTimeMillis();
    Date sysdate = new Date(millitime);
    Double timeNow = new Double((System.nanoTime()-this.timeStart) * 0.000000001);
      //coloca data e tempo despendido desde o inicio
    extrainfo += " " + sysdate.toString() + " " + 
                                       timeNow.toString().substring(0,6) + "s";
    
    CallArrow ca = new CallArrow(sender, receiver, label, extrainfo);
    // Se o metodo for o constructor da classe 
    if (label.substring(0,3).equals("new")) {
      ca.setNewCreation(true);
    } else if (label.substring(0,1).equals(label.substring(0,1).toUpperCase())){
      ca.setSuperCreation(true);
    }
    
    // adiciona o objecto a pilha de objectos usados
    this.objTrace.push( receiver );
         
    /******************************************************
     * Adiciona a seta de chamada de metodo
     ******************************************************/
    
    // ******** CHECK trace mode ************************
    if (this.traceMode == 1) {
      // CHECK if is class.function to start tracing
      //if ((!this.traceStart) && (sender.getObjId().equals(this.from))) {
      if ((!this.traceStart) && (className.equals(this.startClass)) && 
                   (methodName.equals(this.startMethod))) {
        this.methodCount++;
        if (this.methodCount == this.fromCount) {
        this.traceStart = true;
        this.startArrow = ca.getId();
        }
      }
      if (this.traceStart)
        this.sd.insertCallArrow( ca );
    }
    else this.sd.insertCallArrow( ca );

    //System.out.println("CALL " + label);
  }

  /**
   * Process Method Exit
   * @param label
   *        Arrow Text Information
   */
  private void methodExit(String label) {

    //System.out.println("-> Adding ReturnArrow..."+label);
    
    String className = "";
    String methodName = "";
    
    // tira e retorna o ultimo elemento da lista de objectos
    ObjectData sender = this.objTrace.pop();
    String senderObject = sender.getObjId();
    String senderClass = sender.getClassName();

    ObjectData receiver = this.objTrace.lastElement();
    String receiverObject = receiver.getObjId();
    String receiverClass = receiver.getClassName();
    
    String extrainfo = "[line " + this.lineNumber + "]";

    long millitime = System.currentTimeMillis();
    Date sysdate = new Date(millitime);
    Double timeNow = new Double((System.nanoTime()-this.timeStart)* 0.000000001);
    // coloca data e tempo despendido desde o inicio
    extrainfo += " " + sysdate.toString() + " " + 
                                        timeNow.toString().substring(0,6) + "s";
    
    ReturnArrow ra = new ReturnArrow(sender, receiver, label, extrainfo);
    
    /******************************************************
     * Adiciona a seta de retorno de metodo
     ******************************************************/
    
    // ******** CHECK trace mode ************************
    if (this.traceMode == 1) {
      if (this.traceStart) {    
        this.sd.insertReturnArrow( ra );
        // CHECK if is class.function to stop tracing
        if (ra.getRefArrow().getId() == this.startArrow)
          this.traceStart = false;
      }
    }
    else this.sd.insertReturnArrow( ra );
    
    //System.out.println("RET " + label);
  }
/*
  public static void main(String args[])
  {
    SD sd = new SD();
    TraceFile traceFile = new TraceFile(sd, "program.log", "");
  }
  */
}
