package nlp_test;

import java.util.Objects;

/**
 * A simple class for creating tuples of strings.
 * 
 * @author Dave Turner
 *
 */
public class Tuple {
	
	/*
	 * Make the fields immutable, this will make things easier
	 * for us when setting up equality.
	 */
	public final String x; 
	public final String y; 
	
	/**
	 * Construct a Tuple with a pair of empty strings.
	 */
	public Tuple()
	{
		this.x = "";
		this.y = "";
	}
	
	/**
	 * Construct a tuple from a pair of strings.
	 * @param x First string to use.
	 * @param y Secon string to use.
	 */
	public Tuple(String x, String y) { 
		this.x = x; 
		this.y = y; 
	} 
	
	/**
	 * Get the first element of the tuple.
	 * @return
	 */
	public String getX() { return x; }
	
	/**
	 * Get the second element of the tuple/
	 * @return
	 */
	public String getY() { return y; }
	
	/**
	 * Comparison between two tuples.
	 */
	@Override public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Tuple) {
            Tuple that = (Tuple) other;
            result = (this.x == that.getX() && this.getY() == that.getY());
        }
        return result;
    }

	/**
	 * Hash code for the tuple.
	 */
    @Override public int hashCode() {
        return Objects.hash(x, y);
    }
	
}
