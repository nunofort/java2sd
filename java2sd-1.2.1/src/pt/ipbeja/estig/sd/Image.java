
package pt.ipbeja.estig.sd;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.event.*;  

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import javax.imageio.*;


/**
 * Class Definition to construct Sequence Diagram Image from JAVA SWING API.
 *            
 * @author Nuno Fortes
 * @version 1.0 
 */
//class Image extends JPanel implements Scrollable
class Image extends JPanel
{
   //private final String LABEL_SAVE_IMAGE = "Save Image";
   //private final String LABEL_EXIT = "Exit";
   private final static int SCREEN_WIDTH = 800;
   private final static int SCREEN_HEIGHT = 600;
   private final static int OBJECT_YPOSITION = 50;
   private final static int OBJECT_XSTART = 100;
   private final static int OBJECT_YSTART = 100;
   private final static int OBJECT_DISTANCE = 150;
   private final static int ARROW_YSTART = 180;
   private final static int ARROW_DISTANCE = 20;
   
   private BufferedImage img;
   //private Graphics2D g;
   private Diagram diagram;
   private int[] objPositions; // vector de posicoes dos objectos
   private int[] objLevels; // nivel da seta em cada objecto

   //private String xmlFile;
   private String epsText; // instrucoes para criar imagem no formato EPS
   private int screenWidth = Image.SCREEN_WIDTH;
   private int screenHeight = Image.SCREEN_HEIGHT;
   
   /**
    * Initialize Class Properties
    * @param dia
    *        Diagram Structure
    * @param width
    *        Width for the Diagram
    * @param height
    *        Height for the Diagram
    */
   public Image(Diagram dia, int width, int height)
   {
      this.diagram = dia; // guarda a referencia do diagrama
      this.screenWidth = width;
      this.screenHeight = height;
    
	    this.img = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB);
      this.setPreferredSize(new Dimension(width, height));
      this.setBorder(BorderFactory.createLineBorder (Color.blue, 2));
      this.setBackground(Color.white);
      this.epsText = "";

   
       /*addWindowListener(new WindowAdapter() {
     	 public void windowClosing(WindowEvent e)
           { System.exit(0); }
      });*/ 
       
