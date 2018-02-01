//package pt.ipbeja.estig.test;

// SD Control
import pt.ipbeja.estig.sd.SDControl;

/**
 * Class for Testing the Application.
 * uses some arithmetic type objects to perform the operations.
 * 
 * Aplicacao de Teste para criar um diagrama de sequencia  
 * que faz chamadas a varios tipos de objecto 
 * serve de exemplo para facil verificacao do funcionamento
 * das varias componentes da aplicacao em questao. 
 * 
 * @author Nuno Fortes
 * @version 1.0
 *
 */
public class Test
{
    public Test()
    {
    }
	

    public String make(int n1, int n2)
    {
    	//int res;
      String s = "";
      int[] subs = new int[5];
    	Sub sub = new Sub( new Add(400,2100), 1300 );
      for (int i = 0; i < subs.length; i++)
          subs[i] = sub.getResult();
      // Hide Construction now
      SDControl.off(); // set SD construction invisible 
      for (int i = 0; i < subs.length; i++) 
          s = s + "  " + String.valueOf( subs[i] );
      Mul mul = new Mul( sub, 1000 );
      //SDControl.on(); // set SD construction visible
      Div div = new Div( mul, 100);
      Add add = new Add( div, 1000 );
      // Show Construction now
      SDControl.on();
      int val = this.getObjectValue(add); // use hidden object
    	return String.valueOf( new Sub( sub.getResult(), 1000 ).getResult() );
      //return 1000;
    }
    
    public int getObjectValue(Operation op) {
       return op.getResult();
    }
      
    
   public static void main(String args[]) 
   {
     System.out.println("Test Program");
   
     Test test = new Test();
     //int res = test.make(400,2100);
     System.out.println("(400 + 2100)-1300 * 1000 = "+test.make(400, 2100));
   } 
}

//debugging option
//javac -g

