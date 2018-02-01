
package pt.ipbeja.estig.sd;

import pt.ipbeja.estig.sd.CallArrow;

import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * XML Handler to Parse XML document and Load the Sequence Diagram Structure.
 *
 * @author Nuno Fortes
 * @version 1.1 
 */
public class XMLLoader extends DefaultHandler {
    private Diagram diagram;
    private String currentElem = ""; 
    private String currentType;
    private int creation = 0;
    private String methodType = "";
    private boolean isVoid = false;
    private int currentID = 0;
    private int currentRef = 0;
    private ObjectData sender;
    private ObjectData receiver;
    private String label = "";
    private String extrainfo = "";
    private int textPosition = 0;
    //private CallArrow ca;

    private Boolean loaded = false;

    /**
     * Set Diagram 
     * @param dia
     *        Sequence Diagram reference
     */
    public void setDiagram(Diagram dia)
    {
    	this.diagram = dia;
    }
    
    public void startDocument() {
       //this.diagram = new Diagram();
       System.out.println("Processing XML ...");
       this.loaded = false;
    }

    public void startElement(String namespace, 
                            String localName, 
                            String qName,
                            Attributes atts) 
    {
       // processa informacao principal do diagrama
       if (localName.equals("sd"))
       {
    	  // suporte para "checking" de versao e linguagem de programacao
    	  this.diagram.setSD(atts.getValue("version"), atts.getValue("lang"));
       }
       // processa um objecto
       if (localName.equals("object"))
       {
          ObjectData od = new ObjectData();
          od.setId( Integer.parseInt( atts.getValue("id") ) ); // set ID Number
          od.setObjId(atts.getValue("name")); // set ID String
          od.setClassName(atts.getValue("class")); // Set Class Name
          //od.setMethodType( Integer.parseInt( atts.getValue("methodType") ));
          
          // adiciona objecto
          this.diagram.addObject( od );
          //System.out.println("id="+atts.getValue("id")+
          //                      "  class="+atts.getValue("class") );
       }
       // processa uma seta
       if (localName.equals("arrow")) 
       {
    	    this.label = "";
          this.extrainfo = "";
          this.currentType = atts.getValue("type");
          this.currentID = Integer.parseInt(atts.getValue("id"));
          this.currentRef = Integer.parseInt(atts.getValue("ref"));
          if (this.currentType.equals("CALL")) {
            this.creation = 0;
            if (atts.getValue("creation") != null)
            this.creation = Integer.parseInt(atts.getValue("creation"));
            this.methodType = atts.getValue("methodType");
          }
          if (this.currentType.equals("RET"))
            this.isVoid = Boolean.parseBoolean(atts.getValue("isVoid"));
          
       }
       // processa o objecto que enviou a seta
       if (localName.equals("sender"))
       {
        // get object with ID Number
    	  this.sender = this.diagram.objectOfID(
                           Integer.parseInt(atts.getValue("id")) );
       }
       // processa o objecto que recebeu a seta
       if (localName.equals("receiver"))
       {
        // get object with ID Number
     	  this.receiver = this.diagram.objectOfID(
                             Integer.parseInt(atts.getValue("id")) );
       }
       this.currentElem = localName; 
    }

    public void endElement(String namespace, 
                            String localName, 
                            String qName) 
    {
       if (localName.equals("arrow")) 
       {
	      if (this.currentType.equals("CALL")) { 
            CallArrow ca = new CallArrow(this.sender, this.receiver, 
                                  this.label.trim(), this.extrainfo.trim());
            ca.setCreation( this.creation );
            if (this.methodType.equals(CallArrow.METHOD_STATIC))
              ca.setMethodStatic(true);
            else if (this.methodType.equals(CallArrow.METHOD_NATIVE))
              ca.setMethodNative(true);
            if (ca.methodStatic()) this.receiver.setStatic(true);
		        this.diagram.addCall( ca );
	      }
	      else {// return ?
            ReturnArrow ra = new ReturnArrow ( this.sender, this.receiver, 
                this.label.trim(), this.extrainfo.trim() );
            ra.setVoid( this.isVoid );
	          this.diagram.addReturn( ra );
	      }
        // Reference Arrow is set automatically when inserted into diagram
        // so, we should not need to set it manually.  
        
	      //arro.setRefArrow(this.diagram.getArrow(this.currentRef-1));
        //arro.setId(this.currentID);
        
        //System.out.println(this.currentType+" "+this.sender.getObjId()+
        //     " "+this.receiver.getObjId()+" "+this.label);
       }
 
    }

    public void characters(char[] ch, int start, int length) 
    {
       if (this.currentElem.equals("label")) { //le o texto do elemento label
          String s = new String(ch);
          this.label += s.substring(start,start+length);
       }
       // le o texto do elemento extrainfo
       if (this.currentElem.equals("extrainfo")) {
         String s = new String(ch);
         this.extrainfo += s.substring(start,start+length); 
      }
    } 
    
    public void process(String filename) 
    {
       try {
        try {
        // Verificar ja se o ficheiro XML existe
        FileInputStream fis = new FileInputStream(filename);
        fis.close();
        } catch (IOException e) {
          System.err.println("File not Found : "+filename);
          System.exit(1);
        }
        // Preparar para o parsing do XML
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        ParserAdapter pa = new ParserAdapter(sp.getParser());
        pa.setContentHandler(this);
        pa.parse(filename);
       } catch (Exception e) {
          System.out.println("(XMLLoader.process) ERROR: " + e.toString());
       }
    }

    public void endDocument() {
       System.out.println("THE END.");
       this.loaded = true;
    }

    public Boolean isLoaded() {
       return this.loaded;
    }

    public Diagram getDiagram()
    {
       return this.diagram;
    }
    
}

