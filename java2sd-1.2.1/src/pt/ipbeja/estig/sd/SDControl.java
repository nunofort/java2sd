
package pt.ipbeja.estig.sd;

/**
 * For Controlling the construction of Sequence Diagram 
 * inside the JAVA Application. 
 * 
 * @author Nuno Fortes (nunofort@gmail.com)
 * @version 1.0
 */
public class SDControl {
  private boolean enabled = true;
  
  public SDControl() {
    
  }
  
  // visible status
  public static void show() {
    // Diagram Generation ON
    System.out.println("Generate Diagram... ON"); // for debugging purposes! 
  }
  
  // invisible status
  public static void hide() {
    // Diagram Generation OFF
    System.out.println("Generate Diagram... OFF"); // for debugging purposes!
  }  
  
  public static void on() {
    // Diagram Generation ON
    System.out.println("Generate Diagram... ON"); // for debugging purposes! 
  }
  
  public static void off() {
    // Diagram Generation OFF
    System.out.println("Generate Diagram... OFF"); // for debugging purposes!
  }
  
}
//SDControl.on()
//SDControl.off()
