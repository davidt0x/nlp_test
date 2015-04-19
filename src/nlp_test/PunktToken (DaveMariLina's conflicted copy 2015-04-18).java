package nlp_test;

import java.util.regex.Pattern;

/**
 * This class stores a token of text along with annotations that we need
 * for sentence boundary detection.
 * 
 * @author Dave Turner
 *
 */
public class PunktToken {

	/*
	 * The raw value of the token. 
	 */
	private String token;
	
	private boolean isParaStart = false;
	private boolean isLineStart = false;
	private boolean isSentBreak = false;
	private boolean isInitial = false;
	private boolean isEllipsis = false;
	
	/**
	 * Is this token the start of a paragraph.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean isParaStart()
	{
		return isParaStart;
	}
	
	/**
	 * Is this token the start of a line.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean isLineStart()
	{
		return isLineStart;
	}
	
	/**
	 * Is this token a sentence break.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean isSentBreak()
	{
		return isSentBreak;
	}
	
	/**
	 * Is this token an initial.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean isInitial()
	{
		return isInitial;
	}
	
	/**
	 * Is this token and ellipsis.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean isEllipsis()
	{
		return isEllipsis;
	}
	
	/**
	 * Check if the first character is upper case.
	 * 
	 * @return True if yes, false if no.
	 */
	public boolean isFirstUpper()
	{
		return Character.isUpperCase(token.charAt(0));
	}
	

	/**
	 * Check if the first character is lower case.
	 * 
	 * @return True if yes, false if no.
	 */
	public boolean isFirstLower()
	{
		return Character.isLowerCase(token.charAt(0));
	}
	
	
	/*
	 * This is a tag we use as a placeholder for numbers in tokens.
	 */
	private final static String NUMBER_TAG = "##number##";
	
	/*
	 * Here is a simple regex for matching numbers
	 */
	private Pattern regex_number = Pattern.compile("^-?[.,]?\\d[0-9.,]*\\.?");
	
	/*
	 * This regex will match elipsis. No spaces between periods allowed!
	 */
	private Pattern regex_ellipsis = Pattern.compile("\\.\\.+");
	
	/*
	 * This regex matches initials of the form of "D." 
	 * We use [^\\W\\d] for a more general approximation for [A-Za-z]
	 * Also turn on Unicode support for all default character classes
	 */
	private Pattern regex_initials = Pattern.compile("[^\\W\\d]\\.", Pattern.UNICODE_CHARACTER_CLASS);
	
	/*
	 * This regex matches alpha characters.
	 * We use [^\\W\\d] for a more general approximation for [A-Za-z]
	 * Also turn on Unicode support for all default character classes
	 */
	private Pattern regex_alpha = Pattern.compile("[^\\W\\d]+", Pattern.UNICODE_CHARACTER_CLASS);
	
	/**
	 * Get the lowercase normalized value of this token. Numbers are replaced
	 * with the special tag ##number##
	 * 
	 * @return The type for this token.
	 */
	public String getTokenType()
	{
		// Normalize to lowercase
		String type = token.toLowerCase();
		
		// Replace numbers with place holder.
		type = regex_number.matcher(type).replaceAll(NUMBER_TAG);
		
		return type;
	}
	
	/**
	 * Return the lower case normalize token type with the trailing period
	 * removed if it is there.
	 * 
	 * @return The token type sans end period.
	 */
	public String getTokenTypeNoPeriod()
	{
		String type = getTokenType();
		if(type.length() > 1 && type.endsWith("."))
			return type.substring(0, type.length()-1);
		else
			return type;
	}
	
	
}
