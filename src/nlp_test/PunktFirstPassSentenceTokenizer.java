package nlp_test;

import java.util.Arrays;

/**
 * 
 * This class is just an implementation of the first pass portion of 
 * the Punkt sentence tokenization algorithm. That is, it requires 
 * no training. This is a simple sentence tokenizer that handles 
 * certain special cases like ellipsis, abbreviations (by dictionary).
 * This class contains a small simple dictionary of common abbreviations
 * but it is not comprehensive. A more robust dictionary can be made
 * by using the full Punkt algorithm with unsupervised training in
 * PunktTrainer and PunktSentenceTokenizer.
 * 
 * @author Dave Turner
 *
 */
public class PunktFirstPassSentenceTokenizer extends PunktAlgoBase {

	/*
	 * A list of common abbreviations I came up. 
	 */
	private String[] abbreviations = {
			"mrs", "mr", "dr", "ms", "inc", 
			"corp", "ave", "st", "dept", "univ"};
	
	public PunktFirstPassSentenceTokenizer()
	{
		super();
		
		// Setup the abbreviations
		params.getAbbreviations().addAll(Arrays.asList(abbreviations));
	}
	
}
