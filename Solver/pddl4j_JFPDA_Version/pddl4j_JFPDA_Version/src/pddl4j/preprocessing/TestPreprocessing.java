package pddl4j.preprocessing;

import pddl4j.parser.Domain;
import pddl4j.parser.Parser;
import pddl4j.parser.Problem;

public final class TestPreprocessing {

	/**
	 * The main method of the parser example. The command line syntax is as follow:
	 * <pre>
	 * usage of parser:
	 * 
     * OPTIONS   DESCRIPTIONS
     *
     * -p <str>    path for operator and fact file
     * -o <str>    operator file name
     * -f <str>    fact file name 
     * </pre>
	 * 
	 * @param args the arguments of the command line.
	 */
	public static void main(String[] args) {
		
		try {
			Parser parser = new Parser();
			
			parser.parse(args[1], args[3]);
			Domain domain = parser.getDomain();
			Problem problem = parser.getProblem();
			if (!parser.getErrorManager().isEmpty()) {
				parser.getErrorManager().printAll();
				System.exit(1);
			}
			
			
			
			
			final CodedProblem pb = Preprocessing.encode(domain, problem);
			
			domain.standardize();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
