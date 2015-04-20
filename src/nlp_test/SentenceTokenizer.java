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

	public abstract SentenceList buildSentenceList(String text, List<Token> tokens);
	
}