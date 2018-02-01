/*
 * @(#)EventThread.java	1.5 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*
 * Copyright (c) 1997-2001 by Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
 
package com.sun.tools.example.trace;

import java.io.PrintWriter;

// classes que implementam estruturas de dados uteis
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import java.util.Stack;

// JDI Classes 
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
//import com.sun.jdi.Mirror;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.ThreadDeathRequest;

// importa classes necessarias do pacote sd  
import pt.ipbeja.estig.sd.ObjectData;
import pt.ipbeja.estig.sd.CallArrow;
import pt.ipbeja.estig.sd.ReturnArrow;
//import pt.ipbeja.estig.sd.Arrow;
import pt.ipbeja.estig.sd.SD;
import pt.ipbeja.estig.sd.TraceSource;

//import java.lang.reflect.*;

/**
 * This class processes incoming JDI events and displays them
 * 
 * @version EventThread.java 1.5 03/12/19 00:07:03
 * @author Robert Field
 *
 * Modified for Sequence Diagram
 * 2007 Instituto Politecnico de Beja
 * Escola Superior de Tecnologia e Gestao
 * by Nuno Fortes
 */
public class EventThread extends Thread
{

  private final VirtualMachine vm; // Running VM
  private final String[] excludes; // Packages to exclude
  private final PrintWriter writer; // Where output goes
  static String nextBaseIndent = ""; // Starting indent for next thread
  private boolean connected = true; // Connected to VM
  private boolean vmDied = true; // VMDeath occurred
  // Maps ThreadReference to ThreadTrace instances
  private Map traceMap = new HashMap();
  
  // Reference of our Sequence Diagram Application
  private SD sd;
  
  // start from "class.method"
  private String from;
  

  /**
   * 
   * @param vm
   *        Virtual Machine
   * @param excludes
   *        Exclude Packages
   * @param writer
   *        Device to write to
   * @param sd
   *        Sequence Diagram Reference
   * @param from
   *        class.function name to start from
   */
  EventThread(VirtualMachine vm, String[] excludes,
                     PrintWriter writer, SD sd, String from)
  {
    super("event-handler");
    this.vm = vm;
    this.excludes = excludes;
    this.writer = writer;
    this.sd = sd;
    this.from = from;
  }

  /**
   * Get Sequence Diagram
   * @return SD
   *         Return Sequence Diagram Reference
   */
  SD getDiagram()
  {
    return this.sd;
  }

  /**
   * Run the event handling thread. As long as we are connected, get event sets
   * off the queue and dispatch the events within them.
   */
  public void run()
  {
    EventQueue queue = this.vm.eventQueue();
    while (this.connected)
    {
      try
      {
        EventSet eventSet = queue.remove();
        EventIterator it = eventSet.eventIterator();
        while (it.hasNext())
        {
          handleEvent(it.nextEvent());
        }
        eventSet.resume();
      }
      catch (InterruptedException exc)
      {
        // Ignore
      }
      catch (VMDisconnectedException discExc)
      {
        handleDisconnectedException();
        break;
      }
    }
  }

  /**
   * Create the desired event requests, and enable them so that we will get
   * events.
   * 
   * @param watchFields
   *          Do we want to watch assignments to fields
   */
  void setEventRequests(boolean watchFields)
  {
    EventRequestManager mgr = this.vm.eventRequestManager();

    // want all exceptions
    ExceptionRequest excReq = mgr.createExceptionRequest(null, true, true);
    // suspend so we can step
    excReq.setSuspendPolicy(EventRequest.SUSPEND_ALL);
    excReq.enable();

    // suspend Method Entry Events
    MethodEntryRequest menr = mgr.createMethodEntryRequest();
    // exclude class packages for restrict policy
    for (int i = 0; i < this.excludes.length; ++i)
    {
      menr.addClassExclusionFilter(this.excludes[i]);
    }
    /*String[] includes = {"pt.ipbeja.estig.sd.SDControl.*"};
    for (int i = 0; i < includes.length; ++i)
    {
      menr.addClassFilter(includes[i]);
    }*/
    menr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
    menr.enable();

    // suspend Method Exit Events
    MethodExitRequest mexr = mgr.createMethodExitRequest();
    // excludes class packages for restrict policy
    for (int i = 0; i < this.excludes.length; ++i)
    {
      mexr.addClassExclusionFilter(this.excludes[i]);
    }
    mexr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
    mexr.enable();

    // suspend Method Thread Death Events
    ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
    // Make sure we sync on thread death
    tdr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
    tdr.enable();
    
    watchFields = true; // we want to be true for now
    
    if (watchFields) // if watchFields is set
    {
      // suspend Class Field Events
      ClassPrepareRequest cpr = mgr.createClassPrepareRequest();
      for (int i = 0; i < this.excludes.length; ++i)
      {
        cpr.addClassExclusionFilter(this.excludes[i]);
      }
      cpr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
      cpr.enable();
    }
  }

