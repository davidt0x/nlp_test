package nlp_test;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This is a simple little class that contains a list of
 * sentences.
 * 
 * @author Dave Turner
 *
 */
@XmlRootElement
@XmlSeeAlso(Sentence.class)
public class SentenceList extends AbstractList<Sentence> {

	 /*
     * This annotated string representation for this sentence list.
     */
    protected String stringRep;
    
    /**
     * Get the annotated string representation of this sentence list.
     * @return
     */
    @XmlElement(name="stringAnnotated")
    public String getStringRepresentation()
    {
    	if(stringRep == null)
    		stringRep = toString();
    	
    	return stringRep;
    }
	
	@XmlElement(name="sentence")
    private List<Sentence> list = new ArrayList<Sentence>();
	
	@Override
    public Sentence get(int index) {
	    return list.get(index);
    }

	@Override
    public Sentence set(int index, Sentence sent) {
        return list.set(index, sent);
    }
	
    @Override
    public boolean add(Sentence e) {
        return list.add(e);
    }

    @Override
    public int size() {
        return list.size();
    }
    
    /**
     * Create a sentence list object from a collections of sentences.
     * 
     * @param list
     * @param stringRep
     */
    public SentenceList(List<Sentence> list)
    {
    	this.list = list;
    }
    
    /**
     * Create and empty sentence list.
     */
    public SentenceList()
    {
   
    }
   
    public String toString()
    {
    	stringRep = "";
    	for(Sentence s: list)
    		stringRep += s + "\n";
    	
    	return stringRep;
    }
}
