/**
 * Square root is a unary operation/function
 * 
 * @author Joao Paulo Barros
 * @version 2005/04/30
 */
public class Sqrt extends UnaryOperation
{
	public Sqrt(Expression expression)
	{
	    super(expression);
	}
	
	public double evaluate()
	{
		return Math.sqrt(getExpression().evaluate());
	}
}