  /**
   * This class keeps context on events in one thread. In this implementation,
   * context is the indentation prefix.
   *
   * Modified to Support Sequence Diagram
   * by Nuno Fortes
   */
  public class ThreadTrace extends TraceSource
  {
    final ThreadReference thread;

    final String baseIndent;

    static final String threadDelta = "                     ";

    StringBuffer indent;

    
    /**
     * Thread Trace Initialization
     * @param thread
     *        Thread to trace
     * @param sd
     *        Sequence Diagram Reference
     * @param from
     *        class.function name to start from
     */
    ThreadTrace(ThreadReference thread, SD sd, String from)
    {
      this.thread = thread;
      this.baseIndent = nextBaseIndent;
      this.indent = new StringBuffer(this.baseIndent);
      nextBaseIndent += threadDelta;
      println("====== " + thread.name() + " ======");

      this.init(sd,from); // use TraceSource method

    }

    private void println(String str)
    {
      writer.print(this.indent);
      writer.println(str);
    }

    void printStackFrame(StackFrame sf)
    {
      try
      {
        for (LocalVariable localvar : sf.visibleVariables())
        {
          println("Local variable: " + localvar.name());
          // println("Local variable: " + localvar.name() + "
          // "+sf.getValue(localvar));
        }
      }
      catch (AbsentInformationException e)
      {
        println("Got AbsentInformationException");
      }
      catch (Exception e)
      {
        // whatever
        System.out.println("ERROR: " + e.toString());
      }
    }
    
    /**
     * Get this Object Reference for method
     * funcao que retorna a referencia para o objecto "this"
     * @param event
     *        Method Entry Event
     * @return ObjectReference
     *         this Object Reference or null if method static or native
     */
    private ObjectReference getThisObject(MethodEntryEvent event)
    {
      ThreadReference tr = event.thread();
      ObjectReference obj = null;
      Method m = event.method();
      //ReferenceType rt = m.declaringType();
      // verifica se é um metodo nao nativo e nao estatico
      // para que a informacao do objecto esteja disponivel
      if ((!m.isNative()) && (!m.isStatic()))
      {
        try
        {
          // vai buscar o objecto 'this' referenciado pela StackFrame
          //System.out.println("Get Object Reference");
          obj = tr.frame(0).thisObject();
        }
        catch (com.sun.jdi.IncompatibleThreadStateException e)
        {
          e.printStackTrace();
        }
        catch (Exception e)
        {
          System.err.println("ERROR (objreference): " + e.toString());
        }
      }
      return obj;
    }

    /**
     * Return Value of Variable Reference on StackFrame Visible Variables
     * funcao que vai buscar o valor da variavel
     * @param sf
     *        StackFrame Reference
     * @param v
     *        LocalVariable Reference
     * @return Value
     *         Value of the LocalVariable or null if not found
     */
    private Value getVariableValue(StackFrame sf, LocalVariable v)
    {
      try {
          // interacao das variaveis visiveis na frame
          for (LocalVariable localvar : sf.visibleVariables())
          {
            // a variavel existe na frame?
            if (localvar.equals(v)) {
              //System.out.println("VAR "+v.name+ "TYPE "+ v.typeName());
              return sf.getValue(localvar);
            }
          }

      }
      catch (Exception e)
      {
        // whatever
        //System.err.println("ERROR(getVariableValue): " + e.toString());
      }
      return null;
    }
    
