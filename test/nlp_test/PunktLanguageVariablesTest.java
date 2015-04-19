/**
 * 
 */
package nlp_test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nlp_test.PunktLanguageVariables;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Dave Turner
 *
 */
public class PunktLanguageVariablesTest {

	private PunktLanguageVariables punktLangVars = null;
	
	/**
	 * Setup the PunktLanguageVariablesTest by building the regex patterns
	 */
	@Before
    public void setUp() {
		punktLangVars = new PunktLanguageVariables();
		punktLangVars.getWordTokenizePattern();
    }


	/**
	 * Test o see if non word starting characters are detected properly.
	 */
	@Test
	public void checkNonWordStartingChars()
	{
		for(String ch : punktLangVars.getNonWordStartChars())
			assertTrue("Character " + ch + " is not excluded from starting a word.", 
					punktLangVars.isNonWordStartingChar(ch));
	}
	
	/**
	 * Test to see if non word characters are detected properly.
	 */
	@Test
	public void checkNonWordChars()
	{
		for(String ch : punktLangVars.getNonWordChars())
			assertTrue("Character " + ch + " is not excluded from being in a word.", 
					punktLangVars.isNonWordChar(ch));
	}
	
	/**
	 * Test to see if multi character punctuation like ellipses and
	 * hyphens are detected properly.
	 */
	@Test
	public void checkMultiCharPunctuation()
	{
		// Some test cases
		String [] multiCharPunct = {"...", ". . .", "--", "----"};
		
		for(String ch : multiCharPunct)
			assertTrue("Characters " + ch + " are not seen a multi-character punctuation.", 
					punktLangVars.isMultiCharPunct(ch));
	}
	
	
	/**
	 * This test just does a quick sanity check on word tokenization. It should not be
	 * considered comprehensive.
	 */
	@Test
	public void wordTokenizerRegexTest() {
		
		String[] testTokens = 
			{"This ", "is ", "a ", "test. ", "Mr. ", "Doe ", "went ", "to ", "the ", "mall", "! ", 
			  "He ", "said", ",", " \"", "It", "'s ", "a ", "good ", "film", "?", "\"",
			  "+1235 ", "Test-Hyphen ", "... ", ". . .", "Test-Double-Hyphen", "--"
			};
		
		// Make a test sentence from these tokens
		String testString = "";
		for(String t: testTokens)
			testString = testString + t;
		
		Matcher m = punktLangVars.getWordTokenizePattern().matcher(testString);
		
		// Find all the matches for our test string. Make sure it is correct.
		List<String> allMatches = new ArrayList<String>(); 
		int i = 0;
		while(m.find()) {
			String match = m.group();
			allMatches.add(match);
			assertEquals("Expected Test Token " + i + ", " + testTokens[i] + ", found: " + match, match, testTokens[i].trim());
			i++;
		}		
		
	}
	
	@Test
	public void sentenceEndContextCaptured()
	{
		String[] testSents = 
			{"\"This is a test.\"",
			 "This is another test. Token"};
		String [] testMatches = {"test.", "test."};
		String [] testAfterTokens = {"\"", "Token"};
		String [] testNextTokens = {null, "Token"};
		
		
		Pattern sentCont = punktLangVars.getSentenceEndContextPattern();
		
		int i=0;
		for(String s: testSents)
		{
			Matcher m = sentCont.matcher(s);
			while(m.find())
			{
				String match = m.group();
				String after_tok = m.group("after");
				String next_tok = m.group("next");
				
				if(after_tok != null)
					after_tok = after_tok.trim();
				if(next_tok != null)
					next_tok = next_tok.trim();
				
				assertEquals("Sentence="+s+": match", testMatches[i], match);
				assertEquals("Sentence="+s+": <after> match", testAfterTokens[i], after_tok);
				assertEquals("Sentence="+s+": <next> match",  testNextTokens[i], next_tok);
			}
			i++;
		}
		assertTrue("Test", true);
	}
	
}
