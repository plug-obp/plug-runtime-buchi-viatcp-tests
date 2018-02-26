package plug.languages.buchi.viatcp;


import java.io.PrintWriter;

import plug.core.ILanguageModule;
import plug.core.ILanguageRuntime;
import plug.explorer.buchi.AcceptanceCycleDetectedException;
import plug.explorer.buchi.nested_dfs.BA_GaiserSchwoon_Iterative;
import plug.explorer.buchi.nested_dfs.BA_GaiserSchwoon_Recursive;
import plug.language.buchi.runtime.BuchiRuntime;
import plug.language.buchikripke.runtime.KripkeBuchiProductSemantics;
import plug.language.viatcp.ViaTCPModule;
import plug.language.viatcp.runtime.ViaTCPRuntime;
import properties.BuchiAutomata.BuchiAutomataModel.BuchiDeclaration;
import properties.LTL.parser.Parser;
import properties.LTL.transformations.LTL2Buchi;
import properties.PropositionalLogic.PropositionalLogicModel.DeclarationBlock;
import properties.PropositionalLogic.PropositionalLogicModel.Expression;

public class KripkeBuchiProductSemanticsHelper {
	
	/**
	 * The instance of the language module.
	 */
    private ILanguageModule module;
    
    /**
     * The instance of the model interpreter runtime.
     */
	private ViaTCPRuntime runtime;
    
    /**
     * Constructor of KripkeBuchiProductSemanticsTest.
     * 
     * @return an instance of KripkeBuchiProductSemanticsTest.
     */
    public KripkeBuchiProductSemanticsHelper() {
    	module = new ViaTCPModule();
    	runtime = new ViaTCPRuntime("localhost", 12121);
    }

    /**
     * Getter of the model interpreter runtime.
     * @return the instance of the model interpreter runtime
     */
     public ILanguageRuntime getViaTCPRuntime() {
    	 return runtime;
     }
     
     public ViaTCPRuntime getViaTCPRuntimeRealType() {
    	 return runtime;
     }

     /**
      * Get the Buchi declaration corresponding to a LTL formula.
      * @param ltlFormula the LTL formula.
      * @return the Buchi declaration.
      */
     public BuchiDeclaration getBuchiDeclaration(String ltlFormula) {
         DeclarationBlock propertiesBlock = Parser.parseBlock(ltlFormula);
         Expression property = propertiesBlock.getDeclarations().iterator().next().getExpression();
         LTL2Buchi convertor = new LTL2Buchi(new PrintWriter(System.out));

         BuchiDeclaration decl = convertor.convert(property);
         return decl;
     }
     


     public void verify(String fileName, String ltl) throws AcceptanceCycleDetectedException {
    	 runtime.getPilot().resetInterpretation();
    	 
         //verify_recursive(fileName, ltl);
         verify_iterative(ltl);
     }

     private void verify_recursive(String fileName, String ltl) throws AcceptanceCycleDetectedException {
         ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
         BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
         BuchiRuntime buchiRuntime = new BuchiRuntime(buchiAutomaton);

         KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripkeRuntime, module, buchiRuntime);
         BA_GaiserSchwoon_Recursive verifier = new BA_GaiserSchwoon_Recursive(kbProductSemantics);
         //verifier.setRuntime(kbProductSemantics);

         verifier.execute();
     }

     private void verify_iterative(String ltl) throws AcceptanceCycleDetectedException {
         ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
         BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
         BuchiRuntime buchiRuntime = new BuchiRuntime(buchiAutomaton);

         KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripkeRuntime, module, buchiRuntime);
         BA_GaiserSchwoon_Iterative verifier = new BA_GaiserSchwoon_Iterative(kbProductSemantics);
         //verifier.setRuntime(kbProductSemantics);

         verifier.execute();
     }
}