    /**
     * Get Method argument values
     * funcao que nos devolve os valores dos argumentos passados para o metodo
     * @param m
     *        Method Reference
     * @return String
     *         argument values string
     */
    private String getMethodArgumentValues(Method m)
    {
      List<LocalVariable> args = new ArrayList<LocalVariable>();

      // verifica se é um metodo nao nativo e nao abstracto
      // para que as informacoes dos argumentos estejam disponiveis
      if ((!m.isNative()) && (!m.isAbstract()))
      {
        try
        {
          // A preparar para ver as variaveis dos argumentos da funcao
          // que existem disponiveis na StackFrame
          //System.out.println("Get Function Arguments");
          args = m.arguments();
          // se nao existir argumentos no metodo sai imediatamente
          if ((args == null) || (args.size() == 0)) return "";
        } catch (com.sun.jdi.AbsentInformationException e) {
          //System.out.println("no arguments information");
        }
      }
      //System.out.printf("arguments=%d: Get Argument Values\n", args.size());
      String argsString = "";
      try {
        // Get Thread Frames
        List<StackFrame> frames = this.thread.frames();
        for (int i = 0; i < args.size(); i++)
        {
          //Get variable from arguments
          LocalVariable v = args.get(i);
          //System.out.println("Get Variable " + v.name() + " Value");
       
          for (int ii = 0; ii < frames.size(); ii++)
          {
            // Get Value of variable from Stackframe
            Value val = this.getVariableValue(frames.get(ii), v);
            if (val != null) {
              String value = val.toString();
              if ((value.length() > 8) && 
                  (value.substring(0,8).equals("instance"))) {
                   // known objects
                   int start = value.indexOf("(id=");
                   int end = value.indexOf(')');
                   String objId = value.substring(start+4,end);
                   // search for object on diagram
                   int oid = this.sd.getDiagram().foundObjectID("obj" + objId);
                   if (oid != -1) { // objid found in memory
                     //System.out.println("VAL " + value + " : obj" + objId);
                     argsString += " obj" + objId + ":" + 
                     this.sd.getDiagram().objectOfIndex(oid).getClassName() + " ,";
                   } else { // unknown objects
                     String name = val.type().name();
                     String[] tokens = name.split("\\.");
                     if (tokens.length > 1)
                       name = tokens[tokens.length-1];
                     argsString += "instance_of_" + name + ",";
                   }
              } else
                 argsString += val.toString() + ",";
            } //else argsString += "unknown,";
          }
        }
        if (argsString.length() > 2)
          // remove last char
          argsString = argsString.substring(0, argsString.length() - 1);
      }
      catch (Exception e)
      {
          // whatever
          System.err.println("ERROR(valString): " + e.toString());
      }
      return argsString;
    }
    
    
    /**
     * Method Entry Event handler
     * evento de entrada num metodo
     * @param event
     *        Event Reference of Method Entry
     */
    void methodEntryEvent(MethodEntryEvent event)
    {
      ThreadReference tr = event.thread();
      Method m = event.method(); // method reference
      String className = m.declaringType().name(); // class of the method
      String methodName = m.name(); // method name
        
      int nArgs = 0;
      try {
        nArgs = m.arguments().size();
      } catch (Exception e) {
        //System.err.println("ERROR(arguments): "+e.toString());
      }
      ReferenceType rt = m.declaringType();
      String rt_small = rt.name();
      String[] tokens = rt_small.split("\\.");
      if (tokens.length > 1)
         rt_small = tokens[tokens.length-1];
      
      /**********************************************************
       * Get Method Object Reference 
       **********************************************************/
      //System.out.printlnGet Object Reference : "+rt.name() );
      ObjectReference obj = this.getThisObject(event); 
      
      /**********************************************************
       * Get Method Arguments
       **********************************************************/
      String argsString = this.getMethodArgumentValues(m);

      /********************************************************** 
       * Get All Arrow Information 
       **********************************************************/
      
      // cria objecto emissor
      ObjectData sender = this.objTrace.lastElement();
    
      //String senderObject = sender.getObjId();
      //String senderClass = sender.getClassName();

      ObjectData receiver = new ObjectData( TraceSource.UNKNOWN, rt.name() );

      // verifica se o objecto e nativo ou estatico
      if (obj == null) {
          // Static
          if (m.isStatic()) {
            receiver.setObjId(TraceSource.STATIC + rt_small);
            receiver.setClassName(rt.name());
            receiver.setStatic(true);
          } else if (m.isNative()) {
            // Native
            receiver.setObjId(TraceSource.NATIVE + rt_small);
            receiver.setClassName(rt.name());
            receiver.setNative(true);
          }
      }
      else {
        // se a referencia do objecto e diferente de null
        // coloca a informacao sobre o objecto
        receiver.setObjId( "obj" + String.valueOf(obj.uniqueID()) );
        receiver.setClassName( obj.type().name() );
      }

      //String receiverObject = receiver.getObjId();
      //String receiverClass = receiver.getClassName(); 

      // se o valor comecar por < supoe-se que sera "<init>"
      // ou seja, o constructor da Class entao coloca-se o nome da classe
      // The name of the method is modified to avoid conflict with xml
      if  (methodName.charAt(0) == '<') methodName = receiver.getClassName();
      // replace double quotes on arguments, to avoid problems on label string
      argsString = argsString.replace('"', '\''); 
      
      Location location = event.location();
      String label = methodName + "(" + argsString + ")";
      String extrainfo = "[line " + location.lineNumber() + "]";
      
      
      /*String textInfo = sender.getObjId() + ":" + sender.getClassName() + " -> "
          + receiver.getObjId() + ":" + receiver.getClassName() + "." 
          + methodName + "(" + argsString + ")";
      println("CallArrow " + textInfo + " Line " + location.lineNumber());
      */
      long millitime = System.currentTimeMillis();
      Date sysdate = new Date(millitime);
      Double timeNow = new Double((System.nanoTime()-this.timeStart) * 0.000000001);
        //coloca data e tempo despendido desde o inicio
      extrainfo += " " + sysdate.toString() + " " + 
                                    timeNow.toString().substring(0,6) + "s";
      
      // create Call Arrow
      CallArrow ca = new CallArrow(sender, receiver, label, extrainfo);
      // set the actual visibility status
      ca.setVisible(this.visibility); 
      
      /************************************************************
       * check for SD controller class
       ************************************************************/
      if (rt_small.equals("SDControl")) {
        //System.out.println("className : " + rt_small);
        if (this.sd.getSDControl()) { // continue only, if sd_control is set
        //System.out.println("methodName : " + methodName);
        if ((methodName.equals("on")) || (methodName.equals("show")))
          this.visibility = true; // set ON visibility
        else if ((methodName.equals("off")) || (methodName.equals("hide"))) 
          this.visibility = false; // set OFF visibility
        }
        return;
      }
      
      // CHECK CLASS and METHOD
      if (m.isConstructor()) {
      //if (label.substring(0,6).equals("<init>")) {
      // Se o metodo for o constructor da classe 
        if (rt.isAbstract()) {
        // New Object Creation of the Abstract Class
        ca.setLabel(TraceSource.ABSTRACT_NEW + label);
        ca.setSuperCreation(true);
        } else {
        // new Object Creation of the Class
        ca.setLabel(TraceSource.NORMAL_NEW + label);
        ca.setNewCreation(true);
        }
      }
      if (m.isNative()) {
        // method is native
        ca.setLabel(CallArrow.METHOD_NATIVE + label);
        ca.setMethodNative(true);
      } else if (m.isStatic()) {
        // method is static
        ca.setLabel(CallArrow.METHOD_STATIC + label);
        ca.setMethodStatic(true);
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
        //System.out.println(rt_small+"."+methodName+" -> "+this.startClass+"."+this.startMethod);
        if ((!this.traceStart) && (rt_small.equals(this.startClass)) && 
                     (methodName.equals(this.startMethod))) {
          this.methodCount++;
          // verifica se a funcao apareceu na N vez
          if (this.methodCount == this.fromCount) {
          this.traceStart = true;
          this.startArrow = ca.getId();
          }
        }
        if (this.traceStart)
          this.sd.insertCallArrow( ca );
      }
      else this.sd.insertCallArrow( ca );
      
      // print some log information
      println(ca.getLog());
      
      this.indent.append("| ");


    }
    
