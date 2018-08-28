package plug.languages.buchi.viatcp;



import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class LevelCrossingTest {
	
	private static KripkeBuchiProductSemanticsHelper productSemantics;

    /**
     * Constructor of KripkeBuchiProductSemanticsTest.
     * 
     * @return an instance of KripkeBuchiProductSemanticsTest.
     */
    public LevelCrossingTest() {
    	
    }

    @BeforeClass
    public static void setUpClass() {
    	// Initialize stuff before every test
    	productSemantics = new KripkeBuchiProductSemanticsHelper();
    }
     
     @AfterClass
     public static void tearDownClass() {
    	 // Do something after each test
    	 productSemantics.getViaTCPRuntime().close();
     }
     
     @Before
     public void setUp() {
//    	 try {
//			productSemantics.getViaTCPRuntime().getClient().reset();
//		} catch (EMIException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		};
     }

///////////////////////////////////////////////// Model Case0sync /////////////////////////////////////////////////

	@Test
	public void testGateClosed() throws Exception {
		String ltl = "exclusion = [] !((|train.state == STATE_TRAIN_PASSING|) and |gate.state == STATE_GATE_OPEN|)";
		productSemantics.verify("", ltl, true);
	}
	
	@Test
	public void testRoadSignActive() throws Exception {
		String ltl = "exclusion = [] !((|train.state == STATE_TRAIN_PASSING|) and |roadSign.state == STATE_ROADSIGN_INACTIVE|)";
		productSemantics.verify("", ltl, true);
	}
	
	@Test
	public void testGateOpenedAtferBeingClosed() throws Exception {
		String ltl = "vivacite = [] (|gate.state == STATE_GATE_CLOSED| -> <> |gate.state == STATE_GATE_OPEN|)";
		productSemantics.verify("", ltl, true);
	}

	@Test
	public void testRoadSignActiveAtferBeingInactive() throws Exception {
		String ltl = "vivacite = [] (|roadSign.state == STATE_ROADSIGN_ACTIVE| -> <> |roadSign.state == STATE_ROADSIGN_INACTIVE|)";
		productSemantics.verify("", ltl, true);
	}
	
//	@Test
//	public void testTrainPassing() throws Exception {
//		String ltl = "vivacite = [] (|train.state == STATE_TRAIN_CLOSE| -> <> |train.state == STATE_TRAIN_PASSING|)";
//		productSemantics.verify("", ltl, true);
//	}
	
//	@Test
//	public void deadlockfree() throws Exception {
//		int nbDeadlocks[] = new int[] {0};
//		ITransitionRelation kripkeRuntime =  productSemantics.getRuntime();
//		AbstractExplorer explorer = new BFSExplorer(kripkeRuntime, new SimpleStateSpaceManager<>());
//
//		DeadlockVerifier dV = new DeadlockVerifier(explorer.getAnnouncer());
//
//		boolean deadLockFree[] = new boolean[] { true };
//		dV.announcer.when(FinalStateDetected.class, (ann, ev) -> {
//			System.out.println("Final state detected: " + ev.getFinalState() );
//			ConfigurationItem item = ConfigurationTreeGenerator.generateTree((Configuration) ev.getFinalState());
//			deadLockFree[0] = false;
//			nbDeadlocks[0] = nbDeadlocks[0] + 1;
//		});
//
//		explorer.execute();
//
//		assertTrue("The model has " + nbDeadlocks[0] + " deadlocks.", deadLockFree[0]);
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


