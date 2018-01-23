package plug.languages.buchi.viatcp;


import org.junit.Test;
import plug.core.ILanguageModule;
import plug.core.ILanguageRuntime;
import plug.explorer.AcceptanceCycleDetectedException;
import plug.explorer.BuchiGaiserSchwoon;
import plug.language.buchi.runtime.BuchiRuntime;
import plug.language.buchikripke.runtime.KripkeBuchiProductSemantics;
import plug.language.viatcp.ViaTCPModule;
import plug.language.viatcp.runtime.ViaTCPRuntime;
import properties.BuchiAutomata.BuchiAutomataModel.BuchiDeclaration;


import properties.LTL.parser.Parser;
import properties.LTL.transformations.LTL2Buchi;
import properties.PropositionalLogic.PropositionalLogicModel.DeclarationBlock;
import properties.PropositionalLogic.PropositionalLogicModel.Expression;

import java.io.PrintWriter;

/**
 * Test class of the model AliceBobPetterson.
 * Verify formal properties on the target.
 * 
 * @author valentin
 */
public class KripkeBuchiProductSemanticsTest {
	
	/**
	 * Instance of the fiacre module.
	 */
	ILanguageModule fiacreModule;
	
	/**
	 * Instance of the runtime that interprets the model.
	 */
    ViaTCPRuntime runtime;
    
    /**
     * Constructor of KripkeBuchiProductSemanticsTest.
     * 
     * @return an instance of KripkeBuchiProductSemanticsTest.
     */
	public KripkeBuchiProductSemanticsTest()
    {
    	this.fiacreModule = new ViaTCPModule();
    	this.runtime = new ViaTCPRuntime("localhost", 12121);
    }

	/**
	 * Getter of the runtime that interprets the model.
	 * 
	 * @return the runtime that interprets the model.
	 */
	ILanguageRuntime getViaTCPRuntime() {
		return this.runtime;
	}

	/**
	 * Getter of the Buchi declaration.
	 * @param ltlFormula the ltl formula to verify.
	 * @return the Buchi declaration corresponding to the ltl formula.
	 */
    BuchiDeclaration getBuchiDeclaration(String ltlFormula) {
    	DeclarationBlock propertiesBlock = Parser.parseBlock(ltlFormula);
    	Expression property = propertiesBlock.getDeclarations().iterator().next().getExpression();
    	LTL2Buchi convertor = new LTL2Buchi(new PrintWriter(System.out));

    	BuchiDeclaration decl = convertor.convert(property);
    	return decl;
     }

     @Test
     public void testMutualExclusionOK() {
         String ltl = "exclusion = ![]!(|alice.state == STATE_ALICE_CS| && |bob.state == STATE_BOB_CS|)";
         verify("", ltl);
     }
     
     @Test
     public void testMutualExclusionNOK() {
         String ltl = "exclusion = ![]!(|alice.state == STATE_ALICE_I| && |bob.state == STATE_BOB_I|)";
         verify("", ltl);
     }
     
     @Test
     public void testFairnessPetterson() {
    	 String ltl = "fairness = ! ([] (|alice.flagAlice == 1| -> <> |alice.state == STATE_ALICE_CS|) && (|bob.flagBob == 1| -> <> |bob.state == STATE_BOB_CS|)  )";
         verify("", ltl);
      }

     /*@Test(expected = AcceptanceCycleDetectedException.class)
     public void testMutualExclusionNOK() {
         String ltl = "exclusion = ![]!(|{Alice}1@CS| && |{Bob}1@CS|)";
         verify("tests/resources/AliceBob0.fcr", ltl);
     }*/

    // @Test
    // public void testMutualExclusionOK() {
    //     String ltl = "exclusion = ![]!(|{Alice}1@CS| && |{Bob}1@CS|)";
    //     verify("tests/resources/AliceBob1.fcr", ltl);
    // }

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

     /**
      * Verify a formal property.
      * @param fileName Not used here.
      * @param ltl the ltl formula to verify.
      * @throws AcceptanceCycleDetectedException the exception thown when a cylce is detected.
      */
     private void verify(String fileName, String ltl) throws AcceptanceCycleDetectedException {
         //verify_recursive(fileName, ltl);
         verify_iterative(ltl);
         
         this.runtime.getPilot().close();
     }

     /**
      * Verify a formal property using a recursive algorithm.
      * @param fileName Not used here.
      * @param ltl the ltl formula to verify.
      * @throws AcceptanceCycleDetectedException the exception thown when a cylce is detected.
      */
     private void verify_recursive(String fileName, String ltl) throws AcceptanceCycleDetectedException {
    	 ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
    	 BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
    	 BuchiRuntime buchiRuntime = new BuchiRuntime();

    	 buchiRuntime.buchiAutomaton = buchiAutomaton;

    	 KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripkeRuntime, buchiRuntime);
    	 BuchiGaiserSchwoon verifier = new BuchiGaiserSchwoon();
    	 verifier.setRuntime(kbProductSemantics);

    	 verifier.verify_recursive();
     }

     /**
      * Verify a formal property using an iterative algorithm.
      * @param fileName Not used here.
      * @param ltl the ltl formula to verify.
      * @throws AcceptanceCycleDetectedException the exception thown when a cylce is detected.
      */
     private void verify_iterative(String ltl) throws AcceptanceCycleDetectedException {
         ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
         BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
         BuchiRuntime buchiRuntime = new BuchiRuntime();

         buchiRuntime.buchiAutomaton = buchiAutomaton;

         KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripkeRuntime, buchiRuntime);

         BuchiGaiserSchwoon verifier = new BuchiGaiserSchwoon();
         verifier.setRuntime(kbProductSemantics);

         verifier.verify();
     }
}
