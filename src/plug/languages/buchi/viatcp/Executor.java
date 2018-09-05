package plug.languages.buchi.viatcp;

import org.junit.runner.JUnitCore;

/**
 * Class used to laucnh the execution of a test suite.
 * 
 * @author Valentin Besnard (valentin.besnard@eseo.fr)
 */
public class Executor {
	/**
	 * Main method to launch a test suite for a given model.
	 * 
	 * @param args
	 *            args[0] is the name of the model to test.
	 */
	public static void main(String[] args) {
		String modelName = args[0];
		if (modelName != null) {
			JUnitCore.main("plug.languages.buchi.viatcp." + modelName + "Test");
		} else {
			System.err.println("Model name (first argument) is null.");
		}
	}
}
