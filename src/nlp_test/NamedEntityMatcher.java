package nlp_test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class defines processing common to most named entity extractors
 * for text. It handles processing of an input file\database of known
 * named entities for matching. It also has a common interface methods
 * for extracting the matches.
 * 
 * @author Dave Turner
 *
 */
public abstract class NamedEntityMatcher {

	/*
	 * A list of strings which this matcher considers named entities
	 */
	protected List<String> entities = new ArrayList<String>();
	
	/**
	 * Create an empty named entity matcher. This matches nothing.
	 */
	public NamedEntityMatcher()
	{
		
	}
	
	/**
	 * Create a named entity matcher from a list known entity strings
	 * 
	 * @param entities This list of known entities.
	 */
	public NamedEntityMatcher(List<String> entities)
	{
		this.entities = entities;
	}
	
	/**
	 * Reads a file of named entities separated by end lines. Assumes UTF-8 
	 * encoding.
	 * 
	 * @param file This file to read.
	 * @throws IOException
	 */
	public NamedEntityMatcher(String filePath)
		throws IOException
	{
		String ents = readFile(filePath, StandardCharsets.UTF_8);
		
		// Split our entities by lines and add to entities list.
		this.entities = Arrays.asList(ents.split("\n"));
	}
	
	/*
	 * Nice little method to read a file into a string. 
	 */
	private static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
	
	/**
	 * Process a token stream and find any named entities. The list of
	 * tokens returned for the stream will be annotated as named entities.
	 * 
	 * @param stream The stream to process.
	 * @param origText The original raw text that the tokens were extracted from.
	 * @return The list of annotated tokens.
	 */
	public abstract List<TokenNE> annotate(List<Token> tokens, String origText);
		
}
