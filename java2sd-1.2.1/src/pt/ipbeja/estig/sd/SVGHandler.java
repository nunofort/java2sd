package pt.ipbeja.estig.sd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * XML Handler to Parse SVG XML format, modify and save it again
 * 
 * @author Nuno Fortes
 * @version 1.0
 */

public class SVGHandler extends DefaultHandler
{
  private static String TITLE = "Sequence Diagram";
  private static String DESC = "This was produced by Java2SD Application";
  private String title = "";
  private String desc = "";
  private String text = "";
  private String transform = "";
  private String style = "";
  private String element = "";

  private BufferedWriter writer; // buffer to write the new file

  private Diagram diagram;

  private String docPath = "../doc";
  private String rootPath = ".."; // directorio principal do programa

  /**
   * Set Sequence Diagram information
   * @param dia
   *        Sequence Diagram reference
   * @param docPath
   *        javadoc path for the host application
   * @param outfile
   *        SVG file name to write the output
   */
  public void setDiagram(Diagram dia, String docPath, String outfile)
  {
    this.diagram = dia;
    this.docPath = docPath;
    // set output stream
    try
    {
      this.writer = new BufferedWriter( new FileWriter(outfile) );
    }
    catch (IOException e)
    {
      System.out.println("ERROR: " + e.toString());
    }
  }

  public void startDocument()
  {
    System.out.println("Modifying XML ...Adding Links\n");
    try
    {
/*
<?xml version="1.0" standalone="no"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN"
"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
*/
/*
<svg width="100" height="30" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" >
<a xlink:href="http://www.zvon.org" xlink:type="simple">
<text font-family="SansSerif" x="0" y="20">Go to ZVON</text>
</a>
</svg>
*/
      this.writer.write(
      "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n" +
      "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20000303 Stylable//EN\"\n" +
      "\"http://www.w3.org/TR/2000/03/WD-SVG-20000303/DTD/svg-20000303-stylable.dtd\">\n");
      //"\""+this.rootPath+"/svg11.dtd\" [\n"+
      //"<!ATTLIST svg xmlns:xlink CDATA #FIXED \"http://www.w3.org/1999/xlink\">\n"+
      //"<!ATTLIST svg xmlns:xlink CDATA #FIXED \""+this.rootPath+"/xlink\">\n"+
      //"]>\n");

    }
    catch (IOException e)
    {
      System.out.println("ERROR: " + e.toString());
    }
  }

  public void startElement(String namespace, String localName, String qName,
      Attributes atts)
  {
    try
    {
      if (localName.equals("svg"))
      {
        //this.writer.write("<svg width=\"100%\" height=\"100%\" version=\"1.1\" " +
        //"xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n");

        this.writer.write("<svg width=\"" + atts.getValue("width") +
        "\" height=\"" + atts.getValue("height") + "\" viewBox=\"" +
        atts.getValue("viewBox") + "\" preserveAspectRatio=\"" +
        atts.getValue("preserveAspectRatio") + "\" " +
        "xmlns=\"http://www.w3.org/2000/svg\" " + 
        //">\n");
        "xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n");
        //"xmlns:xlink=\""+this.rootPath+"/xlink\">\n");
      }
      if (localName.equals("rect"))
      {
        this.writer.write("<rect x=\"" + atts.getValue("x") + "\" y=\""
            + atts.getValue("y") + "\"" + " width=\"" + atts.getValue("width")
            + "\"" + " height=\"" + atts.getValue("height") + "\""
            + " style=\"" + atts.getValue("style") + "\"/>\n");
      }

      if (localName.equals("g"))
      {
        String style = atts.getValue("style"); // for fixing possible bug?!
        this.writer.write("<g transform=\""
            + atts.getValue("transform") + "\" xml:space=\""
            + atts.getValue("xml:space") + "\" style=\""
            + style.replace("fill-rule:even-odd;", "fill-rule:evenodd;") + "\">\n");
      }
      // <circle cx="1.4" cy="0.19" r="0.06" style="stroke-width:0.0094118;"/>
      if (localName.equals("circle"))
      {
        this.writer.write("<circle cx=\"" + atts.getValue("cx")
            + "\" cy=\"" + atts.getValue("cy") + "\" r=\"" + atts.getValue("r")
            + "\" style=\"" + atts.getValue("style") + "\"/>\n");
      }
      if (localName.equals("line"))
      {
        this.writer.write("<line x1=\"" + atts.getValue("x1")
            + "\" y1=\"" + atts.getValue("y1") + "\" x2=\""
            + atts.getValue("x2") + "\" y2=\"" + atts.getValue("y2")
            + "\" style=\"" + atts.getValue("style") + "\"/>\n");
      }
      if (localName.equals("text"))
      {
        this.transform = atts.getValue("transform");
        this.style = atts.getValue("style");

      }
      if (localName.equals("polyline"))
      {
        this.writer.write("<polyline points=\""
            + atts.getValue("points") + "\" style=\"" + atts.getValue("style")
            + "\"/>\n");

      }
      if (localName.equals("polygon"))
      {
        this.writer.write("<polygon points=\""
            + atts.getValue("points") + "\" style=\"" + atts.getValue("style")
            + "\"/>\n");

      }
    }
    catch (IOException e)
    {
      System.out.println("ERROR: " + e.toString());
    }
    this.element = localName;
  }

