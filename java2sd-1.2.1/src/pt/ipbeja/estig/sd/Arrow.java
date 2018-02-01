
package pt.ipbeja.estig.sd;


/**
 * Abstract Class Definition for different types of Arrow Information
 * 
 * @author Nuno Fortes
 * @version 1.1
 */
public abstract class Arrow 
{
public final static int CALL_ARROW_TYPE = 1;
public final static int RETURN_ARROW_TYPE = 2;
//variavel estatica cujo valor e igual em todos os ObjectData
// incrementador
private static int nid = 0; 
private int id;
//atributos visiveis nas classes derivadas desta
protected ObjectData sender; // o objecto de emissao
protected ObjectData receiver; // o objecto de recepcao
protected String label; // informacao no meio da seta, mensagem
protected String extrainfo;
//protected String senderId;
//protected String receiverId;
private Arrow refArrow; // seta de referencia

protected int type = 0; 
protected boolean visible = true;


public Arrow(ObjectData sender, ObjectData receiver, 
             String label, String extrainfo)
{
  // incrementa e atribui o valor da variavel estatica nid
	this.id = ++Arrow.nid;
	this.sender = sender;
	this.receiver = receiver;
	this.label = label.trim();
	this.extrainfo = extrainfo.trim();

}

/**
 * Return Arrow ID Number
 * @return int
 *         ID number
 */
public int getId()
{
	return this.id;
}

/**
 * Set Arrow ID Number
 * @param id
 *        ID Number
 */
public void setId(int id)
{
  this.id = id;  
}

/**
 * Return Sender Object of Arrow
 * @return ObjectData
 *         Sender Object of Arrow
 */
public ObjectData getSender() 
{
   //by reflection
   //return this.sender.getClass().getName();
   return this.sender;
}

/**
 * Set Sender Object on Arrow
 * @param sender
 *        Sender Object of Arrow
 */
public void setSender(ObjectData sender)
{
   this.sender = sender;
}

/**
 * Return Receiver Object of Arrow
 * @return ObjectData
 *         Receiver Object of Arrow
 */
public ObjectData getReceiver() {
   //return this.receiver.getClass().getName();
   return this.receiver;
}

/**
 * Set Receiver Object of Arrow
 * @param receiver
 *        Receiver Object of Arrow
 */
public void setReceiver(ObjectData receiver)
{
   this.receiver = receiver;	
}

/**
 * Return Arrow Text Label
 * @return String
 *         Text Label of Arrow
 */
public String getLabel()
{
	return this.label;
}

/**
 * Set Arrow Text Label
 * @param label
 *        Text Label of Arrow
 */
public void setLabel(String label)
{
  this.label = label;
}

// metodo canonico, para verificar se a seta e identica,
// para comparar facilmente em uma outra estrutura de dados,
// como por exemplo uma lista de setas
/**
 * Verify if Arrows are equal
 * @return boolean
 *         true if Arrows are equal, false otherwise 
 */
public boolean equals(Arrow arrow)
{
	return (this.getType() == arrow.getType()) && 
  (this.getId() == arrow.getId()) && (this.sender.equals(arrow.sender)) && 
  (this.receiver.equals(arrow.receiver)) && 
  (this.getLabel().equals(arrow.getLabel()));
}

// metodo canonico
/**
 * Convert Arrow to String
 * @return String
 *         Text Information about the Arrow
 */
public String toString()
{
	return this.getLog();
}

/**
 * Set the Relation Reference Arrow
 * @param ref
 *        Reference Arrow
 */
public void setRefArrow(Arrow ref)
{
  this.refArrow = ref;
}

/**
 * Return Relation Reference Arrow
 * @return Arrow
 *         Reference Arrow
 */
public Arrow getRefArrow()
{
  return this.refArrow;
}

/**
 * Return Arrow Type
 * @return int
 *         Arrow Type
 */
public int getType()
{
   return this.type;
   //return an integer value, for performance purposes, on checking
}

/**
 * Check if is Call Arrow
 * @return boolean
 *         true if is a Call Arrow; false otherwise
 */
public boolean isCall()
{
  return (this.type == Arrow.CALL_ARROW_TYPE);
}

/**
 * Check if is Return Arrow
 * @return boolean
 *         true if is Return Arrow; false otherwise
 */
public boolean isReturn() 
{
  return (this.type == Arrow.RETURN_ARROW_TYPE);
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
 *         return true if arrow is visible; false otherwise
 */
public boolean isVisible() {
  return this.visible;
}

public abstract boolean getVoid();

public abstract String getTypeString();

public abstract String getLog(); 

public abstract String getXML();

public abstract String getPIC();

public abstract String getSequence();


}




