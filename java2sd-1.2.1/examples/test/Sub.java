//package pt.ipbeja.estig.test;

/**
 * Class for Subtraction Operation
 * @author Nuno Fortes
 * @version 1.0
 *
 */
public class Sub extends Operation
{
    public Sub(int n1, int n2)
    {
    	this.result = this.calc( n1, n2, '-');
    }
    
    public Sub(Operation n1, Operation n2)
    {
    	this.result = this.calc( n1.result, n2.result, '-');
    }

    public Sub(Operation n1, int n2)
    {
    	this.result = this.calc( n1.result, n2, '-');
    }

    public Sub(int n1, Operation n2)
    {
    	this.result = this.calc( n1, n2.result, '-');
    }
}

