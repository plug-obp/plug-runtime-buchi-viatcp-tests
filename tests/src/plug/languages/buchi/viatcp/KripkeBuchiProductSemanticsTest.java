package plug.languages.buchi.viatcp;


import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import plug.core.ILanguageModule;
import plug.core.ILanguageRuntime;
import plug.explorer.AbstractExplorer;
import plug.explorer.BFSExplorer;
import plug.explorer.buchi.AcceptanceCycleDetectedException;
import plug.explorer.buchi.nested_dfs.BA_GaiserSchwoon_Iterative;
import plug.explorer.buchi.nested_dfs.BA_GaiserSchwoon_Recursive;
import plug.language.buchi.runtime.BuchiRuntime;
import plug.language.buchikripke.runtime.KripkeBuchiProductSemantics;
import plug.language.viatcp.ViaTCPModule;
import plug.language.viatcp.runtime.ViaTCPRuntime;
import plug.statespace.SimpleStateSpaceManager;
import plug.verifiers.deadlock.DeadlockVerifier;
import plug.verifiers.deadlock.FinalStateDetected;
import properties.BuchiAutomata.BuchiAutomataModel.BuchiDeclaration;
import properties.LTL.parser.Parser;
import properties.LTL.transformations.LTL2Buchi;
import properties.PropositionalLogic.PropositionalLogicModel.DeclarationBlock;
import properties.PropositionalLogic.PropositionalLogicModel.Expression;

public class KripkeBuchiProductSemanticsTest {
	
	/**
	 * The instance of the language module.
	 */
    ILanguageModule module;
    
    /**
     * The instance of the model interpreter runtime.
     */
	ViaTCPRuntime runtime;
    
    /**
     * Constructor of KripkeBuchiProductSemanticsTest.
     * 
     * @return an instance of KripkeBuchiProductSemanticsTest.
     */
    public KripkeBuchiProductSemanticsTest() {
    	module = new ViaTCPModule();
    	runtime = new ViaTCPRuntime("localhost", 12121);
    }

    /**
     * Getter of the model interpreter runtime.
     * @return the instance of the model interpreter runtime
     */
     ILanguageRuntime getViaTCPRuntime() {
    	 return runtime;
     }

     /**
      * Get the Buchi declaration corresponding to a LTL formula.
      * @param ltlFormula the LTL formula.
      * @return the Buchi declaration.
      */
     BuchiDeclaration getBuchiDeclaration(String ltlFormula) {
         DeclarationBlock propertiesBlock = Parser.parseBlock(ltlFormula);
         Expression property = propertiesBlock.getDeclarations().iterator().next().getExpression();
         LTL2Buchi convertor = new LTL2Buchi(new PrintWriter(System.out));

         BuchiDeclaration decl = convertor.convert(property);
         return decl;
     }
     
     
     @Before
     public void setUp() {
    	 // Initialize stuff before every test
    	 //runtime.getPilot().resetInterpretation();
     }
     
     @After
     public void tearDown() {
    	 // Do something after each test
    	 runtime.closeConnection();
     }

///////////////////////////////////////////////// Model ALiceBobPetterson /////////////////////////////////////////////////
//	@Test
//	public void testMutualExclusionOK() {
//	String ltl = "exclusion = ![]!(|alice.state == STATE_ALICE_CS| && |bob.state == STATE_BOB_CS|)";
//	verify("", ltl);
//	}
//	
//	@Test
//	public void testMutualExclusionNOK() {
//	String ltl = "exclusion = ![]!(|alice.state == STATE_ALICE_I| && |bob.state == STATE_BOB_I|)";
//	verify("", ltl);
//	}
//	
//	@Test
//	public void testFairnessPetterson() {
//	String ltl = "fairness = ! ([] (|alice.flagAlice == 1| -> <> |alice.state == STATE_ALICE_CS|) && (|bob.flagBob == 1| -> <> |bob.state == STATE_BOB_CS|)  )";
//	verify("", ltl);
//	}
	
///////////////////////////////////////////////// Model Case0sync /////////////////////////////////////////////////
	@Test
	public void testGateClosed() {
	String ltl = "exclusion = ![]!((|train.state == STATE_TRAIN_APPROACHDETECTION| || |train.state == STATE_TRAIN_WAITEXITDETECTION|) && |gate.state == STATE_GATE_OPENED|)";
	verify("", ltl);
	}
	
	@Test
	public void testRoadSignActive() {
	String ltl = "exclusion = ![]!((|train.state == STATE_TRAIN_APPROACHDETECTION| || |train.state == STATE_TRAIN_WAITEXITDETECTION|) && |gate.state == STATE_ROADSIGN_INACTIVE|)";
	verify("", ltl);
	}
	
	@Test
	public void testGateOpenedAtferBeingClosed() {
	String ltl = "vivacite = ! ([] |gate.state == STATE_GATE_CLOSED| -> <> |gate.state == STATE_GATE_OPENED|)";
	verify("", ltl);
	}

	@Test
	public void testRoadSignActiveAtferBeingInactive() {
	String ltl = "vivacite = ! ([] |roadSign.state == STATE_ROADSIGN_ACTIVE| -> <> |roadSign.state == STATE_ROADSIGN_INACTIVE|)";
	verify("", ltl);
	}
	
	@Test
	public void deadlockfree() {
		ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
		AbstractExplorer explorer = new BFSExplorer(kripkeRuntime, new SimpleStateSpaceManager<>());

		DeadlockVerifier dV = new DeadlockVerifier(explorer.getAnnouncer());

		boolean deadLockFree[] = new boolean[] { true };
		dV.announcer.when(FinalStateDetected.class, (ann, ev) -> {
			System.out.println("Final state detected: " + ev.getFinalState() );
			deadLockFree[0] = false;
		});

		explorer.execute();

		assertTrue("The model has a deadlock.", deadLockFree[0]);
		
	}

	
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

     private void verify(String fileName, String ltl) throws AcceptanceCycleDetectedException {
    	 runtime.getPilot().resetInterpretation();
    	 
         //verify_recursive(fileName, ltl);
         verify_iterative(ltl);
     }

     private void verify_recursive(String fileName, String ltl) throws AcceptanceCycleDetectedException {
         ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
         BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
         BuchiRuntime buchiRuntime = new BuchiRuntime(buchiAutomaton);

         KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripkeRuntime, module, buchiRuntime);
         BA_GaiserSchwoon_Recursive verifier = new BA_GaiserSchwoon_Recursive();
         verifier.setRuntime(kbProductSemantics);

         verifier.execute();
     }

     private void verify_iterative(String ltl) throws AcceptanceCycleDetectedException {
         ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
         BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
         BuchiRuntime buchiRuntime = new BuchiRuntime(buchiAutomaton);

         KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripkeRuntime, module, buchiRuntime);

         BA_GaiserSchwoon_Iterative verifier = new BA_GaiserSchwoon_Iterative();
         verifier.setRuntime(kbProductSemantics);

         verifier.execute();
     }
}
