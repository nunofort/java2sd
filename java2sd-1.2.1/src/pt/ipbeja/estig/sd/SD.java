/** 
 * Trabalho Final de Licenciatura em Engenharia Informatica
 * 
 * Escola Superior de Tecnologia e Gestao de Beja (ESTIG)
 * Instituto Politecnico de Beja (IPB)
 * http://www.estig.ipbeja.pt
 * 
 * Created by Nuno Fortes (nunofort@gmail.com) 
 * Orientation by Eng. Joao Paulo Barros
 * http://www.estig.ipbeja.pt/~jp
 * 
 */

/**
 * JAVA 6 Compatible (CODIGO BASEADO NA API J2SE JDK 1.6) 
 * JAVA API Documentation
 * http://java.sun.com/javase/6/docs/api/index.html
 * VERSAO 5 
 * http://java.sun.com/j2se/1.5.0/docs/api/overview-summary.html
 * JPDA (Java Platform Debugger Architecture)  
 * http://java.sun.com/j2se/1.5.0/docs/guide/jpda/
 * JDI (Java Debug Interface) 
 * http://java.sun.com/javase/6/docs/jdk/api/jpda/jdi/index.html
 */

/**
 * Importa pacotes JAVA
 */

// pacote java onde esta inserida esta classe
package pt.ipbeja.estig.sd;

// importa as classes do pacote de exemplo Trace
import com.sun.tools.example.trace.*;

import java.util.Date;
import java.util.Properties;
import java.util.Arrays;
import java.util.List;
//import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


/**
 * Main Class for construction of Sequence Diagram of a JAVA Application
 * (MSC - Message Sequence Chart)
 * 
 * @author Nuno Fortes (nunofort@gmail.com)
 * @version 1.2
 *   
 */
public class SD
{
  // Constantes da classe , valores das propriedades por omissao (default)
  private final static String ABOUT_PROGRAM = 
    "Java2SD 1.2           JAVA Program to Sequence Diagram Generator";
  private final static String PROGRAM_VERSION = "1.2";
  private final static String LOCATION = 
    "Management and Technology School (ESTIG)\n" + 
    "Polytechnic Institute of Beja (PORTUGAL)\n" + 
    "http://www.estig.ipbeja.pt\n"; 
  private final static String AUTHOR = "Nuno Fortes";
  private final static String ORIENTATION = "Joao Paulo Barros";
  private final static String ABOUT = "JAVA Program to Sequence Diagram Generator";
  private final static String XML_SIGN = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
  private final static String SD_ENDTAG = "</sd>\n";
  private final static String XML_FILE = "SD.xml";
  private final static String INI_FILE = "SD.ini";
  private final static String LOG_FILE = "SD.log";
  private final static String STDOUT_FILE = "stdout.txt";
  private final static String DIAGRAM_VERSION = "1.0";
  private final static String DIAGRAM_LANGUAGE = "java";
  private final static String TRACE_SOURCE = "jdi";
  //private final static String UMLGRAPH_PATH = "sequence.pic";
  private final static String SEQUENCE_CMD = "java -jar sequence.jar";
  private final static String PS_VIEWER = "gv";
  private final static String SVG_VIEWER = "firefox";
  private final static String JAVADOC_PATH = "../doc";
  private final static String OUTPUT_PREFIX = "output.";
  // UML Diagrams PIC macros path
  private final static String PIC_FILE = "sequence.pic";
  private final static String PIC_CMD = "pic2plot -T X";
  private final static String PIC_GIF = "pic2plot -T gif --font-name Courier";
  private final static String PIC_PNG = "pic2plot -T png --font-name Courier";
  private final static String PIC_PS = "pic2plot -T ps --font-name Courier";
  private final static String PIC_SVG = "pic2plot -T svg --font-name Courier";
  private final static String GIMP_CMD = "gimp";
  private final static String TRACE_FILE = "trace.log";
  private final static boolean FULL_CLASSNAMES = false;
  private final static boolean RETURN_CONSTRUCTOR = false;
  private final static boolean RETURN_VOID = false;
  private final static boolean SD_CONTROL = false;
  // dimensoes do diagrama por default
  private final static int SCREEN_WIDTH = 800;
  private final static int SCREEN_HEIGHT = 600;
  private final static String CLASS_PATH = "pt/ipbeja/estig/sd/SD";
  private final static String CLASS_PACKAGE = "pt.ipbeja.estig.sd.*";
  // exclude packages for events
  // dont need trace package, because allready on com.sun.*
  private final static String EXCLUDE_EVENTS = "pt.ipbeja.estig.sd.SD.*," +
      "pt.ipbeja.estig.sd.Arrow.*, pt.ipbeja.estig.sd.CallArrow.*," +
      "pt.ipbeja.estig.sd.ReturnArrow.*, pt.ipbeja.estig.sd.ObjectData.*," +
      "pt.ipbeja.estig.sd.Diagram.*, pt.ipbeja.estig.sd.Gui.*, " +
      "pt.ipbeja.estig.sd.Image.*, pt.ipbeja.estig.sd.SVGHandler.*, " +
      "pt.ipbeja.estig.sd.TraceSource.*, pt.ipbeja.estig.sd.TraceFile.*," +
      "pt.ipbeja.estig.sd.XMLLoader.*";
      // except pt.ipbeja.estig.sd.SDControl.*
      // except pt.ipbeja.estig.sd.Invoke.*
  