    /**
     * Get Return Value at Method Exit Event
     * @param event
     *        Method Exit Event Reference
     * @return Value
     *         Method Return Value or null on void
     */
    private Value getReturnValue(MethodExitEvent event)
    {
      ThreadReference tr = event.thread();
      Value val = null;
      try {
        StackFrame sf = tr.frame(0);

        if (sf.visibleVariables().size() > 0) {
          // retira o valor que esta na ultima posicao da Stackframe
          int lastElement = sf.visibleVariables().size()-1;
          LocalVariable localvar = sf.visibleVariables().get(lastElement);
          //LocalVariable localvar = sf.visibleVariables().get(0);

          //System.out.println("metodo "+event.method().name());
          val = sf.getValue(localvar);
          /*for(int i = 0; i < sf.visibleVariables().size(); i++)
          {
            System.out.println("["+ i + "] = " + 
                               sf.getValue(sf.visibleVariables().get(i)));
          }*/
        
        }
      }
      catch (AbsentInformationException e)
      {
          // whatever
          System.err.println("ERROR(retval): " + e.toString());
      }
      catch (IncompatibleThreadStateException e)
      {
          // whatever
          System.err.println("ERROR(retval): " + e.toString());
      }
      return val;
    }

    /**
     * Exit Method Event handler
     * evento de saida de um metodo
     * @param event
     *        Event Reference of Exit Method
     */
    void methodExitEvent(MethodExitEvent event)
    {
      ThreadReference tr = event.thread();
      Method m = event.method();
      String className = m.declaringType().name();
      
      String methodName = m.name();
      Location location = event.location();
      String val = TraceSource.NO_VALUE;
      String valType = TraceSource.TYPE_VOID;
     
      /************************************************
       * Get Small Class Name
       ************************************************/
      ReferenceType rt = m.declaringType();
      String rt_small = rt.name();
      String[] tokens = rt_small.split("\\.");
      if (tokens.length > 1)
         rt_small = tokens[tokens.length-1];

      /****************************************************
       * check if is our controller class
       ****************************************************/
       if (rt_small.equals("SDControl")) return;
      
      /************************************************
       * Get Return Value
       ************************************************/
      try {
        Value va = null;
        if (m.virtualMachine().canGetMethodReturnValues())
          va = event.returnValue(); // supported since java 6.0
        else // otherwise go check the value in stackframe
          va = this.getReturnValue(event);
        if (va != null) {
        String value = va.toString();
        String name = va.type().name();
        tokens = name.split("\\.");
        if (tokens.length > 1)
           name = tokens[tokens.length-1]; // retira o ultimo nome da classe 
        // se for uma instancia de uma classe
        if ((value.length() > 8) && 
            (value.substring(0,8).equals("instance"))) {
          // known objects
          int start = value.indexOf("(id=");
          int end = value.indexOf(')');
          String objId = value.substring(start+4,end);
          // search for object on diagram
          if (this.sd.getDiagram().foundObjectID("obj" + objId) != -1){
            //System.out.println("VAL " + value + " : obj" + objId);
            val = "obj" + objId;
          } else { // unknown objects
           val = "instance_of_" + name;
          }
        }
        else {
           val = va.toString();
        }
        valType = name;
        
        //System.out.println("EXIT VALUE "+valType+" = "+val);
      }
      } catch (Exception e) {
        System.err.println("ERROR(retvalue): "+e.toString());
      }
      
      /*********************************************
       * Get All Arrow Information
       *********************************************/
      // retira e remove o ultimo elemento da lista de objectos
      ObjectData sender = this.objTrace.pop();
      //String senderObject = sender.getObjId();
      //String senderClass = sender.getClassName();

      ObjectData receiver = this.objTrace.lastElement();
      //String receiverObject = receiver.getObjId();
      //String receiverClass = receiver.getClassName();
      
      // simplifies when is void value
      if (val.equals("<void value>")) val = "void";
      //val = val.replace('"', '');
      // Se for retorno de constructor
      String label = methodName.charAt(0) == '<' ? 
                     // devolve o nome do objecto
                     sender.getObjId() :
                     // se o valor comecar por < supoe-se que sera "<value>"
                     // entao retira os sinais para nao haver problemas no XML
                     (val.charAt(0) == '<') ?
                     val.substring(1, val.length()-1):
                     (val.charAt(0) == '"') ?
                     val.substring(1,val.length()-1):
                     // se nao, retorna o valor
                     val;
      
      String extrainfo = "[line " + location.lineNumber() + "]";
      
      /*String textInfo = sender.getObjId() + ":" + sender.getClassName() + " -> "
          + receiver.getObjId() + ":" + receiver.getClassName() + ": return "
          + valType + " = " + val;
      println("ReturnArrow " + textInfo + " Line " + location.lineNumber());
      */
      long millitime = System.currentTimeMillis();
      Date sysdate = new Date(millitime);
      Double timeNow = new Double((System.nanoTime()-this.timeStart)* 0.000000001);
      // coloca data e tempo despendido desde o inicio
      extrainfo += " " + sysdate.toString() + " " + 
                                     timeNow.toString().substring(0,6) + "s";
      
      ReturnArrow ra = new ReturnArrow(sender, receiver, label, extrainfo);
      ra.setVisible(this.visibility);
      
      if (label.equals("void")) ra.setVoid(true);
      
      //System.out.println("[" + label + "]" + ra.getVoid());
      /******************************************************
       * Adiciona a seta de retorno de metodo
       ******************************************************/
      
      // ******** CHECK trace mode ************************
      if (this.traceMode == 1) {
        if (this.traceStart) {
          // check reference arrow ID    
          this.sd.insertReturnArrow( ra );
          // At this moment the ReturnArrow should 
          // have allready the call reference from the diagram
          // CHECK if is return from class.function to stop tracing
          if (ra.getRefArrow().getId() == this.startArrow)
            this.traceStart = false;
        }        

      }
      else this.sd.insertReturnArrow( ra );
      
      // print some log information
      println(ra.getLog());
      
      this.indent.setLength(this.indent.length() - 2);
      
    }