      /*this.addComponentListener(
    		  new ComponentAdapter() {
    			  //redesenha o conteudo da janela se redimensionarmos
    			  public void componentResized(ComponentEvent e) {
    				  repaint();
    			  }
    		  }
   	   );*/
     

   }
  
   /**
    * Draw object on Diagram Image
    * @param g Graphics 2D Device Reference
    * @param x 
    *        Screen X Position to place object
    * @param obj
    *        ObjectData Reference
    */
   private void drawObject(Graphics2D g, int x, ObjectData obj)
   { 
      String cname = "";
      if (this.diagram.getFullClassNames())
        cname = obj.getClassPath();
      else
        cname = obj.getClassName();
      g.setColor(Color.black);
      g.drawRect(x,Image.OBJECT_YPOSITION,100,50);
      g.setColor(Color.cyan);
      g.fillRect(x,Image.OBJECT_YPOSITION,100,50);
      g.setColor(Color.red);
      g.drawLine(x+50,Image.OBJECT_YSTART+1,x+50,Image.OBJECT_YSTART+20);       
      g.setColor(Color.black);
      if (obj.isStatic()) {
        g.drawLine(x+5,Image.OBJECT_YPOSITION+21,x+90,Image.OBJECT_YPOSITION+21);
        Font font_arial = new Font("Arial",Font.BOLD,10);
        //Font font_times = new Font("TimesRoman",Font.ITALIC,10);
        //Font font_serif = new Font("Serif",Font.BOLD,10);
        g.setFont(font_arial); 
        g.drawString(cname,x+5,Image.OBJECT_YPOSITION+20);
      } else {
        Font font_arial = new Font("Arial",Font.BOLD,10);
        //Font font_times = new Font("TimesRoman",Font.ITALIC,10);
        //Font font_serif = new Font("Serif",Font.BOLD,10);
        g.setFont(font_arial); 
        g.drawString(obj.getObjId() + ":" + cname,x+5,Image.OBJECT_YPOSITION+20); 
      }
   }
   
   /**
    * Draw Object on EPS Format
    * @param x
    *        Screen X Position to place object
    * @param obj
    *        ObjectData reference
    */
   private void epsDrawObject(int x, ObjectData obj)
   {
     String cname = "";
     if (this.diagram.getFullClassNames())
       cname = obj.getClassPath();
     else
       cname = obj.getClassName();
	  this.epsText += "newpath blue\n";
	  this.epsText += x+" "+(this.screenHeight-Image.OBJECT_YPOSITION)+" moveto\n";
	  this.epsText += (x+100)+" "+(this.screenHeight-Image.OBJECT_YPOSITION)+
                    " lineto\n";
	  this.epsText += (x+100)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+50))+
                    " lineto\n";
	  this.epsText += x+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+50))+
                    " lineto\n";
	  this.epsText += x+" "+(this.screenHeight-Image.OBJECT_YPOSITION)+" lineto\n";
	  this.epsText += "fill\n";
	  this.epsText += "black\n";
    if (obj.isStatic()) {
      this.epsText += (x+5)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+21))
                      + " moveto\n";
      this.epsText += (x+90)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+21))
                      + " lineto\n";
      this.epsText += (x+50)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+50))
      +" moveto\n";
      this.epsText += (x+50)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+70))
      +" lineto\n";
      this.epsText += "closepath\n";
      this.epsText += "stroke\n";
      this.epsText += (x+5)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+20))
      +" moveto\n";
      this.epsText += "/Times-Roman findfont 9 scalefont setfont\n";
      this.epsText += "("+cname+") black show\n";
    } else {
	    this.epsText += (x+50)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+50))
                    +" moveto\n";
	    this.epsText += (x+50)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+70))
                    +" lineto\n";
	    this.epsText += "closepath\n";
	    this.epsText += "stroke\n";
	    this.epsText += (x+5)+" "+(this.screenHeight-(Image.OBJECT_YPOSITION+20))
                    +" moveto\n";
	    this.epsText += "/Times-Roman findfont 9 scalefont setfont\n";
	    this.epsText += "("+obj.getObjId() + ":" + cname+") black show\n";
    }
   }
      
   /**
    * Draw Arrow on Diagram Image
    * @param g 
    *        Graphics 2D Device Reference
    * @param x1 
    *        Screen Initial X Position
    * @param x2 
    *        Screen Finish X Position
    * @param y 
    *        Screen Y Position
    * @param arrow
    *        Arrow Reference
    */
   private void drawArrow(Graphics2D g, int x1, int x2, int y, Arrow arrow)
   {
      int dist = 5; // distancia do sinal > 
      g.setColor(Color.red);

      int x = x1; // posicao X, onde comeca o texto do label 
      
      ObjectData sender = arrow.getSender();
      ObjectData receiver = arrow.getReceiver();
      if (arrow.getTypeString().equals("CALL")) {
        //if (receiver.getNid() == sender.getNid())
        if (receiver.equals(sender)) { 
          // receiver object = sender object
          g.drawLine(x1+50, y, x2+60, y);
          x = x1+60;
        } else {
          x1 += 50;
          x2 += 50;
          g.drawLine(x1, y, x2, y); // desenha a linha horizontal
          if (receiver.getId() > sender.getId()) {
          g.drawLine(x2-dist, y-dist, x2, y);
          g.drawLine(x2-dist, y+dist, x2, y);
          x = x1 + 10; // posicao X do label
          } else {
          g.drawLine(x2+dist, y-dist, x2, y);
          g.drawLine(x2+dist, y+dist, x2, y);
          x = x2 + 10;
          }
          
        }
        g.setColor(Color.black);
        //Font font_times = new Font("TimesRoman",Font.BOLD,10);
        Font font_arial = new Font("Arial",Font.BOLD,10);
        //Font font_serif = new Font("Serif",Font.BOLD,10);   
        g.setFont(font_arial);
        g.drawString(arrow.getLabel(), x, y-dist);
      } else {
        CallArrow ca = (CallArrow) arrow.getRefArrow();
        // se for permitido retorno de constructor
        if (((this.diagram.getReturnConstructor()) || 
            // se nao for nenhum tipo de chamada de constructor
            (!ca.isCreation())) && 
          // e se for permitido valor void
          // ou se o valor devolvido for diferente de "void"
            ((this.diagram.getReturnVoid()) || (!arrow.getVoid())))
        {
          if (receiver.equals(sender)) {
            g.drawLine(x1+50, y, x2+40, y);
            x = x2+60;
          } else {
            x1 += 50;
            x2 += 50;
            g.drawLine(x1, y, x2, y); // desenha a linha horizontal 
            if (receiver.getId() < sender.getId()) {
	          g.drawLine(x2+dist, y-dist, x2, y);
	          g.drawLine(x2+dist, y+dist, x2, y);
	          x = x2 + 10; // posicao X do label
            } else {
              g.drawLine(x2-dist, y-dist, x2, y);
              g.drawLine(x2-dist, y+dist, x2, y);
              x = x1 + 10; 
            }
          }
          g.setColor(Color.black);
          //Font font_times = new Font("TimesRoman",Font.BOLD,10);
          Font font_arial = new Font("Arial",Font.BOLD,10);
          //Font font_serif = new Font("Serif",Font.BOLD,10);   
          g.setFont(font_arial);
          g.drawString(arrow.getLabel(), x, y-dist);
        }
      }

   }
   

   /**
    * Draw Arrow on EPS Format Image
    * @param x1 
    *        Screen Initial X Position
    * @param x2 
    *        Screen Finish X Position
    * @param y 
    *        Screen Y Position
    * @param arrow
    *        Arrow Reference 
    */
   private void epsDrawArrow(int x1, int x2, int y, Arrow arrow)
   {
      int dist = 5; // distancia do sinal > 

      int x = x1; // posicao X, onde comeca o texto do label 
      
      ObjectData sender = arrow.getSender();
      ObjectData receiver = arrow.getReceiver();
      if (arrow.getTypeString().equals("CALL")) {
        //if (receiver.getNid() == sender.getNid())
        if (receiver.equals(sender)) { 
          // receiver object = sender object
          this.epsText += "red\n";
          this.epsText += x1+50+" "+(this.screenHeight-y)+" moveto\n";
          this.epsText += x2+60+" "+(this.screenHeight-y)+" lineto\n";
          this.epsText += "closepath\n";
          this.epsText += "stroke\n";
          x = x1+60;
        } else {
          x1 += 50;
          x2 += 50;
          this.epsText += "red\n";
          this.epsText += x1+" "+(this.screenHeight-y)+" moveto\n";
          this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
          this.epsText += "closepath\n";
          this.epsText += "stroke\n";
          if (receiver.getId() > sender.getId()) {
    	    this.epsText += (x2-dist)+" "+(this.screenHeight-(y-dist))+" moveto\n";
    	    this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
       	  this.epsText += (x2-dist)+" "+(this.screenHeight-(y+dist))+" moveto\n";
    	    this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
    	    x = x1+10; // posicao X do label
          } else {
            this.epsText += (x2+dist)+" "+(this.screenHeight-(y-dist))+" moveto\n";
            this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
            this.epsText += (x2+dist)+" "+(this.screenHeight-(y+dist))+" moveto\n";
            this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
            x = x2+10;            
          }
        } 
        this.epsText += "closepath\n";
        this.epsText += "stroke\n";
        this.epsText += x+" "+(this.screenHeight-(y-dist))+" moveto\n";
        this.epsText += "/Times-Roman findfont 9 scalefont setfont\n";
        this.epsText += "("+arrow.getLabel()+") black show\n";
      } else {
        CallArrow ca = (CallArrow) arrow.getRefArrow();
        if (((this.diagram.getReturnConstructor()) || 
            // se nao for nenhum tipo de chamada de constructor
            (!ca.isCreation())) &&
            // e se for permitido valor void
            // ou se o valor devolvido for diferente de "void"
              ((this.diagram.getReturnVoid()) || (!arrow.getVoid())))
        {
          //if (receiver.getNid() == sender.getNid())
          if (receiver.equals(sender)) { 
            // receiver object = sender object
            this.epsText += "red\n";
            this.epsText += x1+50+" "+(this.screenHeight-y)+" moveto\n";
            this.epsText += x2+40+" "+(this.screenHeight-y)+" lineto\n";
            this.epsText += "closepath\n";
            this.epsText += "stroke\n";
            x = x2+60;
          } else {
            x1 += 50;
            x2 += 50;
            this.epsText += "red\n";
            this.epsText += x1+" "+(this.screenHeight-y)+" moveto\n";
            this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
            this.epsText += "closepath\n";
            this.epsText += "stroke\n";
            if (receiver.getId() < sender.getId()) {
            this.epsText += (x2+dist)+" "+(this.screenHeight-(y-dist))+" moveto\n";
    	      this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
       	    this.epsText += (x2+dist)+" "+(this.screenHeight-(y+dist))+" moveto\n";
    	      this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
    	      x = x2+10;
            } else {
              this.epsText += (x2-dist)+" "+(this.screenHeight-(y-dist))+" moveto\n";
              this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
              this.epsText += (x2-dist)+" "+(this.screenHeight-(y+dist))+" moveto\n";
              this.epsText += x2+" "+(this.screenHeight-y)+" lineto\n";
              x = x1+10; // posicao X do label              
            }
          }
          this.epsText += "closepath\n";
          this.epsText += "stroke\n";
          this.epsText += x+" "+(this.screenHeight-(y-dist))+" moveto\n";
          this.epsText += "/Times-Roman findfont 9 scalefont setfont\n";
          this.epsText += "("+arrow.getLabel()+") black show\n";
        }
      }     
   
   }
   
   /**
    * Re-Draw All Vertical Lines of Objects on each new arrow 
    * @param g 
    *        Graphics 2D Device Reference
    */
   private void drawObjectsLines(Graphics2D g)
   {

     g.setColor(Color.red);
     for (int i = 1; i < this.diagram.objectsSize(); i++)
     {
       //this.objLevels[i] = 0; // nivel 0 em todos os objectos
       //this.objPositions[i] = x+(i*distance); // posicao x do objecto
       int y = Image.ARROW_YSTART + 
       (this.objLevels[i]*Image.ARROW_DISTANCE)-Image.ARROW_DISTANCE;
       g.drawLine(this.objPositions[i]+50,Image.OBJECT_YSTART+1,
                                          this.objPositions[i]+50,y+5); 
     }
   }
   
   /**
    * Draw Objects on Diagram Image
    * @param g
    *        Graphics 2D Device Reference
    */
   private void drawObjects(Graphics2D g)
   {
      int x = Image.OBJECT_XSTART;
      int distance = Image.OBJECT_DISTANCE;
      ArrayList<ObjectData> objs = this.diagram.getObjects();
      int objSize = objs.size();
      //System.out.println("N Objectos: "+objSize);
      // aloca size elementos para o array de inteiros
      this.objPositions = new int[objSize];
      this.objLevels = new int[objSize];
 
      for (int i = 1; i < objSize; i++)
      {
    	  this.objLevels[i] = 0; // nivel 0 em todos os objectos
        this.objPositions[i] = x+(i*distance); // posicao x do objecto
        //System.out.println(objs.get(i).getLog());
        this.drawObject(g,this.objPositions[i], objs.get(i));
      }
   }

   /**
    * Re-Draw All Vertical Lines of Objects on each new arrow 
    * (EPS Format, Text Commands)
    */
   private void epsDrawObjectsLines()
   {

      for (int i = 1; i < this.diagram.objectsSize(); i++)
      {
        int y = Image.ARROW_YSTART + 
        (this.objLevels[i]*Image.ARROW_DISTANCE)-Image.ARROW_DISTANCE;
        this.epsText += "black\n";
        this.epsText += "newpath\n";
        this.epsText += (this.objPositions[i]+50)+" "+
                                        (this.screenHeight-(OBJECT_YSTART+1))+" moveto\n";
        this.epsText += (this.objPositions[i]+50)+" "+
                                        (this.screenHeight-(y+5))+" lineto\n";
        this.epsText += "closepath\n";
        this.epsText += "stroke\n";
      }
   }
   
   /**
    * Draw Diagram Objects on EPS Image Format (Text Commands)
    */
   private void epsDrawObjects()
   {
      int x = Image.OBJECT_XSTART;
      int distance = Image.OBJECT_DISTANCE;
      ArrayList<ObjectData> objs = this.diagram.getObjects();
      int objSize = objs.size();
      //System.out.println("N Objectos: "+objSize);
      // aloca size elementos para o array de inteiros
      this.objPositions = new int[objSize];
      this.objLevels = new int[objSize];

      for (int i = 1; i < objSize; i++)
      {
    	  this.objLevels[i] = 0;
        this.objPositions[i] = x+(i*distance);
        this.epsDrawObject(this.objPositions[i], objs.get(i));
      }
   }
   
   /**
    * Draw Diagram Arrows
    * @param g Graphics 2D Device Image
    */
   private void drawArrows(Graphics2D g)
   {
      //ArrayList<Arrow> arrs = this.diagram.getArrows();
      for (int i = 0; i < this.diagram.arrowsSize(); i++)
      {
      ObjectData sender = this.diagram.arrowOfIndex(i).getSender();
      ObjectData receiver = this.diagram.arrowOfIndex(i).getReceiver();
      //String type = arrs.get(i).getTypeString();
      //String label = arrs.get(i).getLabel();
      
      int x1 = this.objPositions[sender.getId()-1];
      int x2 = this.objPositions[receiver.getId()-1];
      int y = Image.ARROW_YSTART + 
      	      (this.objLevels[sender.getId()-1]*Image.ARROW_DISTANCE);
      //System.out.println(y + " LABEL : "+ label);
 
      if (this.diagram.arrowOfIndex(i).isVisible()) // for SDControl feature
      this.drawArrow(g, x1, x2, y, this.diagram.arrowOfIndex(i) );

      // incrementa a posicao vertical dos objectos de onde sai a seta
      this.objLevels[sender.getId()-1] = i+1;
      // o proximo nivel do receiver sera sempre o do sender
      this.objLevels[receiver.getId()-1] = this.objLevels[sender.getId()-1];
      this.drawObjectsLines(g);
      
      }

   }
 
   /**
    * Draw Diagram Arrows on EPS Format (Text Commands)
    */
   private void epsDrawArrows()
   {
      //ArrayList<Arrow> arrs = this.diagram.getArrows();
      for (int i = 0; i < this.diagram.arrowsSize(); i++)
      {
      ObjectData sender = this.diagram.arrowOfIndex(i).getSender();
      ObjectData receiver = this.diagram.arrowOfIndex(i).getReceiver();
      //String label = arrs.get(i).getLabel();
      //String type = arrs.get(i).getTypeString();
      
      int x1 = this.objPositions[sender.getId()-1];
      int x2 = this.objPositions[receiver.getId()-1];
      int y = Image.ARROW_YSTART + 
      		(this.objLevels[sender.getId()-1]*Image.ARROW_DISTANCE);
      //System.out.println("LABEL : "+ label);

      if (this.diagram.arrowOfIndex(i).isVisible()) // for SDControl feature
      this.epsDrawArrow( x1, x2, y, this.diagram.arrowOfIndex(i));
      
      this.objLevels[sender.getId()-1] = i+1;
      this.objLevels[receiver.getId()-1] = this.objLevels[sender.getId()-1];
      this.epsDrawObjectsLines();
      }
   }

   /**
    * Paint Override Main Method
    * @param g Graphics 2D Device Reference
    */
   @Override public void paintComponent(Graphics g) {

       super.paintComponent(g); // clear and do default painting
       this.paint(g); // desenha todo o diagrama 

   }

   /**
    * awt paint() compatible function support
    * @param g Graphics 2D Device Reference
    */
   public void paint(Graphics g)
   {
      Graphics2D g2d = (Graphics2D) g;
      // Create a buffered image in which to draw
      //this.img = new BufferedImage(this.SCREEN_WIDTH, this.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
      this.img = (BufferedImage) this.createImage(this.screenWidth, this.screenHeight);
      // create Graphics Context from Image Type
      g2d = this.img.createGraphics();
      this.drawObjects(g2d);
      this.drawArrows(g2d);
      //g = (Graphics) g2d;
      g.drawImage(this.img,0,0,this); // desenha a imagem no ambiente grafico
      g2d.dispose();
      
   }
   

   /**
    * Save Diagram Image to JPEG File
    * @param filename
    *        name for the file
    */
   public void saveJPEG(String filename)
   {
      try {
        // Save as JPEG
        File jpg = new File(filename);
        ImageIO.write(this.img, "jpeg", jpg);
      } catch (IOException e) {
    	  System.out.println("(Image.JPEG) ERROR: " + e.toString());
      }	  
   }
  
    /**
     * Generate Commands for the Diagram Image on EPS Format
     * @return String
     *         EPS Text Commands
     */
    public String createEPS()
    {
      this.epsDrawObjects();
      this.epsDrawArrows();
      return this.epsText;
    }

