package nlp_test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements simple named entity matching using regular expressions.
 * 
 * @author Dave Turner
 *
 */
public class SimpleNamedEntityMatcher extends NamedEntityMatcher {

	/*
	 * A set of regular expressions, one for each named entity in our
	 * database.
	 */
	List<Pattern> regexs = new ArrayList<Pattern>();
	
	/**
	 * Construct a simple named entity matcher from file which lists known named
	 * entities separated by new lines.
	 * 
	 * @param filePath The path of the file to load.
	 * @throws IOException
	 */
	public SimpleNamedEntityMatcher(String filePath) throws IOException {
		super(filePath);
		compileRegexs();
	}

	/**
	 * Construct a simple named entity matcher from a list of known named
	 * entities.
	 *
	 * @param entities
	 */
	public SimpleNamedEntityMatcher(List<String> entities) {
		super(entities);
		compileRegexs();
	}

	/*
	 * This function builds our list of regular expression for the named entities.
	 */
	protected void compileRegexs()
	{
		// Compile a regex for each entity that matches it
		for(String e: entities)
		{
			// Ignore case
			String regex = "(" + Pattern.quote(e.toLowerCase()) + ")";
			
			// Replace any whitespace within the regex string pattern with
			// a matcher for any amount of white space.
			//regex = regex.replaceAll("\\s","\\\\s+");
			
			// Compile our regex and add it to the list
			regexs.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
		}
	}
	
	@Override
	public List<TokenNE> annotate(List<Token> tokens, String origText) {
	
		// Create a new list of TokenNE so we can annotate named entities
		List<TokenNE> mod_tokens = new ArrayList<TokenNE>();
		
		// Copy the existing tokens to their new objects
		for(Token t: tokens)
			mod_tokens.add(new TokenNE(t));
		
		// For each entity, lets find all copies of in the orginal text and
		// then mark those corresponding tokens with the named entity info.
		int entI = 0;
		for(String entity: entities)
		{
			// Get the regex for this entity
			Pattern pat = regexs.get(entI);
			Matcher m = pat.matcher(origText);
			
			// Get all the matches for this entity in our original text
			while(m.find())
			{
				String match = m.group();
				
				// Now lets find out which tokens we need to annotate. 
				// Get the start and end indices for this entity. These
				// indicies are within the original string.
				int start = m.start();
				int end = m.end()-1;
				
				// Get all tokens that fall within this range. Since we can
				// do this with a modified binary search because we know
				// our tokens list is sorted from least to greatest by 
				// position index within the string.
				List<Integer> tokis = binaryRangeSearch(tokens, start, end);
				
				// Annotate this tokens with the named entity
				for(Integer index: tokis)
					mod_tokens.get(index).addEntity(entity);
			}
			
			
			entI++;
		}
		
		return mod_tokens;
	}
	
	/*
	 * This function performs a modified binary search on a set of tokens
	 * looking for tokens whos position fall within an upper and lower 
	 * bound inclusive.
	 */
	List<Integer> binaryRangeSearch(List<Token> tokens, int lower, int upper)
	{
		List<Integer> in_range = new ArrayList<Integer>();
		
		int min = bsearchMax(tokens, lower);
		int max = bsearchMin(tokens, upper);
		
		for(int i=min;i<=max;i++)
			in_range.add(i);
		
		return in_range;
	}
	
	/*
	 * Find the max number smaller then a limit
	 */
	int bsearchMin(List<Token> tokens, int limit)
	{
		int start = 0;
		int end = tokens.size()-1;
		int midPt = 0;
		
		while(start <= end)
		{
			// Find our middle
			midPt = (start + end) / 2; 
			
			if(tokens.get(midPt).getPosition() < limit)
				start = midPt + 1;
			else if(tokens.get(midPt).getPosition() > limit)
				end = midPt - 1;
			else // They are equal. 
			{
				while(tokens.get(midPt).getPosition() == limit) {midPt++;}
				return midPt - 1;
			}
		}
		
		if(tokens.get(midPt).getPosition() < limit)
			return midPt;
		else
			return midPt-1;
	}
	
	/*
	 * Find the min number greater than a limit
	 */
	int bsearchMax(List<Token> tokens, int limit)
	{
		int start = 0;
		int end = tokens.size()-1;
		int midPt = 0;
		
		while(start <= end)
		{
			// Find our middle
			midPt = (start + end) / 2; 
			
			if(tokens.get(midPt).getPosition() < limit)
				start = midPt + 1;
			else if(tokens.get(midPt).getPosition() > limit)
				end = midPt - 1;
			else // They are equal. 
			{
				while(tokens.get(midPt).getPosition() == limit) {midPt--;}
				return midPt + 1;
			}
		}
		
		if(tokens.get(midPt).getPosition() > limit)
			return midPt;
		else
			return midPt+1;
	}
	
		
}
