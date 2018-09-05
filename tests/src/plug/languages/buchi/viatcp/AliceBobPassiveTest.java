package plug.languages.buchi.viatcp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test suite for the AliceBobPetterson model.
 * 
 * @author Valentin Besnard (valentin.besnard@eseo.fr)
 */
public class AliceBobPassiveTest {

	/**
	 * Instance of the Kripke Buchi product semantics helper.
	 */
	private static KripkeBuchiProductSemanticsHelper productSemantics;

	/**
	 * Constructor of KripkeBuchiProductSemanticsTest.
	 * 
	 * @return an instance of KripkeBuchiProductSemanticsTest.
	 */
	public AliceBobPassiveTest() {

	}

	@BeforeClass
	public static void setUpClass() {
		productSemantics = new KripkeBuchiProductSemanticsHelper();
	}

	@AfterClass
	public static void tearDownClass() {
		productSemantics.getViaTCPRuntime().close();
	}

	@Test
	public void testMutualExclusionOK() throws Exception {
		String ltl = "exclusion = []!(|alice.state == STATE_ALICE_CS| && |bob.state == STATE_BOB_CS|)";
		productSemantics.verify("", ltl, true);
	}

	@Test
	public void testMutualExclusionNOK() throws Exception {
		String ltl = "exclusion = []!(|alice.state == STATE_ALICE_I| && |bob.state == STATE_BOB_I|)";
		productSemantics.verify("", ltl, true);
	}

	@Test
	public void testFairnessPetterson() throws Exception {
		String ltl = "fairness = ([] (|objects[0].flag == 1| -> <> |alice.state == STATE_ALICE_CS|) && (|objects[1].flag == 1| -> <> |bob.state == STATE_BOB_CS|)  )";
		productSemantics.verify("", ltl, true);
	}

}
