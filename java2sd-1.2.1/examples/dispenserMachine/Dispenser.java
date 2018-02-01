
/**
 * A dispenser machine where each product is associated 
 * to one location. The location is specified by and Integer object.
 * 
 * @author Joao Paulo Barros
 * @version 2005/05/12
 */
import java.util.HashMap;

public class Dispenser
{
    //
    private HashMap<Integer, Product> products;
    private int selected;
    private double moneyReceived;

    /**
     * creates an empty dispenser with
     * no received money and no selected location
     */
    public Dispenser()
    {
        products = new HashMap<Integer, Product>();
        moneyReceived = 0.0;
        selected = -1; // not selected
    }

    /** adds a new product to be sold by the dispenser */
    public void addProduct(Integer location, Product product)
    {
        products.put(location, product);
    }
    
    /** selects a product location */
    public void selectLocation(Integer location)
    {
        selected = location;
    }
    
    /** accepts money received and increments the
     * quantity of received money
     */
    public void insertMoney(double money)
    {
        moneyReceived = moneyReceived + money;
    }
    
    /** returns money received and
     * unselects the location
     */
    public void cancel()
    {
        moneyReceived = 0.0;
        selected = -1; // not selected
    }
    
    /** removes product from location and returns the change */
    public double sell()
    {
        Product p = products.get(selected);
        if (moneyReceived >= p.getPrice())
        {
            products.remove(selected);
            double change = moneyReceived - p.getPrice();
            cancel(); // resets the machine
            return change;
        }
        else
        {
            System.out.println("Missing " + 
                               (p.getPrice() - moneyReceived) + 
                               " euro");
            return 0;
        }
    }
}