    /**
     * fields of the class
     */ 
    void fieldWatchEvent(ModificationWatchpointEvent event)
    {
      Field field = event.field();
      Value value = event.valueToBe();
      Value current = event.valueCurrent();
      Location location = event.location();

    }

    void exceptionEvent(ExceptionEvent event)
    {
      println("Exception: " + event.exception() + " catch: "
          + event.catchLocation());

      // Step to the catch
      EventRequestManager mgr = vm.eventRequestManager();
      StepRequest req = mgr.createStepRequest(this.thread, StepRequest.STEP_MIN,
          StepRequest.STEP_INTO);
      req.addCountFilter(1); // next step only
      req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
      req.enable();

    }

    // Step to exception catch
    void stepEvent(StepEvent event)
    {
      // Adjust call depth
      int cnt = 0;
      this.indent = new StringBuffer(this.baseIndent);
      try
      {
        cnt = this.thread.frameCount();
      }
      catch (IncompatibleThreadStateException exc)
      {
      }
      while (cnt-- > 0)
      {
        this.indent.append("| ");
      }

      EventRequestManager mgr = vm.eventRequestManager();
      mgr.deleteEventRequest(event.request());
    }

    void threadDeathEvent(ThreadDeathEvent event)
    {
      this.indent = new StringBuffer(this.baseIndent);
      println("====== " + this.thread.name() + " end ======");
    }
  }

