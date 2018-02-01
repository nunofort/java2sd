package pt.ipbeja.estig.sd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;  

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;


/**
 * Class Definition for the Graphic User Interface.
 *
 * @author Nuno Fortes
 * @version 1.0
 */
//class Diagram extends JFrame implements ActionListener
class Gui extends JPanel implements ActionListener
{
   private final String LABEL_EXPORT_JPEG = "Export to JPEG";
   //private final String LABEL_EXPORT_EPS = "Export to EPS";
   private final String LABEL_EXIT = "Exit";
   // dimensoes da interface grafica
   private final int SCREEN_WIDTH = 640;
   private final int SCREEN_HEIGHT = 50;
   private Image image;
   
 
   /**
    * Initialize Class
    */
   public Gui()
   {         
     this.setPreferredSize(new Dimension(this.SCREEN_WIDTH, this.SCREEN_HEIGHT));
     this.setBorder(BorderFactory.createLineBorder (Color.blue, 2));
     this.setBackground(Color.white);
    
     this.addComponentListener(
 	  new ComponentAdapter() {
 		  //redesenha o conteudo da janela se redimensionarmos
 		  public void componentResized(ComponentEvent e) {
 			  repaint();
 		  }
 	  }
     );
   
   }

   /**
   * Draw User Interface
   * @param img
   *        Image reference
   */
 
   public void userInterface(Image img)
   {
      this.image = img;

      JButton b1 = new JButton(this.LABEL_EXPORT_JPEG);
      b1.addActionListener(this);
      b1.setEnabled(true);
      this.add(b1);
      JButton b2 = new JButton(this.LABEL_EXIT);
      b2.addActionListener(this);
      b2.setEnabled(true);
      this.add(b2);

   }
 
   /**
   * Method to process events from class
   */ 
   
   public void actionPerformed( ActionEvent ev )
   {
      String label = ev.getActionCommand();
      if (label.equals(this.LABEL_EXPORT_JPEG)) {
    	  this.image.saveJPEG("output.jpg");
      }

      if (label.equals(this.LABEL_EXIT)) {
    	   System.out.println("EXIT");
           System.exit(0);  
      }
   }


}

