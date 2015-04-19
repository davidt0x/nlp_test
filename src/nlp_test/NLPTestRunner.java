package nlp_test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * 
 * @author Dave Turner
 *
 */
public class NLPTestRunner {

	public static void main(String[] args) {
		
		// =====================================================================
		// 1. Read in data/nlp_data.txt and perform sentence boundary detection.
		// =====================================================================
		
		String INPUT_FILE1 = "data/my_nlp_data.txt";
		String OUTPUT_FILE1 = "test_out/nlp_data.xml";
		try {
			// Read file into memory, this is bad to do in general if the corpus is
			// very large. Lets assume UTF-8.
			String inputText = readFile(INPUT_FILE1,  StandardCharsets.UTF_8);
			
			// Setup a stream tokenizer
			PunktTokenStream stream = new PunktTokenStream(inputText);
			
			// Pass the token stream to our PunktTokenizer
			SentenceTokenizer sTokenizer = new PunktFirstPassSentenceTokenizer();
			
			// Get the full list of annotated tokens from the stream using our
			// sentence tokenizer.
			ArrayList<Token> tokens =  sTokenizer.annotate(stream);
			
			// Build a sentence list from the tokens and text
			SentenceList sents = sTokenizer.buildSentenceList(inputText, tokens);
			
			// Print each sentence on its own line
			for(Sentence s: sents)
				System.out.println(s);
			
			// Output the list of sentences to XML using JAXB
			File file = new File(OUTPUT_FILE1);
			JAXBContext jaxbContext = JAXBContext.newInstance(SentenceList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 
			// Write it to the file
			jaxbMarshaller.marshal(sents, file);
				
		} catch(IOException ex) {
			System.out.println("Error: Could not read input file: " + INPUT_FILE1);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Nice little method to read a file into a string. 
	 */
	static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
}
