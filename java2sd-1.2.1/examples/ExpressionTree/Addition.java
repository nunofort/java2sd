
/**
 * Addition is a binary operation
 * 
 * @author Joao Paulo Barros
 * @version 2005/04/30
 */
public class Addition extends BinaryOperation
{

static {
System.out.println("__STATIC_BLOCK__");
}
static {
System.out.println("__STATIC_BLOCK__");
}

	public Addition(Expression left, Expression right)
	{
	    super(left, right);
	}
	
	public double evaluate()
	{
		return getLeft().evaluate() + getRight().evaluate();
	}
}
