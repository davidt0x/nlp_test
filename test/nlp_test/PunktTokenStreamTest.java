package nlp_test;

import static org.junit.Assert.*;

import org.junit.Test;

import nlp_test.Token;
import nlp_test.PunktTokenStream;

public class PunktTokenStreamTest {

	@Test
	public void getTokenTest() {
		String [] testTokens = {"This ", "string ", "is ", "a ", "basic ", "test ", "for ", "getToken. ",
								"Here ", "is ", "an ", "ellipsis ", "... ", "and ", "initial ", "D."};
		
		String testString = "";
		for(String s: testTokens)
			testString+=s;
		
		TokenStream tStream = new PunktTokenStream(testString);
		
		Token curr;
		int i=0;
		while( (curr=tStream.getToken()) != null )
		{
			assertEquals("Unexpected token! ", testTokens[i].trim(), curr.getValue());
			if(testTokens[i] == "...")
				assertTrue("Ellipsis detection failed!", curr.getIsEllipsis());
			if(testTokens[i] == "D.")
				assertTrue("Initial detection failed!", curr.getIsInitial());
				
			i++;
		}
	}

}
 