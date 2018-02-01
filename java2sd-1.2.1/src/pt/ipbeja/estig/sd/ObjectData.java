
package pt.ipbeja.estig.sd;

/**
 * Class Definition for the Object Information.
 *
 * @author Nuno Fortes
 * @version 1.0
 */

public class ObjectData
{
   // variavel estatica cujo valor e igual em todos os ObjectData
   // incrementador
   private static int nid = 0; 
   private int id;
   private String objId;
   private String className;
   //private String objName;
   private boolean isStatic = false;
   private boolean isNative = false;
   
   private boolean fullClassNames = false;
   
   private boolean visible = true;
   
   /**
    * Class Initialization with properties by default
    */
   public ObjectData()
   {
     //this.id = ++ObjectData.nid; // void problems, dont increment here
     this.objId = "obj" + Integer.toString( this.id );
     this.className = "unknown";
     
   }
   
   /**
    * Class Initialization with properties by default
    * @param objid
    *        Object ID String
    * @param classname
    *        Class name
    */
   public ObjectData(String objid, String classname)
   {
      // incrementa e atribui o valor da variavel estatica nid
      //this.id = ++ObjectData.nid; // avoid problems, dont increment here
      this.objId = objid;
      this.className = classname;
   }
   
   /**
    * Set Full Class Names
    * @param fullClassNames
    *        true if Full Class Names option is set, false otherwise
    */
   public void setFullClassNames(boolean fullClassNames)
   {
     this.fullClassNames = fullClassNames;
   }
   
   /**
    * Return true if Full Class Names option is set
    * @return boolean
    *         true if Full Class Names option is Set, false otherwise
    */
   public boolean getFullClassNames()
   {
     return this.fullClassNames;
   }
   
   /** 
    * set if is static
    * @param isStatic
    *        true if is static; false otherwise
    */
   public void setStatic(boolean isStatic)
   {
     this.isStatic = isStatic;
   }
      
   /**
    * Verify if is Static
    * @return boolean
    *         true if is static; false otherwise
    */
   public boolean isStatic()
   {
     return this.isStatic;
   }

   /** 
    * set if is Native
    * @param isNative
    *        true if is native; false otherwise
    */
   public void setNative(boolean isNative)
   {
     this.isNative = isNative;
   }
      
   /**
    * Verify if is Native
    * @return boolean
    *         true if is native; false otherwise
    */
   public boolean isNative()
   {
     return this.isNative;
   }   
   
   /**
    * Set Visibility status
    * @param visi
    *        true if visibility is set; false otherwise      
    */
   public void setVisible(boolean visi) {
     this.visible = visi;
   }

   /**
    * Get visibility status
    * @return boolean
    *         return true if object is visible; false otherwise
    */
   public boolean isVisible() {
     return this.visible;   
   }
   
// metodo canonico, para verificar se o objecto e identico,
// para comparar facilmente em uma outra estrutura de dados,
// como por exemplo uma lista de objectos
   /**
    * Verify if Objects are equal
    * @param od
    *        Object to compare
    * @return boolean
    *         true if Objects are equal; false otherwise
    */
   public boolean equals(ObjectData od)
   {
      return (this.objId.equals(od.objId)) && 
                 (this.className.equals(od.className));

   }
   
   /**
    * Set Class Name on Object Information
    * @param name
    *        Class Name
    */
   public void setClassName(String name)
   {
     this.className = name;   
   }

   /**
    * Return Class Name of Object Information
    * @return String
    *         Class Name
    */
   public String getClassName()
   {
     String smallName = this.className;
     String[] tokens = this.className.split("\\.");
     if (tokens.length > 1)
        smallName = tokens[tokens.length-1];
     return smallName;  
   }
   
   /**
    * Return Class Path of Object Information
    * (support for optional full class name)
    * @return String
    *         Class Path
    */
   public String getClassPath()
   {
      return this.className;
   }
   
/*
   public void setObjName(String name)
   {
	  this.objName = name;
   }
   
   public String getObjName()
   {
	  return this.objName;
   }
*/
   
   /**
    * Set Object ID String
    * @param id
    *        ID String for the Object
    */
   public void setObjId(String id)
   {
     this.objId = id;

   }

   /**
    * Return Object ID String
    * @return String
    *         ID String
    */
   public String getObjId()
   {
      return this.objId;
   }
         
   /**
    * Return Object ID Number
    * @return int
    *         ID Number
    */
   public int getId()
   {
	   return this.id;
   }
   
   /**
    * Set Object ID Number
    * @param id
    *        ID Number
    */
   public void setId(int id)
   {
     this.id = id;
   }

   /**
    * Increment Object ID Number
    */
   public void incrementId()
   {
     this.id = ++ObjectData.nid;
   }
   
   // funcao canonica
   /**
    * Return the String representation of the object
    * @return String
    *         Text representing the object
    */
   public String toString()
   {
     return this.id + ":" + this.objId + ":" + this.className;
   }
   
   /**
    * Return some log information of the object
    * @return String
    *         Text log information about the object
    */
   public String getLog()
   {
     return this.toString();
   }
   
   /**
    * Return XML Object Definition
    * @return String
    *         XML Text
    */
   public String getXML()
   {
     /*int methodType = 0;
     if (this.methodStatic)
         methodType = 1;
     else if (this.methodNative)
         methodType = 2;*/
	   //String xml = "<object id=\""+this.objId+"\" methodType=\""+methodType+"\""
     String xml = "<object id=\"" + this.id + "\" name=\"" + this.objId + "\"" 
	   //name=\""+ this.objName
	   + " class=\"" + this.className + "\" />\n";
	   return xml;
   }
   
   /**
    * Return PIC Commands for Object Creation
    * @return String
    *         PIC Text Commands
    */
   public String getPIC()
   {
     String pic = "";
     String cname = this.getClassName();
     if (this.fullClassNames)
       cname = this.getClassPath();
 
     if (this.isStatic)
     pic = "class(O" + this.id + ",\"" + cname + "\");\n";   
     // static object (class)
     else
	   pic = "pobject(O" + this.id + ",\"" + this.objId + ":" + cname+"\");\n";
     //pobject() -> exists, place it!
     //object() -> create and place at start
     return pic;
   }
}

