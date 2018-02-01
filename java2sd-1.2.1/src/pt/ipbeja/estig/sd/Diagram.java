
// pacote java onde esta inserida esta classe
package pt.ipbeja.estig.sd;


// importa classes do pacote java.util
import java.util.ArrayList;
import java.util.Stack;

/**
 * Class Definition which represents Sequence Diagram Structure and Information
 * 
 * @author Nuno Fortes 
 * @version 1.0
 */

public class Diagram {
	   
	private String version; // Versao do Diagrama

	private String lang; // linguagem do codigo do programa "host"

	private ArrayList<Arrow> arrows; // lista de setas

	private ArrayList<ObjectData> objects; // lista de objectos que sao unicos
  
  // uma pilha para ir guardando a posicao das CallArrow
	private Stack<Integer> callArrow; 
  
  private boolean fullClassNames = false;
  private boolean returnConstructor = false;
  private boolean returnVoid = false;

  /**
   * NOTA:
   * Para evitar a repeticao de informacoes sobre o objecto, utiliza-se
   * somente um identificador do objecto nas setas. A informacao sobre objecto
   * e referenciado na lista de objectos atraves do identificador unico.
   */

/**
 * Initialize Diagram properties
 */
	public Diagram() {
		// inicializa uma lista de setas
		this.arrows = new java.util.ArrayList<Arrow>();
		// inicializa uma lista de objectos
		this.objects = new ArrayList<ObjectData>();
    //inicializa a pilha 
    this.callArrow = new java.util.Stack<Integer>();
    
	}
  
  /**
   * Set Object Full Class Names option
   * @param fullClassNames
   *        true if Full Class Names Option is set or false otherwise
   */
  public void setFullClassNames(boolean fullClassNames)
  {
     this.fullClassNames = fullClassNames;   
  }

