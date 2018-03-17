package plug.languages.buchi.viatcp;

import java.io.PrintWriter;
import org.junit.Assert;
import plug.core.ILanguagePlugin;
import plug.core.ILanguageRuntime;
import plug.core.RuntimeDescription;
import plug.events.PropertyEvent;
import plug.explorer.buchi.nested_dfs.BA_GaiserSchwoon_Iterative;
import plug.explorer.buchi.nested_dfs.BA_GaiserSchwoon_Recursive;
import plug.language.buchi.runtime.BuchiRuntime;
import plug.language.buchikripke.runtime.KripkeBuchiProductSemantics;
import plug.language.viatcp.ViaTCPPlugin;
import plug.language.viatcp.runtime.ViaTCPRuntime;
import plug.statespace.SimpleStateSpaceManager;
import properties.BuchiAutomata.BuchiAutomataModel.BuchiDeclaration;
import properties.LTL.parser.Parser;
import properties.LTL.transformations.LTL2Buchi;
import properties.PropositionalLogic.PropositionalLogicModel.DeclarationBlock;
import properties.PropositionalLogic.PropositionalLogicModel.Expression;

public class KripkeBuchiProductSemanticsHelper {
	
	/**
	 * The instance of the language module.
	 */
    private ILanguagePlugin module;
    
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
    	module = new ViaTCPPlugin();
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
     


     public void verify(String fileName, String ltl, boolean verified) {
         //verify_recursive(fileName, ltl);
         verify_iterative(ltl, verified);
     }

     private void verify_recursive(String fileName, String ltl, boolean verified) {
         ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
         BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
         BuchiRuntime buchiRuntime = new BuchiRuntime(buchiAutomaton);
         RuntimeDescription kripke = new RuntimeDescription(kripkeRuntime, module);

         KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripke, buchiRuntime);
         BA_GaiserSchwoon_Recursive verifier = new BA_GaiserSchwoon_Recursive(kbProductSemantics);

         boolean[] result = new boolean[] { true };
         verifier.getAnnouncer().when(PropertyEvent.class, (announcer, event) -> {
             result[0] &= event.isVerified();
         });
         verifier.execute();

         if (result[0] != verified) {
             Assert.fail("Property " + (result[0] ? "is verified but shouldn't" : "isn't verified but should"));
         }
     }

     private void verify_iterative(String ltl, boolean verified) {
         ILanguageRuntime kripkeRuntime = getViaTCPRuntime();
         BuchiDeclaration buchiAutomaton = getBuchiDeclaration(ltl);
         BuchiRuntime buchiRuntime = new BuchiRuntime(buchiAutomaton);
         RuntimeDescription kripke = new RuntimeDescription(kripkeRuntime, module);

         KripkeBuchiProductSemantics kbProductSemantics = new KripkeBuchiProductSemantics(kripke, buchiRuntime);

         BA_GaiserSchwoon_Iterative verifier = new BA_GaiserSchwoon_Iterative(kbProductSemantics, new SimpleStateSpaceManager());

         boolean[] result = new boolean[] { true };
         verifier.getAnnouncer().when(PropertyEvent.class, (announcer, event) -> {
             result[0] &= event.isVerified();
         });
         verifier.execute();

         if (result[0] != verified) {
             Assert.fail("Property " + (result[0] ? "is verified but shouldn't" : "isn't verified but should"));
         }
     }
}
