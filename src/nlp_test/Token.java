package nlp_test;

import java.util.regex.Pattern;

/*
 * I use this for XML serialization. I hope that is ok. 
 */
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class stores a token of text along with annotations that we need
 * for sentence boundary detection. This class can be extended to add
 * additional functionality.
 * 
 * @author Dave Turner
 *
 */
@XmlRootElement(name="token")
public class Token {

	/*
	 * The raw value of the token. 
	 */
	protected String token;
	
	protected boolean isParaStart = false;
	protected boolean isLineStart = false;
	protected boolean isSentBreak = false;
	protected boolean isAbbreviation = false;
	
	protected boolean isInitial = false;
	protected boolean isEllipsis = false;
	protected boolean isAlpha = false;
	protected boolean isNonPunct = false;
	protected boolean isNumber = false;
	
	/**
	 * Is this token annotated as an abbreviation.
	 * 
	 * @return Yes if true, false if no.
	 */
	public boolean getIsAbbreviation()
	{
		return isAbbreviation;
	}
	
	/**
	 * Set whether this token is an abbreviation.
	 * 
	 * @param val Yes if true, false if not.
	 */
	@XmlAttribute
	public void setIsAbbreviation(boolean val)
	{
		isAbbreviation = val;
	}
	
	/**
	 * Is this token the start of a paragraph.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean getIsParaStart()
	{
		return isParaStart;
	}
	
	/**
	 * Set whether this token is a paragraph starter.
	 * 
	 * @param val True if yes, false if no.
	 */
	@XmlAttribute
	public void setIsParaStart(boolean val)
	{
		isParaStart = val;
	}
	
	/**
	 * Is this token the start of a line.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean getIsLineStart()
	{
		return isLineStart;
	}
	
	/**
	 * Set whether this token is a line starter.
	 * 
	 * @param val True if yes, false if no.
	 */
	@XmlAttribute
	public void setIsLineStart(boolean val)
	{
		isLineStart = val;
	}
	
	/**
	 * Is this token a sentence break.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean getIsSentBreak()
	{
		return isSentBreak;
	}
	
	/**
	 * Set whether this token is a sentence breaker.
	 * 
	 * @param val True if yes, false if no.
	 */
	@XmlAttribute
	public void setIsSentBreak(boolean val)
	{
		isSentBreak = val;
	}
	
	/**
	 * Is this token an initial.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean getIsInitial()
	{
		return isInitial;
	}
	
	/**
	 * Set whether this token is an intial.
	 * @param val true if yes, false if no;
	 */
	@XmlAttribute
	public void setIsInitial(boolean val)
	{
		isInitial = true;
	}
	
	/**
	 * Is this token and ellipsis.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean getIsEllipsis()
	{
		return isEllipsis;
	}
	
	/**
	 * Set whether this token is an ellipsis.
	 * 
	 * @param val true if yes, false if no;
	 */
	@XmlAttribute
	public void setIsEllipsis(boolean val)
	{
		isEllipsis = true;
	}
	
	/**
	 * Is this token is a number.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean getIsNumber()
	{
		return isNumber;
	}
	
	/**
	 * Set whether this token is a number.
	 * 
	 * @param val true if yes, false if no;
	 */
	@XmlAttribute
	public void setIsNumber(boolean val)
	{
		isNumber = true;
	}
	
	/**
	 * Check if the first character is upper case.
	 * 
	 * @return
	 */
	public boolean getIsFirstUpper()
	{
		return Character.isUpperCase(token.charAt(0));
	}
	
	public boolean getIsFirstLower()
	{
		return Character.isLowerCase(token.charAt(0));
	}
	
	/*
	 * This is a tag we use as a placeholder for numbers in tokens.
	 */
	protected final static String NUMBER_TAG = "##number##";
	
	/*
	 * Here is a simple regex for matching numbers
	 */
	protected Pattern regex_number = Pattern.compile("^-?[.,]?\\d[0-9.,]*\\.?");
	
	/*
	 * This regex will match elipsis. No spaces between periods allowed!
	 */
	protected Pattern regex_ellipsis = Pattern.compile("\\.\\.+");
	
	/*
	 * This regex matches initials of the form of "D." 
	 * We use [^\\W\\d] for a more general approximation for [A-Za-z]
	 * Also turn on Unicode support for all default character classes
	 */
	protected Pattern regex_initials = Pattern.compile("[^\\W\\d]\\.", Pattern.UNICODE_CHARACTER_CLASS);
	
	/*
	 * This regex matches alpha characters.
	 * We use [^\\W\\d] for a more general approximation for [A-Za-z]
	 * Also turn on Unicode support for all default character classes
	 */
	protected Pattern regex_alpha = Pattern.compile("[^\\W\\d]+", Pattern.UNICODE_CHARACTER_CLASS);
	
	/*
	 * A lower case normalized value of this token.
	 */
	protected String type = null;
	
	/**
	 * Get the lowercase normalized value of this token. Numbers are replaced
	 * with the special tag ##number##
	 * 
	 * @return The type for this token.
	 */
	public String getTokenType()
	{
		if(type == null)
		{
			// Normalize to lowercase
			type = token.toLowerCase();
			
			// Replace numbers with place holder.
			type = regex_number.matcher(type).replaceAll(NUMBER_TAG);
		} 
		
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
		if(type.length() > 1 && type.endsWith("."))
			return type.substring(0, type.length()-1);
		else
			return type;
	}
	
	/**
	 * Default constructor
	 */
	public Token()
	{

	}
	
	/**
	 * Create a token from a string.
	 * 
	 * @param token The string to use.
	 */
	public Token(String token)
	{
		this.token = token;
		this.type = getTokenType();
		
		isEllipsis = regex_ellipsis.matcher(token).matches();
		isInitial = regex_initials.matcher(token).matches();
		isAlpha = regex_alpha.matcher(token).matches();
		isNumber = type.startsWith(NUMBER_TAG);
			
		isParaStart = false;
		isLineStart = false;
		isSentBreak = false;
		isNonPunct = false;	
	}
	
	/**
	 * This function creates a string representation of the token with non-default
	 * annotations in the form. This uses the annotations from Kiss and Strunk
	 * 
	 *  <A> for abbreviation
	 *  <E> for ellipsis
	 *  <S> for sentence break
	 */
	public String toString()
	{
		String ret = token;
		
		if(getIsEllipsis())
			ret = ret + "<E>";
		if(getIsAbbreviation())
			ret = ret + "<A>";
		if(getIsSentBreak())
			ret = ret + "<S>";
		
		return ret;	
	}

	/**
	 * Return the raw token value.
	 * 
	 * @return The token string.
	 */
	@XmlElement
	public String getValue() {
		return token;
	}
	
}
