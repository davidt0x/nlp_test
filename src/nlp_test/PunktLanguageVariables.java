package nlp_test;

import java.util.regex.Pattern;

/**
 * The main purpose of this class is to encapsulate, at least as much as 
 * possible, the language specific properties\variables of the Punkt 
 * algorithm. The default class is for the English language. Users that
 * wish to change these variables can inherit from this class and override
 * where appropriate. The end result of this class is the construction of
 * a regular expression which will tokenize text into words.
 * 
 * This class is heavily based off the class PunktLanguageVars in the
 * Python library nltk.
 * 
 * @author Dave Turner
 *
 */
public class PunktLanguageVariables	 {

	/*
	 * Characters which may be sentence boundaries. Mostly used for
	 * testing if the regex below is working properly.
	 */
	private static String[] sentence_end_chars = {".","?","!"};
	
	/*
	 * A simple regex to match the sentence end characters.
	 */
	private static String regex_sent_end_chars = "[.?!]";
	
	/*
	 * Keep a compiled regex for sentence end characters. 
	 */
	private static Pattern sent_end_pattern = Pattern.compile(regex_sent_end_chars);
	
	/**
	 * Get a list of characters considered for candidate sentence enders.
	 * @return The list of strings.
	 */
	public String[] getSentenceEndChars()
	{
		return sentence_end_chars;
	}
	
	/**
	 * Get a compiled regex that matches sentence end characters.
	 * 
	 * @return The compiled pattern.
	 */
	public Pattern getSentenceEndPattern()
	{
		return sent_end_pattern;
	}
	
	/*
	 * A regex that matches non-punctuation characters.
	 */
	private String regex_non_punct = "[^\\W\\d]"; 
	
	/*
	 * A variable to cache the compile non-punctuation regex
	 */
	private Pattern non_punct_pattern = Pattern.compile(regex_non_punct);
	
	/**
	 * Gets regex pattern the matches non-punctuation characters.
	 * 
	 * @return The compiled pattern.
	 */
	public Pattern getNonPunctPattern()
	{
		return non_punct_pattern;
	}
	
	/*
	 * Characters which are sentence internal punctuation if preceded by period
	 * token.
	 */
	private static String internal_punctuation = ",:;";
	
	private String[] nonWordStartChars = 
		{"(", "\"", "`", "{", 
		 ":", ";", "&", "#", 
		 "*", "@", ")", "}", 
		 "-", ",", "]", "["};
	
	/*
	 * This regular expression excludes some characters from starting words tokens.
	 * It could be derived from the list above but this is faster.
	 */
	private String regex_word_start = "[^](\"`{:;&#*\\[@)},\\-]";
	
	/*
	 * These are characters that can go directly after a sentence end character 
	 * but are also included in the sentence. For example, "This is a quote."
	 * The quote mark should be in the sentence.
	 */
	private String[] boundary_realign_chars = {"\"", "]", "}", "'", "\""};
	
	/*
	 * A regex to match the boundary realign characters above. This should be
	 * compiled as multiline to force the $ to grab an endline as well.
	 */
	private String regex_bound_realign = "[\"')\\]}]+?(?:\\s+|(?=--)|$)";
	
	/*
	 * Cache a compiled copy of the boundary realign regex.
	 */
	private Pattern bound_realign_pattern = Pattern.compile(regex_bound_realign, Pattern.MULTILINE);
	
	/**
	 * Get the pattern for matching characters that should be included in a sentence 
	 * even though that fall directly after the sentence boundary. 
	 * For example, "This is a quote." The quote mark should be in the sentence.
	 * 
	 * @return The compiled pattern
	 */
	public Pattern getSentBoundRealignPattern()
	{
		return bound_realign_pattern;
	}
	
	/**
	 * Get a list of characters which are excluded for being allowed to start words.
	 * 
	 * @return List of non-starting characters for words.
	 */
	public String[] getNonWordStartChars()
	{
		return nonWordStartChars;
	}
	
	/**
	 * Check if a character is excluded from starting a word.
	 * 
	 * @param character The character to check
	 * @return true if the character is excluded, false if not. 
	 */
	public boolean isNonWordStartingChar(String character)
	{
		Pattern patt = Pattern.compile(regex_word_start);
		return !patt.matcher(character).matches();
	}
	
	private String[] nonWordChars = 
		{"[", "]", "?", "!", ")", "\"", 
		 ";", "}", "*", "[", ":", "@", 
		 "'", "(", "{"};
	
	/*
	 * This regular expression matches characters which cannot appear in words.
	 * It could be derived from the list above but this is faster.
	 */
	private String regex_non_word_chars = "(?:[]?!)\";}*\\[:@'({])";
	