  public void endElement(String namespace, String localName, String qName)
  {
    try
    {
      if (localName.equals("title"))
      {
        this.writer.write("<title> " + SVGHandler.TITLE + " </title>\n");
        this.title = "";

      }
      if (localName.equals("desc"))
      {
        this.writer.write("<desc> " + SVGHandler.DESC + " </desc>\n");
        this.desc = "";
      }
      if (localName.equals("text"))
      {
        String found = "";
        // se o conteudo do texto for um objecto
        if ((this.text.length() > 6)
            && (this.text.trim().substring(0, 3).equals("obj")))
        {
          String[] tokens = this.text.trim().split(":");
          String xName = "";
          
          if (tokens.length > 1) xName = tokens[1]; else xName = tokens[0];
          
          int oid = this.diagram.foundObjectID(tokens[0]);
          // se existe o objecto com o nome que esta na tag text
          if (oid != -1) {
            // guarda o caminho completo da classe do objecto na variavel
            found = this.diagram.objectOfIndex(oid).getClassPath()
                // substitui os . no pacote pela / das directorias 
                .replace('.', '/');
            found += ".html";
            // guarda somente o nome da classe na variavel
            xName = this.diagram.objectOfIndex(oid).getClassName();
          }
          /*
          for (int i = 0; i < this.diagram.objectsSize(); i++) { 
            if (xName.equals(this.diagram.objectOfIndex(i).getClassName())) { 
              found = this.diagram.objectOfIndex(i).getClassPath(). replace('.','/'); 
              found += ".html"; 
              break; 
            } 
          }*/
           
          System.out.println("-----> " + xName + " : " + found);
          // link element
          this.writer.write("<a xlink:href=\""+this.docPath+"/" + found + "\" "
                   + "xlink:title=\"" + xName + "\" xlink:type=\"simple\"" 
                          + " target=\"_blank\">\n");
        }
        // original TEXT
        this.writer.write("<text transform=\"" + this.transform
                                        + "\" style=\"" + this.style + "\">");
        this.writer.write(this.text + "</text>\n");
        // END link element
        if (found.length() > 0) {
          this.writer.write("</a>\n");
        }
        this.text = "";
      }
      if (localName.equals("g"))
      {
        this.writer.write("</g>\n");
      }
      if (localName.equals("svg"))
      {
        this.writer.write("</svg>\n");
      }
    }
    catch (IOException e)
    {
      System.out.println("ERROR: " + e.toString());
    }
  }

  public void characters(char[] ch, int start, int length)
  {
    String s = new String(ch);
    if (this.element.equals("title"))
    {
      this.title += s.substring(start, start + length);
    }
    if (this.element.equals("desc"))
    {
      this.desc += s.substring(start, start + length);
    }
    if (this.element.equals("text"))
    {
      this.text += s.substring(start, start + length);
    }
  }

  public void process(String input)
  {
    try
    { // tmp.svg
      System.out.println("Processing XML file : " + input);
      try {

        // Verificar se o ficheiro XML existe
        FileInputStream fis = new FileInputStream(input);
        fis.close();
      } catch (IOException e) {
        System.err.println("File not Found : " + input);
        System.exit(1);
      }
      // Preparar para o parsing do XML
      SAXParserFactory spf = SAXParserFactory.newInstance();
      SAXParser sp = spf.newSAXParser();
      ParserAdapter pa = new ParserAdapter(sp.getParser());
      pa.setContentHandler(this);
      pa.parse(input);

    }
    catch (Exception e)
    {
      System.out.println("(SVGHandler.process) ERROR: " + e.toString());
    }
    finally
    {
      try
      {
        this.writer.close();
      }
      catch (Exception e)
      {
        ;
      }
    }
  }
}



