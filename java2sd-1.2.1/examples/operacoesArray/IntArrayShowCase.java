
/**
 * This is not a "real" class. Its only objective is to
 * illustrate operations on int arrays.
 * @author Joao Paulo Barros
 * @version 2005-04-21
 */
public class IntArrayShowCase
{
	// instance variables - replace the example below with your own
	private int[] theVector;

	/**
	 * @param n  number of positions in array
	 */
	public IntArrayShowCase(int n)
	{
	    theVector = new int[n];
	}
	
	public IntArrayShowCase(int[] oneVector)
	{
	    theVector = oneVector;
	}
	
	public void insertElementAtPosition(int element, int position) 
	{
	    theVector[position] = element;
	}

        public String toString()
	{
	    String s = "";
	    for(int elem : theVector) 
	    //for(int i = 0; i < theVector.length; i++) 
	    {
	      //s = String.valueOf(elem);
	      s = s + elem;
	      //s = s + String.valueOf(theVector[i]);
	      //s = s + " oops";
	    } 
	    return s;
	}
	
	public int sumAll()
	{
	    int total = 0;
	    for(int element : theVector) 
	    {
	        total = total + element; // or total += elem;
	    }
	    return total;
	}
	
	public int multiplyAll()
	{
	    int total = 1;
	    for(int element : theVector) 
	    {
	        total = total * element; // or total *= elem;
	    }
	    return total;
	}

    /** contains with flag */
	public boolean contains1(int oneElement)
	{
	    boolean found = false; // initially not found
	    for(int i = 0; !found && (i < theVector.length); i++) 
	    {
	        if (theVector[i] == oneElement) {
	            found = true;
	        }
	    }
	    return found;
	}

    /** contains with flag */
	public boolean contains2(int oneElement)
	{
	    boolean found = false; // initially not found
	    for(int element : theVector) 
	    {
	        if (element == oneElement) 
	        {
	            found = true;
	            break;
	        }
	    }
	    return found;
	}
	
	/** contains without flag */
    public boolean contains3(int oneElement)
	{
	    for(int element : theVector) 
	    {
	        if (element == oneElement) 
	        {
	            return true;
	        }
	    }
	    return false;
	}
	
    /** indexOf with auxiliary variable */
	public int indexOf1(int oneElement)
	{ 
	    int index = -1; // initially not found
	    for(int i = 0; i < theVector.length; i++) 
	    {
	        if (theVector[i] == oneElement) 
	        {
	            index = i; // found!
	            break;
	        }
	    }
	    return index;
	}
	        
    /** indexOf without auxiliary variable */
	public int indexOf2(int oneElement)
	{ 
	    for(int i = 0; i < theVector.length; i++) 
	    {
	        if (theVector[i] == oneElement) 
	        {
	            return i; // found!
	        }
	    }
	    return -1; // not found
	}
	    
	/** returns maximum value in vector */    
	public int max1()
	{
	    int m = theVector[0];
	    for(int element: theVector) 
	    {
	        if (element > m) 
	        {
	            m = element;
	        }
	    }
	    return m;
	}
	
	/** returns maximum value in vector */    
	public int max2()
	{
	    int m = theVector[0];
	    for(int i = 1; i < theVector.length; i++) 
	    {
	        if (theVector[i] > m) 
	        {
	            m = theVector[i];
	        }
	    }
	    return m;
	}       
	
	/** returns maximum value in vector */    
	public int max3()
	{
	    int maxIndex = 0;
	    for(int i = 1; i < theVector.length; i++) 
	    {
	        if (theVector[i] > theVector[maxIndex]) {
	            maxIndex = i;
	        }
	    }
	    return theVector[maxIndex];
	}       

    
    /** returns minimum value in vector.
     * Only one version is presented. The others are 
     * similar to the max ones.
     */    
	public int min()
	{
	    int m = theVector[0];
	    for(int i = 1; i < theVector.length; i++) 
	    {
	        if (theVector[i] < m) {
	            m = theVector[i];
	        }
	    }
	    return m;
	}     


	/** returns a new vector with the same elements in reverse order */
	public IntArrayShowCase getInvertedCopy() 
	{
	    int[] newVector = new int[theVector.length];
	    int lastPos = theVector.length - 1; // last position in vector
	    for(int i = 0; i < theVector.length; i++) 
	    {
	        newVector[lastPos - i] = theVector[i];
	    }
	    return new IntArrayShowCase(newVector);
	}
	
	/** reverses the vector elements position */
	public void invert() 
	{
	    int middlePos = theVector.length / 2; // middle position in vector -1
  	    int lastPos = theVector.length - 1; // last position in vector
	    for(int i = 0; i < middlePos; i++) 
	    {
	        int tmp = theVector[i];
	        theVector[i] = theVector[lastPos - i];
	        theVector[lastPos - i] = tmp;
	    }
	}
	    
	/** removes an element from the vector */
	public void remove(int oneElement) 
	{
	    int pos = indexOf1(oneElement);
	    if (pos != -1) 
	    { // found
	        for(int i = pos; i < (theVector.length - 1); i++) 
	        {
	            theVector[i] = theVector[i+1];
	        }
	        theVector[theVector.length - 1] = 0; // reset last
	    }
	    else
	    {
	        // nothing to do
	    }
	}
} // end class IntArrayShowCase
