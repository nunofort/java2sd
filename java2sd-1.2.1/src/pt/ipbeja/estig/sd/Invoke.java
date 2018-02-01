
package pt.ipbeja.estig.sd;

import java.lang.reflect.*;

/**
 * Invoke a defined class method directly
 * @author Nuno Fortes (nunofort@gmail.com)
 *
 */
class Invoke {
  
  public Invoke() {
    
  }
  
  /**
   * Direct Class Method Invokation
   * @param cName
   *        Name of the Class object
   * @param cArgs
   *        Argument Array of the Class Constructor 
   *        with format "type::value" for each element
   *        types supported: string and int
   *        value: string or an integer
   * @param mName
   *        Name of the Method to invoke
   * @param mArgs
   *        Argument Array of the Class Method
   *        note: cArgs alike format
   * @return Object
   *         return object from method
   */
  public Object callMethod(String cName, String[] cArgs, String mName, String[] mArgs) {
    Object result = null;
    try {
      Class cls = null;
      try {
      cls = Class.forName(cName);
      } catch (ClassNotFoundException e) {
        System.err.println("ERROR: " + e.toString());
      }
      Class parTypes[] = new Class[cArgs.length];
      Object argList[] = new Object[cArgs.length];
      for (int i = 0; i < cArgs.length; i++) {
      //System.out.println("CARGS: " + cArgs[i]);
      String[] tokens = cArgs[i].split("::"); // \\.
      if (tokens[0].equals("string")) {
        parTypes[i] = String.class;
        argList[i] = new String(tokens[1]);
      } else if (tokens[0].equals("int")) {
        parTypes[i] = Integer.TYPE;
        argList[i] = Integer.parseInt(tokens[1]);
      } else if (tokens[0].equals("char")) {
        parTypes[i] = char.class;
        argList[i] = tokens[1].charAt(0);
      } else System.err.println("ERROR(callMethod): wrong type");
      }
      Constructor ct = null;
      try {
        ct = cls.getConstructor(parTypes);
      } catch (Exception e) {
        //e.printStackTrace();
        System.err.println("ERROR(callMethod): " + e.toString());
      }
      Object obj = null;
      try {
        obj = ct.newInstance(argList);
      } catch (InstantiationException e) {
        //e.printStackTrace();
        System.err.println("ERROR(callMethod): " + e.toString());          
      }
      Class m_parTypes[] = new Class[mArgs.length];
      Object m_argList[] = new Object[mArgs.length];
      for (int i = 0; i < mArgs.length; i++) {
      //System.out.println("MARGS: " + mArgs[i]);
      String[] tokens = mArgs[i].split("::"); // \\.
      if (tokens[0].equals("string")) {
        m_parTypes[i] = String.class;
        m_argList[i] = new String(tokens[1]);
      } else if (tokens[0].equals("int")) {
        m_parTypes[i] = Integer.TYPE;
        m_argList[i] = Integer.parseInt(tokens[1]);
      } else if (tokens[0].equals("char")) {
        m_parTypes[i] = char.class;
        m_argList[i] = tokens[1].charAt(0);
      } else System.err.println("ERROR(callMethod): wrong type");
      }        
      //ObjectReference objref
      Class aClass = obj.getClass();
      java.lang.reflect.Method m = null;
      try {
        m = (java.lang.reflect.Method) aClass.getMethod(mName, m_parTypes);
      } catch (NoSuchMethodException e) {
        //e.printStackTrace();
        System.err.println("ERROR(callMethod): " + e.toString());
      }
         
      try {
         result = m.invoke(obj, m_argList);
         
      } catch (IllegalAccessException iae) {
         iae.printStackTrace();
      } catch (InvocationTargetException ite) {
         ite.printStackTrace();
      }             
         
    } catch (Throwable e) {
       System.err.println(e);
    }
    return result;
  }  
  
  
  public static void main(String args[]) 
  {
    //full.class.path(type::value,,type::value);method(type::value,,type::value);
    if (args.length == 1) {
    String[] call = args[0].split(";");
    if (call.length >= 2) {
    int argStart = call[0].indexOf("(");
    int argEnd = call[0].indexOf(")");
    String className = call[0].substring(0,argStart).trim();
    String classArgs = call[0].substring(argStart+1,argEnd);
    String[] cParam = new String[0];
    if (!classArgs.equals("")) {
    cParam = classArgs.split(",,");
    }
    argStart = call[1].indexOf("(");
    argEnd = call[1].indexOf(")");
    String funcName = call[1].substring(0,argStart).trim();
    String funcArgs = call[1].substring(argStart+1,argEnd);
    String[] mParam = new String[0];
    if (!funcArgs.equals("")) {
    mParam = funcArgs.split(",,");
    }
    
    Invoke invoke = new Invoke();
    Object mReturn = invoke.callMethod(className,cParam,funcName,mParam);
    System.out.println(className + "(" + classArgs + ")");
    System.out.println(funcName + "(" + funcArgs + ")");
    System.out.println("[" + mReturn + "]");
    //System.exit(0);     
    }
    }
  }
  
}