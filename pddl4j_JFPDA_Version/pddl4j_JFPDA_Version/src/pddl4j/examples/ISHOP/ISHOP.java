/*
 * Copyright (c) 2015 by Abdeldjalil Ramoul <abdeldjalil.ramoul@imag.fr>.
 *
 * This file is part of PDDL4J library.
 *
 * PDDL4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PDDL4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PDDL4J.  If not, see <http://www.gnu.org/licenses/>
 */

package pddl4j.examples.ISHOP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import pddl4j.heuristics.Heuristic;
import pddl4j.parser.Domain;
import pddl4j.parser.Parser;
import pddl4j.parser.Problem;
import pddl4j.preprocessing.CodedProblem;
import pddl4j.preprocessing.Preprocessing;
import pddl4j.util.BitVector;
import pddl4j.util.OneTaskConstraint;
import pddl4j.util.OrderedMethod;
import pddl4j.util.Signature;
import pddl4j.util.TaskNetwork;

/**
 * This class implements a simple forward planner based on A* algorithm.
 * 
 * @author A. Ramoul
 * @version 1.0 - 14.06.2010
 */
public final class ISHOP {
	
	/**
	 * The default heuristic.
	 */
	private static final Heuristic.Type DEFAULT_HEURISTIC = Heuristic.Type.FAST_FORWARD;
	
	/**
	 * The default CPU time allocated to the search in seconds.
	 */
	private static final int DEFAULT_CPU_TIME = 1800;
	
	/**
	 * The default weight of the heuristic.
	 */
	private static final double DEFAULT_WHEIGHT = 1.00;
	
	/**
	 * The default trace level.
	 */
	private static final int DEFAULT_TRACE_LEVEL = 1;
	
	private final boolean COMMENT = false;
	
	/**
	 * The enumeration of the arguments of the planner.
	 */
	private enum Argument {
		/** The planning domain. */
		DOMAIN,
		/** The planning problem. */
		PROBLEM,
		/** The heuristic to use. */
		HEURISTIC_TYPE,
		/** The weight of the heuristic. */
		WEIGHT,
		/** The global time slot allocated to the search. */
		CPU_TIME,
		/** The trace level. */
		TRACE_LEVEL
	}
	
	/**
	 * The time needed to search a solution plan.
	 */
	private long searching_time;
	
	/**
	 * The time needed to encode the planning problem.
	 */
	private long preprocessing_time;
	
	/**
	 * The memory used in bytes to search a solution plan.
	 */
	private long searching_memory;
	
	/**
	 * The memory used in bytes to encode problem.
	 */
	private long problem_memory;
	
	/**
	 * The number of node explored.
	 */
	private int nb_of_explored_nodes;
	
	/**
	 * The arguments of the planner.
	 */
	private final Properties arguments;
	
	private Scanner _sc;
	
	/**
	 * Creates a new planner.
	 * 
	 * @param arguments the arguments of the planner.
	 */
	private ISHOP(final Properties arguments) {
		this.arguments = arguments;
	}
	
	/**
	 * This method parses the PDDL files and encodes the corresponding planning problem into a compact representation.
	 * 
	 * @return the encoded problem.
	 */
	public CodedProblem parseAndEncode() {
		final Parser parser = new Parser();
		final String ops = (String) arguments.get(ISHOP.Argument.DOMAIN);
		final String facts = (String) arguments.get(ISHOP.Argument.PROBLEM);
		try {
			parser.parse(ops, facts);
		} catch (final FileNotFoundException e) {
		}
		if (!parser.getErrorManager().isEmpty()) {
			parser.getErrorManager().printAll();
			System.exit(0);
		}
		final Domain domain = parser.getDomain();
		final Problem problem = parser.getProblem();
		final int traceLevel = (Integer) arguments.get(ISHOP.Argument.TRACE_LEVEL);
		if (traceLevel > 0 && traceLevel != 8) {
			System.out.println();
			System.out.println("Parsing domain file \"" + new File(ops).getName() + "\" done successfully");
			System.out.println("Parsing problem file \"" + new File(facts).getName() + "\" done successfully\n");
		}
		if (traceLevel == 8) {
			Preprocessing.setLogLevel(0);
		} else {
			Preprocessing.setLogLevel(Math.max(0, traceLevel - 1));
		}
		final long begin = System.currentTimeMillis();
		final CodedProblem pb = Preprocessing.encode(domain, problem);
		final long end = System.currentTimeMillis();
		preprocessing_time = end - begin;
		// this.problem_memory = MemoryAgent.deepSizeOf(pb);
		return pb;
	}
	