  /**
   * Return Full Class Names option value
   * @return boolean
   *         true if Full Class Names Option is set or false otherwise
   */
  public boolean getFullClassNames()
  {
     return this.fullClassNames;   
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
   * Get Return Constructor arrow option value
   * @return boolean
   *         true if Return Constructor option is set or false otherwise 
   */
   public boolean getReturnConstructor()
   {
     return this.returnConstructor;
   }

   /**
    * Set Return Arrow Void value option
    * @param returnVoid
    *        true if return void value option is set or false otherwise
    */
   public void setReturnVoid(boolean returnVoid)
   {
      this.returnVoid = returnVoid;   
   }
   
   /**
    * Get Return Arrow Void value option
    * @return boolean
    *         true if Return Void value option is set or false otherwise 
    */
    public boolean getReturnVoid()
    {
      return this.returnVoid;
    }
    
/**
 * Define Diagram Main Properties
 * @param version 
 *        Diagram Version
 * @param lang 
 *        Code Language
 */
	public void setSD(String version, String lang) {
	   // futuro suporte para "checking" de versao e 
	   // linguagem de programacao,
	   // quando gerar o diagrama de sequencia
	   this.version = version;
	   this.lang = lang;
	}
	
/**
 * Return Diagram Version
 * @return String
 *         Version String
 */
	public String getVersion()
	{
	  return this.version;
	}
 /**
  * Return Diagram Language
  * @return String
  *         Language String
  */
	public String getLang()
	{
	  return this.lang;
	}

  /**
   * Empty Diagram, removes all elements from it
   */
   public void clear()
   {
     this.objects.clear(); // Remove all Objects
     this.arrows.clear(); // Remove all Arrows
     this.callArrow.clear(); // Remove all stored Call Arrows
   }
   
   /**
    * Return Object ID if Object with String is found
    * @param objID
    *        String ID of object
    * @return int
    *         Object Index
    */
	public int foundObjectID(String objID) {
	  for (int i = 0; i < this.objects.size(); i++) {
		if (this.objects.get(i).getObjId().equals(objID))
			return i;
	  }
	  return -1; // nao encontrou 
	}

  /**
   * Get Object by id name
   * @param id
   *        id name of the object
   * @return ObjectData
   *         Reference of ObjectData if found; null otherwise
   */
  private ObjectData getObjectById(String id)
  {
    for (int i = 0; i < this.objects.size(); i++)
    {
      if (this.objects.get(i).getObjId().equals(id))
         return this.objects.get(i);
    }
    return null;
  }  
  
  /**
   * Return Index of Object if matched ObjectData is found
   * @param od
   *        Object of Diagram
   * @return int
   *         Object Index
   */
	private int indexOfObject(ObjectData od) {
	  for (int i = 0; i < this.objects.size(); i++) {
		if (this.objects.get(i).equals(od)) 
		return i;
	  }
	  return -1; // nao encontrou o objecto 
	}

  /**
   * Return Index of Arrow if matched Arrow is found
   * @param arrow
   *        Arrow of Diagram 
   * @return int
   *         Arrow Index
   */
  private int indexOfArrow(Arrow arrow) {
    for (int i = 0; i < this.arrows.size(); i++) {
        if (this.arrows.get(i).equals(arrow))
           return i;
    }
    return -1; // nao encontrou arrow 
  }
  
  /**
   * Add Object to Sequence Diagram
   * @param od
   *        ObjectData for the Diagram
   */
	public void addObject(ObjectData od) {
    od.setFullClassNames(this.fullClassNames);
		this.objects.add(od);
	}

  /**
   * Add Arrow to Sequence Diagram
   * @param arrow
   *        Arrow for the Diagram
   */
	public void addArrow(Arrow arrow) {
		this.arrows.add(arrow);
	}
	
  /**
   * Add Call Arrow to Sequence Diagram
   * @param arrow
   *        Call Arrow reference
   *
   */
  public void addCall(CallArrow arrow)
	{        
      // coloca a proxima posicao da lista na pilha de Call Arrows
	    this.callArrow.push( this.arrows.size() );

      // faz a validacao do objecto emissor e receptor da seta
      ObjectData sender = this.objectCheckin( arrow.getSender() );
      ObjectData receiver = this.objectCheckin( arrow.getReceiver() );
      // coloca os objectos correctos na seta
      arrow.setSender(sender);
      arrow.setReceiver(receiver);
      
      // adiciona a seta a lista de setas
      this.arrows.add( arrow );
	}

  /**
   * Add Return Arrow to Sequence Diagram
   * @param arrow
   *        Return Arrow reference
   * 
   */
  public void addReturn(ReturnArrow arrow)
	{
      // faz a validacao dos objectos emissor e receptor da seta
      ObjectData sender = this.objectCheckin( arrow.getSender() );
      ObjectData receiver = this.objectCheckin( arrow.getReceiver() );
      // coloca os objectos ja validados na seta
      arrow.setSender(sender);
      arrow.setReceiver(receiver);
      // coloca a referencia da CallArrow actual dentro da ReturnArrow
      arrow.setRefArrow( this.arrows.get(this.callArrow.lastElement()) );
      // coloca a referencia da ReturnArrow dentro da CallArrow
      this.arrows.get(this.callArrow.pop()).setRefArrow( arrow );
      //ra.setRefArrow( this.arrows.get(this.lastCallArrow) );
      // Set if we want return from constructor arrows
      arrow.setReturnConstructor(this.returnConstructor);
      // Set if we want return void arrows
      arrow.setReturnVoid(this.returnVoid);
      // adiciona a seta a lista de setas
	    this.arrows.add( arrow );
	}
	
  /**
   * Validate Object for the Diagram
   * Valida objecto
   * @param od
   *        ObjectData to check
   * @return ObjectData
   *         ObjectData ready for the Diagram
   */
	private ObjectData objectCheckin(ObjectData od)
	{
	    int found = this.indexOfObject(od); // specific objectdata search method
      if (found == -1) { 
      //if (!this.objects.contains(od)) { // may work this way too
      //if (!this.objects.indexOf(od)) { // may work this way too
        // se nao existir ainda, adiciona o objecto a lista
        //od.setId(this.objects.size()+1);
        od.incrementId(); // increment object ID Number
	      this.objects.add(od);
 	    }
	    else {
        // retorna a referencia do objecto que ja existe na lista  
	    	od = this.objects.get(found);
	    }
      od.setFullClassNames(this.fullClassNames);
	    return od;
	}
  
  /**
   * Check and correct Objects Visibility for diagram representation
   * For SDControl feature 
   *
   */
  public void checkObjectsVisibility() {
    
    for (int i = 0; i < this.arrows.size(); i++) {
        int ii = this.indexOfObject(this.arrows.get(i).getReceiver());
        if (ii > 0) {
          ObjectData obj = this.arrows.get(i).getReceiver();
          // search Call Arrows
          for (int iii = 0; iii < this.arrows.size(); iii++) {
            if (this.arrows.get(iii).isCall()) {
              CallArrow ca = (CallArrow) this.arrows.get(iii);
              // search for object creation on Call Arrow
              if ((ca.getReceiver().equals(obj)) && (ca.isCreation()) &&
                         (!ca.isVisible())) {
                // Set Constructor to Visible if is not
                this.arrows.get(iii).setVisible(this.arrows.get(i).isVisible());
                this.arrows.get(iii).getRefArrow().setVisible(
                                              this.arrows.get(i).isVisible());
                //NOTE: for better diagram understanding, 
                //      return from constructor should be set!
                break;
              }
            }
          }
          // set OBJECT visibility
          this.objects.get(ii).setVisible(this.arrows.get(i).isVisible());
        }
    }
  }
	
  /**
   * Get Arrow List of the Sequence Diagram
   * @return ArrayList<Arrow>
   *         List of Arrows
   */
	public ArrayList<Arrow> getArrows() {
		return this.arrows;
	}

  /**
   * Get Object List of the Sequence Diagram
   * @return ArrayList<ObjectData>
   *         List of ObjectData
   */
	public ArrayList<ObjectData> getObjects() {
		return this.objects;
	}

  /**
   * Get Object on Index of the Sequence Diagram
   * @param i Index of Object List
   * @return ObjectData
   *         Object of the Diagram
   */
	public ObjectData objectOfIndex(int i) {
		return this.objects.get(i);
	}

  /**
   * Get Arrow on Index of the Sequence Diagram
   * @param i Index of Arrow List
   * @return Arrow
   *         Arrow of the Diagram
   */
	public Arrow arrowOfIndex(int i) {
		return this.arrows.get(i);
	}
  
  /**
   * Get Last Arrow of the Sequence Diagram
   * @return Arrow
   *         Last Arrow of the Diagram
   */
  public Arrow lastArrow()
  {
    int size = this.arrows.size();
    return this.arrows.get(size-1);
  }
	
  /**
   * Get Object of ID Number
   * @param id ID Number of Object
   * @return ObjectData
   *         Object of the Diagram
   */
  public ObjectData objectOfID(int id) {
    for (int i = 0; i < this.objects.size(); i++) {
      if (this.objects.get(i).getId() == id)
        return this.objects.get(i);
    }
    return null; // nao encontrou
  }

  /**
   * Get Arrow of ID Number
   * @param id ID Number of Arrow List
   * @return Arrow
   *         Arrow of the Diagram
   */
  public Arrow arrowOfID(int id) {
    for (int i = 0; i < this.arrows.size(); i++) {
      if (this.objects.get(i).getId() == id)
        return this.arrows.get(i);
    }
    return null;
  }

	// metodo canonico, devolve a seta com o index i
  /**
   * Get Arrow of the Sequence Diagram
   * @param i Index of Arrow List
   * @return Arrow
   *         Arrow of the Diagram
   */
	public Arrow get(int i) {
		return this.arrows.get(i);
	}

	// metodo canonico, devolve o tamanho da lista de setas
  /**
   * Return Size of Arrow List
   * @return int
   *         Size of Arrow List
   */
	public int size() {
		return this.arrows.size();
	}
	
  /**
   * Return Size of Arrow List
   * @return int
   *         Size of Arrows on the Diagram
   */
	public int arrowsSize() {
		return this.arrows.size();
	}
	
  /**
   * Return Size of Object List
   * @return int
   *         Size of Objects on the Diagram
   */
	public int objectsSize() {
		return this.objects.size();
	}
  
  /**
   * Get a copy of the Diagram
   * return Diagram
   *        a cloned diagram
   */
  public Diagram clone() {
    Diagram dia = new Diagram();
    dia.version = this.version;
    dia.lang = this.lang;
    dia.objects = (ArrayList<ObjectData>) this.objects.clone();
    dia.arrows = (ArrayList<Arrow>) this.arrows.clone();
    dia.callArrow = (Stack<Integer>) this.callArrow.clone();
    
    dia.fullClassNames = this.fullClassNames;
    dia.returnConstructor = this.returnConstructor;
    dia.returnVoid = this.returnVoid;   
    return dia;
  }
  
  /**
   * Get a diagram starting from count class.method
   * @param count
   *        count times match until start
   * @param className
   *        class of object
   * @param methodName
   *        method name
   * @return Diagram
   *         new diagram 
   */

  public Diagram from(int count, String className, String methodName) {

    Diagram dia = this.clone();
    dia.arrows.clear();
 
    int len = methodName.length();
    int idx = 0;
    int icount = 0;
    int lastRef = this.lastArrow().getId();
    while (idx < this.arrows.size())
    {
      if (this.arrows.get(idx).getType() == Arrow.CALL_ARROW_TYPE) {
       if ((this.arrows.get(idx).getReceiver().getClassName().equals(className)) 
       && (this.arrows.get(idx).getLabel().length() > len)
       && (this.arrows.get(idx).getLabel().substring(0,len).equals(methodName))) {
          icount++;
          if (icount == count) { 
            lastRef = this.arrows.get(idx).getRefArrow().getId(); 
            break; 
          }
       }
      }
      idx++;
    }
    int ref = 0;
    int i = 0;
    while ((idx < this.arrows.size()) && 
                           (this.arrows.get(idx).getId() != lastRef)) {
        dia.arrows.add(this.arrows.get(idx));
        ref = (this.arrows.get(idx).getRefArrow().getId() * i) / idx;
        dia.arrows.get(i).setId(i);
        dia.arrows.get(i).getRefArrow().setId(ref);
        idx++;i++;
    }
    return dia;
  }  
  
  /**
   * Return Diagram from Arrows range
   * @param start
   *        Index of range start
   * @param num
   *        Number of arrows until range end
   * @return Diagram
   *         New Diagram
   */
  public Diagram range(int start, int num) {
    Diagram dia = this.clone();
    dia.arrows.clear();
    //System.out.println("size " + this.arrows.size());
    int idx = start;
    while ((idx < this.arrows.size()) && 
              (this.arrows.get(idx).getType() != Arrow.CALL_ARROW_TYPE))
      idx++;
    int end = idx+num;
    int ref = 0;
    int i = 0;
    while ((idx < this.arrows.size()) && (idx < end)) {
        dia.arrows.add(this.arrows.get(idx));
        ref = (this.arrows.get(idx).getRefArrow().getId() * i) / idx;
        dia.arrows.get(i).setId(i);
        //dia.arrows.get(i).getRefArrow().setId(ref);
        idx++;i++;
    }
    return dia;
  }
	
}
