package plug.languages.buchi.viatcp;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class AliceBobPettersonTest {
	
	private static KripkeBuchiProductSemanticsHelper productSemantics;
	
	/**
	 * Constructor of KripkeBuchiProductSemanticsTest.
	 * 
	 * @return an instance of KripkeBuchiProductSemanticsTest.
	 */
	public AliceBobPettersonTest() {
		
	}
	
	@BeforeClass
	public static void setUpClass() {
		// Initialize stuff before every test
		productSemantics = new KripkeBuchiProductSemanticsHelper();
	}
	 
	 @AfterClass
	 public static void tearDownClass() {
		 // Do something after each test
		 productSemantics.getViaTCPRuntimeRealType().closeConnection();
	 }
	 
	 @Before
	 public void setUp() {
		 productSemantics.getViaTCPRuntimeRealType().getPilot().resetInterpretation();
	 }

	@Test
	public void testMutualExclusionOK() {
		String ltl = "exclusion = ![]!(|alice.state == STATE_ALICE_CS| && |bob.state == STATE_BOB_CS|)";
		productSemantics.verify("", ltl, true);
	}
	
	@Test
	public void testMutualExclusionNOK() {
		String ltl = "exclusion = ![]!(|alice.state == STATE_ALICE_I| && |bob.state == STATE_BOB_I|)";
		productSemantics.verify("", ltl, true);
	}
	
	@Test
	public void testFairnessPetterson() {
		String ltl = "fairness = ! ([] (|alice.flagAlice == 1| -> <> |alice.state == STATE_ALICE_CS|) && (|bob.flagBob == 1| -> <> |bob.state == STATE_BOB_CS|)  )";
		productSemantics.verify("", ltl, true);
	}

}

