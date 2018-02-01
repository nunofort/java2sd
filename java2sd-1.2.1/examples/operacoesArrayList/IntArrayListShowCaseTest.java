
/**
 * Tests IntArrayListShowCase class.
 * 
 * @author Joao Paulo Barros
 * @version 2005-11-21
 */

public class IntArrayListShowCaseTest
{
    private IntArrayListShowCase obj;

    public IntArrayListShowCaseTest()
    {
        obj = new IntArrayListShowCase(3);
        insertionTest();
        sumTest();
        multiplyTest();
        containsTest();
        indexOfTest();
        minTest();
        maxTest();
        invertTest();
        removeTest();
        System.out.println("Tests OK.");
        System.exit(0); // end program (and test)
    }
   
    private void insertionTest()
    {
        obj.addElement(1);
        obj.addElement(3);
        obj.addElement(5);
      
        String s = obj.toString();
        assert(s.equals("[1, 3, 5]")) 
            : "insertionTest failed"; // positive test
    }
    
    private void sumTest()
    {
        assert(obj.sumAll() == (1 + 3 + 5)) 
            : "sumTest failed"; // positive test
    }
    
    private void multiplyTest()
    {
        assert(obj.multiplyAll() == (1 * 3 * 5)) 
            : "multiplyTest failed"; // positive test
    }
    
    private void containsTest()
    {
        assert(obj.contains(1)) 
            : "containsTest 1 failed"; // positive test
        assert(obj.contains(3)) 
            : "containsTest 3 failed"; // positive test
        assert(obj.contains(5)) 
            : "containsTest 5 failed"; // positive test
        assert(obj.contains(7) == false) 
            : "containsTest 7 failed"; // negative test
    }

    private void indexOfTest()
    {
        assert(obj.indexOf(1) == 0) 
            : "indexOfTest 1 failed";  // positive test
        assert(obj.indexOf(3) == 1) 
            : "indexOfTest 3 failed";  // positive test 
        assert(obj.indexOf(5) == 2) 
            : "indexOfTest 5 failed";  // positive test
        assert(obj.indexOf(7) == -1)
            : "indexOfTest 7 failed";  // negative test
    }

    private void minTest()
    {
        assert(obj.min() == 1) : "minTest failed"; // positive test
    }
        
    private void maxTest()
    {
        assert(obj.max() == 5) : "maxTest failed"; // positive test
    }

    private void invertTest()
    {
        assert(obj.invert().toString().equals("[5, 3, 1]")) 
            : "invert() failed";
        assert(obj.getInvertedCopy().toString().equals("[1, 3, 5]"))
            : "getInvertedCopy() failed";
    }
              
    private void removeTest()
    {
        assert(obj.toString().equals("[5, 3, 1]"));
        assert(obj.remove(1))
            : "remove(1) failed";
        assert(obj.toString().equals("[5, 3]"));
        assert(obj.remove(5))
            : "remove(5) failed";
        assert(obj.toString().equals("[3]"));
    }      

    public static void main(String[] args)
    {
        IntArrayListShowCaseTest test = new IntArrayListShowCaseTest();
    }

}