  public final static String ARG_JAVA_CALL = "-call";
  public final static String ARG_JAVA_CLASSPATH = "-cp";
  public final static String ARG_JAVA_FROM = "-from";
  public final static String ARG_JAVA_START = "-start";
  public final static String ARG_XML_SAVE = "-save";
  public final static String ARG_XML_LOAD = "-load";
  public final static String ARG_PIC_ONLY = "-pic";
  public final static String ARG_PIC_PS = "-ps";
  public final static String ARG_PIC_SVG = "-svg";
  public final static String ARG_PIC_PNG = "-png";
  public final static String ARG_PIC_GIF = "-gif";
  public final static String ARG_EPS_ONLY = "-eps";
  public final static String ARG_SEQUENCE = "-seq";
  public final static String ARG_JAVA_VIEW = "-view";
  
  public final static String ARG_HELP = "-help";
  
  public final static String MSG_NO_XML_FILE = "no xml file specified";
  public final static String MSG_NO_JAVA_FROM = "missing java source";
  public final static String MSG_NO_JAVA_START = "missing 'count class.method' "+
                                                 "to start from";
  public final static String MSG_NO_JAVA_CALL = "missing class method call";
  public final static String MSG_NO_JAVA_CLASSPATH = "missing class path";

  // program variables
  // can be overwritten by configuration file

  private String diagramVersion = SD.DIAGRAM_VERSION;
  private String diagramLanguage = SD.DIAGRAM_LANGUAGE;
  private String traceSource = SD.TRACE_SOURCE;
  //private String umlgraphPath = SD.UMLGRAPH_PATH;
  // sequence tool path
  private String sequenceCmd = SD.SEQUENCE_CMD;
  private String psViewer = SD.PS_VIEWER;
  private String svgViewer = SD.SVG_VIEWER;
  private String javadocPath = SD.JAVADOC_PATH;
  public String outputPrefix = SD.OUTPUT_PREFIX;
  private String picCmd = SD.PIC_CMD;
  private String picGIF = SD.PIC_GIF;
  private String picPNG = SD.PIC_PNG;
  private String picPS = SD.PIC_PS;
  private String picSVG = SD.PIC_SVG;
  private String gimpCmd = SD.GIMP_CMD;
  private String traceFile = SD.TRACE_FILE;
  private boolean fullClassNames = SD.FULL_CLASSNAMES;
  private boolean returnConstructor = SD.RETURN_CONSTRUCTOR;
  private boolean returnVoid = SD.RETURN_VOID;
  private boolean sdControl = SD.SD_CONTROL;
  private Diagram diagram;
  private String logFile = SD.LOG_FILE;
  public String picFile = SD.PIC_FILE;
  private String xmlFile = SD.XML_FILE;
  private String stdoutFile = SD.STDOUT_FILE;
  private int screenWidth = SD.SCREEN_WIDTH;
  private int screenHeight = SD.SCREEN_HEIGHT;
  private String excludeEvents = SD.EXCLUDE_EVENTS;
  
  private FileOutputStream logStream;
  private StringBuffer indent;
  
  private String callString = "";
  private String javaClassPath = "";
  
  /**
   * Initilize Class properties
   */
  public SD()
  {
    this.logFile = SD.LOG_FILE;
    this.xmlFile = SD.XML_FILE;
    //this.init();
  }

  /**
   * Initilize Class properties
   */  
  public SD(String logFile, String xmlFile)
  {
    this.logFile = logFile;
    this.xmlFile = xmlFile;
    //this.init();
  }

  /**
   * Initialize program properties
   */
  public void init()
  {
    this.loadConfig(SD.INI_FILE);
    this.indent = new StringBuffer("|");
    
    // Initialize Diagram properties
    this.diagram = new Diagram();
    this.diagram.setSD(this.diagramVersion, this.diagramLanguage);
    this.diagram.setFullClassNames(this.fullClassNames);
    this.diagram.setReturnConstructor(this.returnConstructor);
    this.diagram.setReturnVoid(this.returnVoid);
    
  }

