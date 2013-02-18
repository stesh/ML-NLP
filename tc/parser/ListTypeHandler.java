package tc.parser;
import tc.util.*;
import tc.dstruct.*;
import java.util.Vector;
import java.util.Enumeration;
import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * * 4ict2 lab 01: Implement ListTypeHandler 
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


public class ListTypeHandler extends HandlerBase 
{
  private ParsedText parsedText;

  private int error = 0;
  private ParsedNewsItem currentNewsItem;
  private boolean parsingTopics = false;
  private boolean parsingText = false;
  private String currentOpeningTag = "";
  private String currentClosingTag = "";

  public ListTypeHandler() {
    this.parsedText = new ParsedText();
  }
  
    
  /*
    In order to do the exercise you will need to implement the following methods: 
   startElement, endElement and characters.
  */
  public void startElement (String name, AttributeList atts)
  {
      currentOpeningTag = name;

      // Start of a news item
      if (currentOpeningTag.equals("REUTERS")) {
        currentNewsItem = new ParsedNewsItem();

        // Set the ID of the current news item
        currentNewsItem.setId(atts.getValue("NEWID"));
      }
    
      // Start of a block of topics
      if (currentOpeningTag.equals("TOPICS")) {
        parsingTopics = true;
      }

      // Start of the text content of a news item
      if (currentOpeningTag.equals("TEXT")) {
        parsingText = true;
      }
  } 
  
  public void endElement (String name)
  {
    currentClosingTag = name;
    
    // End of a news item
    if (currentClosingTag.equals("REUTERS")) {
      parsedText.addNewsItem(this.currentNewsItem);
    }

    // End of a block of topics
    if (currentClosingTag.equals("TOPICS")) {
      parsingTopics = false;
    }

    // End of the text content of a news item
    if (currentClosingTag.equals("TEXT")) {
      parsingText = false;
    }
  }
  
  private void print(char ch[], int start, int length) {
    for (int i = 0; i < length; i++) {
        System.out.print(ch[start+i]);
    }
    System.out.println();
  }

  public void characters (char ch[], int start, int length)
  {

    // Just parsed an individual topic
    if (parsingTopics && currentOpeningTag.equals("D")) {
      StringBuffer topic = new StringBuffer();
      for (int i = start; i < start+length; i++) {
        topic.append(ch[i]);
      }  
      currentNewsItem.addCategory(topic.toString());
    }
  
    // Just parsed the text content of a document
    if (parsingText) {
      if (currentOpeningTag.equals("BODY")) {
        StringBuffer text = new StringBuffer();
        for (int i = start; i < start+length; i++) {
          text.append(ch[i]);
        }
        currentNewsItem.setText(text.toString());
      }
    }
  } 

  public ParsedText getParsedText() {
    return this.parsedText;
  }
    
  /**
   * Print a message for ignorable whitespace.
   *
   //* @see org.xml.sax.DocumentHandler#ignorableWhitespace
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





