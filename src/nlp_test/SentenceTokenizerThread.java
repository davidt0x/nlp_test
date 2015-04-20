package nlp_test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class SentenceTokenizerThread implements Runnable 
{
	/*
	 * String to process
	 */
	protected String inputText;

	/*
	 * Output file name
	 */
	protected String output_file;
	
	/*
	 * This is the sentence tokenizer to use.
	 */
	protected SentenceTokenizer tokenizer = null;
	
	/*
	 * Name entity matcher to use if requested
	 */
	protected NamedEntityMatcher nerer = null;
	
	/**
	 * Create a worker thread that processing a string into sentence
	 * tokens and outputs to XML.
	 * 
	 * @param text The text to process
	 * @param outFile The file to output to, create or overwrite if 
	 * needed.
	 */
	public SentenceTokenizerThread(String text, String outFile, 
		SentenceTokenizer tokenizer)
	{
		this.inputText = text;
		this.output_file = outFile;
		this.tokenizer = tokenizer;
	}
	
	/**
	 * Setup a named entity matcher to use. This is optional.
	 * 
	 * @param n The matcher to use.
	 */
	public void setNamedEntityMatcher(NamedEntityMatcher n)
	{
		nerer = n;
	}
	
	/*
	 * This function does all of the work.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() 
	{
		
		// Read file into memory, this is bad to do in general if the corpus is
		// very large. Lets assume UTF-8.
		try
		{
			// Setup a stream tokenizer
			TokenStream stream = new PunktTokenStream(inputText);
			
			// Pass the token stream to our PunktTokenizer
			SentenceTokenizer sTokenizer = new PunktFirstPassSentenceTokenizer();
			
			// Get the full list of annotated tokens from the stream using our
			// sentence tokenizer.
			ArrayList<Token> tokens =  sTokenizer.annotate(stream);
			
			// If we need to do named entity matching
			if(nerer != null)
			{
				// Find named entities and annotate our tokens
				List<TokenNE> tokens_mod = nerer.annotate(tokens, inputText);
	
				// Lets copy our TokenNE's to our original list of tokens so we can output 
				// them with the base class stuff for Token.
				tokens.clear();
				for(TokenNE tk: tokens_mod)
					tokens.add((Token)tk);
			}
			
			// Build a sentence list from the tokens and text
			SentenceList sents = sTokenizer.buildSentenceList(inputText, tokens);
			
			// Output the list of sentences to XML using JAXB
			File file = new File(output_file);
			JAXBContext jaxbContext = JAXBContext.newInstance(SentenceList.class, TokenNE.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 
			// Write it to the file
			jaxbMarshaller.marshal(sents, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		} 
		
	}

}