  /**
   * Reads configuration file options
   * program properties values will be overide
   * 
   * @param filename
   *          SD configuration file
   */
  private void loadConfig(String filename)
  {
    // Read properties file.
    Properties properties = new Properties();
    try
    {
      try {
      // Verificar se o ficheiro de configuracao existe
      FileInputStream fis = new FileInputStream(filename);
      fis.close();
      } catch (IOException e) {
        System.err.println("File not Found : " + filename);
        System.exit(1);
      }
      properties.load(new FileInputStream(filename));
      // overwrite variables
      this.diagramVersion = properties.getProperty("version", 
                                       DIAGRAM_VERSION).trim();
      this.diagramLanguage = properties.getProperty("language", 
                                       DIAGRAM_LANGUAGE).trim();
      this.traceSource = properties.getProperty("trace_source",
                                       TRACE_SOURCE).trim();
      this.logFile = properties.getProperty("log_file", LOG_FILE).trim();
      //this.picFile = properties.getProperty("pic_file", PIC_FILE).trim();
      //System.out.println("LOG_FILE "+this.logFile);
      this.psViewer = properties.getProperty("ps_viewer", PS_VIEWER).trim();
      this.svgViewer = properties.getProperty("svg_viewer", SVG_VIEWER).trim();
      this.javadocPath = properties.getProperty("javadoc_path", 
                                                       JAVADOC_PATH).trim();
      this.outputPrefix = properties.getProperty("output_prefix", 
                                                       OUTPUT_PREFIX).trim();
      this.picFile = properties
          .getProperty("pic_file", PIC_FILE).trim();
      this.picCmd = properties.getProperty("pic_cmd", PIC_CMD).trim();
      this.picGIF = properties.getProperty("pic_gif", PIC_GIF).trim();
      this.picPNG = properties.getProperty("pic_png", PIC_PNG).trim();
      this.picPS = properties.getProperty("pic_ps", PIC_PS).trim();
      this.picSVG = properties.getProperty("pic_svg", PIC_SVG).trim();
      this.gimpCmd = properties.getProperty("gimp_cmd", GIMP_CMD).trim();
      this.sequenceCmd = properties
          .getProperty("sequence_cmd", SEQUENCE_CMD).trim();
      this.traceFile = properties.getProperty("trace_file", TRACE_FILE).trim();
      if (properties.getProperty("full_classnames",
          String.valueOf(SD.FULL_CLASSNAMES)).trim().equals("true"))
          this.fullClassNames = true;
      if (properties.getProperty("return_constructor",
          String.valueOf(SD.RETURN_CONSTRUCTOR)).trim().equals("true"))
          this.returnConstructor = true;
      if (properties.getProperty("return_void",
          String.valueOf(SD.RETURN_VOID)).trim().equals("true"))
          this.returnVoid = true;
      if (properties.getProperty("sd_control",
          String.valueOf(SD.SD_CONTROL)).trim().equals("true"))
          this.sdControl = true;
      //System.out.println("RETURN_CONSTRUCTOR "+this.returnConstructor);
      // dimensoes do diagrama
      this.screenWidth = Integer.parseInt(properties.getProperty(
          "screen_width", String.valueOf(SCREEN_WIDTH)).trim());
      this.screenHeight = Integer.parseInt(properties.getProperty(
          "screen_height", String.valueOf(SCREEN_HEIGHT)).trim());

      this.excludeEvents = properties.getProperty("exclude_events", 
                                                     EXCLUDE_EVENTS).trim();
      // System.out.println("log = "+this.logFile);
      // System.out.println("umlgraph = "+this.umlgraphPath);
      // System.out.println("sequence = "+this.sequencePath);
    }
    catch (IOException e)
    {
      System.out.println("(SD.loadConfig) ERROR: " + e.toString());
    }

  }

  /**
   * Get Trace Source Type
   * @return String
   *         Where to get Trace Information
   */ 
  public String getTraceSource()
  {
    return this.traceSource;
  }
  
  /**
   * Get Application Class Path
   * @return String
   *         Class Path of the Application
   */
  public String getClassPath()
  {
    return SD.CLASS_PATH;
  }
 
  /**
   * Get Application Exclude Events
   * @return String
   *         A list of exclude packages delimited by coma
   */
  public String getExcludeEvents()
  {
    return this.excludeEvents;
  }
  
  /**
   * Get Application Class Package
   * @return String
   *         Class Package of the Application
   */
  public String getClassPackage()
  {
    return SD.CLASS_PACKAGE;
  }
  
  /**
   * Get SDControl variable value
   * @return boolean
   *         true if sdControl variable is set, false otherwise
   */
  public boolean getSDControl() {
    return this.sdControl;
  }
  
  /**
   * Open output Stream for logging
   */
  public void logStart()
  {
    try
    {
      this.logStream = new FileOutputStream(this.logFile);
    }
    catch (IOException e)
    {
      System.out.println("(SD.logStart) ERROR: " + e.toString());
    }
  }

  /*
   * Close Output Stream for logging
   */
  public void logEnd()
  {
    try
    {
      this.logStream.close();
    }
    catch (IOException e)
    {
      System.out.println("(SD.logEnd) ERROR: " + e.toString());
    }
  }

  /**
   * Add Text to LOG file
   * 
   * @param s
   *        Text to put on LOG file
   */
  private void addToLog(String s)
  {
    try
    {
      // FileOutputStream fos = new FileOutputStream(filename,true);
      this.logStream.write(s.getBytes());
      // fos.close();
    }
    catch (IOException e)
    {
      System.out.println("(SD.addToLog) ERROR: " + e.toString());
    }
  }
  
