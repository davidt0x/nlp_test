package nlp_test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a very simple class that encapsulates a list of tokens into a 
 * sentence. 
 * 
 * @author Dave Turner
 *
 */
@XmlRootElement
public class Sentence {

	/*
	 * The list of tokens in this sentence.
	 */
	protected List<Token> tokens = new ArrayList<Token>();
	
	/*
	 * A string representation for this sentence. 
	 */
	protected String stringRep = null;
	
	/**
	 * Create an empty sentence.
	 */
	public Sentence()
	{
		
	}
	
	/**
	 * Add a token to this sentence.
	 * 
	 * @param token
	 */
	public void addToken(Token token)
	{
		tokens.add(token);
	}

	/**
	 * Get the list of tokens in this sentence.
	 * 
	 * @return
	 */
	@XmlElement(name="token")
	public List<Token> getTokens()
	{
		return tokens;
	}
	
	/**
	 * Set the string representation for this sentence.
	 * 
	 * @param sentString
	 */
	public void setStringRep(String sentString) {
		stringRep = sentString;
	}
	
	public String toString()
	{
		if(stringRep != null)
			return stringRep;
		
		// If a string representation hasn't been set then make one. This is
		// bad because original whitespace will be all wrong.
		String tmp = "";
		for(Token tk: tokens)
			tmp += tk.getValue() + " ";
		
		return tmp;
	}
}
