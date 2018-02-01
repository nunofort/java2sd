
/**
 * A product in the dispenser machine
 * 
 * @author Joao Paulo Barros
 * @version 2005/05/12
 */
public class Product
{
	private String name;
	private double price;

	public Product(String name, double price)
	{
		this.name = name;
		this.price = price;
	}
	
	public double getPrice()
	{
	    return price;
	}
}
