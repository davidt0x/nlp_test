package nlp_test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class PunktAlgoBaseTest {

	@Test
	public void checkFirstAnnotation() {	
		PunktParams params = new PunktParams();
		String[] abbrvs = {"mr", "mrs"};
		params.getAbbreviations().addAll(Arrays.asList(abbrvs));
		
		PunktAlgoBase base = new PunktAlgoBase(params);
		
		String testString = "This is a sentence. Also one! Another one? End then ... \n\nNew paragraph.\nNew line. Mr.";
		
		PunktTokenStream stream = new PunktTokenStream(testString);
		
		ArrayList<Token> tokens = base.first_pass_annotate(stream);
		
		// Just do some quick sanity checks that sentence breaks and ellipsis are being
		// annotated.
		assertEquals("Token[3] is not sentence break", "sentence.<S>", tokens.get(3).toString());
		assertEquals("Token[6] is not sentence break", "!<S>", tokens.get(6).toString());
		assertEquals("Token[9] is not sentence break", "?<S>", tokens.get(9).toString());
		assertEquals("Token[12] is not ellipsis", "...<E>", tokens.get(12).toString());
		assertTrue("Token[13] is not paragraph start",  tokens.get(13).getIsParaStart());
		assertTrue("Token[13] is not line start",  tokens.get(13).getIsLineStart());
		assertTrue("Token[15] is not line start",  tokens.get(15).getIsLineStart());
		assertEquals("Token[17] is not abbreviation", "Mr.<A>", tokens.get(17).toString());
	}

}
