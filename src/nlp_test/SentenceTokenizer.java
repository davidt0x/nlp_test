package nlp_test;

import java.util.ArrayList;
import java.util.List;

public interface SentenceTokenizer {

	/**
	 * Produce an annotated list of tokens from a PunktTokenStream. 
	 * This will detect sentence boundaries and mark those tokens
	 * appropriately.
	 * 
	 * @param stream The token stream to annotate.
	 * @return The list of all tokens in the stream.
	 */
	public abstract ArrayList<Token> annotate(TokenStream stream);

	/**
	 * Construct a sentence list object from and original text string and
	 * the list of tokens.
	 * 
	 * @param text The original text the tokens are derived from.
	 * @param tokens The tokens list.
	 * @return
	 */
	public abstract SentenceList buildSentenceList(String text, List<Token> tokens);
	
}