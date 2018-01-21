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

public class KripkeBuchiProductSemanticsTest {
    ILanguageModule fiacreModule = new ViaTCPModule();

     ILanguageRuntime getViaTCPRuntime() {
         return new ViaTCPRuntime("localhost", 12345);
     }

     BuchiDeclaration getBuchiDeclaration(String ltlFormula) {
         DeclarationBlock propertiesBlock = Parser.parseBlock(ltlFormula);
         Expression property = propertiesBlock.getDeclarations().iterator().next().getExpression();
         LTL2Buchi convertor = new LTL2Buchi(new PrintWriter(System.out));

         BuchiDeclaration decl = convertor.convert(property);
         return decl;
     }

     @Test(expected = AcceptanceCycleDetectedException.class)
     public void testMutualExclusionNOK() {
         String ltl = "exclusion = ![]!(|{Alice}1@CS| && |{Bob}1@CS|)";
         verify("tests/resources/AliceBob0.fcr", ltl);
     }

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

     private void verify(String fileName, String ltl) throws AcceptanceCycleDetectedException {
         //verify_recursive(fileName, ltl);
         verify_iterative(ltl);
     }

    // private void verify_recursive(String fileName, String ltl) throws AcceptanceCycleDetectedException {
    //     ILanguageRuntime kripkeRuntime = getFiacreRuntime(fileName);
    //     BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
    //     BuchiRuntime buchiRuntime = new BuchiRuntime();

    //     buchiRuntime.buchiAutomaton = buchiAutomaton;

    //     KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripkeRuntime, buchiRuntime);
    //     BuchiGaiserSchwoon verifier = new BuchiGaiserSchwoon();
    //     verifier.setRuntime(kbProductSemantics);

    //     verifier.verify_recursive();
    // }

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
