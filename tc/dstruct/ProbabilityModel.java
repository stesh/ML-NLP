package tc.dstruct;
import tc.tsr.*;
import tc.parser.*;
import tc.util.*;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Iterator;
/**
 *  Store inverted indices of terms and categories (indexed to
 *  documents) which form the basis of a probability model (see tTable
 *  and cTable vars below), and implement methods to estimate
 *  probabilities based on these indices.
 *
 * @author  S Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: ProbabilityModel.java,v 1.6 2004/03/29 14:11:18 luzs Exp $</font>
 * @see  
*/
public class ProbabilityModel implements Serializable {

  private boolean ignoreCase = true;
  /** term table:  ( term1 , [(doc_id1, No_of_occurrences_of_term1_in_id1), ..., (doc_idn, No_of_occurrences_of_term1_in_idn)],
   *                   ...
   *                     termk , [(doc_idm, No_of_occurrences_of_termk_in_idm), ..., (doc_idz, No_of_occurrences_of_termk_in_idz)])
   * where term is a String and the elements of its Map value are No. of occurences, indexed by doc_id's.
   */
  private Map tTable = new HashMap();

  /** category table:  ( cat1 , [id1, ..., idn]
   *                            ...
   *                            catk , [id1, ..., idn] )
   *where cat (the key) is a String and the elements of its HashSet values are Strings (the same type as IDs, above)
   */
  private Map cTable = new HashMap();
  /** store the set of documents that make up this ProbabilityModel.
   */
  private Set docSet = new HashSet();
  public int corpusSize = 0;


  public ProbabilityModel(){
  }

  public ProbabilityModel (ParsedText pt, StopWordList swlist){
    addParsedText(pt, swlist);
  }


  /**   ********************  Lab 02: Exercise *********************
   *
   *   Implement addParsedText.  This method will: receive a
   *   ParsedText, tokenise each text it contains, index the resulting
   *   terms (except the ones contained in the StopWordList), and
   *   store the text's category(ies). See also addParsedNewsItem, which
   *   will actually do the indexing.
   */
  public void addParsedText (ParsedText pt, StopWordList swlist)
  {

  }

  /**  ********************  Lab 02: Exercise *********************
   *
   *  Lab 02: tokenise pni, and index its terms (except the ones
   *  contained in swlist) and categories, that is: put each term
   *  (word) into tTable and each category the document belongs to
   *  into cTable. As each document is processed, store its ID into
   *  docSet, so as to keep track of which documents where used in
   *  generating this ProbabilityModel.
   *
   *  TIP: Use BagOfWords for tokenising pni. 
   */
  public void addParsedNewsItem (ParsedNewsItem pni, StopWordList swlist){

  } 

  // return set containing all categories that occur in the corpus
  public Set getCategorySet (){
    return cTable.keySet();
  }

  public Set getDocSet (){
    return docSet;
  }

  /** 
   * Add file (id) to the set of files categorised as cat
   * Return true if cat is new to cTable, false otherwise.
   */
  private Object putIntoTTable(String term, String id, Integer count){
    Map idset = (Map)tTable.get(term);
    if (idset == null)
      idset = new HashMap();
    idset.put(id, count);
    return tTable.put(term,idset);
  }

  /** 
   * Add file (id) to the set of files categorised as cat
   * Return true if cat is new to cTable, false otherwise.
   */
  private Object putIntoCTable(String cat, String id){
    Set idset = (Set)cTable.get(cat);
    if (idset == null)
      idset = new HashSet();
    idset.add(id);
    return cTable.put(cat,idset);
  }

  public boolean containsTerm(String term){
    return tTable.containsKey(term);
  }

  /**  ********************  Lab 02: Exercise *********************
   *
   *   Calculate (and return) the generality of category 'cat' with respect to this model
   */
  public double getCatGenerality(String cat){
    // *** this return statement is just a place holder. you'll need to modify it
    return 0;
  }

  /**  ******************** Lab 02: Exercise *********************
   *
   *   Estimate the prior probabilities of term and cat, the joint
   *   probabilities for each (Boolean) value combination for these
   *   two variables (as defined in class Probabilities) and return a
   *   Probabilities object.
   */
  public Probabilities getProbabilities(String term, String cat){
    // *** this return statement is just a place holder. you'll need to modify it
    return new Probabilities(1,1,1,1,1,1);
  }

  public int getTermSetSize(){
    return tTable.size();
  }

  /**
   * Delete all entries for terms not in the reduced term set 
   */
  public void trimTermSet(Set rts){
    tTable.keySet().retainAll(rts);
  }

  /**
   * Delete all entries for terms not in the reduced term set 
   */
  public void trimTermSet(WordFrequencyPair[] rts){
    tTable.keySet().retainAll(BagOfWords.extractTermCollection(rts));
  }


  public int getCategSetSize(){
    return cTable.size();
  }

  //  Return the number of terms in id
  public int getCount(String id, String term){
    Integer count = (Integer) ((Map)tTable.get(term)).get(id);
    if (count == null)
      return 0;
    else
      return count.intValue();
  }

  // return the vector of categories to which document id belongs 
  // (shouldn't this return a set instead?)
  public Vector getCategVector(String id){
    Vector cv = new Vector();
    for (Iterator e = cTable.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        if ( ((HashSet)kv.getValue()).contains(id) )
          cv.add(kv.getKey());
      }
    return cv;
  }

  // make a new wsp[] with scores initialised to zero
  public WordScorePair[] getBlankWordScoreArray(){
    WordScorePair [] wsp = new WordScorePair[tTable.size()];
    int i = 0;
    for (Iterator e = tTable.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        wsp[i++] = new WordScorePair((String) kv.getKey(), 
                                     0);
      }
    return wsp;
  }

  // gets an initialised wsp and populate it with global term frequency
  public WordScorePair[] setFreqWordScoreArray(WordScorePair[] wsp){
    int i = 0;
    for (Iterator e = tTable.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        wsp[i++] = new WordScorePair((String) kv.getKey(), 
                                     (double) ((HashMap)kv.getValue()).size());
      }
    return wsp;
  }

  public WordScorePair[] getWordScoreArray(){
    WordScorePair [] wsp = new WordScorePair[tTable.size()];
    int i = 0;
    for (Iterator e = tTable.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        wsp[i++] = new WordScorePair((String) kv.getKey(), 
                                     (double) ((HashMap)kv.getValue()).size());
      }
    return wsp;
  }

  // number of files a term occurs in
  public int getTermCount(String term){
    return ((Map)tTable.get(term)).size();
  }

  public boolean occursInCategory(String term, String cat){
    Set ds = ((Map)tTable.get(term)).keySet();
    Set cs = (Set)cTable.get(cat);
    for (Iterator i = ds.iterator(); i.hasNext(); ) 
      if (cs.contains(i.next()))
        return true;
    return false;
  }

  /**
   * Get the value of ignoreCase.
   * @return value of ignoreCase.
   */
  public boolean isIgnoreCase() {
    return ignoreCase;
  }
  
  /**
   * Set the value of ignoreCase.
   * @param v  Value to assign to ignoreCase.
   */
  public void setIgnoreCase(boolean  v) {
    this.ignoreCase = v;
  }
  

}
