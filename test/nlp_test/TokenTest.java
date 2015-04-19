package nlp_test;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

	private Token makeToken(String s) 
	{
		return new Token(s);
	}
	
	@Test
	public void testTokenAttributeDetection() {
		
		assertTrue("Ellipsis Detection Failed", makeToken("...").getIsEllipsis());
		assertTrue("Ellipsis Detection Failed", makeToken("D.").getIsInitial());
		assertTrue("Ellipsis Detection Failed", makeToken("-1,234,453.00").getIsNumber());
		
	}

}
