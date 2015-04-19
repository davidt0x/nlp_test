package nlp_test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This base class implements methods common to both the Punkt
 * training algorithm and the tokenization algorithm. Principally,
 * this is the first pass annotation of a token stream.
 * 
 * @author Dave Turner
 *
 */
public class PunktAlgoBase implements SentenceTokenizer {

	/*
	 * Language specific variables for word tokenization and more.
	 */
	protected PunktLanguageVariables langVars;
	
	/*
	 * Parameters to user for sentence boundary detection.
	 */
	protected PunktParams params;
	
	public PunktAlgoBase()
	{
		this.langVars = new PunktLanguageVariables();
		
		// Load the default pre-trained Punkt paramerts
		this.params = new PunktParams();
	}
	
	public PunktAlgoBase(PunktParams params)
	{
		this.params = params;
		this.langVars = new PunktLanguageVariables();
	}

	public PunktAlgoBase(PunktLanguageVariables langVars)
	{
		this.langVars = langVars;
	}
	
	public PunktAlgoBase(PunktLanguageVariables langVars, PunktParams params)
	{
		this.langVars = langVars;
		this.params = params;
	}

	/* (non-Javadoc)
	 * @see nlp_test.SentenceTokenizer#annotate(nlp_test.PunktTokenStream)
	 */
	public ArrayList<Token> annotate(PunktTokenStream stream)
	{
		// We will just do simple first pass annotation.
		return first_pass_annotate(stream);
	}
	
	/**
	 * Perform a first pass annotation of a token stream. 
	 * 
	 * @param stream Stream to annotate.
 	 * @return The list of all tokens annotated.
	 */
	protected ArrayList<Token> first_pass_annotate(PunktTokenStream stream)
	{
		ArrayList<Token> tokList = new ArrayList<Token>();
		List<String> sentence_end_chars = Arrays.asList(langVars.getSentenceEndChars());
		
		
		Token token;
		while ( (token=stream.getToken()) != null )
		{
			String tokenVal = token.getValue();
			String tokenValChopLast = tokenVal.substring(0, tokenVal.length()-1).toLowerCase();
			
			// If the token is a sentence end character and it isn't,
			// an ellipsis, mark it as sentence break for now.
			if(!token.getIsEllipsis() && 
				sentence_end_chars.contains(tokenVal))
				token.setIsSentBreak(true);
			else if(tokenVal.endsWith(".") && !tokenVal.endsWith(".."))
			{
				// We need to check if hyphenated words end in an abbreviation
				// too.
				String [] splitByHyphen = tokenValChopLast.split("-");
				String afterHyphen = splitByHyphen[splitByHyphen.length-1];
				
				// Check for abbreviations, we have a list to check.
				if(params.isAbbreviation(tokenValChopLast) ||
				   params.isAbbreviation(afterHyphen))
					token.setIsAbbreviation(true);
				else
					token.setIsSentBreak(true);
			}
			
			// If we match a boundary realign token and the token was a sentence break
			// then lets realign the boundary.
			Matcher boundMatcher = langVars.getSentBoundRealignPattern().matcher(tokenVal);
			if(boundMatcher.find() && 
			   tokList.size() > 0 && 
			   tokList.get(tokList.size()-1).getIsSentBreak() )
			{
				Token lastToken =  tokList.get(tokList.size()-1);
				lastToken.setIsSentBreak(false);
				token.setIsSentBreak(true);
			}
			
			tokList.add(token);
		}
		
		return tokList;
	}

	/**
	 * This is a helper function that returns an original input text broken into 
	 * sentences. It does the job of collecting tokens into sentence objects and
	 * preserving the original whitespace of the text. This method is specific to 
	 * the implementation of our tokenizer because each tokenizer has different
	 * behaviors with stripping white space from tokens.
	 * 
	 * @param tokens The list of tokens corresponding to this text.
	 * @param text The original text before any tokenization.
	 * @return
	 */
	public SentenceList buildSentenceList(String text, ArrayList<Token> tokens) {
		ArrayList<Sentence> sents = new ArrayList<Sentence>();
	
		// Lets make a regex for whitespace
		Pattern ws_regex = Pattern.compile("\\s*");
		
		// Keep track of our position in the source text
		int pos = 0;
		
		// Current sentence
		Sentence sent = new Sentence();
		String sentString = "";
		
		for(Token tok: tokens)
		{
			String tokVal = tok.getValue();
			
			// Get whitespace before this token so we can update position
			Matcher ws_matcher = ws_regex.matcher(text);
			
			String ws = "";
			if(ws_matcher.find(pos))
				ws = ws_matcher.group();
			
			// Update the position by the length of the whitespace
			pos += ws.length();
			
			// Check to see if our token doesn't match the string text.
			// This can be because of the regex's we used stripping out 
			// whitespace.
			if(text.substring(pos, pos+tokVal.length()) != tokVal)
			{
				// Create a pattern that matches the preceding whitespace
				// and our token inclusive.
				String regex_patt = "\\s*" + Pattern.quote(tokVal);
				
				// Make a regex and extract the group and make this our new
				// token value. 
				Matcher m = Pattern.compile(regex_patt).matcher(text);
				
				if(m.find(pos))
					tokVal = m.group();
			}
			
			// Better be equal now or we are in trouble
			assert(text.substring(pos, pos+tokVal.length()+1) != tokVal);
			
			// Update the position to move to the end of token
			pos += tokVal.length();
						
			// If this token is not the start of a sentence, add the preceding whitespace
			// before the token.
			if(sentString != "")
				sentString+=ws;
			sentString+=tokVal;
				
			// Add this token to our sentence
			sent.addToken(tok);
			
			// If we are at a sentence break. Start a new sentence
			if(tok.getIsSentBreak())
			{
				sent.setStringRep(sentString);
				sents.add(sent);
				
				sent = new Sentence();
				sentString = "";
			}
		}
		
		// Discard the last sentence if it is empty
		if(sents.get(sents.size()-1).toString() == "")
			sents.remove(sents.size()-1);
		
		return new SentenceList(sents);
	}
	
}
