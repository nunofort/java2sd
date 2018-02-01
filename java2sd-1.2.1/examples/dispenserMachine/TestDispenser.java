
//import pt.ipbeja.estig.pt.sd.SDControl;

/**
 * Tests Dispenser machine program
 * 
 * @author Joao Paulo Barros
 * @version 2005/05/12
 */
public class TestDispenser
{
	public TestDispenser()
	{
		Dispenser dispenser = new Dispenser();
		
		dispenser.addProduct(1 /*location*/, new Product("Sumo", 1.5));
		dispenser.addProduct(2 /*location*/, new Product("Chocolate", 2.0f));
		
		dispenser.selectLocation(2);
		
		dispenser.insertMoney(0.5);
		dispenser.insertMoney(0.5);
		dispenser.insertMoney(2.0);
		
		assert(dispenser.sell() == 1.0);
		
		System.out.println("ok");
	}

        public static void main(String[] args)
        {
           TestDispenser dispenser = new TestDispenser();
        }
}
