/** 
 * 4ICT2, Lab 01: Parsing the REUTERS-21578 corpus
 *
 * --Saturnino Luz, luzs@cs.tcd.ie
 **/ 
package tc.parser;
import tc.dstruct.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Arrays;
import org.xml.sax.Parser;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.helpers.ParserFactory;
import org.xml.sax.InputSource;

/**
 * 
 * Read a REUTERS-21578 document (encoded in XML) and parse it, extract
 * text, title and categories and print these data on the standard output.
 * 
 * This program illustrates the use of the XML parser (xp.jar and
 * sax.jar), and auxiliary classes (package tsr) which you may want to
 * use later to implement your Term Space Reduction (feature
 * selection) algorithm, classifier etc.

 * @author  Saturnino Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: NewsParser.java,v 1.4 2004/03/19 15:48:08 luzs Exp $</font>
 * @see  IndentationHandler
*/
public class NewsParser
{


  String document = null;
  static Parser parser = null;

  /* ---- You have to implement this!! ---- */
  static ListTypeHandler handler = new ListTypeHandler();

  /** 
   *  Set up the main user interface items
   */
  public  NewsParser() {    
     super();
  }

  /** 
   * parseNews: Set up parser object, perform parsing of REUTERS-21578
   * documents, and print data onto STDOUT
   */
  public static void  setParser ()
    throws Exception
  {
    
    parser = ParserFactory.makeParser("com.jclark.xml.sax.Driver");
    parser.setDocumentHandler(handler);
    parser.setErrorHandler((ErrorHandler) handler);

  }

  public static ParsedText parseNews (String filename)
    throws Exception 
  {
    FileInputStream in = new FileInputStream(filename);
    InputSource source = new InputSource(in);
    source.setEncoding("ISO-8859-1");
    parser.parse(source);

    return handler.getParsedText();    
  }


  public static void main(String[] args) {
    try {
      NewsParser f = new NewsParser();
      setParser();
      ParsedText parsedText = parseNews(args[0]);
    
      for (Enumeration i = parsedText.elements() ; i.hasMoreElements() ;) {
        ParsedNewsItem n = (ParsedNewsItem)i.nextElement();
        System.out.println(n.getId());
        
        for (Enumeration j = n.getCategories() ; j.hasMoreElements() ;) {
          System.out.println(j.nextElement());
        }

        System.out.println(n.getText());
      }
    }
    catch (Exception e){
      System.err.println("tc.parser.NewsParser: ");
      System.err.println("Usage: NewsParser FILENAME");
      //e.printStackTrace();
    } 
  }
}