  /**
   * Save Diagram information to XML definition file
   * 
   * @param filename
   *        XML Definition file
   */
  public void saveXML(String filename)
  {
    // constroi a xml tag de inicio para o diagrama
    long millitime = System.currentTimeMillis();
    Date sysdate = new Date(millitime);
    String sdTag = "<sd version=\"" + this.diagram.getVersion()
                   + "\" lang=\"" + this.diagram.getLang() + "\" date=\"" 
                   + sysdate.toString() + "\" creator=\"" + SD.ABOUT + "\">\n";    
    try
    {// grava o ficheiro xml e se houver erro gera uma excepcao
      BufferedWriter writer = new BufferedWriter( new FileWriter(filename) );
      //FileOutputStream fos = new FileOutputStream(filename);
      // escrevendo a assinatura do XML
      writer.write(SD.XML_SIGN);
      //writer.write(SD.SD_STARTTAG);
      writer.write(sdTag);
      // guardando os objectos no formato xml
      for (int i = 0; i < this.diagram.objectsSize(); i++)
      {
        writer.write(this.diagram.objectOfIndex(i).getXML());
      }

      // guardando as setas no formato xml
      for (int i = 0; i < this.diagram.arrowsSize(); i++)
      {
        // System.out.println(this.diagram.getArrow(i).getXML());
        writer.write(this.diagram.arrowOfIndex(i).getXML());
      }
      writer.write(SD.SD_ENDTAG);
      writer.close();
    }
    catch (IOException e)
    {
      System.out.println("(Diagram.saveXML) ERROR: " + e.toString());
    }
  }

