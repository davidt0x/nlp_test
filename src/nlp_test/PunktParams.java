package nlp_test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This simple class stores parameters for the Punkt sentence boundary detection
 * algorithm. This algorithm will be the result of unsupervised training of the
 * Punkt algorithm and it contains the key variables for sentence boundary 
 * detection.
 * 
 * @author Dave Turner
 *
 */
public class PunktParams {

	/*
	 * This list keeps track of known abbreviations.
	 */
	protected HashSet<String> abbrevs = new HashSet<String>();

	/*
	 * A list of string tuples that denote common collocations
	 * where the first word ends in period. E.g. ('S.', 'Bach')
	 * This counts as negative evidence for a sentence boundary.
	 */
	protected HashSet<Tuple> collocs = new HashSet<Tuple>();

	/*
	 * A set of words that commonly appear as sentence starters.
	 */
	protected HashSet<String> sent_starters = new HashSet<String>();

	/*
	 * This hash map keeps track of orthographic context for 
	 * words. We store the multiple states as Long flags.
	 */
	protected HashMap<String, Long> ortho_context = new HashMap<String, Long>();
	
	/**
	 * Get the set of common collocations
	 * @return The set.
	 */
	public HashSet<Tuple> getCollocations()
	{
		return collocs;
	}
	
	/**
	 * Get the set of common abbreviations.
	 * @return The set.
	 */
	public HashSet<String> getAbbreviations()
	{
		return abbrevs;
	}
	
	/**
	 * Get the set of sentence starter tokens.
	 * @return The set.
	 */
	public HashSet<String> getSentenceStarters()
	{
		return sent_starters;
	}
	
	/**
	 * Clear the list of abbreviations.
	 */
	public void clearAbbreviations()
	{
		this.abbrevs = new HashSet<String>();
	}
	
	/**
	 * Clear the list of collocations.
	 */
	public void clearCollocations()
	{
		this.collocs = new HashSet<Tuple>();
	}
	
	/**
	 * Clear the list of sentence starters.
	 */
	public void clearSentenceStarters()
	{
		this.sent_starters = new HashSet<String>();
	}
	
	/**
	 * Clear the orthographic context hash map.
	 */
	public void clearOrthoContext()
	{
		ortho_context = new HashMap<String, Long>();
	}
	
	/**
	 * Check to see if a string matches our abbreviations list. Ignore case.
	 * Assumes that trailing periods have been removed from token.
	 * @param val The string to test. No trailing period.
	 * @return true if it is and abbreviation, false if not.
	 */
	public boolean isAbbreviation(String val)
	{
		return(abbrevs.contains(val.toLowerCase()));
	}
	
	/**
	 * Is the string a sentence starter.
	 * 
	 * @param val String to check.
	 * @return True if yes, false if no.
	 */
	public boolean isSentenceStarter(String val)
	{
		return(sent_starters.contains(val.toLowerCase()));
	}
	
	/**
	 * Is the string Tuple a common collocation.
	 * 
	 * @param val Tuple to check.
	 * @return True if yes, false if no.
	 */
	public boolean isCollocation(String val)
	{
		return(collocs.contains(val));
	}
	
	/**
	 * Get the current orthographic context flags for a given
	 * string.
	 * @param val The string to check.
	 * @return This flags expressed as a long value.
	 */
	public long getOrthoContext(String val)
	{
		Long flags = ortho_context.get(val);
		
		if(flags == null)
			return 0;
		else
			return flags;
	}
	
	/**
	 * Add an orthographic context flag for a specific word.
	 * @param val The word to change.
	 * @param flag The flag to add. This values should come from OrthoGraphFlag.
	 */
	public void addOrthoContext(String val, Long flag)
	{
		// Get the current value if it exists
		Long flags = ortho_context.get(val);
		
		if(flags == null)
			flags = 0L;
		
		flags |= flag;
		
		ortho_context.put(val, flags);
	}
	
	
	public PunktParams()
	{
		
	}
	
}

