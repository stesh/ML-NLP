package tc.parser;
import tc.util.*;
import java.util.Vector;
import java.util.Enumeration;
import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * * 4ict2 lab 01: Implement BasicHandler 
 *
 * see : http://java.sun.com/webservices/docs/1.4/api/org/xml/sax/HandlerBase.html
 *  for HandlerBase API specification
 *
 * HandlerBase methods do nothing by default
 * For this lab, you need to customize the methods startElement(), characters()
 * and endElement () in order to parse News Items and extract 
 * the following pieces of information : 
 * 	- Topics
 *	- Title
 *               - Body (text content)
 *
 * and print them on standard output (screen)
 * 
 * @author  ...
 * @see  
*/


public class BasicHandler extends HandlerBase 
{

  private int error = 0;

  /*
    In order to do the exercise you will need to implement the following methods: 
   startElement, endElement and characters.
  */
  public void startElement (String name, AttributeList atts)
  {
  }
  
  public void endElement (String name)
  {
  }
  
  public void characters (char ch[], int start, int length)
  { 
  }
  
  /**
   * Print a message for ignorable whitespace.
   *
   * @see org.xml.sax.DocumentHandler#ignorableWhitespace
   */
  public void ignorableWhitespace (char ch[], int start, int length)
  {
    //System.out.print("Ignorable Whitespace found ");
  }
  
  /**
   * Report all warnings, and continue parsing.
   *
   * @see org.xml.sax.ErrorHandler#warning
   */
  public void warning (SAXParseException exception)
  {
    error++;
    System.err.print("Warning: " +
                   exception.getMessage() +
                   " (" +
                   exception.getSystemId() +
                   ':' +
                   exception.getLineNumber() +
                   ',' +
                   exception.getColumnNumber() +
                   ')' + "\n");
  }
  
  
  /**
   * Report all recoverable errors, and try to continue parsing.
   *
   * @see org.xml.sax.ErrorHandler#error
   */
  public void error (SAXParseException exception)
  {
    error++;
    System.err.print("Recoverable Error: " +
                   exception.getMessage() +
                   " (" +
                   exception.getSystemId() +
                   ':' +
                   exception.getLineNumber() +
                   ',' +
                   exception.getColumnNumber() +
                   ')' + "\n");
  }
  
  /**
   * Report all fatal errors, and try to continue parsing.
   *
   * <p>Note: results are no longer reliable once a fatal error has
   * been reported.</p>
   *
   * @see org.xml.sax.ErrorHandler#fatalError
   */
  public void fatalError (SAXParseException exception)
  {
    error++;
    System.err.print("Fatal Error: " +
                   exception.getMessage() +
                   " (" +
                   exception.getSystemId() +
                   ':' +
                   exception.getLineNumber() +
                   ',' +
                   exception.getColumnNumber() +
                   ')' + "\n");
  }
  

}