  /**
   * Returns the ThreadTrace instance for the specified thread, creating one if
   * needed.
   */
  ThreadTrace threadTrace(ThreadReference thread)
  {
    ThreadTrace trace = (ThreadTrace) this.traceMap.get(thread);
    if (trace == null)
    {
      // cria um novo thread passando as referencias necessarias do sd e from
      trace = new ThreadTrace(thread, this.sd, this.from);
      traceMap.put(thread, trace);
    }
    return trace;
  }

  /**
   * Dispatch incoming events
   */
  private void handleEvent(Event event)
  {
    if (event instanceof ExceptionEvent)
    {
      exceptionEvent((ExceptionEvent) event);
    }
    else if (event instanceof ModificationWatchpointEvent)
    {
      fieldWatchEvent((ModificationWatchpointEvent) event);
    }
    else if (event instanceof MethodEntryEvent)
    {
      methodEntryEvent((MethodEntryEvent) event);
    }
    else if (event instanceof MethodExitEvent)
    {
      methodExitEvent((MethodExitEvent) event);
    }
    else if (event instanceof StepEvent)
    {
      stepEvent((StepEvent) event);
    }
    else if (event instanceof ThreadDeathEvent)
    {
      threadDeathEvent((ThreadDeathEvent) event);
    }
    else if (event instanceof ClassPrepareEvent)
    {
      classPrepareEvent((ClassPrepareEvent) event);
    }
    else if (event instanceof VMStartEvent)
    {
      vmStartEvent((VMStartEvent) event);
    }
    else if (event instanceof VMDeathEvent)
    {
      vmDeathEvent((VMDeathEvent) event);
    }
    else if (event instanceof VMDisconnectEvent)
    {
      vmDisconnectEvent((VMDisconnectEvent) event);
    }
    else
    {
      throw new Error("Unexpected event type");
    }
  }