	/**
	 * Get a list of characters which are excluded for being allowed inside words.
	 * 
	 * @return List of characters that can't be in words.
	 */
	public String[] getNonWordChars()
	{
		return nonWordChars;
	}
	
	/**
	 * Check if a character is excluded from being in a word.
	 * 
	 * @param character The character to check
	 * @return true if the character is excluded, false if not. 
	 */
	public boolean isNonWordChar(String character)
	{
		Pattern patt = Pattern.compile(regex_non_word_chars);
		return patt.matcher(character).matches();
	}
	
	/*
	 * This regular expression matches hyphen and ellipsis as multi-character 
	 * punctuation
	 */
	private String regex_multi_char_punct = "(?:\\-{2,}|\\.{2,}|(?:\\.\\s){2,}\\.)";
	
	/**
	 * Detect whether a string is consider multi-character punctuation.
	 * @param characters The string to check.
	 * @return true if it is considered multi-character punctuation, false if not.
	 */
	public boolean isMultiCharPunct(String characters) {
		Pattern patt = Pattern.compile(regex_multi_char_punct);
		return patt.matcher(characters).matches();
	}
	
	/*
	 * This is a format for a complex regular expression that matches
	 * word tokens. It is built up from the regular expressions
	 * defined above. 
	 */
	private String word_tokenize_format = 
			"MultiChar" 			  	    			+	// Match multi-char punctuation			
			"|" 					  					+	// Or
			"(?=WordStart)\\S+?" 						+   // Grab word characters till end is found, 
															// use positive lookahead
			"(?=" 										+  	// Sequences marking a word's end
				"\\s|"                      			+   // White-space
				"$|"                  					+	// End-of-string
				"NonWord|MultiChar|" 					+	// Punctuation
				",(?=$|\\s|NonWord|MultiChar)" 		+	// Comma if at end of word
			 ")"										+
			 "|"										+
			 "\\S";
	
	/*
	 * Lets cache a copy of the word tokenizer pattern to save time compiling
	 * it each time getWordTokenizerPattern() is called
	 */
	private Pattern word_tokenizer_pattern = null;
	
	/**
	 * This function builds a complex pattern matcher for word tokenization
	 * depending on the underlying lanugage specific regular expressions 
	 * defined within this class instance.
	 * 
	 * @return The word tokenizer compiled regex.
	 */
	public Pattern getWordTokenizePattern()
	{
		if(word_tokenizer_pattern == null)
		{
			// Build the giant regex for word tokenization using our format above
			// and the sub regexs for multi-char punctuation, non-word characters,
			// and word start characters.
			String regex_string = word_tokenize_format;
			regex_string = regex_string.replace("MultiChar", regex_multi_char_punct);
			regex_string = regex_string.replace("NonWord", regex_non_word_chars);
			regex_string = regex_string.replace("WordStart", regex_word_start);

			word_tokenizer_pattern = Pattern.compile(regex_string);
		}
				
		// Return the compiled pattern for this regex.
		return word_tokenizer_pattern;
	}

	/*
	 * This format string helps form a complex regular expression
	 * which allows us to get the context for periods. It lets us
	 * find possible sentence boundaries as well as the contexts 
	 * around them.
	 */
	private String period_context_format = 
			"\\S*" 							+ // Match 0 or more word characters
			"SentEndChars" 					+ // A sentence end character
			"(?=(?<after>" 					+ // Get the token after the sentence end
				"NonWord"					+ // Some kind of punctuation
				"|" 						+ // Or
				"\\s+(?<next>\\S+)" 		+ // Whitespace, capture the next token too
			"))";
	
	/*
	 * Lets cache a copy of the period context pattern to save time compiling
	 * it each time getSentenceEndContextPattern() is called
	 */
	private Pattern period_context_pattern = null;
	
	/**
	 * This function builds a complex pattern matcher for find possible sentence
	 * boundaries and their context. That is the tokens following them. 
	 * It depends on the underlying lanugage specific regular expressions 
	 * defined within this class instance.
	 * 
	 * @return The compiled regular expression for find possible sentence
	 * boundaries.
	 */
	public Pattern getSentenceEndContextPattern()
	{
		if(period_context_pattern == null)
		{
			// Build the giant regex for word tokenization using our format above
			// and the sub regexs for multi-char punctuation, non-word characters,
			// and word start characters.
			String regex_string = period_context_format;
			regex_string = regex_string.replace("SentEndChars", regex_sent_end_chars);
			regex_string = regex_string.replace("NonWord", regex_non_word_chars);
			
			period_context_pattern = Pattern.compile(regex_string);
		}
				
		// Return the compiled pattern for this regex.
		return period_context_pattern;
	}
	
}
