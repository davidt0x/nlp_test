package nlp_test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class add support to the normal Token class for annotating named entities.
 *  
 * @author Dave Turner
 *
 */
@XmlRootElement
public class TokenNE extends Token {

	/*
	 * Is this token a part of a named entity.
	 */
	protected boolean isNamedEntity = false;

	/*
	 * The entities that this token belongs to
	 */
	@XmlElement(name="entities")
	protected List<String> entities = new ArrayList<String>();
	
	/*
	 * Position within the named entity. It is either
	 * marked as beginning, middle, or end.
	 */
	
	/**
	 * Default constructor, create emtpy token.
	 */
	public TokenNE()
	{
		super();
	}
	
	/**
	 * Create a token from a string.
	 * 
	 * @param token The string to use.
	 */
	public TokenNE(String token)
	{
		super(token);
	}
	
	/**
	 * Take a normal token create a token with named entity support.
	 * 
	 * @param token The token to copy.
	 */
	public TokenNE(Token token)
	{
		super(token);
	}
	
	/**
	 * Add a tag to this token for a specific named entity.
	 * 
	 * @param entity The String representation of the entity.
	 */
	public void addEntity(String entity)
	{
		isNamedEntity = true;
		entities.add(entity);
	}
	
	/**
	 * Clear the list of entities to which this token belongs
	 */
	public void clearEntities()
	{
		isNamedEntity = false;
		entities.clear();
	}
	
	/**
	 * Get whether this token is a named entity.
	 * 
	 * @return
	 */
	@XmlAttribute
	public boolean getIsNamedEntity()
	{
		return isNamedEntity;
	}
	
	public String toString()
	{
		String ret = super.toString();
		
		// If this is a named entity, add a tag
		if(isNamedEntity)
			ret = ret + "<Ent>";
		
		return ret;
	}
	
}
