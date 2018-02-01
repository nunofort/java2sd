
package pt.ipbeja.estig.sd;

/**
 * Class Definition for Call Method Arrow Information
 * 
 * @author Nuno Fortes
 * @version 1.0
 */
public class CallArrow extends Arrow 
{
public final static String METHOD_NATIVE = "__native__";
public final static String METHOD_STATIC = "__static__";
public final static String METHOD_NORMAL = "__normal__";
public final static String typeString = "CALL"; 
public final static int CALL_NEWCREATION_TYPE = 1;
public final static int CALL_SUPERCREATION_TYPE = 2;
public final static int CALL_NORMAL_TYPE = 0;

private boolean isNewCreation = false; // is object class creation
private boolean isSuperCreation = false; // is object creation from super class

private boolean methodNative = false; // is method Native
private boolean methodStatic = false; // is method Static


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
public CallArrow(ObjectData sender, ObjectData receiver, 
                 String label, String extrainfo)
{
   super(sender,receiver,label,extrainfo);
   this.type = Arrow.CALL_ARROW_TYPE;
   
}

public void setCreation(int creation)
{
  if (creation == CallArrow.CALL_NEWCREATION_TYPE)
      this.isNewCreation = true;
  else if (creation == CallArrow.CALL_SUPERCREATION_TYPE)
      this.isSuperCreation = true;
}
/**
 * set if New Class Creation
 * @param newCreation
 *        true if is New Class instance creation
 */
public void setNewCreation(boolean newCreation)
{
  this.isNewCreation = newCreation;
}

/**
 * set if Super Class Creation
 * @param superCreation
 *        true if is Super Class instance creation
 */
public void setSuperCreation(boolean superCreation)
{
  this.isSuperCreation = superCreation;
}

/**
 * set if method is native
 * @param methodNative
 *        true if method is native; false otherwise
 */
public void setMethodNative(boolean methodNative)
{
  this.methodNative = methodNative;
}

/** 
 * set if method is static
 * @param methodStatic
 *        true if method is static; false otherwise
 */
public void setMethodStatic(boolean methodStatic)
{
  this.methodStatic = methodStatic;
}

/**
 * Verify if Method is Native
 * @return boolean
 *         true if method is native; false otherwise
 */
public boolean methodNative()
{
  return this.methodNative;
}

/**
 * Verify if Method is Static
 * @return boolean
 *         true if method is static; false otherwise
 */
public boolean methodStatic()
{
  return this.methodStatic;
}

/**
 * Return true if is class Constructor
 * @return boolean
 *         true if is Class instance creation; false otherwise
 */

public boolean isCreation()
{
  return (this.isNewCreation || this.isSuperCreation);
}

/**
 * Return true if return value is void
 * @return boolean
 *         Set if Return value is Void 
 */
 public boolean getVoid()
 {
   return this.getRefArrow().getVoid();
 }  
 
/**
 * Return Arrow Type String
 * @return String 
 *         Arrow Type
 */
public String getTypeString()
{
   return CallArrow.typeString;
}

/**
 * Return Information about the operation made, for "logging" purposes
 * @return String
 *         Information about Arrow Operation
 */
public String getLog() 
{
   String info = this.getId() + ":" + CallArrow.typeString + " " +
          this.sender.getObjId() + " ("+this.sender.getClassName()+") -> " + 
          this.receiver.getObjId() + " " + this.receiver.getClassName() + "." + 
          this.label + 
           //"  " + this.getRefArrow().getId()+
           "\n" + this.extrainfo;
   // indent.append("| ");
   return info;
}


/**
 * Return the XML information about the Arrow
 * @return String
 *         Information about the Arrow on XML format
 */
public String getXML() 
{
   String creation = "";
   String methodType = "";
   if (this.isNewCreation)
      creation = "creation=\"1\"";
   else if (this.isSuperCreation)
      creation = "creation=\"2\"";
   if (this.methodNative)
     methodType = CallArrow.METHOD_NATIVE;
   else if (this.methodStatic)
     methodType = CallArrow.METHOD_STATIC;
   else
     methodType = CallArrow.METHOD_NORMAL;
   // to be sure, remove START and END TAGS from label and extrainfo here
   this.label = this.label.replace('<', ' ').replace('>', ' ');
   this.extrainfo = this.extrainfo.replace('<', ' ').replace('>', ' ');
   
   int refId = 0;
   // verificar se existe seta de retorno do metodo
   if (this.getRefArrow() != null) refId = this.getRefArrow().getId();
   
   String xml = "<arrow type=\""+CallArrow.typeString+"\" id=\"" +
   this.getId() + "\" ref=\"" + refId +
   "\" methodType=\"" + methodType + "\" " + creation + 
   ">\n  <sender id=\"" + this.sender.getId() +
   "\" />\n  <receiver id=\"" + 
   this.receiver.getId() + "\" />\n  <label> " +
   this.label + " </label>\n<extrainfo> " +
   this.extrainfo + "</extrainfo>\n</arrow>\n";
 
   return xml;
}

/**
 * Return the PIC Commands using UMLGraph Macros for Arrow Creation 
 * @return String
 *         PIC Text Commands
 */
public String getPIC()
{
  String pic = "";
  String cname = this.receiver.getClassName();
  if (this.receiver.getFullClassNames())
     cname = this.receiver.getClassPath();
  // new object creation
  if (this.isNewCreation) {
    pic = "cmessage(O" + this.sender.getId() + ",O" +
	  this.receiver.getId()+",\""+this.receiver.getObjId()+":"+
    cname+"\",\""+this.label + "\");\n";
  } else {
    pic = "message(O"+this.sender.getId()+",O"+
    this.receiver.getId()+",\"" + this.label + "\");\n";
  }
  // chamada de funcao de um outro objecto
  if (this.sender.getId() != this.receiver.getId())	
	pic += "active(O"+ this.receiver.getId()+");\n"; 
  
  return pic;
}

/**
 * Return Sequence Tool Commands for Arrow Creation
 * @return String
 *         Sequence Text 
 */
public String getSequence()
{
  // NOTA: para nao haver problema na syntax do SEQUENCE, 
  //       alguns caracteres tem que ser modificados
   String refLabel = "PROGRAM EXIT"; // no return arrow
   // verificar se existe seta de retorno do metodo
   if (this.getRefArrow() != null) 
     refLabel = this.getRefArrow().getLabel();
   refLabel = refLabel.replace(' ', '_');
   refLabel = refLabel.replace('[', '_').replace(']', '_');

   String newLabel = this.label.replace(' ','_');
   //newLabel = newLabel.replace('[', '(').replace(']', ')');
   String cname = this.receiver.getClassName();
   //if (this.receiver.getFullClassNames())
   //cname = this.receiver.getClassPath();
   String seq = "";

   if (this.methodStatic) {
      seq =  cname + "." + newLabel + " -> " + refLabel;
   } else {
      seq = this.receiver.getObjId() + "_" + cname + 
                     "." + newLabel + " -> " + refLabel;
   }
   
   // se a return arrow nao existir ou vier logo a seguir 
   if ((this.getRefArrow() != null) && 
            (this.getRefArrow().getId() == this.getId()+1))  
     seq += ";\n";
   else
	   seq += " {\n"; 
	
   return seq;
}
/*
objectOne.methodOne {
  objectTwo.methodTwo(foo, bar) -> value {
    objectThree.methodThree(value) -> anotherValue;
    objectFour.methodFour();
  }
}
*/

}



