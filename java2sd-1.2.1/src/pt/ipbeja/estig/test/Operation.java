package pt.ipbeja.estig.test;

/**
 * Abstract Class for Arithmetic Operations
 * @author Nuno Fortes
 * @version 1.0
 *
 */
public abstract class Operation 
{
    protected int result = 0;
	
    public Operation() {
      
    }
    
    protected int calc(int n1, int n2, char op)
    {
       int total = 0;
       switch(op)
       {
          case '+': total = n1 + n2;break;
          case '-': total = n1 - n2;break;
          case '*': total = n1 * n2;break;
          case '/': total = n1 / n2;break;
       }  
       return total;
    }
    
    protected int getResult() {
	    return this.result;
    }

}

