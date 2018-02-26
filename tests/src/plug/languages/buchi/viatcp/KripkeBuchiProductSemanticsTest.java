package plug.languages.buchi.viatcp;



import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import plug.core.ILanguageRuntime;
import plug.explorer.AbstractExplorer;
import plug.explorer.BFSExplorer;
import plug.statespace.SimpleStateSpaceManager;
import plug.verifiers.deadlock.DeadlockVerifier;
import plug.verifiers.deadlock.FinalStateDetected;


public class KripkeBuchiProductSemanticsTest {
	
	private static KripkeBuchiProductSemanticsHelper productSemantics;

    /**
     * Constructor of KripkeBuchiProductSemanticsTest.
     * 
     * @return an instance of KripkeBuchiProductSemanticsTest.
     */
    public KripkeBuchiProductSemanticsTest() {
    	
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

///////////////////////////////////////////////// Model ALiceBobPetterson /////////////////////////////////////////////////
	@Test
	public void testMutualExclusionOK() {
		String ltl = "exclusion = ![]!(|alice.state == STATE_ALICE_CS| && |bob.state == STATE_BOB_CS|)";
		productSemantics.verify("", ltl);
	}
	
	@Test
	public void testMutualExclusionNOK() {
		String ltl = "exclusion = ![]!(|alice.state == STATE_ALICE_I| && |bob.state == STATE_BOB_I|)";
		productSemantics.verify("", ltl);
	}
	
	@Test
	public void testFairnessPetterson() {
		String ltl = "fairness = ! ([] (|alice.flagAlice == 1| -> <> |alice.state == STATE_ALICE_CS|) && (|bob.flagBob == 1| -> <> |bob.state == STATE_BOB_CS|)  )";
		productSemantics.verify("", ltl);
	}
	
///////////////////////////////////////////////// Model Case0sync /////////////////////////////////////////////////

//	@Test
//	public void testGateClosed() {
//		String ltl = "exclusion = ![]!((|train.state == STATE_TRAIN_APPROACHDETECTION| or |train.state == STATE_TRAIN_WAITEXITDETECTION|) and |gate.state == STATE_GATE_OPENED|)";
//		productSemantics.verify("", ltl);
//	}
//	
//	@Test
//	public void testRoadSignActive() {
//		String ltl = "exclusion = ![]!((|train.state == STATE_TRAIN_APPROACHDETECTION| or |train.state == STATE_TRAIN_WAITEXITDETECTION|) and |gate.state == STATE_ROADSIGN_INACTIVE|)";
//		productSemantics.verify("", ltl);
//	}
//	
//	@Test
//	public void testGateOpenedAtferBeingClosed() {
//		String ltl = "vivacite = ! ([] |gate.state == STATE_GATE_CLOSED| -> <> |gate.state == STATE_GATE_OPENED|)";
//		productSemantics.verify("", ltl);
//	}
//
//	@Test
//	public void testRoadSignActiveAtferBeingInactive() {
//		String ltl = "vivacite = ! ([] |roadSign.state == STATE_ROADSIGN_ACTIVE| -> <> |roadSign.state == STATE_ROADSIGN_INACTIVE|)";
//		productSemantics.verify("", ltl);
//	}
//	
//	@Test
//	public void deadlockfree() {
//		ILanguageRuntime kripkeRuntime =  productSemantics.getViaTCPRuntime();
//		AbstractExplorer explorer = new BFSExplorer(kripkeRuntime, new SimpleStateSpaceManager<>());
//
//		DeadlockVerifier dV = new DeadlockVerifier(explorer.getAnnouncer());
//
//		boolean deadLockFree[] = new boolean[] { true };
//		dV.announcer.when(FinalStateDetected.class, (ann, ev) -> {
//			System.out.println("Final state detected: " + ev.getFinalState() );
//			deadLockFree[0] = false;
//		});
//
//		explorer.execute();
//
//		assertTrue("The model has a deadlock.", deadLockFree[0]);
//	}

	
//	@Test
//	public void testGateOpenedAtEnd() {
//	String ltl = "vivacite = ! ([] |train.state == STATE_TRAIN_DONE| -> <> |gate.state == STATE_GATE_CLOSED|)";
//	verify("", ltl);
//	}
//	
//	@Test
//	public void testRoadSignInactiveAtEnd() {
//	String ltl = "vivacite = ! ([] |train.state == STATE_TRAIN_DONE| -> <> |roadSign.state == STATE_ROADSIGN_INACTIVE|)";
//	verify("", ltl);
//	}
	
	
///////////////////////////////////////////////// Others /////////////////////////////////////////////////	
	//@Test
	//public void testRoadSignOffAtEnd() {  // Not verified but I know why
	//String ltl = "exclusion = ![]!(|train.state == STATE_TRAIN_DONE| && |roadSign.state == STATE_ROADSIGN_ACTIVE|)";
	//verify("", ltl);
	//}

    // @Test(expected = AcceptanceCycleDetectedException.class)
    // public void testEventuallyCS2() {
    //     String ltl = "evcs = ![]<>(|{Alice}1@CS| || |{Bob}1@CS|)";
    //     verify("tests/resources/AliceBob2.fcr", ltl);
    // }

    // @Test
    // public void testEventuallyCS3() {
    //     String ltl = "evcs = ![]<>(|{Alice}1@CS| || |{Bob}1@CS|)";
    //     verify("tests/resources/AliceBob3.fcr", ltl);
    // }

    // @Test
    // public void testEventuallyCSPeterson() {
    //     String ltl = "evcs = ![]<>(|{Alice}1@CS| || |{Bob}1@CS|)";
    //     verify("tests/resources/AliceBobMeetPeterson.fcr", ltl);
    // }

    // @Test(expected = AcceptanceCycleDetectedException.class)
    // public void testFairness1() {
    //     String ltl = "fairness = ! ([] (|{sys}1:flags[0] = true| -> <> |{Alice}1@CS|) && (|{sys}1:flags[1] = true| -> <> |{Bob}1@CS|)  )";
    //     verify("tests/resources/AliceBob1.fcr", ltl);
    // }

    // @Test(expected = AcceptanceCycleDetectedException.class)
    // public void testFairness2() {
    //     String ltl = "fairness = ! ([] (|{sys}1:flags[0] = true| -> <> |{Alice}1@CS|) && (|{sys}1:flags[1] = true| -> <> |{Bob}1@CS|)  )";
    //     verify("tests/resources/AliceBob2.fcr", ltl);
    // }

    // @Test(expected = AcceptanceCycleDetectedException.class)
    // public void testFairness3() {
    //     String ltl = "fairness = ! ([] (|{sys}1:flags[0] = true| -> <> |{Alice}1@CS|) && (|{sys}1:flags[1] = true| -> <> |{Bob}1@CS|)  )";
    //     verify("tests/resources/AliceBob2.fcr", ltl);
    // }

    // @Test
    // public void testFairnessPetterson() {
    //     String ltl = "fairness = ! ([] (|{sys}1:flags[0] = true| -> <> |{Alice}1@CS|) && (|{sys}1:flags[1] = true| -> <> |{Bob}1@CS|)  )";
    //     verify("tests/resources/AliceBobMeetPeterson.fcr", ltl);
    // }

    // @Test(expected = AcceptanceCycleDetectedException.class)
    // public void testBobInfoftenPetterson() {
    //     String ltl = "infoften = ! ([] <> |{Bob}1@CS| )";
    //     verify("tests/resources/AliceBobMeetPeterson.fcr", ltl);
    // }

}