  /**
   * Load Diagram information from XML definition file
   * 
   * @param filename
   *        XML Definition file
   */
  public void loadXML(String filename)
  {
    this.diagram.clear(); // empty diagram for loading again if any thing there
    try
    {// le o ficheiro XML e se houver erro gera uma excepcao
      XMLLoader xml = new XMLLoader();
      xml.setDiagram(this.diagram);
      xml.process(filename);
      while (xml.isLoaded() == false) {}
      this.diagram = xml.getDiagram();

    }
    catch (Exception e)
    {
      System.out.println("(SD.loadXML) ERROR: " + e.toString());
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

  /**
   * Save image to EPS format file
   * @param filename 
   *        Filename where to write to
   * @param epsCmds 
   *        Text Commands to write
   */
  public void saveEPS(String filename, String epsCmds)
  {
    long millitime = System.currentTimeMillis();
    Date sysdate = new Date(millitime);
    try
    {
      BufferedWriter writer = new BufferedWriter( new FileWriter(filename) );
      //FileOutputStream fos = new FileOutputStream(filename);
      // inicio do header do formato EPS
      writer.write("%!PS-Adobe-3.0 EPSF-3.0\n");
      writer.write("%%BoundingBox: 0 0 " + this.screenWidth + " "
                                         + this.screenHeight + "\n");
      writer.write("%%Title: Sequence Diagram\n");
      writer.write("%%Creator: " + SD.ABOUT + "\n");
      writer.write("%%CreationDate: " + sysdate.toString() + "\n");
      writer.write("%%Pages: \n");
      writer.write("%%EndComments\n");
      writer.write("%%Page: 1 1\n");
      writer.write("%%BeginDocument: Sequence Diagram\n");
      writer.write("%!PS-2.0~\n");
      // fim do header
      writer.write("/red {1 0 0 setrgbcolor} def\n");
      writer.write("/green {0 1 0 setrgbcolor} def\n");
      writer.write("/blue {0 0 1 setrgbcolor} def\n");
      writer.write("/black {0 0 0 setrgbcolor} def\n");
      writer.write(epsCmds);
      writer.write("%%EndDocument\n");
      writer.write("%%EndPage\n");
      writer.write("%%Trailer\n");
      writer.write("showpage\n");
      writer.write("%%EOF\n");
      writer.close();
    }
    catch (IOException e)
    {
      System.out.println("(Image.saveEPS) ERROR: " + e.toString());
    }
  }

  /**
   * Save image to PIC format file using UMLGraph PIC Macros
   * @param filename 
   *        Filename where to write to
   */
  public void savePIC(String filename)
  {
    
    // grava o diagrama em ficheiro no formato PIC,
    // utilizando as macros pic do programa umlgraph,
    // para criar diagramas de sequencia
    try
    {
      BufferedWriter writer = new BufferedWriter( new FileWriter(filename) );
      //FileOutputStream fos = new FileOutputStream(filename);
      writer.write(".PS\n\n");
      writer.write("# UML Sequence Diagrams Macros\n");
      writer.write("copy \"" + this.picFile + "\";\n\n");
      // guardando os objectos no formato
      writer.write("# Define the objects\n");
      writer.write("pobject(O1);\n");
      writer.write("actor(O1,\" \");\n"); // USER
      //fos.write(new String("class(O1,\"Main\");\n").getBytes());
      
      for (int i = 1; i < this.diagram.objectsSize(); i++)
      {
        //System.out.println("OBJECT: " + this.diagram.objectOfIndex(i).toString());
        //System.out.println("VISIBLE: " + this.diagram.objectOfIndex(i).isVisible());
        //if (this.diagram.getObject(i).methodStatic())
        if (this.diagram.objectOfIndex(i).isVisible()) // for SDControl feature
        writer.write(this.diagram.objectOfIndex(i).getPIC());
      }
      writer.write("\nstep();\n\n");
      // guardando as setas no formato
      writer.write("# Message sequences\n");
      //fos.write(new String("message(O1,O2,\"BEGIN\");\n").getBytes());
      //fos.write(new String("active(O1);\n").getBytes());

      for (int i = 0; i < this.diagram.arrowsSize(); i++)
      {
        //System.out.println(i + " : " + this.diagram.arrowOfIndex(i).getLabel() +
        //                   " : " + this.diagram.arrowOfIndex(i).isVisible());
        if (this.diagram.arrowOfIndex(i).isVisible()) // for SDControl feature
        writer.write(this.diagram.arrowOfIndex(i).getPIC());
      }
      /*fos.write(new String("dmessage(O"
          + String.valueOf(this.diagram.getObject(0).getId()) + ",P,\"\");\n")
          .getBytes());*/
      //fos.write(new String("inactive(O1);\n\n").getBytes());
      writer.write("\nstep();\n\n");
      // completando a sequencia dos objectos
      for (int i = this.diagram.objectsSize() - 1; i > 0; i--)
      {
        if (this.diagram.objectOfIndex(i).isVisible()) // for SDControl feature
        writer.write("complete(O"
            + String.valueOf(this.diagram.objectOfIndex(i).getId()) + ");\n");
      }
      writer.write("complete(O1);\n");
      writer.write("\n.PE\n");
      writer.close();
      // pic2plot -T X simple.pic
      // sequence.pic sao as macros do umlgraph para os diagramas
    }
    catch (IOException e)
    {
      System.out.println("(Diagram.saveUMLGraph) ERROR: " + e.toString());
    }
  }

  /**
   * Save image to Sequence Tool format file
   * @param filename 
   *        Filename where to write to
   */
  public void saveSequence(String filename)
  {
    // grava em ficheiro no formato do programa Sequence
    try
    {
      BufferedWriter writer = new BufferedWriter( new FileWriter(filename) );
      //FileOutputStream fos = new FileOutputStream(filename);
      // guardando as setas no formato
      writer.write("RUN.START {\n");
      for (int i = 0; i < this.diagram.arrowsSize(); i++)
      {
        if (this.diagram.arrowOfIndex(i).isVisible()) // for SDControl feature
        writer.write(this.diagram.arrowOfIndex(i).getSequence());
      }
      writer.write("}");
      writer.close();
    }
    catch (IOException e)
    {
      System.out.println("(Diagram.saveSequence) ERROR: " + e.toString());
    }
  }

  /**
   * Modify SVG generated PIC Image to handle links
   * @param infile
   *        Filename where to read from
   * @param outfile
   *        Filename where to write to
   */
  public void modifySVG(String infile, String outfile)
  {
    try
    {// le o ficheiro XML e se houver erro gera uma excepcao
      SVGHandler svg = new SVGHandler();
      svg.setDiagram(this.diagram,this.javadocPath,outfile);
      svg.process(infile);
    }
    catch (Exception e)
    {
      System.out.println("(SD.modifySVG) ERROR: " + e.toString());
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }

  }
  
  /**
   * View Image from format
   * @param filename 
   *        Filename where to read from
   * @param format
   *         Image Format String
   */
  public void viewImageFrom(String filename, String format)
  {
    if (format.equals("pic")) {
    // corre o comando para visualizar imagens no formato PIC
    this.runCommand(this.picCmd + " " + filename, this.stdoutFile);
    } else if (format.equals("png")) {
      // utiliza o comando para converter de PIC para o formato de imagem PNG
      this.runCommand(this.picPNG + " " + filename , filename + ".png");
      this.runCommand(this.gimpCmd+ " " + filename + ".png", this.stdoutFile);
    } else if (format.equals("gif")) {
      // utiliza o comando para converter de PIC para o formato de imagem GIF
      this.runCommand(this.picGIF + " " + filename , filename + ".gif");
      this.runCommand(this.gimpCmd + " " + filename + ".gif", this.stdoutFile);
    } else if (format.equals("ps")) {
      // utiliza o comando para converter de PIC para o formato PS
      // saves the output on the new file
      this.runCommand(this.picPS + " " + filename ,filename + ".ps");
      // pic2plot -T ps
      this.runCommand(this.psViewer + " "+filename + ".ps", this.stdoutFile);
    } else if (format.equals("svg")) {
      // utiliza o comando para converter de PIC para o formato SVG
      this.runCommand(this.picSVG + " " + filename, this.stdoutFile + ".xml");
      this.modifySVG(this.stdoutFile + ".xml", filename + ".svg");
      this.runCommand(this.svgViewer + " " + filename + ".svg", "");
    } else if (format.equals("seq")) { 
      // corre o comando do programa sequence
      this.runCommand(this.sequenceCmd + " .", this.stdoutFile);
      //this.runCommand("java -jar " + this.sequencePath + " --headless " +
      //                                               filename, this.stdoutFile);
      //this.runCommand(this.gimpCmd+" "+filename, this.stdoutFile);*/
    } else if (format.equals("eps")) {
      this.runCommand(this.psViewer + " " + filename, this.stdoutFile);
    }
  }  
  

  /**
   * Insert Call Arrow on Sequence Diagram 
   * @param arrow
   *        Call Arrow reference        
   */
  public void insertCallArrow(CallArrow arrow)
  {
    this.diagram.addCall( arrow );
    /**
     * Vamos escrever a informacao da seta num ficheiro de LOG, logo depois da
     * insercao na lista. Assim, se houver algum problema ao correr o programa
     * 'host', saberemos em que seta parou a execucao (for debugging purposes).
     */
    this.addToLog(this.indent + arrow.getLog() + "\n");
    this.indent.append("|");

  }

  /**
   * Insert Return Arrow on Sequence Diagram 
   * @param arrow
   *        Return Arrow Reference
   */
  public void insertReturnArrow(ReturnArrow arrow)
  {
    this.diagram.addReturn( arrow );

    this.indent.setLength(this.indent.length() - 1);
    this.addToLog(this.indent + arrow.getLog() + "\n");
  }

  /**
   * Return Sequence Diagram
   * @return Diagram
   *         Object with Diagram Sequence Structure
   */
  public Diagram getDiagram()
  {
    return this.diagram;
  }

  /**
   * Run Program on Operating System
   * @param cmd
   *        Text Command to Execute
   * @param stdout
   *        File Name to be used for Standard Output
   */
  private void runCommand(String cmd, String stdout)
  {
    System.out.println("> " + cmd);
    // get the current run time
    Runtime rt = Runtime.getRuntime();
    try
    {
      // Set standard output stream file, to store command output
      FileOutputStream fos = null;
      if (stdout.equals("")) stdout = this.stdoutFile;
      fos = new FileOutputStream(stdout);
      System.out.println("Send output to file " + stdout);
      // execute the command as a separate process
      Process proc = rt.exec(cmd);
      InputStream is = proc.getInputStream();

      PrintWriter pw = null;
      if (fos != null)
          pw = new PrintWriter(fos);
          
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line=null;
      while ( (line = br.readLine()) != null)
      {
          if (pw != null)
              pw.println(line);
          
      }
      if (pw != null)
          pw.flush();
   
      fos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  
  /**
   * Create Diagram from Trace Source 
   * @param source
   *        JAVA Trace Source  
   * @param from
   *        count class.method string to start from
   */
  public void createDiagram(String source, String from)
  {
    System.out.println("[+] Create Diagram from Trace...");
    if (this.traceSource.equals("jdi")) {
      // The real thing, using JDI modification Trace Example
      Trace trace = new Trace(this, source, from);
      // correct objects visibility for diagram representation
      this.diagram.checkObjectsVisibility();
      // Get this class modified reference
      //this.diagram = trace.getDiagram();
    }
    else if (this.traceSource.equals("tracefile")) {
      // Get trace information from a simple file
      // usefull to get a quick diagram without code! :)
      TraceFile trace = new TraceFile(this, source, from);
    }
  }

  /**
   * Get Diagram Arrows by range
   * @param range
   *        start and number of arrows to get
   */
  public void getRange(String range) {

    String[] token = range.split(" ");
    if (token.length == 2) {
      int start = Integer.parseInt(token[0]);
      int num = Integer.parseInt(token[1]);
      //System.out.println(start + " " + num);
      this.diagram = this.diagram.range(start,num);
    }
  }
  
  /**
   * Get Diagram Arrows from Method
   * @param from
   *        start from count class.method
   */
  public void getMethod(String from) {
 
    String[] token = from.split(" ");
    if (token.length == 2) {
      int count = Integer.parseInt(token[0]);
      String classMethod = token[1].trim();
      token = classMethod.split("\\.");
      if (token.length == 2) {
        String className = token[0].trim();
        String methodName = token[1].trim();
        System.out.println(count + " " + className + " " + methodName);
        this.diagram = this.diagram.from(count,className,methodName);
    }
    }
  }
  
  /**
   * Get Java Class Paths for the Running Java Application
   * @return String
   *         Java Class Path's
   */
  public String getJavaClassPath() {
    return this.javaClassPath;
  }

  /**
   * Get Call class method Invokation String
   * @return String
   *         Invokation class method String
   */
  public String getCallString() {
    return this.callString;
  }
  
  /**
   * Add Java Class Paths for the Running Application
   * @param paths
   *        java class paths string
   */
  public void addJavaClassPath(String paths) {
    this.javaClassPath = paths;  
  }
  
  /**
   * Set Method Invokation string
   * @param callStr
   *        Call String
   */
  public void invokeMethod(String callStr) {
    this.callString = callStr;
  }
  
  /**
   * View Sequence Diagram through JAVA
   */
  public void viewImage()
  {
    // System.out.println("Sequence Diagram");

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout( new BorderLayout() );
    // cria uma interface grafica para o utilizador
    Gui gui = new Gui();

    // le a informacao do diagrama e gera a imagem respectiva
    // cria um objecto que vai conter a imagem
    Image img = new Image(this.getDiagram(), this.screenWidth,
        this.screenHeight);
    
    // cria a interface para o utilizador e passa uma referencia da imagem
    gui.userInterface(img);
    
    Dimension scrollSize = new Dimension(this.screenWidth,this.screenHeight);
    JScrollPane scroll = new JScrollPane(img);
   
    //JScrollBar hScroll = scroll.createHorizontalScrollBar();
    //JScrollBar vScroll = scroll.createVerticalScrollBar();
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    //scroll.setAutoscrolls(true); 
    scroll.setVisible(true);
    scroll.setPreferredSize(scrollSize);
    scroll.getViewport().add( img );
    
    //JViewport viewport = scroll.getViewport();
    //viewport.setView(img);
    //scroll.setViewportView(img); 

    mainPanel.add( gui, BorderLayout.NORTH );
    mainPanel.add( scroll );        
    
    JFrame frame = new JFrame(SD.ABOUT);
    frame.setTitle(SD.ABOUT);
    frame.setPreferredSize(new Dimension(this.screenWidth, this.screenHeight + 200));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(mainPanel);
    frame.pack();
    frame.setVisible(true);
    frame.setResizable(true);
/*    
    Container content = frame.getContentPane();
    content.setBackground(Color.lightGray);
    content.setLayout(new BorderLayout());
    
    content.add(gui, BorderLayout.NORTH);
    content.add(scroll, BorderLayout.SOUTH);
*/

    

  }

  /**
   * Print message
   * @param msg
   *        message to print
   */
  public void println(String msg) {
    System.out.println(msg);
  }

  /**
   * Print message And Exit Program
   * @param msg
   *        message to print
   */
  public void msgAndExit(String msg) {
    System.out.println(msg);
    System.exit(0);
  }
  
  /**
   * About Program, some Text Screen help
   */
  public void aboutProgram()
  {
    System.out.println(SD.LOCATION);
    System.out.println(SD.ABOUT_PROGRAM);
    System.out.println("                      by " + SD.AUTHOR + " and " + 
                                                     SD.ORIENTATION);
    System.out.println();
    System.out.println("Usage: SD <option parameters>");
    System.out.println();
    System.out
          .println("  -from <source options>");
    System.out
    .println("  -cp <java class paths>         Java Class Paths for Application");
    System.out
          .println("          JDI = [Java Command Arguments]");
    System.out
          .println("          TraceFile = [trace file] ");
    System.out
    .println("(NOTE: check trace source from configuration file)");
    System.out.println(
             "  -start \"<count> <class.method>\" ");
    System.out.println(
             "  Start from number of times class.method appeared in execution");
    
    System.out.println(
    "  -call \"full.class.path(type::value,,type::value);method(type::value,,type::value);\"");
    System.out.println("  Invoke a Class Method directly");
    System.out.println(
    "   eg. -call \"pt.ipbeja.estig.test.Add(int::15,,int::20);getResult();\"");    
    
    System.out.println(
                   "  -save <file.xml>         Save Diagram to XML file");
    System.out.println(
                   "  -load <file.xml>         Load Diagram from XML file");

    System.out
          .println("  -view                    Show Diagram with JAVA Graphics");
    System.out
          .println("  -eps                     View on EPostscript Format");
    System.out
          .println("  -seq                     View on Sequence Format");
    System.out
          .println("  -pic                     View on PIC Format");
    System.out
     .println("(NOTE: need the PIC macros file sequence.pic for PIC format)");
    System.out
          .println("  -png                     View PIC from PNG Format");
    System.out
          .println("  -ps                      View PIC from PS Format");
    System.out
          .println("  -svg                     View PIC from SVG Format");
    System.out
     .println("(NOTE: The output imagem will be on file output.format)");
    System.out
          .println("  -help                    This about program text");
    System.out.println();
  }
  
  /**
   * Where All thing begins!!!
   * @param args
   *        Command Line arguments
   */
  public static void main(String args[])
  {
    // convert the strings array to a list of strings
    List<String> argsList = Arrays.asList(args);
    // sd = new SD("SD.log","SD.xml");
    SD sd = new SD();
    
    if ((argsList.size() == 0) || (argsList.get(0).equals(SD.ARG_HELP))) {
      sd.aboutProgram();
      System.exit(0);
    }
    
    // System.out.println(args.length);
    
    // Initialize Main Properties
    sd.init();
    // Nome do ficheiro XML onde esta a informacao do diagrama
    // String xml = "SD.xml";
    String from = "pt/ipbeja/estig/test/Test"; // run Test example by default
    String start = "";
    String range = "";
    String xml = "";
    
    // PARAMETER INVOKE CLASS METHOD 
    if (argsList.contains(SD.ARG_JAVA_CALL)) {
      int idx = argsList.indexOf(SD.ARG_JAVA_CALL);
      if (idx == argsList.size()-1) {
        sd.println("ERROR: " + SD.MSG_NO_JAVA_CALL);
      } else {
        sd.invokeMethod(argsList.get(idx+1));    
      }
    }
    
    // PARAMETER TO ADD JAVA CLASS PATHs
    if (argsList.contains(SD.ARG_JAVA_CLASSPATH)) {
      int idx = argsList.indexOf(SD.ARG_JAVA_CLASSPATH);
      if (idx == argsList.size()-1) {
        sd.println("ERROR: " + SD.MSG_NO_JAVA_CLASSPATH);
      } else {
        sd.addJavaClassPath(argsList.get(idx+1));    
      }
    }    
    
    // PARAMETER START FROM
    if (argsList.contains(SD.ARG_JAVA_START)) {
      int idx = argsList.indexOf(SD.ARG_JAVA_START);
      if (idx == argsList.size()-1) {
        sd.println("ERROR: " + SD.MSG_NO_JAVA_START);
      } else {
        start = argsList.get(idx+1);
      }
    }
    // PARAMETER RANGE FROM
    if (argsList.contains("-range")) {
      int idx = argsList.indexOf("-range");
      if (idx == argsList.size()-1) {
        sd.println("ERROR: no range numbers");
      } else {
        range = argsList.get(idx+1);
      }
    }
    // PARAMETER FROM SOURCE 
    if (argsList.contains(SD.ARG_JAVA_FROM)) {
       int idx = argsList.indexOf(SD.ARG_JAVA_FROM);
       if (idx == argsList.size()-1) {
         sd.println("ERROR: " + SD.MSG_NO_JAVA_FROM);
       } else {
         from = argsList.get(idx+1);

//       create diagram and saves information on construction
         sd.logStart(); // start LOG 
         System.out.println("[+] Trace from source ..." + sd.getTraceSource());
         sd.createDiagram( from, start );
         sd.logEnd(); // end LOG

       }
    }

    
    if (argsList.contains(SD.ARG_XML_SAVE)) {
      int idx = argsList.indexOf(SD.ARG_XML_SAVE);
      if (idx == argsList.size()-1) {
        sd.println("ERROR: " + SD.MSG_NO_XML_FILE);
      } else {
        xml = argsList.get(idx+1);
        if (xml.charAt(0) == '-')
          sd.println("ERROR: " + SD.MSG_NO_XML_FILE);
        else {
        // Guarda a representacao do diagrama numa definicao XML
        System.out.println("[+] Saving Diagram to XML... " + xml);

        sd.saveXML(xml); // guarda no ficheiro xml
        }
      }
    }
    
    if (argsList.contains(SD.ARG_XML_LOAD)) {
      int idx = argsList.indexOf(SD.ARG_XML_LOAD);
      if (idx == argsList.size()-1) {
        sd.println("ERROR: " + SD.MSG_NO_XML_FILE);
      } else {
        xml = argsList.get(idx+1);
        if (xml.charAt(0) == '-')
          sd.println("ERROR: " + SD.MSG_NO_XML_FILE);
        else {
        System.out.println("[+] Loading Diagram from XML... " + xml);
        sd.loadXML(xml); // Le a representacao XML
        if (!range.equals(""))
           sd.getRange(range); // get arrows on range
        if (!from.equals(""))
           sd.getMethod(from); // get arrows from "count class.method"
        }
      }
    }
    // At this point, a Sequence Diagram should exist!
    
    if (argsList.contains(SD.ARG_JAVA_VIEW)) {
      System.out.println("[+] View Diagram on JAVA Graphics...");
      sd.viewImage();
    }    
    if (argsList.contains(SD.ARG_EPS_ONLY)) {
      // Create Encapsulated Postscript Image
      Image img = new Image(sd.getDiagram(), sd.screenWidth,
          sd.screenHeight);
       // convert diagram image to Encapsulated Postscript
       String cmds = img.createEPS();
       // save EPS commands
       System.out.println("[+] Saving EPS file... " + sd.outputPrefix + "eps");
       sd.saveEPS(sd.outputPrefix + "eps", cmds);
       System.out.println("[+] View Diagram on Encapsulated Postscript Format... ");
       // View EPS
       sd.viewImageFrom(sd.outputPrefix + "eps","eps");
    }    
    if (argsList.contains(SD.ARG_PIC_ONLY)) {
      // use Pic2plot tool and UMLGraph macros
      // utiliza o ficheiro de macros da ferramenta UMLGRAPH
      System.out.println("[+] View Diagram on PIC Format...");
      sd.savePIC(sd.outputPrefix + "pic");
      sd.viewImageFrom(sd.outputPrefix + "pic","pic");   
      //sd.viewPicFrom(sd.outputPrefix + "pic","pic");
    }
    if (argsList.contains(SD.ARG_PIC_PNG)) {
      System.out.println("[+] View Diagram on PNG Format...");
      sd.savePIC(sd.outputPrefix + "pic");
      // convert PIC to PNG and view it!
      sd.viewImageFrom(sd.outputPrefix + "pic","png");
    }    
    if (argsList.contains(SD.ARG_PIC_GIF)) {
      System.out.println("[+] View Diagram on GIF Format...");
      sd.savePIC(sd.outputPrefix + "pic");
      // convert PIC to GIF and view it!
      sd.viewImageFrom(sd.outputPrefix + "pic","gif");
    }    
    if (argsList.contains(SD.ARG_PIC_PS)) {
      System.out.println("[+] View Diagram on Postscript Format...");
      sd.savePIC(sd.outputPrefix + "pic");
      // convert PIC to PS and view it!
      sd.viewImageFrom(sd.outputPrefix + "pic","ps");
    }    
    if (argsList.contains(SD.ARG_PIC_SVG)) {
      System.out.println("[+] View Diagram on SVG Format...");
      sd.savePIC(sd.outputPrefix + "pic");
      // convert PIC to SVG and view it!
      sd.viewImageFrom(sd.outputPrefix + "pic","svg");      
    }    
    if (argsList.contains(SD.ARG_SEQUENCE)) {
      // grava o diagrama em ficheiro no formato do programa Sequence
      sd.saveSequence(sd.outputPrefix + "seq");
      System.out.println("[+] View Diagram on Sequence Format...");
      // visualiza na ferramenta sequence
      sd.viewImageFrom(sd.outputPrefix + "seq","seq");
    }


    // MUST exit here without System.exit(0);
    // because gui thread may be running 
  }

}
