
package pt.ipbeja.estig.sd;

/**
 * Class Definition for Return Method Arrow Information
 * 
 * @author Nuno Fortes
 * @version 1.0
 */
public class ReturnArrow extends Arrow 
{
public final static String typeString = "RET";
private boolean returnConstructor = false;
private boolean returnVoid = false;
private boolean isVoid = false;

/**
 * Class Initialization
 * @param sender 
 *        Sender Object of the Arrow
 * @param receiver 
 *        Receiver Object of the Arrow
 * @param label 
 *        Text Label for the Arrow
 * @param extrainfo
 *        Extra Information for the Arrow
 */
public ReturnArrow( ObjectData sender, ObjectData receiver, 
                    String label, String extrainfo)
{
   super(sender,receiver,label,extrainfo);
   this.type = Arrow.RETURN_ARROW_TYPE;
}

/**
 * Set Return Constructor Arrow option
 * @param returnConstructor
 *        true if Return Constructor Option is set or false otherwise
 */
public void setReturnConstructor(boolean returnConstructor)
{
   this.returnConstructor = returnConstructor;   
}

/**
 * Get Return Constructor if option is set
 * @return boolean
 *         Set if Return Constructor option is set 
 */
 public boolean getReturnConstructor()
 {
   return this.returnConstructor;
 }

 /**
  * Set Return Void Arrow option
  * @param returnVoid
  *        true if Return Void Option is set or false otherwise
  */
 public void setReturnVoid(boolean returnVoid)
 {
    this.returnVoid = returnVoid;   
 }

 /**
  * Get Return Void if option is set
  * @return boolean
  *         Set if Return Void option is set
  */
  public boolean getReturnVoid()
  {
    return this.returnVoid;
  }
 
  /**
   * Set if return Void Value
   * @param isVoid
   *        Set if Return Void Value; else otherwise
   */
   public void setVoid(boolean isVoid)
   {
     this.isVoid = isVoid;
   }  
  
  /**
   * Return true if return value is void
   * @return boolean
   *         Set if Return value is Void 
   */
   public boolean getVoid()
   {
     //return this.label.equals("return void");
     return this.isVoid;
   }  
  
/**
 * Return Arrow Type String
 * @return String 
 *         Arrow Type
 */
public String getTypeString() {
	return ReturnArrow.typeString;
}

/**
 * Return Information about the operation made, for "logging" purposes
 * @return String
 *         Information about Arrow Operation
 */
public String getLog() {
   String info = this.getId() + ":" + ReturnArrow.typeString + " " + 
   this.sender.getObjId() + " (" + this.sender.getClassName() + ") -> " + 
   this.receiver.getObjId() + " (" + this.receiver.getClassName() + ") " + 
   this.label + 
   //" " + this.getRefArrow().getId()+
   "\n" + this.extrainfo;
   return info;
}

/**
 * Return the XML information about the Arrow
 * @return String
 *         Information about the Arrow on XML format
 */
public String getXML() {
  // to be sure, remove START and END TAGS from label and extrainfo here
  this.label = this.label.replace('<', ' ').replace('>', ' ');
  this.extrainfo = this.extrainfo.replace('<', ' ').replace('>', ' ');
  
  String xml = "<arrow type=\""+ReturnArrow.typeString+"\" id=\"" + this.getId() + 
   "\" ref=\""+this.getRefArrow().getId() + "\" isVoid=\"" + this.isVoid +
   "\">\n  <sender id=\"" + this.sender.getId() + 
   "\" />\n  <receiver id=\"" + this.receiver.getId() + 
   "\" />\n  <label> return " + this.label + " </label>\n<extrainfo>" +
   this.extrainfo + "</extrainfo>\n</arrow>\n";
   return xml;
}

/**
 * Return the PIC Commands using UMLGraph Macros for Arrow Creation 
 * @return String
 *         PIC Text Commands
 */
public String getPIC() {
   String pic = "";
   CallArrow ca = (CallArrow) this.getRefArrow();
   
   // se permitir retorno de constructor
   // ou se nao for chamada de constructor
   if (((this.returnConstructor) || (!ca.isCreation())) &&  
     // e se for permitido valor void
     // ou se o valor devolvido for diferente de "void"
       ((this.returnVoid) || (!this.getVoid())))
   {
       if (this.sender.getId() == this.receiver.getId()) {
         // seta de/para o mesmo objecto
         pic = "recrmessage(O"+this.sender.getId()+",O"+
                     this.receiver.getId()+",\""+this.label+"\");\n";
       } else {
         pic = "rmessage(O"+this.sender.getId()+",O"+
                     this.receiver.getId()+",\""+this.label+"\");\n";
       }
   }
   if (this.sender.getId() != this.receiver.getId())
      pic += "inactive(O"+ this.sender.getId()+");\n";
    
   return pic;
}

/**
 * Return Sequence Tool Commands for Arrow Creation
 * @return String
 *         Sequence Text 
 */
public String getSequence() {
   String seq = "";

   // verifica se a callarrow desta arrow nao e id-1   
   // ou seja, se e um bloco de arrows 
   if (this.getRefArrow().getId() < this.getId()-1)
        seq = "}\n";
   
   return seq;
}


}