  /*****************************************************************************
   * A VMDisconnectedException has happened while dealing with another event. We
   * need to flush the event queue, dealing only with exit events (VMDeath,
   * VMDisconnect) so that we terminate correctly.
   */
  synchronized void handleDisconnectedException()
  {
    EventQueue queue = this.vm.eventQueue();
    while (this.connected)
    {
      try
      {
        EventSet eventSet = queue.remove();
        EventIterator iter = eventSet.eventIterator();
        while (iter.hasNext())
        {
          Event event = iter.nextEvent();
          if (event instanceof VMDeathEvent)
          {
            vmDeathEvent((VMDeathEvent) event);
          }
          else if (event instanceof VMDisconnectEvent)
          {
            vmDisconnectEvent((VMDisconnectEvent) event);
          }
        }
        eventSet.resume(); // Resume the VM
      }
      catch (InterruptedException exc)
      {
        // ignore
      }
    }
  }

  private void vmStartEvent(VMStartEvent event)
  {
    this.writer.println("-- VM Started --");
  }

  // Forward event for thread specific processing
  private void methodEntryEvent(MethodEntryEvent event)
  {
    threadTrace(event.thread()).methodEntryEvent(event);
  }

  // Forward event for thread specific processing
  private void methodExitEvent(MethodExitEvent event)
  {
    threadTrace(event.thread()).methodExitEvent(event);
  }

  // Forward event for thread specific processing
  private void stepEvent(StepEvent event)
  {
    threadTrace(event.thread()).stepEvent(event);
  }

  // Forward event for thread specific processing
  private void fieldWatchEvent(ModificationWatchpointEvent event)
  {
    threadTrace(event.thread()).fieldWatchEvent(event);
  }

  void threadDeathEvent(ThreadDeathEvent event)
  {
    ThreadTrace trace = (ThreadTrace) this.traceMap.get(event.thread());
    if (trace != null)
    { // only want threads we care about
      trace.threadDeathEvent(event); // Forward event
    }
  }

  /**
   * A new class has been loaded. Set watchpoints on each of its fields
   */
  private void classPrepareEvent(ClassPrepareEvent event)
  {
    EventRequestManager mgr = this.vm.eventRequestManager();
    List fields = event.referenceType().visibleFields();
    for (Iterator it = fields.iterator(); it.hasNext();)
    {
      Field field = (Field) it.next();
      ModificationWatchpointRequest req = mgr
          .createModificationWatchpointRequest(field);
      for (int i = 0; i < this.excludes.length; ++i)
      {
        req.addClassExclusionFilter(this.excludes[i]);
      }
      req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
      req.enable();
    }
  }

  private void exceptionEvent(ExceptionEvent event)
  {
    ThreadTrace trace = (ThreadTrace) this.traceMap.get(event.thread());
    if (trace != null)
    { // only want threads we care about
      trace.exceptionEvent(event); // Forward event
    }
  }

  public void vmDeathEvent(VMDeathEvent event)
  {
    this.vmDied = true;
    this.writer.println("-- The application exited --");

  }

  public void vmDisconnectEvent(VMDisconnectEvent event)
  {
    this.connected = false;
    if (!this.vmDied)
    {
      this.writer.println("-- The application has been disconnected --");
    }
  }
}
