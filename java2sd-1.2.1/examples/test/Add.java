//package pt.ipbeja.estig.test;


/**
 * Class for Addition Operation
 * @author Nuno Fortes
 * @version 1.0
 *
 */
public class Add extends Operation
{

    public Add(int n1, int n2)
    {
    	this.result = this.calc( n1, n2, '+');
    }

    public Add(Operation n1, Operation n2)
    {
    	this.result = this.calc( n1.result, n2.result, '+');
    }
    
    public Add(Operation n1, int n2)
    {
    	this.result = this.calc( n1.result, n2, '+');
    }
    
    public Add(int n1, Operation n2)
    {
    	this.result = this.calc( n1, n2.result, '+');
    }
    
}
