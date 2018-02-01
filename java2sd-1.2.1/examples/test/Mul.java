//package pt.ipbeja.estig.test;

import pt.ipbeja.estig.sd.SDControl;

/**
 * Class for Multiplication Operation
 * @author Nuno Fortes
 * @version 1.0
 *
 */
public class Mul extends Operation
{
    public Mul(int n1, int n2)
    {
    	this.result = this.calc( n1, n2, '*');
    }
    
    public Mul(Operation n1, Operation n2)
    {
    	this.result = this.calc( n1.result, n2.result, '*');
    }

    public Mul(Operation n1, int n2)
    {
    	this.result = this.calc( n1.result, n2, '*');
      /*SDControl.off();
      Div div = new Div(150,10);
      */
    }

    public Mul(int n1, Operation n2)
    {
    	this.result = this.calc( n1, n2.result, '*');
    }
}

