
/**
 * A unary operation is a kind of arithmetic expression
 * 
 * @author Joao Paulo Barros
 * @version 2005/04/30
 */
public abstract class UnaryOperation implements Expression
{
    private Expression expression;
    
	public UnaryOperation(Expression expression)
	{
	    this.expression = expression;
	}
	
	public Expression getExpression()
	{
	    return expression;
	}
}
