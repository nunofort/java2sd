// pacote java onde esta inserida esta classe
package pt.ipbeja.estig.sd;

import java.util.Stack;

//importa classes necessarias do Sequence Diagram  
import pt.ipbeja.estig.sd.ObjectData;
//import pt.ipbeja.estig.sd.CallArrow;
//import pt.ipbeja.estig.sd.ReturnArrow;
//import pt.ipbeja.estig.sd.Arrow;
import pt.ipbeja.estig.sd.SD;

/**
 * Abstract Class to get program trace information from different sources
 *
 * @author Nuno Fortes
 * @version 1.0
 *
 */
abstract public class TraceSource {

  public final static String TRACE_BEGIN = "__BEGIN__";
  //public final static String METHOD_NATIVE = "__native__";
  //public final static String METHOD_STATIC = "__static__";
  public final static String STATIC = "__static__"; 
  public final static String NATIVE = "__native__";
  public final static String UNKNOWN = "__unknown__";
  public final static String NO_VALUE = "NoValue";
  public final static String TYPE_VOID = "void";
  public final static String ABSTRACT_NEW = "new "; // SUPER class support
  public final static String NORMAL_NEW = "new ";
  
  // Reference of our Sequence Diagram Application
  protected SD sd;
  
  // make diagram from "class.method"
  protected String from;

  // Uma pilha para ir guardando os objectos por onde passa
  protected Stack<ObjectData> objTrace;
    
  //private long timing = 0; // how much time since 1970 UNIX Epoch ?
  protected long timeStart = 0;

  protected int traceMode = 0; // for trace operation modes
  // defaults to 0 = normal tracing
  protected boolean traceStart = false;
  protected String startClass = "";
  protected String startMethod = "main";
  // numero de sequencia em que aparece a funcao no diagrama
  protected int fromCount = 1;
  protected int methodCount = 0;    

  protected int startArrow = 0;
  
  protected boolean visibility = true;
    

  public void init(SD sd, String from) {

      // Sequence Diagram Main Class Reference
      this.sd = sd;
      // inicializa a pilha de objectos
      this.objTrace = new Stack<ObjectData>();
      // cria o primeiro objecto emissor da lista
      ObjectData od = new ObjectData(TraceSource.TRACE_BEGIN,"RUN_PROGRAM");
      // adiciona o objecto
      this.objTrace.push( od );
      
      
      this.timeStart = System.nanoTime(); // nanoseconds
      
      this.from = from;
      if (!this.from.equals("")) {
        
        // verifica se existe o numero contador da funcao
        String[] tokens = this.from.split(" ");
        if (tokens.length == 2) {
          this.fromCount = Integer.parseInt( tokens[0] );
          this.from = tokens[1];
        }
        // se existe class.method para comecar o tracing 
        tokens = this.from.split("\\.");
        if (tokens.length > 1) {
           this.startClass = tokens[0];
        /*if (this.startClass.equals(tokens[1]))
           this.startMethod = "<init>"; // object creation 
        else*/
        this.startMethod = tokens[1];
        }
        // String.trim() 
        this.traceMode = 1; // modo 1 = tracing from class.method 
        this.methodCount = 0;
        System.out.println("FROM " + this.fromCount + "#" + 
                                     this.startClass+"."+this.startMethod);
           
      }
  }
} 