	/**
	 * Search a solution plan to a specified domain and problem.
	 * 
	 * @param pb the problem to solve.
	 */
	public void search(final CodedProblem pb) {
		
		List<LinkedList<String>> plan = null;
		if (pb.getInitExpansion() != null) {
			plan = planSearch(pb);
		}
		
		// The following is just to print the result
		final int traceLevel = (Integer) arguments.get(ISHOP.Argument.TRACE_LEVEL);
		int methodsSize = 0;
		for (final Signature sign : pb.getMethods().keySet()) {
			methodsSize += pb.getMethods().get(sign).size();
		}
		if (traceLevel > 0 && traceLevel != 8) {
			if (pb.isHtnSolvable()) {
				if (plan != null) {
					System.out.printf("\nFound plan as follows:\n\n");
					System.out.println("Tasks:");
					for (int i = 0; i < plan.get(0).size(); i++) {
						System.out.printf(" %4d: %s\n", i, plan.get(0).get(i));
					}
				} else {
					System.out.printf("\nNo plan found\n\n");
				}
			} else {
				System.out.printf("goal can be simplified to FALSE. no plan will solve it\n\n");
			}
			System.out.printf("\nNumber of explored nodes: " + nb_of_explored_nodes);
			System.out.printf("\ntime spent: %8.2f seconds preprocessing (" + pb.getOperators().size() + " operator, " + methodsSize + " method, " + pb.getRelevantFacts().size() + " fact, " + pb.getRelevantTasks().size() + " task)\n", (preprocessing_time / 1000.0));
			System.out.printf("            %8.2f seconds searching\n", (searching_time / 1000.0));
			System.out.printf("            %8.2f seconds total time\n", ((preprocessing_time + searching_time) / 1000.0));
			System.out.printf("\n");
			System.out.printf("memory used: %8.2f MBytes for problem representation\n", +(problem_memory / (1024.0 * 1024.0)));
			System.out.printf("             %8.2f MBytes for searching\n", +(searching_memory / (1024.0 * 1024.0)));
			System.out.printf("             %8.2f MBytes total\n", +((problem_memory + searching_memory) / (1024.0 * 1024.0)));
			System.out.printf("\n\n");
			
		}
		if (traceLevel == 8) {
			final String problem = (String) arguments.get(ISHOP.Argument.PROBLEM);
			final String[] str_array = problem.split("/");
			final String pb_file = str_array[str_array.length - 1];
			final String pb_name = pb_file.substring(0, pb_file.indexOf("."));
			//System.out.println("Name	Facts	Tasks	Operators	Methods	Preprocessing_time	Problem_memory	Searching_time	Total_time	Search_memory	Total_memory	Nb_nodes	Plan_length");
			
			System.out.printf("%5s %8d %8d %8d %8d %8.2f %8.2f", pb_name, pb.getRelevantFacts().size(), pb.getRelevantTasks().size(), pb.getOperators().size(), methodsSize, (preprocessing_time / 1000.0), (problem_memory / (1024.0 * 1024.0)));
			if (plan != null) {
				System.out.printf("%8.2f %8.2f %8.2f %8.2f %8d %5d\n", (searching_time / 1000.0), ((preprocessing_time + searching_time) / 1000.0), (searching_memory / (1024.0 * 1024.0)), ((problem_memory + searching_memory) / (1024.0 * 1024.0)), nb_of_explored_nodes, plan.get(0).size());
			} else {
				System.out.printf("%8s %8s %8s %8s %8s %5s\n", "-", "-", "-", "-", "-", "-");
			}
		}
	}
	
