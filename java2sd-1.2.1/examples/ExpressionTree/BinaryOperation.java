
/**
 * A binary operation is a kind of arithmetic expression
 * 
 * @author Joao Paulo Barros
 * @version 2005/04/30
 */
public abstract class BinaryOperation implements Expression
{
    private Expression left;
    private Expression right;
    
	public BinaryOperation(Expression left, Expression right)
	{
	    this.left = left;
	    this.right = right;
	}
	
	public Expression getLeft() 
	{
	    return left;
	}
	
	public Expression getRight() 
	{
	    return right;
	}
}