// Scrollable abstract methods 
/*    
    public void setBounds( int x, int y, int width, int height ) {
      super.setBounds( x, y, getParent().getWidth(), height );
    }
 
    public Dimension getPreferredSize() {
      return new Dimension( getWidth(), getPreferredHeight() );
    }
 
    public Dimension getPreferredScrollableViewportSize() {
      return super.getPreferredSize();
    }
 
    public int getScrollableUnitIncrement( Rectangle visibleRect, int orientation, int direction ) {
      int hundredth = ( orientation ==  SwingConstants.VERTICAL
          ? getParent().getHeight() : getParent().getWidth() ) / 100;
      return ( hundredth == 0 ? 1 : hundredth );
    }
 
    public int getScrollableBlockIncrement( Rectangle visibleRect, int orientation, int direction ) {
      return orientation == SwingConstants.VERTICAL ? getParent().getHeight() : getParent().getWidth();
    }
 
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
 
    public boolean getScrollableTracksViewportHeight() {
      return false;
    }
 
    private int getPreferredHeight() {
      int rv = 0;
      for ( int k = 0, count = getComponentCount(); k < count; k++ ) {
        Component comp = getComponent( k );
        Rectangle r = comp.getBounds();
        int height = r.y + r.height;
        if ( height > rv )
          rv = height;
      }
      rv += ( (FlowLayout) getLayout() ).getVgap();
      return rv;
    }	  
  */  
    
}

