
/**
 * Value is a (trivial) expression
 * 
 * @author Joao Paulo Barros
 * @version 2005/04/30
 */
public class Value implements Expression
{
    private double value;
    
	public Value(double value)
	{
	    this.value = value;
	}
	
	public double evaluate()
	{
		return value;
	}
}
