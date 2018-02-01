/**
 * Natural logarithm (base e) is a unary operation/function
 * 
 * @author Joao Paulo Barros
 * @version 2005/04/30
 */
public class Ln extends UnaryOperation
{
	public Ln(Expression expression)
	{
	    super(expression);
	}
	
	public double evaluate()
	{
		return Math.log(getExpression().evaluate());
	}
}
