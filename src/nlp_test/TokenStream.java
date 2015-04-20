package nlp_test;

public interface TokenStream {

	/**
	 * Get the raw untokenized text that this stream is working with.
	 * 
	 * @return The raw text as a stream.
	 */
	public abstract String getText();
	
	/**
	 * Get the next token in the stream. This function updates previous
	 * token with the current token as a side effect.
	 * 
	 * @return The current token. Will be null when stream end is reached.
	 */
	public abstract Token getToken();

	/**
	 * Get the previous token that we processed.
	 * 
	 * @return The previous token, null if none.
	 */
	public abstract Token getPreviousToken();

}