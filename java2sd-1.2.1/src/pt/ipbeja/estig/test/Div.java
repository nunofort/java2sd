package pt.ipbeja.estig.test;

import pt.ipbeja.estig.sd.SDControl;

/**
 * Class for Division Operation
 * @author Nuno Fortes
 * @version 1.0
 *
 */
public class Div extends Operation
{
    public Div(int n1, int n2)
    {
    	this.result = this.calc( n1, n2, '/');
      /*SDControl.on();
      Add add = new Add(12,8);
      */
    }
    
    public Div(Operation n1, Operation n2)
    {
    	this.result = this.calc( n1.result, n2.result, '/');
    }

    public Div(Operation n1, int n2)
    {
    	this.result = this.calc( n1.result, n2, '/');
    }

    public Div(int n1, Operation n2)
    {
    	this.result = this.calc( n1, n2.result, '/');
    }
}

