/*
 * @(#)Trace.java 1.5 03/12/19 Copyright 2004 Sun Microsystems, Inc. All rights
 * reserved. SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*
 * Copyright (c) 1997-2001 by Sun Microsystems, Inc. All Rights Reserved. Sun
 * grants you ("Licensee") a non-exclusive, royalty free, license to use, modify
 * and redistribute this software in source and binary code form, provided that
 * i) this copyright notice and license appear on all copies of the software;
 * and ii) Licensee does not utilize the software in a manner which is
 * disparaging to Sun. This software is provided "AS IS," without a warranty of
 * any kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL
 * SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE
 * USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES. This software is not designed or intended for
 * use in on-line control of aircraft, air traffic, aircraft navigation or
 * aircraft communications; or in the design, construction, operation or
 * maintenance of any nuclear facility. Licensee represents and warrants that it
 * will not use or redistribute the Software for such purposes.
 */
package com.sun.tools.example.trace;


// JDI classes
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.*;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

// importa classes necessarias do Sequence Diagram
import pt.ipbeja.estig.sd.SD;
//import pt.ipbeja.estig.sd.Diagram;

/**
 * This program traces the execution of another program. See "java Trace -help".
 * It is a simple example of the use of the Java Debug Interface.
 * 
 * @version
 * @(#) Trace.java 1.5 03/12/19 00:07:03
 * @author Robert Field 
 * 
 * Modified for Sequence Diagram 
 * 2007 Instituto Politecnico de Beja 
 * Escola Superior de Tecnologia e Gestao
 * by Nuno Fortes
 */
public class Trace {

    // Running remote VM
    private final VirtualMachine vm;

    // Thread transferring remote error stream to our error stream
    private Thread errThread = null;

    // Thread transferring remote output stream to our output stream
    private Thread outThread = null;

    // Mode for tracing the Trace class (default= 0 off)
    private int debugTraceMode = 0; // debug vm program execution

    // Do we want to watch assignments to fields
    private boolean watchFields = false;

    // Class patterns for which we don't want events
    private String[] excludes = {"java.*", "javax.*", "sun.*","org.*",
          // exclui as classes do pacote com.sun
				  "com.sun.*"
  
          // exclui as classes da aplicacao SD
        
          //"pt.ipbeja.estig.sd.*"
         };
    
    private String traceLog = "trace.log";

/*
 *  public static void main(String[] args) { 
 *    SD sd = new SD();
 *    Trace(sd,args); 
 *  }
 */

    /**
     * Parse the command line arguments. Launch target VM. Generate the trace.
     */
    public Trace(SD sd, String cmd, String from) {

     String[] tmp = this.excludes.clone(); // clone excludes
     // get extra exclude events and convert from string to array
     String excludeEvents = sd.getExcludeEvents();
     String[] extraExcludes = excludeEvents.split(","); 
     System.out.println("+excludes : " + excludeEvents);
     
     // create new String array with new size for the packages excludes
     this.excludes = new String[tmp.length+extraExcludes.length];
     int i;
     // re-add the original excludes
     for (i = 0; i < tmp.length; i++)
        this.excludes[i] = tmp[i];
     // add extra excludes
     for (int ii = 0; ii < extraExcludes.length; ii++,i++)
       this.excludes[i] = extraExcludes[ii].trim(); 
     
    // Inicializa o dispositivo onde se vai escrever as mensagens do trace
     PrintWriter writer = new PrintWriter(System.out);
     try {
        // change print device to write some debug messages from trace
        writer = new PrintWriter(new FileWriter( this.traceLog ));
      } catch (IOException exc) {
        System.err.println("Cannot open output log file: " +  exc);
        System.exit(1);
      }
      
      //////////////////////////////////////////////////////
      // Check for Direct Class Method Invokation
      //////////////////////////////////////////////////////
       String callString = sd.getCallString();
       if (callString != "") { // call class.method directly
          cmd = "pt/ipbeja/estig/sd/Invoke " + callString;
       }
       String cp = sd.getJavaClassPath();
       if (!cp.equals("")) cmd = "-cp \""+cp+"\" " + cmd;
       // envia o comando a executar para a maquina virtual
       System.out.println("[+] Running command on VM... ");
       System.out.println(cmd);
       this.vm = this.launchTarget( cmd );  
       // Gera a informacao para debugging (trace)
       this.generateTrace(writer,sd,from);              

    }
    