	/**
	 * Solves the planning problem and returns the first solution plan found. This method must be completed.
	 * 
	 * @param problem the coded planning problem to solve.
	 * @return a solution plan or null if it does not exist.
	 */
	private List<LinkedList<String>> planSearch(final CodedProblem problem) {
		final long begin = System.currentTimeMillis();
		final int tasksNumber = problem.getRelevantTasks().size();
		ExplanationManager.run();
		InteractionManager.run(0);
		
		// Get the initial expansion
		final TaskNetwork initExpansion = problem.getInitExpansion();
		
		// Get the goal state from the intiExpansion
		final BitVector goalState = new BitVector(initExpansion.getAfterConstraints().size());
		for (final OneTaskConstraint afterCon : initExpansion.getAfterConstraints()) {
			final int predicate = afterCon.getGroundPredicate();
			if (problem.isPositiveLiteral(predicate)) {
				goalState.set(predicate);
			}
		}
		
		// Create the priority Open list containing the pending nodes
		final List<ISHOPNode> openList = new LinkedList<>();
		
		// The list of primitive tasks (the plan)
		List<LinkedList<String>> plan = null;
		
		// Test if the expansion is not empty
		if (initExpansion.getTasks() == null) {
			return plan;
		} else {
			// Creates the root node of the tree search
			final ISHOPNode root = new ISHOPNode(initExpansion.getTasks(), problem.getInit());
			// Add the root to the list of pending nodes
			openList.add(root);
		}
		final int CPUTime = (Integer) arguments.get(ISHOP.Argument.CPU_TIME);
		
		////////////////////////////////////////////////////////////////////////////////
		//// 														Start Search 																////
		////////////////////////////////////////////////////////////////////////////////
		
		nb_of_explored_nodes = 0;
		
		while (!openList.isEmpty() && plan == null && searching_time < CPUTime) {
			
			//// Pop the first node in the pending list open ////
			final int last = openList.size() - 1;
			final ISHOPNode currentNode = openList.get(last);
			openList.remove(last);
			nb_of_explored_nodes++;
			// If there is composed tasks in the current node
			if (currentNode.get_statePosition() != currentNode.getExpansion().size()) {
				final int currentTaskPosition = currentNode.get_statePosition();
				//// Choose a composed task to decompose ////
				final int currentTask = currentNode.getExpansion().get(currentTaskPosition);
				final int absoluteCurrentTask = problem.getAbsoluteIndex(currentTask);
				if (COMMENT) {
					System.out.println("-- CURRENT TASK: " + problem.printTask(currentTask));
				}
				
				//// Decompose the task with methods chosen by the user verifying preconditions////
				final List<OrderedMethod> relevantMethods = InteractionManager.askForMethods(problem.getRelevantMethods(absoluteCurrentTask), absoluteCurrentTask, problem);
				if (relevantMethods != null) {
					for (final OrderedMethod relevantMethod : relevantMethods) {
						if (COMMENT) {
							System.out.println("-- RELEVANT METHOD: " + relevantMethod.getName() + "\n" + problem.printMethod(relevantMethod));
						}
						if (currentNode.get_state().include(relevantMethod.getPositivePreconditions()) && currentNode.get_state().exclude(relevantMethod.getNegativePreconditions())) {
							if (COMMENT) {
								System.out.println("--> ACCEPTED ");
							}
							// Create a new taskNetwork
							ISHOPNode newShopNode;
							newShopNode = new ISHOPNode(currentNode);
							// Remove the current task from the Task Network node
							newShopNode.removeTask(currentTask);
							// Replace the current task in the currentNode with the expansion of the relevant Method
							final List<Integer> newAbsoluteExpansion = new ArrayList<>(relevantMethod.getExpansion());
							final List<Integer> newRelativeExpansion = new ArrayList<>(newShopNode.getRelativeIndexes(newAbsoluteExpansion, tasksNumber));
							final boolean noPreconditionError = newShopNode.addTasks(newRelativeExpansion, currentTaskPosition, tasksNumber, problem);
							if (COMMENT) {
								System.out.println("-- ADDED Expansion\n " + newShopNode.toString(problem));
							}
							if (noPreconditionError) {
								openList.add(newShopNode);
								if (COMMENT) {
									System.out.println("--> ADDED node to Open List");
								}
							} else {
								if (COMMENT) {
									System.out.println("--> NOT ADDED node to Open List");
								}
							}
							
						} else {
							if (COMMENT) {
								System.out.println("--> REJECTED ");
							}
						}
					}
				}
			} else if (currentNode.get_state().include(goalState)) {
				plan = extract(currentNode, problem);
			}
			// Update SearchTime
			searching_time = System.currentTimeMillis() - begin;
			try {
				Thread.sleep(0);
				// System.out.println("Open list size: "+ openList.size());
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (COMMENT) {
				System.out.println("------------------------------------------------------\n" + "|		Open list size: " + openList.size() + "			|\n" + "------------------------------------------------------");
				final Scanner scaner = new Scanner(System.in);
				final String s = scaner.nextLine();
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////
		//// 														End Search																	////
		////////////////////////////////////////////////////////////////////////////////
		
		// Take time to compute the searching time
		final long end = System.currentTimeMillis();
		// Compute the searching time
		searching_time = end - begin;
		
		//ExplanationManager.printActionsRejectList(problem);
		//ExplanationManager.printMethodsRejectList(problem);
		//ExplanationManager.printTasksRejectList(problem);
		
		// return the plan computed or null if no plan was found
		return plan;
	}
	
	/**
	 * Extracts a plan from a specified node.
	 * 
	 * @param node the node.
	 * @param problem the problem.
	 * @return the plan (tasks + order constraints) extracted from the specified node.
	 */
	private List<LinkedList<String>> extract(final ISHOPNode node, final CodedProblem problem) {
		
		final LinkedList<String> tasks = new LinkedList<>();
		final LinkedList<String> order = new LinkedList<>();
		final List<LinkedList<String>> plan = new ArrayList<>(2);
		for (final Integer relativeIndex : node.getExpansion()) {
			tasks.add(problem.printTask(relativeIndex));
		}
		plan.add(tasks);
		//		for (final OrderConstraint orderCon : node.getOrderingConstraints()) {
		//			order.add(orderCon.toString(node, problem));
		//		}
		plan.add(order);
		return plan;
	}
	
	/**
	 * The main method of the <code>AHSP</code> example. The command line syntax is as follow:
	 * 
	 * <pre>
	 * usage of SHOP:
	 * 
	 * OPTIONS   DESCRIPTIONS
	 * 
	 * -o    operator file name
	 * -f    fact file name
	 * -w    the weight used in the a star search (preset: 1)
	 * -t    specifies the maximum CPU-time in seconds (preset: 300)
	 * -u    specifies the heuristic to used (preset: 0)
	 *      0      ff heuristic
	 *      1      sum heuristic
	 *      2      sum mutex heuristic
	 *      3      adjusted sum heuristic
	 *      4      adjusted sum 2 heuristic
	 *      5      adjusted sum 2M heuristic
	 *      6      combo heuristic
	 *      7      max heuristic
	 *      8      set-level heuristic
	 * -i    run-time information level (preset: 1)
	 *      0      nothing
	 *      1      info on action number, search and plan
	 *      2      1 + info on problem constants, types and predicates
	 *      3      1 + 2 + loaded operators, initial and goal state
	 *      4      1 + predicates and their inertia status
	 *      5      1 + 4 + goal state and operators with unary inertia encoded
	 *      6      1 + actions, initial and goal state after expansion of variables
	 *      7      1 + final domain representation
	 *      8      line representation:
	 *                - problem name
	 *                - number of operators
	 *               - number of facts
	 *                - preprocessing time in seconds
	 *                - memory used for problem representation in MBytes
	 *                - number of states explored
	 *                - searching time in seconds
	 *                - memory used for searching in MBytes
	 *                - global memory used in MBytes
	 *                - solution plan length
	 *      100   1 + various debugging information
	 * -h          print this message
	 * </pre>
	 * 
	 * @param args the arguments of the command line.
	 */
	public static void main(final String[] args) {
		
		final Signature sign = new Signature("test", 80);
		// Parse the command line
		final Properties arguments = ISHOP.parseArguments(args);
		// Create the planner
		final ISHOP planner = new ISHOP(arguments);
		// Parse and encode the PDDL file into compact representation
		final CodedProblem problem = planner.parseAndEncode();
		// Search for a solution and print the result
		planner.search(problem);
	}
	
	/**
	 * This method parse the command line and return the arguments.
	 * 
	 * @param args the arguments from the command line.
	 * @return The arguments of the planner.
	 */
	private static Properties parseArguments(final String[] args) {
		final Properties arguments = ISHOP.getDefaultArguments();
		try {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-o") && ((i + 1) < args.length)) {
					arguments.put(ISHOP.Argument.DOMAIN, args[i + 1]);
					if (!new File(args[i + 1]).exists()) {
						System.out.println("operators file does not exist");
						System.exit(0);
					}
					i++;
				} else if (args[i].equalsIgnoreCase("-f") && ((i + 1) < args.length)) {
					arguments.put(ISHOP.Argument.PROBLEM, args[i + 1]);
					if (!new File(args[i + 1]).exists()) {
						System.out.println("facts file does not exist");
						System.exit(0);
					}
					i++;
				} else if (args[i].equalsIgnoreCase("-t") && ((i + 1) < args.length)) {
					final int cpu = Integer.valueOf(args[i + 1]) * 1000;
					if (cpu < 0) {
						ISHOP.printUsage();
					}
					i++;
					arguments.put(ISHOP.Argument.CPU_TIME, cpu);
				} else if (args[i].equalsIgnoreCase("-u") && ((i + 1) < args.length)) {
					final int heuristic = Integer.valueOf(args[i + 1]);
					if (heuristic < 0 || heuristic > 8) {
						ISHOP.printUsage();
					}
					if (heuristic == 0) {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.FAST_FORWARD);
					} else if (heuristic == 1) {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.SUM);
					} else if (heuristic == 2) {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.SUM_MUTEX);
					} else if (heuristic == 3) {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.AJUSTED_SUM);
					} else if (heuristic == 4) {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.AJUSTED_SUM2);
					} else if (heuristic == 5) {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.AJUSTED_SUM2M);
					} else if (heuristic == 6) {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.COMBO);
					} else if (heuristic == 7) {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.MAX);
					} else {
						arguments.put(ISHOP.Argument.HEURISTIC_TYPE, Heuristic.Type.SET_LEVEL);
					}
					i++;
				} else if (args[i].equalsIgnoreCase("-w") && ((i + 1) < args.length)) {
					final double weight = Double.valueOf(args[i + 1]);
					if (weight < 0) {
						ISHOP.printUsage();
					}
					arguments.put(ISHOP.Argument.WEIGHT, weight);
					i++;
				} else if (args[i].equalsIgnoreCase("-i") && ((i + 1) < args.length)) {
					final int level = Integer.valueOf(args[i + 1]);
					if (level < 0) {
						ISHOP.printUsage();
					}
					arguments.put(ISHOP.Argument.TRACE_LEVEL, level);
					i++;
				} else {
					ISHOP.printUsage();
				}
			}
			if (arguments.get(ISHOP.Argument.DOMAIN) == null || arguments.get(ISHOP.Argument.PROBLEM) == null) {
				ISHOP.printUsage();
			}
		} catch (final Throwable t) {
			ISHOP.printUsage();
		}
		return arguments;
	}
	
	/**
	 * This method print the usage of the command-line planner.
	 */
	private static void printUsage() {
		System.out.println("\nusage of iSHOP:\n");
		System.out.println("OPTIONS   DESCRIPTIONS\n");
		System.out.println("-o <str>    operator file name");
		System.out.println("-w <num>    the weight used in the a star seach (preset: 1)");
		System.out.println("-t <num>    specifies the maximum CPU-time in seconds (preset: 300)");
		System.out.println("-u <num>    specifies the heuristic to used (preset: 0)");
		System.out.println("     0      ff heuristic");
		System.out.println("     1      sum heuristic");
		System.out.println("     2      sum mutex heuristic");
		System.out.println("     3      adjusted sum heuristic");
		System.out.println("     4      adjusted sum 2 heuristic");
		System.out.println("     5      adjusted sum 2M heuristic");
		System.out.println("     6      combo heuristic");
		System.out.println("     7      max heuristic");
		System.out.println("     8      set-level heuristic");
		System.out.println("-i <num>    run-time information level (preset: 1)");
		System.out.println("     0      nothing");
		System.out.println("     1      info on action number, search and plan");
		System.out.println("     2      1 + info on problem constants, types and predicates");
		System.out.println("     3      1 + 2 + loaded operators, initial and goal state");
		System.out.println("     4      1 + predicates and their inertia status");
		System.out.println("     5      1 + 4 + goal state and operators with unary inertia encoded");
		System.out.println("     6      1 + actions, initial and goal state after expansion of variables");
		System.out.println("     7      1 + final domain representation");
		System.out.println("     8      line representation:");
		System.out.println("               - problem name");
		System.out.println("               - number of operators");
		System.out.println("               - number of facts");
		System.out.println("               - preprocessing time in seconds");
		System.out.println("               - memory used for problem representation in MBytes");
		System.out.println("               - number of states explored");
		System.out.println("               - searching time in seconds");
		System.out.println("               - memory used for searching in MBytes");
		System.out.println("               - global memory used in MBytes");
		System.out.println("               - solution plan length");
		System.out.println("     > 100  1 + various debugging information");
		System.out.println("-h          print this message\n\n");
		System.out.println("\n		SHOP accept only ordered tasks and before constraints chich are seen as preconditions");
		System.exit(0);
	}
	
	/**
	 * This method return the default arguments of the planner.
	 * 
	 * @return the default arguments of the planner.
	 */
	private static Properties getDefaultArguments() {
		final Properties options = new Properties();
		options.put(ISHOP.Argument.HEURISTIC_TYPE, ISHOP.DEFAULT_HEURISTIC);
		options.put(ISHOP.Argument.WEIGHT, ISHOP.DEFAULT_WHEIGHT);
		options.put(ISHOP.Argument.CPU_TIME, ISHOP.DEFAULT_CPU_TIME * 1000);
		options.put(ISHOP.Argument.TRACE_LEVEL, ISHOP.DEFAULT_TRACE_LEVEL);
		return options;
	}
}
