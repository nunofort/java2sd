
/**
 * This is not a "real" class. Its only objective is to
 * illustrate operations on int arrays.
 * 
 * Os comentários em português são apenas para fins didácticos.
 * Esse tipo de comentários não deve estar presente nos programas
 * "a sério" como, por exemplo, os que são entregues para avaliação...
 * 
 * @author Joao Paulo Barros
 * @version 2005-11-21
 */
import java.util.ArrayList;

public class IntArrayListShowCase
{
    // instance variables - replace the example below with your own
    private ArrayList<Integer> theVector;

    /**
     * @param n  number of positions in ArrayList<Integer>
     */
    public IntArrayListShowCase(int n)
    {
        theVector = new ArrayList<Integer>(n);
    }
    
    public IntArrayListShowCase(ArrayList<Integer> oneVector)
    {
        theVector = oneVector;
    }
    
    /** Adds an alement to theVector
     * @return the modified IntArrayLisyShowCase object
     */
    public IntArrayListShowCase addElement(int element) 
    {
        theVector.add(element);
        return this;
    }

    public String toString()
    {
        /*String s = "";
        for(Integer elem : theVector) 
        {
            s = s + elem;
        }
        return s;*/
        return theVector.toString();
    }
    
    public int sumAll()
    {
        int total = 0;
        for(Integer element : theVector) 
        {
            total = total + element; // or total += elem;
        }
        return total;
    }
    
    public int multiplyAll()
    {
        int total = 1;
        for(Integer element : theVector) 
        {
            total = total * element; // or total *= elem;
        }
        return total;
    }

    /** contains */
    public boolean contains(int oneElement)
    {
        return theVector.contains(oneElement);
    }
    
    /** indexOf with auxiliary variable */
    public int indexOf(int oneElement)
    { 
        return theVector.indexOf(oneElement);
    }
            
    /** returns maximum value in vector */    
    public int max()
    {
        int m = theVector.get(0);
        for(Integer element : theVector) 
        {
            if (element > m) 
            {
                m = element;
            }
        }
        return m;
    }
     
    /** returns minimum value in vector.
     */    
    public int min()
    {
        int m = theVector.get(0);
        for(int i = 1; i < theVector.size(); i++) 
        {
            if (theVector.get(i) < m) {
                m = theVector.get(i);
            }
        }
        return m;
    }     

    /** returns a new vector with the same elements in reverse order */
    public IntArrayListShowCase getInvertedCopy() 
    {
        // cria um novo ArrayList ainda vazio 
        ArrayList<Integer> newVector = new ArrayList<Integer>();
        // adiciona, do fim para o principio, os elementos em 
        // theVector no novo ArrayList
        for(int i = theVector.size() - 1; i >= 0; i--) 
        {
            newVector.add(theVector.get(i));
        }
        return new IntArrayListShowCase(newVector);
    }
    
    /** Reverses the vector elements position.
     *  @return the object containing the inverted vector 
     */
    public IntArrayListShowCase invert() 
    {
        int middlePos = theVector.size() / 2; // middle position in vector -1
        int lastPos = theVector.size() - 1; // last position in vector
        for(int i = 0; i < middlePos; i++) 
        {
            int tmp = theVector.get(i);
            theVector.set(i, theVector.get(lastPos - i));
            theVector.set(lastPos - i, tmp);
        }
        return this;
    }
        
    /** removes an element from the vector 
     *  @return true if the list contained the element
     */
    public boolean remove(int oneElement) 
    {
        return theVector.remove((Integer)oneElement); // to remove the element
        //theVector.remove(oneElement); would remove the element at
 	    //                              position oneElement
	}
} // end class IntArrayShowCase
