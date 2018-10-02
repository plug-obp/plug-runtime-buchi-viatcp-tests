package plug.languages.buchi.viatcp;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import plug.core.ITransitionRelation;
import plug.explorer.AbstractExplorer;
import plug.explorer.BFSExplorer;
import plug.statespace.SimpleStateSpaceManager;
import plug.verifiers.deadlock.DeadlockVerifier;
import plug.verifiers.deadlock.FinalStateDetected;

/**
 * Test suite for the LevelCrossing model.
 * 
 * @author Valentin Besnard (valentin.besnard@eseo.fr)
 */
public class CruiseControlVVTest {

	/**
	 * Instance of the Kripke Buchi product semantics helper.
	 */
	private static KripkeBuchiProductSemanticsHelper productSemantics;

	/**
	 * Constructor of KripkeBuchiProductSemanticsTest.
	 * 
	 * @return an instance of KripkeBuchiProductSemanticsTest.
	 */
	public CruiseControlVVTest() {

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
	public void testCruiseControl() throws Exception {
		String ltl = "vivacite = [] ((|speedManager.state == STATE_SPEEDMANAGER_ON|) -> <> |speedManager.speed == speedManager.cruiseSpeed|)";
		productSemantics.verify("", ltl, true);
	}

//	@Test
//	public void deadlockfree() throws Exception {
//		int nbDeadlocks[] = new int[] {0};
//		ITransitionRelation kripkeRuntime = productSemantics.getRuntime();
//		AbstractExplorer explorer = new BFSExplorer(kripkeRuntime, new SimpleStateSpaceManager<>());
//		DeadlockVerifier dV = new DeadlockVerifier(explorer.getAnnouncer());
//		
//		boolean deadLockFree[] = new boolean[] { true };
//			dV.announcer.when(FinalStateDetected.class, (ann, ev) -> {
//				System.out.println("Final state detected: " + ev.getFinalState() );
//				deadLockFree[0] = false;
//				nbDeadlocks[0] = nbDeadlocks[0] + 1;
//	 	});
//	
//		explorer.execute();
//		assertTrue("The model has " + nbDeadlocks[0] + " deadlocks.", deadLockFree[0]);
//	}


}
