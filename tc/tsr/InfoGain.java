/** 
 * Term Space Reduction by Information Gain.
 * (see dimreduct-4up.pdf and termextract-4up.pdf)
 * (S. Luz, luzs@cs.tcd.ie)
 **/ 
package tc.tsr;
import tc.parser.*;
import tc.dstruct.*;
import tc.util.*;
import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;

/**
 * 
 * 
 * @author  S. Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: InfoGain.java,v 1.5 2004/03/29 14:29:33 luzs Exp $</font>
 * @see  GenerateARFF
*/

public class InfoGain extends TermFilter
{

  public  InfoGain(ProbabilityModel pm){
    super(pm);
  }

  /** ********** Lab 02 exercise: *************** 
   *
   * Implement a method to calculate and return (as a double-precision
   * integer) the information gain for a given term and category.
   */
  public double computeLocalTermScore(String term, String cat){
    return 1;
  }
}

