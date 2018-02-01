
/**
 * Test the evaluation of arithmetic expression test.
 * 
 * @author Joao Paulo Barros
 * @version 2005/04/30
 */
public class TreeEvaluationTest
{
/*
static {
System.out.println("__STATIC_BLOCK__");
}
*/
	public TreeEvaluationTest()
	{
		/* 2 + 4 * 5
		         + 
		        / \
		       2   *
		          / \
		         4   5
		*/
		Expression tree = (new Addition(new Value(2.0), 
		                             new Multiplication(new Value(4.0),
		                                                new Value(5.0)
		                                               )
		                  ));
	        //assert(tree.evaluate() != 1.0);
		System.out.println(tree.evaluate());
	}

        public static void main(String[] args)
        {
	   TreeEvaluationTest eval = new TreeEvaluationTest();
        }
}