    /**
     * Generate the trace. Enable events, start thread to display events, start
     * threads to forward remote error and output streams, resume the remote VM,
     * wait for the final event, and shutdown.
     * 
     * @param writer
     *          Device to write to
     * @param sd
     *          Sequence Diagram reference
     * @param from
     *          class.function to start from, otherwise empty string
     */
    void generateTrace(PrintWriter writer, SD sd, String from) {
        this.vm.setDebugTraceMode(this.debugTraceMode);
	  // Coloca a maquina virtual que vai correr na Thread (processo)
        EventThread eventThread = new EventThread(this.vm, 
                              this.excludes, writer, sd, from); 
        eventThread.setEventRequests(this.watchFields);
        eventThread.start(); // Corre a Thread
        redirectOutput();
        this.vm.resume();

      // Shutdown begins when event thread terminates
	    try {
	    eventThread.join();
	    this.errThread.join(); // Make sure output is forwarded
	    this.outThread.join(); // before we exit
	    } catch (InterruptedException exc) {
	    // we don't interrupt
	    }
	    writer.close();
	    // recebe o diagrama construido pelo "trace" do programa
 	    sd = eventThread.getDiagram();
    }   

    /**
     * Launch target VM. Forward target's output and error.
     */
       VirtualMachine launchTarget(String mainArgs) {
	     LaunchingConnector connector = findLaunchingConnector();
	     // Mapea os argumentos com a ligacao
	     Map arguments = connectorArguments(connector, mainArgs);
       //System.out.println("MAIN ARGS: " + mainArgs);
        try {
        // executa os argumentos na ligacao
	       return connector.launch(arguments);
        } catch (IOException exc) {
            throw new Error("Unable to launch target VM: " + exc);
        } catch (IllegalConnectorArgumentsException exc) {
            throw new Error("Internal error: " + exc);
        } catch (VMStartException exc) {
            throw new Error("Target VM failed to initialize: " +
			    exc.getMessage());
        }
    }

    void redirectOutput() {
        Process process = this.vm.process();

        // Copy target's output and error to our output and error.
        this.errThread = new StreamRedirectThread("error reader",
                                             process.getErrorStream(),
                                             System.err);
        this.outThread = new StreamRedirectThread("output reader",
                                             process.getInputStream(),
                                             System.out);
        this.errThread.start();
        this.outThread.start();
    }

    /**
     * Find a com.sun.jdi.CommandLineLaunch connector
     */
    LaunchingConnector findLaunchingConnector() {
        List connectors = Bootstrap.virtualMachineManager().allConnectors();
        Iterator iter = connectors.iterator();
        while (iter.hasNext()) {
            Connector connector = (Connector)iter.next();
            if (connector.name().equals("com.sun.jdi.CommandLineLaunch")) {
                return (LaunchingConnector)connector;
            }
        }
        throw new Error("No launching connector");
    }

    /**
     * Return the launching connector's arguments.
     */
    Map connectorArguments(LaunchingConnector connector, String mainArgs) {
        Map arguments = connector.defaultArguments();
        Connector.Argument mainArg = 
	                   (Connector.Argument)arguments.get("main");
        if (mainArg == null) {
            throw new Error("Bad launching connector");
        }
        //String cmdoptions = "-classpath \"../examples\"";
        //String cmdoptions = "-classpath \"/usr/lib/jvm/java-6-sun/lib/tools.jar:../examples:.\"";
	      mainArg.setValue( mainArgs );

	    if (this.watchFields) { 
	      // We need a VM that supports watchpoints
	      Connector.Argument optionArg = 
		                               (Connector.Argument)arguments.get("options");
	      if (optionArg == null) {
		     throw new Error("Bad launching connector");
	      }
	      optionArg.setValue("-classic");
    	}
	    return arguments;
    }


}
