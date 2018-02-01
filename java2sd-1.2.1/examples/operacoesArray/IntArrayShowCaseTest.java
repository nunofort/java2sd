
/**
 * Tests IntArrayShowCase class.
 * 
 * @author Joao Paulo Barros
 * @version 2005-04-21
 */
public class IntArrayShowCaseTest
{
    private IntArrayShowCase obj;

    public IntArrayShowCaseTest()
    {
        obj = new IntArrayShowCase(3);
        insertionTest();
        sumTest();
        multiplyTest();
        containsTest();
        indexOfTest();
        minTest();
        maxTest();
        invertTest();
        removeTest();
        System.out.println("Tests OK!");
    }
   
    private void insertionTest()
    {
        obj.insertElementAtPosition(1, 0);
        obj.insertElementAtPosition(5, 2);
        obj.insertElementAtPosition(3, 1);
        String s = obj.toString();
        assert(s.equals("135")); // positive test
    }
    
    private void sumTest()
    {
        assert(obj.sumAll() == (1 + 3 + 5)); // positive test
    }
    
    private void multiplyTest()
    {
        assert(obj.multiplyAll() == (1 * 3 * 5)); // positive test
    }
    
    private void containsTest()
    {
        assert(obj.contains1(1)); // positive test
        assert(obj.contains1(3)); // positive test
        assert(obj.contains1(5)); // positive test
        assert(obj.contains1(7) == false); // negative test
        
        assert(obj.contains2(1)); // positive test
        assert(obj.contains2(3)); // positive test
        assert(obj.contains2(5)); // positive test
        assert(obj.contains2(7) == false); // negative test       
    }

    private void indexOfTest()
    {
        assert(obj.indexOf1(1) == 0);  // positive test
        assert(obj.indexOf1(3) == 1);  // positive test 
        assert(obj.indexOf1(5) == 2);  // positive test
        assert(obj.indexOf1(7) == -1); // negative test

        assert(obj.indexOf2(1) == 0);  // positive test
        assert(obj.indexOf2(3) == 1);  // positive test 
        assert(obj.indexOf2(5) == 2);  // positive test
        assert(obj.indexOf2(7) == -1); // negative test
    }

    private void minTest()
    {
        assert(obj.min() == 1); // positive test
    }
        
    private void maxTest()
    {
        assert(obj.max1() == 5); // positive test
        assert(obj.max2() == 5); // positive test
        assert(obj.max3() == 5); // positive test
    }

    private void invertTest()
    {
        obj.invert();
        assert(obj.toString().equals("531"));
           
        assert(obj.getInvertedCopy().toString().equals("135"));
    }
              
    private void removeTest()
    {
        obj.remove(1);
        assert(obj.toString().equals("530"));
        obj.remove(5);
        assert(obj.toString().equals("300"));
    }

    public static void main(String[] args)
    {
        IntArrayShowCaseTest test = new IntArrayShowCaseTest();
    }
}
