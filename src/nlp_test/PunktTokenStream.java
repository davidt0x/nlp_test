package nlp_test;

import java.util.regex.Matcher;

/**
 * This class allows us to iterate through a piece of text and keep track of
 * the current and previous token. This class uses PunktLanguageVariables and
 * the regular expression within it to process text into a stream of tokens.
 * 
 * @author Dave Turner
 *
 */
public class PunktTokenStream implements TokenStream  {

	/*
	 * This text we are processing
	 */
	private String text;
	
	/*
	 * Keep a copy of our string split by lines
	 */
	private String[] lines;
			
	/*
	 * What line number are we on?
	 */
	int currLineNum = 0;
	
	/*
	 * Where in the original string are we?
	 */
	int currPos = 0;
	
	/*
	 * Where are we within the current line we are processing?
	 */
	int currPosInline = 0;
	
	/*
	 * Where was the previous match within the current line?
	 */
	int prevPosInline = 0;
	
	/*
	 * The matcher for word tokens built by langVars
	 */
	private Matcher wordMatcher; 
	
	/*
	 * Language specific variables for word tokenization and more.
	 */
	private PunktLanguageVariables langVars;
	
	/*
	 * At all time we keep track of the previous, current, and next token in
	 * the stream.
	 */
	Token prev = null;
	Token curr = null;
	Token next = null;
	
	private void generalInit()
	{
		// Break our text into lines and process each individually.
		lines = text.split("\n");
		
		if(lines.length == 0)
			langVars.getWordTokenizePattern().matcher(text);
			
		// Setup the matcher for the first line
		wordMatcher = langVars.getWordTokenizePattern().matcher(lines[0]);
		
		currLineNum = 0;
		currPos = 0;
		currPosInline = 0;
		prevPosInline = 0;
	}
	
	public PunktTokenStream(String text, PunktLanguageVariables langVars)
	{
		this.langVars = langVars;
		this.text = text;
		
		generalInit();		
	}
	
	public PunktTokenStream(String text)
	{
		// Use the default english language variables
		this.langVars = new PunktLanguageVariables();

		this.text = text;
		
		generalInit();
	}
	
	/*
	 * Are we currently on a paragraph start state. This means we encountered
	 * a blank line. We need to keep track of this so we can annotate the next
	 * token as a paragraph start or not.
	 */
	private boolean isParaStart = false;
	
	/*
	 * Are we currently on a line start state. 
	 * We need to keep track of this so we can annotate the next
	 * token as a line starter or not.
	 */
	private boolean isLineStart = false;
	
	/* (non-Javadoc)
	 * @see nlp_test.TokenStream#getToken()
	 */
	public Token getToken()
	{
		boolean matchFound = wordMatcher.find();
		
		// If we have no matches left on the current line, then move to the next
		while(!matchFound)
		{
			// Move to the next line
			currLineNum++;
			isLineStart = true;
			
			// Add two to the position because we consumed an endline when splitting the lines
			currPos+=2;
			currPosInline=0;
			prevPosInline=0;
			
			// If we have reached the end of the lines list. We are done.
			if(currLineNum == lines.length)
			{
				prev = curr;
				curr = null; 
				return curr;
			}
			
			// Skip blank lines. But keep track of when we do because the next token
			// will be annotated with a paragraph start tag
			while(lines[currLineNum].trim().length() == 0)
			{
				currPos+=lines[currLineNum].length()+1;
				currLineNum++;
				isParaStart = true;
				
				// If we have reached the end of the lines list. We are done.
				if(currLineNum == lines.length)
				{
					prev = curr;
					curr = null; 
					return curr;
				}
			}
			
			// Create a matcher for the new current line
			wordMatcher = langVars.getWordTokenizePattern().matcher(lines[currLineNum]);
			
			// Check if we have a match
			prevPosInline = currPosInline;
			matchFound = wordMatcher.find();
			currPosInline = wordMatcher.start();
		}

		// If the above loop did not lead to a return then we know we
		// have a match. Move the current position to offset of the match
		prevPosInline = currPosInline;
		currPosInline = wordMatcher.start();
		currPos += (currPosInline - prevPosInline);
		
		// Grab the match and its start index.
		String currTok = wordMatcher.group();
		
		// Make the current guy, the new previous guy
		prev = curr;
		
		// Create a new token from the current token object string
		curr = new Token(currTok);
		curr.setIsParaStart(isParaStart);
		curr.setIsLineStart(isLineStart);
		isLineStart = false;
		isParaStart = false;
		curr.setPosition(currPos);
	
		// Make sure we got the correct token position
		String checkText = text.substring(currPos, currPos+currTok.length());
		if(!checkText.equals(currTok))
		{
			System.err.println("Tokenization error at index " + currPos);
		}
	
		return curr;
	}
	
	/* (non-Javadoc)
	 * @see nlp_test.TokenStream#getPreviousToken()
	 */
	public Token getPreviousToken()
	{
		return prev;
	}

	/* (non-Javadoc)
	 * @see nlp_test.TokenStream#getText()
	 */
	public String getText() {
		return text;
	}
	
	
}
