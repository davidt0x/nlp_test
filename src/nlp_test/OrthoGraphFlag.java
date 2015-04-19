package nlp_test;

/**
 * These flags are used to describe the orthographic contexts in which 
 * words can occur. 
 * 
 * @author Dave Turner
 *
 */
public class OrthoGraphFlag 
{
	
	/**
	 * Beginning of sentence with upper case
	 */
	public final long BEGINNING_UC = (1 << 1);
	
	/**
	 * Middle of sentence with upper case
	 */
	public final long MIDDLE_UC = (1 << 2);
	
	/**
	 * Unknown position but upper case.
	 */
	public final long UNKNOWN_UC = (1 << 3);
	
	/**
	 * Beginning of sentence with lower case.
	 */
	public final long BEGINNING_LC = (1 << 4);
	
	/**
	 * Middle of sentence with lower case.
	 */
	public final long MIDDLE_LC = (1 << 5);
	
	/**
	 * Unknown position but lower case.
	 */
	public final long UNKNOWN_LC = (1 << 6);
	
	/**
	 * Upper case in general 
	 */
	public final long UC = BEGINNING_UC + MIDDLE_UC + UNKNOWN_UC;
	
	/**
	 * Lower case in general
	 */
	public final long LC = (BEGINNING_LC + MIDDLE_LC + UNKNOWN_LC);
	
}
