/*
 * Copyright MAGMA TEAM Laboratory Leibniz.
 *
 * This software is a computer program whose purpose is to propose an
 * environment for multi-agent planning.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package core.planner;

import core.planner.lexer.ParseException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;

/**
 * The <code>Planner</code> class implements the main class of the Java Assumption-based Planner.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Planner {
	
	/**
	 * The constant for the size of the default assumptions window.
	 */
	public static final int DEFAULT_ASSUMPTIONS_WINDOW = 0;
	
	/**
	 * The constant of the default log level.
	 */
	public static final int DEFAULT_LOG_LEVEL = 1;
	
	/**
	 * The constant of the default number of conjectures to find.
	 */
	public static final int DEFAULT_NUMBER_OF_CONJECTURES = 1;
	
	/**
	 * The constant of the default negation with problem.
	 */
	public static final boolean DEFAULT_PROBLEM_WITH_NEGATION = false;
	
	/**
	 * The constant of the default parse option.
	 */
	public static final boolean DEFAULT_PARSE = false;
	
	/**
	 * Milliseconds constant.
	 */
	private static final int MILLISECOND = 1000;
	
	/**
	 * The <tt>Domain</tt> description.
	 */
	private Domain domain;
	
	/**
	 * The <tt>Problem</tt> description.
	 */
	private Problem problem;
	
	/**
	 * Creates a new <tt>Planner</tt> with a specific file.
	 *
	 * @param d the <tt>Domain</tt> description.
	 * @param p the <tt>Problem</tt> description
	 */
	public Planner(final Domain d, final Problem p) {
		setPlanningDomain(d);
		setPlanningProblem(p);
	}
	
	/**
	 * Finds the more reasonnable <tt>Conjecture</tt>.
	 *
	 * @param window the size of the assumptions window.
	 * @param negation to indicate that the <tt>Problem</tt> use negative facts.
	 * @return the list of <tt>Conjecture</tt> s found or <tt>null</tt> otherwise.
	 */
	public Conjecture[] findAllConjectures(final int window, final boolean negation) {
		return findConjectures(Integer.MAX_VALUE, window, negation);
	}
	
	/**
	 * Finds the more reasonnable <tt>Conjecture</tt>.
	 *
	 * @param window the size of the assumptions window.
	 * @param nbConj the number of <tt>Conjecture</tt> s to find.
	 * @param negation to indicate that the <tt>Problem</tt> use negative facts.
	 * @return the list of <tt>Conjecture</tt> s found or <tt>null</tt> otherwise.
	 */
	public Conjecture[] findConjectures(final int nbConj, final int window, final boolean negation) {
		assert nbConj > 0 : "Assertion: parameter <nbConj> > 0";
		assert window >= 0 : "Assertion: parameter <window> >= 0";
		// Initialization of the conjecture tree
		final ConjectureTree tree = new ConjectureTree(domain, problem);
		// Expansion of the conjecture tree
		tree.expand(tree.getRootNode(), nbConj, window, negation);
		// Extraction of the conjectures
		final Conjecture[] conjectures = tree.extractConjectures();
		return conjectures;
	}
	
	/**
	 * Sets the planning <tt>Domain</tt> of this <tt>Planner</tt>.
	 *
	 * @param d the <tt>Domain</tt> of this <tt>Planner</tt>.
	 */
	public void setPlanningDomain(final Domain d) {
		assert d != null : "Assertion: parameter <d> == null";
		domain = d;
	}
	
	/**
	 * Returns the planning <tt>Domain</tt> of this <tt>Planner</tt>.
	 *
	 * @return the planning <tt>Domain</tt> of this <tt>Planner</tt>.
	 */
	public Domain getPlanningDomain() {
		return domain;
	}
	
	/**
	 * Sets the planning <tt>Problem</tt> of this <tt>Planner</tt>.
	 *
	 * @param p the <tt>Problem</tt> of this <tt>Planner</tt>.
	 */
	public void setPlanningProblem(final Problem p) {
		assert p != null : "Assertion: parameter <p> == null";
		problem = p;
	}
	
	/**
	 * Returns the planning <tt>Problem</tt> of this <tt>Planner</tt>.
	 *
	 * @return the planning <tt>Problem</tt> of this <tt>Planner</tt>.
	 */
	public Problem getPlanningProblem() {
		return problem;
	}
	
	/**
	 * Main method of the <tt>Planner</tt>.
	 *
	 * @param args the Planner arguments: [Usage Planner domain-file problem-file log-level]. The argument log-level is optionnal. It allows to have more log
	 *        details while the domain debugging.
	 */
	public static void main(final String[] args) {
		
		String domainPath = null;
		String problemPath = null;
		String problemName = null;
		final String agentPath = null;
		int window = DEFAULT_ASSUMPTIONS_WINDOW;
		Logger.setLevel(DEFAULT_LOG_LEVEL);
		int nbc = DEFAULT_NUMBER_OF_CONJECTURES;
		boolean negation = DEFAULT_PROBLEM_WITH_NEGATION;
		
		// Check command line
		for (int i = 0; i < args.length; i = i + 2) {
			if (args[i].equals("-d")) {
				if (args.length <= i + 1) {
					displayUsage();
				}
				domainPath = args[i + 1];
			} else if (args[i].equals("-s")) {
				if (args.length <= i + 1) {
					displayUsage();
				}
				problemName = args[i + 1];
			} else if (args[i].equals("-p")) {
				if (args.length <= i + 1) {
					displayUsage();
				}
				problemPath = args[i + 1];
			} else if (args[i].equals("-w")) {
				if (args.length <= i + 1) {
					displayUsage();
				}
				try {
					window = Integer.parseInt(args[i + 1]);
					if (window < 0) {
						displayUsage();
					}
				} catch (final NumberFormatException nfe) {
					displayUsage();
				}
			} else if (args[i].equals("-l")) {
				if (args.length <= i + 1) {
					displayUsage();
				}
				try {
					final int logLevel = Integer.parseInt(args[i + 1]);
					if ((logLevel < -1) || (logLevel > 9)) {
						displayUsage();
					}
					Logger.setLevel(logLevel);
				} catch (final NumberFormatException nfe) {
					displayUsage();
				}
			} else if (args[i].equals("-n")) {
				if (args.length <= i + 1) {
					displayUsage();
				}
				if (args[i + 1].equals("all")) {
					nbc = Integer.MAX_VALUE;
				} else {
					try {
						nbc = Integer.parseInt(args[i + 1]);
						if (nbc < 1) {
							displayUsage();
						}
					} catch (final NumberFormatException nfe) {
						displayUsage();
					}
				}
			} else if (args[i].equals("-f")) {
				negation = true;
				i--;
			} else if (args[i].equals("-h")) {
				displayUsage();
			} else {
				displayUsage();
			}
		}
		
		if (domainPath == null || problemPath == null) {
			displayUsage();
		}
		
		// Parse domain and problems
		ProblemSet problems = null;
		Domain domain = null;
		try {
			final Parser parser = new Parser(new FileInputStream(domainPath));
			domain = parser.readDomain();
			Logger.log(1, "Domain ", domainPath, " parsed successfully.");
		} catch (final ParseException pe) {
			Logger.error("parsing domain file: ", pe.getMessage());
			System.exit(1);
		} catch (final FileNotFoundException fnfe) {
			Logger.error("parsing domain file: ", domainPath + " not found.");
			System.exit(1);
		}
		try {
			final Parser parser = new Parser(new FileInputStream(problemPath));
			problems = parser.readProblems();
			Logger.log(1, "Problem ", problemPath, " parsed successfully.");
		} catch (final ParseException pe) {
			Logger.error("parsing problem file: ", pe.getMessage());
			System.exit(1);
		} catch (final FileNotFoundException fnfe) {
			Logger.error("parsing problem file: ", problemPath, " not found.");
			System.exit(1);
		}
		
		// Solves problems
		if (problems.isEmpty()) {
			Logger.log(1, "No problem to solve");
			System.exit(0);
		}
		
		if (problemName == null) {
			for (final Problem problem : problems) {
				solve(domain, problem, nbc, window, negation);
			}
		} else {
			final Problem problem = problems.getProblemWithName(problemName);
			if (problem == null) {
				Logger.log(1, "Problem ", problemName, " not found in file ", problemPath, ".");
			} else {
				solve(domain, problem, nbc, window, negation);
			}
		}
	}
	
	/**
	 * Solve a specific <tt>Domain</tt> and <tt>Problem</tt>.
	 *
	 * @param domain the <tt>Domain</tt>.
	 * @param problem the <tt>Problem</tt> to solve.
	 * @param nbc the number of <tt>Conjecture</tt> q to found.
	 * @param window the number of <tt>Assumption</tt> allowed.
	 * @param negation flag to indicate if negation are use in the <tt>Problem</tt>.
	 */
	private static void solve(final Domain domain, final Problem problem, final int nbc, final int window, final boolean negation) {
		try {
			Logger.log(1, "----------------------------------------------");
			Logger.log(1, "Solving Problem: ", problem.getName());
			if (problem.getTasks().isEmpty()) {
				Logger.log(1, "Problem ", problem.getName(), " no task to do.");
			} else {
				final Planner planner = new Planner(domain, problem);
				// Searches for a valid conjecture
				final long startTime = Calendar.getInstance().getTimeInMillis();
				final Conjecture[] conjectures = planner.findConjectures(nbc, window, negation);
				final long endTime = Calendar.getInstance().getTimeInMillis();
				final Long duration = new Long(endTime - startTime);
				final double seconds = duration.doubleValue() / MILLISECOND;
				printResult(problem.getName(), conjectures, seconds);
			}
		} catch (final Exception e) {
		}
	}
	
	/**
	 * Print results.
	 *
	 * @param conjectures the list of <tt>Conjecture</tt> s computed.
	 * @param time the time of computation.
	 */
	private static void printResult(final String problemName, final Conjecture[] conjectures, final double time) {
		if (Logger.level == -1) {
			if (conjectures != null) {
				System.out.printf("%5s %8.2f %5d\n", problemName, time, conjectures[0].size());
			} else {
				System.out.printf("%5s %8.2f %5d\n", problemName, "-", "-");
			}
		} else if (conjectures == null) {
			Logger.log(1, "----------------------------------------------");
			Logger.log(1, "No solution found in " + time + " s");
		} else {
			Logger.log(1, "----------------------------------------------");
			Logger.log(1, "Conjectures found in " + time + " s:");
			System.out.println("size: " + conjectures.length);
			for (int i = 0; i < conjectures.length; i++) {
				final Conjecture conjecture = conjectures[i];
				Logger.log(1, "Conjecture # " + (i + 1));
				Logger.log(1, conjecture.toString());
			}
		}
	}
	
	/**
	 * Displays the command ligne usage of <tt>Planner</tt>.
	 */
	private static void displayUsage() {
		Logger.error("[Usage Planner [-f;h] -d <domain> -p <problem> -n " + "<number> -w <window> -l <log-level>] :\n" + "  -d  the path of the file containing the domain description.\n" + "  -p  the path of the file containing the problems " + "description.\n" + "  -s  the name of problem to solve. If the name is not " + "specified all problems are solved.\n" + "  -n  use this option to express the number of conjectures to " + "find. This number\n" + "      must be greater than 1. Use -n all to find all " + "conjectures.\n" + "  -f  use this option if problem use negative facts.\n" + "  -l  the log level for debuging. The log level parameter is " + "into the interval [0;10].\n" + "      0 for indicating no output. The default value is 1.\n" + "  -w  the size of the assumption window. The window parameter " + "cannot be negative.\n" + "      The default value is 0 (i.e., no assumption must be " + "done).\n" + "  -h  Display this help information.\n");
		System.exit(0);
	}
	
}
