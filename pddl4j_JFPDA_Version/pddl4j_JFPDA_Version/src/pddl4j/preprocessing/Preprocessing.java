/*
 * Copyright (c) 2015 by:
 * 	Damien Pellier <Damien.Pellier@imag.fr>.
 * 	Abdeldjalil Ramoul <abdeldjalil.ramoul@imag.fr>.
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

package pddl4j.preprocessing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import pddl4j.parser.Domain;
import pddl4j.parser.Problem;
import pddl4j.parser.RequireKey;
import pddl4j.util.BitExp;
import pddl4j.util.BitOp;
import pddl4j.util.BitVector;
import pddl4j.util.CondBitExp;
import pddl4j.util.IntExp;
import pddl4j.util.OrderedMethod;
import pddl4j.util.Signature;
import pddl4j.util.TaskNetwork;

/**
 * <p>
 * This class implements the pre-processing needed to instantiate and encode planning problem in an efficient manner (see On the Instantiation of ADL Operators
 * Involving Arbitrary First-Order Formulas. Koehler, J. and Hoffmann, J.).
 * <p>
 * Preprocessing starts by generating the code tables, which map strings to unique numbers, i.e., we obtain one number for each predicate name, variable name,
 * and constant name. Internally, all subsequently described operations work over trees of numbers representing the formulas. Now from the generated code
 * tables, the operators, the initial state and the goal are encoded according to the integer code tables. Then, proceeds over the domain and problem
 * description and collects all used relation names. For each relation it checks if it satisfies one of the following definitions:
 * <p>
 * <i>Definition:</i> A relation is a positive inertia iff it does not occur positively in an unconditional effect or the consequent of a conditional effect of
 * an operator.
 * </p>
 * <p>
 * <i>Definition:</i> A relation is a negative inertia iff it does not occur negatively in an unconditional effect or the consequent of a conditional effect of
 * an operator.
 * </p>
 * <p>
 * Relations, which are positive as well as negative inertia, are simply called inertia. Relations, which are neither positive nor negative inertia, are called
 * fluents. The detection of inertia and fluents is easy because in ADL, effects are restricted to conjunctions of literals. Furthermore, this information can
 * be obtained with a single pass over the domain description, which takes almost no time at all.
 * </p>
 * <p>
 * Then, the preprocessing creates the predicates tables to count the number of occurrences of each predicate and instantiate the operators and simplified them.
 * After this first instantiation, the operators are simplifies again with the ground inertia information. So far we have only considered the predicates which
 * are never made true or false by a planning operator. These were used to constrain the instantiation process. Once the set of all actions has been determined,
 * one can similarly define the ground facts that are never made true or false by one of the actions.
 * <p>
 * <i>Definition:</i> A ground fact is a positive ground inertia iff it does not occur positively in an unconditional effect or the consequent of a conditional
 * effect of an action.
 * <p>
 * <i>Definition:</i> A ground fact is a negative ground inertia iff it does not occur negatively in an unconditional effect or the consequent of a conditional
 * effect of an action.
 * </p>
 * <p>
 * An initial fact, which is a negative ground inertia, is never made FALSE and thus always satisfied in all reachable world states. It can be removed from the
 * state description. All its occurrences in the preconditions of actions and in the antecedents of conditional effects can be simplified to TRUE. A fact, which
 * is a positive ground inertia and not contained in the initial state, is never satisfied in any reachable world state. All its occurrences in the
 * preconditions of actions and in the antecedents of conditional effects can be simplified to FALSE. The remaining facts are fluents that, roughly speaking,
 * can possibly change their truth value during the planning process. They are therefore relevant to the representation of the planning problem.
 * </p>
 * <p>
 * Then, preprocessing extracts relevant facts from the instantiated operator. A relevant fact is defines as follow:
 * </p>
 * <p>
 * <i>Definition:</i> A ground fact is relevant if and only if:
 * <ol>
 * <li>it is an initial fact and not a negative ground inertia, or if</li>
 * <li>it is not an initial fact and not a positive ground inertia.</li>
 * </ol>
 * </p>
 * <p>
 * Finally, every operator is normalized, i.e, transform precondition and effects of the operators in disjunctive and conjunctive normal form, before being
 * encoding in a compact bit set representation.
 * </p>
 * <b>Warning:</b> the preprocessing is only implemented for ADL problems. Revisions:
 * <ul>
 * <li>23.01.2013: add of the case when the goal can be simplified to TRUE. The coded problem returned contained in that case an empty goal expression (
 * <code>BitExp.isEmpty()</code>).</li>
 * </ul>
 * 
 * @author D. Pellier
 * @version 1.0 - 08.06.2010
 */
public final class Preprocessing {
	
	/**
	 * The table of types.
	 */
	static List<String> tableOfTypes;
	
	/**
	 * The table of inferred domains based on unary inertia encoding.
	 */
	static List<Set<Integer>> tableOfInferredDomains;
	
	/**
	 * The domain of associated to the type.
	 */
	static List<Set<Integer>> tableOfDomains;
	
	/**
	 * The table of constants.
	 */
	static List<String> tableOfConstants;
	
	/**
	 * The table of predicates.
	 */
	static List<String> tableOfPredicates;
	
	/**
	 * The table that contains the types of the arguments of the predicates.
	 */
	static List<List<Integer>> tableOfTypedPredicates;
	
	/**
	 * The table of the functions.
	 */
	static List<String> tableOfFunctions;
	
	/**
	 * The table that contains the types of the arguments of the functions.
	 */
	static List<List<Integer>> tableOfTypedFunctions;
	
	/**
	 * The table that defines for each predicates its type of inertia.
	 */
	static List<Inertia> tableOfInertia;
	
	/**
	 * The log level of the planner.
	 */
	static int logLevel;
	
	/**
	 * The table that contains the ground inertia.
	 */
	static Map<IntExp, Inertia> tableOfGroundInertia;
	
	/**
	 * The list of predicates tables used to count the occurrence of a specified predicate in the initial state.
	 */
	static List<List<IntMatrix>> predicatesTables;
	
	/**
	 * The table of the relevant facts.
	 */
	static List<IntExp> tableOfRevelantFacts;
	
	/**
	 * The table of the relevant operators for a specified task.
	 */
	static List<BitVector> tableOfRelevantOperators;
	
	/**
	 * The table of reachable primitive tasks for a specified task.
	 */
	static List<BitVector> tableOfReachableTasks;
	/**
	 * The table of positive primitive effectors for a fact
	 */
	static List<BitVector> tableOfPositivePrimitiveEffectors;
	/**
	 * The table of negative primitive effectors for a fact
	 */
	static List<BitVector> tableOfNegativePrimitiveEffectors;
	/**
	 * The table of positive composed effectors for a fact
	 */
	static List<BitVector> tableOfPositiveComposedEffectors;
	/**
	 * The table of negative composed effectors for a fact
	 */
	static List<BitVector> tableOfNegativeComposedEffectors;
	
	/**
	 * The list of instantiated operators encoded into bit sets.
	 */
	static Map<Signature, List<BitOp>> operators;
	
	/**
	 * The list of instantiated methods encoded into compact representation.
	 */
	static Map<Signature, List<OrderedMethod>> methods;
	
	/**
	 * The goal.
	 */
	static BitExp goal;
	
	/**
	 * The initial expansion
	 */
	static TaskNetwork initExpansion;
	
	/**
	 * The encoded goal.
	 */
	static List<BitExp> codedGoal;
	
	/**
	 * The initial state.
	 */
	static BitExp init;
	
	/**
	 * The list of task symbols.
	 */
	static List<String> TableOfTaskSymbols;
	
	/**
	 * The list of relevant tasks, i.e., the task used in the expansion of the methods.
	 */
	static List<IntExp> tableOfRelevantTasks;
	
	static long tStand;
	static long tIntEnc;
	static long tPreInst;
	static long tInst1;
	static long tInst2;
	static long inst1;
	static long inst2;
	static long tPostInst1;
	static long tPostInst2;
	static long tPostInst3;
	static long tPostInst4;
	static long tPostInst5;
	static long tPostInst6;
	static long tBitEnc1;
	static long tBitEnc2;
	static long tBitEnc3;
	static long tBitEnc4;
	static long tBitEnc5;
	static long tBitEnc6;
	static long enc1;
	static long enc2;
	static long enc3;
	static long enc4;
	
	/**
	 * Creates a new planner.
	 */
	private Preprocessing() {
	}
	
	/**
	 * Returns the log level of the planner.
	 * 
	 * @return the log level of the planner.
	 */
	public static int getLogLevel() {
		return Preprocessing.logLevel;
	}
	
	/**
	 * Set the log level of the planner. By default:
	 * <ul>
	 * <li>0 - nothing</li>
	 * <li>1 - 1 + info on problem constants, types and predicates</li>
	 * <li>2 - 1 + 2 + loaded operators, initial and goal state</li>
	 * <li>3 - 1 + predicates and their inertia status</li>
	 * <li>4 - 1 + 4 + goal state and operators with unary inertia encoded</li>
	 * <li>5 - 1 + actions, initial and goal state after expansion of variables</li>
	 * <li>6 - 1 + facts selected as relevant to the problem</li>
	 * <li>7 - 1 + final domain representation</li>
	 * <li>>100 - 1 + various debugging information</li>
	 * </ul>
	 * 
	 * @param level the log level of the planner.
	 * @throws IllegalArgumentException if <code>level < 0</code>.
	 */
	public static void setLogLevel(final int level)
	throws IllegalArgumentException {
		if (level < 0) {
			throw new IllegalArgumentException("level < 0");
		}
		Preprocessing.logLevel = level;
	}
	
	/**
	 * Instantiate, simplify and encode the problem in a compact representation. (see On the Instantiation of ADL Operators Involving Arbitrary First-Order
	 * Formulas. Koehler, J. and Hoffmann, J.):
	 * 
	 * @param domain the domain to encode.
	 * @param problem the problem to encode.
	 * @return the problem encoded.
	 * @throws IllegalArgumentException if the problem to encode is not ADL.
	 */
	public static CodedProblem encode(final Domain domain, final Problem problem)
	throws IllegalArgumentException {
		
		// Check that the domain and the problem are ADL or HTN otherwise the preprocessing is not
		// implemented for the moment.
		final Set<RequireKey> adl = new HashSet<>();
		adl.add(RequireKey.STRIPS);
		adl.add(RequireKey.HTN);
		adl.add(RequireKey.TYPING);
		adl.add(RequireKey.EQUALITY);
		adl.add(RequireKey.NEGATIVE_PRECONDITIONS);
		adl.add(RequireKey.DISJUNCTIVE_PRECONDITIONS);
		adl.add(RequireKey.EXISTENTIAL_PRECONDITIONS);
		adl.add(RequireKey.UNIVERSAL_PRECONDITIONS);
		adl.add(RequireKey.QUANTIFIED_PRECONDITIONS);
		adl.add(RequireKey.CONDITIONAL_EFFECTS);
		
		inst1 = 0;
		inst2 = 0;
		enc1 = 0;
		enc2 = 0;
		enc3 = 0;
		enc4 = 0;
		
		final Set<RequireKey> requirements = new LinkedHashSet<RequireKey>();
		requirements.addAll(domain.getRequirements());
		requirements.addAll(problem.getRequirements());
		for (final RequireKey rk : requirements) {
			if (!adl.contains(rk)) {
				throw new IllegalArgumentException("problem to encode not ADL");
			}
		}
		if (problem.getRequirements().contains(RequireKey.HTN)) {
			if (problem.getGoalTasks().getChildren().size() == 0) {
				System.err.println("The goal expansion is empty");
				return null;
			}
		}
		// *****************************************************************************************
		// Step 1: Standardization
		// *****************************************************************************************
		long localNow = System.currentTimeMillis();
		
		// Standardize the variables symbol contained in the domain
		domain.standardize();
		// Standardize the variables symbol contained in the domain
		problem.standardize();
		
		tStand = System.currentTimeMillis() - localNow;
		// *****************************************************************************************
		// Step 2: Integer encoding
		// *****************************************************************************************
		localNow = System.currentTimeMillis();
		// Encode the types declared in the domain
		IntEncoding.encodeTypes(domain);
		// Encode the constants declared in the domain and the objects of the problem
		IntEncoding.encodeConstants(domain, problem);
		// Encode the type of the form (either t1 t2...) declared in the domain and the problem
		IntEncoding.encodeEitherTypes(domain, problem);
		// Encode the predicates defined in the domain.
		IntEncoding.encodePredicates(domain);
		// Encode the functions defined in the domain.
		IntEncoding.encodeFunctions(domain);
		// Encode the task symbols used in the domain
		if (domain.getRequirements().contains(RequireKey.HTN)) {
			IntEncoding.encodeOperatorAndMethodSymbols(domain);
		}
		
		// Encode operators in integer representation
		List<IntOp> intOps = IntEncoding.encodeOperators(domain.getOperators());
		
		// Encode the methods into integer representation
		List<IntMeth> intMeths = new ArrayList<>();
		if (domain.getRequirements().contains(RequireKey.HTN)) {
			intMeths = IntEncoding.encodeMethods(domain.getMethods());
		}
		
		// Encode the initial state in integer representation
		final Set<IntExp> intInit = IntEncoding.encodeInit(problem.getInit());
		// Encode the goal in integer representation
		IntExp intGoal = null;
		IntExp initTasks = null;
		IntExp initTaskConstraints = null;
		
		if (!problem.getRequirements().contains(RequireKey.HTN)) {
			intGoal = IntEncoding.encodeGoal(problem.getGoal());
		} else {
			initTasks = IntEncoding.encodeGoalTasks(problem.getGoalTasks());
			initTaskConstraints = IntEncoding.encodeGoalTasksConstraints(problem.getGoalTasks(), problem.getGoalTasksConstraints());
		}
		
		// Just for logging
		if (Preprocessing.logLevel == 1 || Preprocessing.logLevel == 2) {
			Preprocessing.printTableOfConstants();
			System.out.println();
			Preprocessing.printTableOfPredicates();
			System.out.println();
			Preprocessing.printTableOfTypes();
		}
		
		// Just for logging
		if (Preprocessing.logLevel == 2) {
			System.out.println("\nCoded initial state:");
			System.out.print("(and");
			for (final IntExp f : intInit) {
				System.out.print(" ");
				System.out.println(Preprocessing.toString(f));
			}
			System.out.println(")");
			System.out.println("\nCoded goal state:");
			System.out.print("(and");
			for (final IntExp g : intGoal.getChildren()) {
				System.out.print(" ");
				System.out.println(Preprocessing.toString(g));
			}
			System.out.println(")");
			System.out.println("\nCoded operators:");
			for (final IntOp op : intOps) {
				System.out.println(Preprocessing.toString(op));
				System.out.println();
			}
		}
		tIntEnc = System.currentTimeMillis() - localNow;
		// *****************************************************************************************
		// Step 3: PreInstantiation
		// *****************************************************************************************
		localNow = System.currentTimeMillis();
		// Computed inertia from the encode operators
		PreInstantiation.extractInertia(intOps);
		// Infer the type from the unary inertia
		PreInstantiation.inferTypesFromInertia(intInit);
		// Simplify the encoded operators with the inferred types.
		intOps = PreInstantiation.simplifyOperatorsWithInferedTypes(intOps);
		// Create the predicates tables used to count the occurrences of the predicates in the
		// initial state
		PreInstantiation.createPredicatesTables(intInit);
		
		// Just for logging
		if (Preprocessing.logLevel == 3 || Preprocessing.logLevel == 4) {
			Preprocessing.printTableOfInertia();
		}
		// Just for logging
		if (Preprocessing.logLevel == 4) {
			System.out.print("\n");
			Preprocessing.printTableOfConstants();
			System.out.print("\n");
			Preprocessing.printTableOfTypes();
			System.out.print("\n");
			System.out.println("\nPre-instantiation initial state:");
			System.out.print("(and");
			for (final IntExp f : intInit) {
				System.out.print(" ");
				System.out.println(Preprocessing.toString(f));
			}
			System.out.println(")");
			System.out.println("\nPre-instantiation goal state:");
			System.out.print("(and");
			for (final IntExp g : intGoal.getChildren()) {
				System.out.print(" ");
				System.out.println(Preprocessing.toString(g));
			}
			System.out.println("\nPre-instantiation operators with infered types (" + intOps.size() + " ops):");
			for (final IntOp op : intOps) {
				System.out.println(Preprocessing.toString(op));
			}
		}
		tPreInst = System.currentTimeMillis() - localNow;
		// *****************************************************************************************
		// Step 4: Instantiation
		// *****************************************************************************************
		
		// Instantiate the operators
		localNow = System.currentTimeMillis();
		intOps = Instantiation.instantiateOperators(intOps);
		tInst1 = System.currentTimeMillis() - localNow;
		
		// Instantiate the methods
		if (domain.getRequirements().contains(RequireKey.HTN)) {
			localNow = System.currentTimeMillis();
			intMeths = Instantiation.instantiateMethods(intMeths);
			tInst2 = System.currentTimeMillis() - localNow;
		}
		// Expand the quantified expression in the goal
		if (!problem.getRequirements().contains(RequireKey.HTN)) {
			Instantiation.expandQuantifiedExpression(intGoal);
		}
		// The tables of predicates are no more needed
		Preprocessing.predicatesTables = null;
		
		// Just for logging
		if (Preprocessing.logLevel == 5) {
			System.out.print("\n");
			Preprocessing.printTableOfConstants();
			System.out.print("\n");
			Preprocessing.printTableOfTypes();
			System.out.print("\n");
			System.out.println("\nPre-instantiation initial state:");
			System.out.print("(and");
			for (final IntExp f : intInit) {
				System.out.print(" ");
				System.out.println(Preprocessing.toString(f));
			}
			System.out.println(")");
			System.out.println("\nPre-instantiation goal state:");
			System.out.print("(and");
			for (final IntExp g : intGoal.getChildren()) {
				System.out.print(" ");
				System.out.println(Preprocessing.toString(g));
			}
			System.out.println("\nPre-instantiation operators with infered types (" + intOps.size() + " ops):");
			for (final IntOp op : intOps) {
				System.out.println(Preprocessing.toString(op));
				
			}
			if (domain.getRequirements().contains(RequireKey.HTN)) {
				if (!intMeths.isEmpty()) {
					System.out.println("\nPre-instantiation methods with infered types (" + intMeths.size() + " methods):");
					for (final IntMeth meth : intMeths) {
						System.out.println(Preprocessing.toString(meth));
					}
				}
			}
			
		}
		// *****************************************************************************************
		// Step 5: PostInstantiation
		// *****************************************************************************************
		
		// Extract the ground inertia from the instantiated operators
		localNow = System.currentTimeMillis();
		PostInstantiation.extractGroundInertia(intOps);
		tPostInst1 = System.currentTimeMillis() - localNow;
		
		// Simplify the operators with the ground inertia information previously extracted
		localNow = System.currentTimeMillis();
		PostInstantiation.simplyOperatorsWithGroundInertia(intOps, intInit);
		tPostInst2 = System.currentTimeMillis() - localNow;
		
		// Simplify the methods with the ground inertia information previously extracted
		if (domain.getRequirements().contains(RequireKey.HTN)) {
			localNow = System.currentTimeMillis();
			PostInstantiation.simplyMethodsWithGroundInertia(intMeths, intInit);
			tPostInst3 = System.currentTimeMillis() - localNow;
		}
		
		// Extract the relevant facts from the simplified and instantiated operators
		localNow = System.currentTimeMillis();
		PostInstantiation.extractRelevantFacts(intOps, intInit);
		tPostInst4 = System.currentTimeMillis() - localNow;
		
		// Extract the relevant tasks from instantiated methods and problem
		if (domain.getRequirements().contains(RequireKey.HTN)) {
			localNow = System.currentTimeMillis();
			PostInstantiation.extractRelevantTasks(intMeths, intOps, initTasks);
			tPostInst5 = System.currentTimeMillis() - localNow;
		}
		
		// Simplify the goal with ground inertia information
		localNow = System.currentTimeMillis();
		if (!problem.getRequirements().contains(RequireKey.HTN)) {
			PostInstantiation.simplifyGoalWithGroundInertia(intGoal, intInit);
		} else {
			// Simplify the goal tasks based on relevant tasks extracted
			PostInstantiation.simplifyGoalTasks(initTasks, tableOfRelevantTasks);
			// Simplify the constraints on the goal task with the ground inertia
			PostInstantiation.simplifyGoalTasksConstraintWithGroundInertia(initTaskConstraints, intInit);
		}
		tPostInst6 = System.currentTimeMillis() - localNow;
		// The table of ground inertia are no more needed
		Preprocessing.tableOfGroundInertia = null;
		
		// Just for logging
		if (Preprocessing.logLevel == 6) {
			Preprocessing.printRelevantFactsTable();
		}
		
		// *****************************************************************************************
		// Step 6: Finalization and bit set encoding of the problem
		// *****************************************************************************************
		
		// Create a map of the relevant facts with their index to speedup the bit set encoding of
		// the operators
		final Map<IntExp, Integer> map = new LinkedHashMap<>(Preprocessing.tableOfRevelantFacts.size());
		int index = 0;
		for (final IntExp fact : Preprocessing.tableOfRevelantFacts) {
			map.put(fact, index);
			index++;
		}
		
		// Creates the list of bit operators
		Preprocessing.operators = new LinkedHashMap<>(Constants.DEFAULT_OPERATORS_TABLE_SIZE);
		
		// Creates the list of methods
		Preprocessing.methods = new LinkedHashMap<>(Preprocessing.tableOfRelevantTasks.size());
		;
		
		localNow = System.currentTimeMillis();
		if (!problem.getRequirements().contains(RequireKey.HTN)) {
			// Encode the goal in bit set representation
			if (!intGoal.getChildren().isEmpty()) { // Case where the goal was not already simplified to TRUE
				Preprocessing.goal = BitEncoding.encodeGoal(intGoal, map);
			} else {
				Preprocessing.goal = new BitExp();
			}
		} else {
			if (!initTasks.getChildren().isEmpty()) {
				Preprocessing.initExpansion = BitEncoding.encodeGoalExpansion(initTasks, initTaskConstraints);
			} else {
				Preprocessing.initExpansion = new TaskNetwork();
			}
		}
		tBitEnc1 = System.currentTimeMillis() - localNow;
		
		// Encode the initial state in bit set representation
		localNow = System.currentTimeMillis();
		Preprocessing.init = BitEncoding.encodeInit(intInit, map);
		tBitEnc2 = System.currentTimeMillis() - localNow;
		// Encode the operators in bit set representation
		localNow = System.currentTimeMillis();
		BitEncoding.encodeOperators(intOps, map);
		tBitEnc3 = System.currentTimeMillis() - localNow;
		// Encode the methods in bit set representation
		
		if (domain.getRequirements().contains(RequireKey.HTN)) {
			localNow = System.currentTimeMillis();
			BitEncoding.encode(intMeths);
			tBitEnc4 = System.currentTimeMillis() - localNow;
			// Creates the relevant operator table that matches for each task the set of operators relevant for the task
			localNow = System.currentTimeMillis();
			//Preprocessing.createRelevantOperatorsTable();
			tBitEnc5 = System.currentTimeMillis() - localNow;
			// Creates the relevant methods table that matches for each task the set of methods relevant for the task
			localNow = System.currentTimeMillis();
			//Preprocessing.createRelevantMethodsTable();
			tBitEnc6 = System.currentTimeMillis() - localNow;
			// Creates the relevant primitive tasks table that matches for each composed task the set of primitive reachable tasks.
			//Preprocessing.createReachableTasksTable();
			// Create the tables of primitive effectors
			//createPrimitiveEffectorsTable();
			// Create the tables of composed effectors
			//createComposedEffectorsTable();
		}
		
		// The lists of instantiated operators and methods are no more needed.
		intOps = null;
		if (domain.getRequirements().contains(RequireKey.HTN)) {
			intMeths = null;
		}
		
		// Just for logging
		if (Preprocessing.logLevel == 7) {
			System.out.println("\nfinal operators:");
			for (final Signature sign : operators.keySet()) {
				final List<BitOp> ops = operators.get(sign);
				for (final BitOp op : ops) {
					System.out.println(Preprocessing.toString(op));
				}
			}
			System.out.println("\nfinal initial state:");
			System.out.println(Preprocessing.toString(Preprocessing.init));
			if (!problem.getRequirements().contains(RequireKey.HTN)) {
				System.out.println("\nfinal goal state:");
				if (Preprocessing.goal != null && !Preprocessing.goal.isEmpty()) {
					System.out.println(Preprocessing.toString(Preprocessing.goal));
				} else if (Preprocessing.goal.isEmpty()) {
					System.out.println("goal can be simplified to TRUE");
				} else {
					System.out.println("goal can be simplified to FALSE");
				}
			} else {
				System.out.println("**************** Relevant Facts ******************");
				Preprocessing.printRelevantFactsTable();
				
				int i = 0;
				System.out.println("**************** Relevant Tasks ******************");
				for (final IntExp intexp : Preprocessing.tableOfRelevantTasks) {
					System.out.println(i++ + ": " + Preprocessing.toString(intexp));
				}
				
				i = 0;
				System.out.println("**************** Effectors ******************");
				for (int n = 0; n < Preprocessing.tableOfRevelantFacts.size(); n++) {
					System.out.println(i++ + ": " + Preprocessing.tableOfPositivePrimitiveEffectors.get(n) + Preprocessing.tableOfPositiveComposedEffectors.get(n) + Preprocessing.tableOfNegativePrimitiveEffectors.get(n) + Preprocessing.tableOfNegativeComposedEffectors.get(n));
				}
				
			}
		}
			System.out.println("\n********* Preprocessing stats: **********");
			System.out.println("1- Standardization done in: " + (tStand / 1000) + "s");
			System.out.println("2- Integer Encoding done in: " + (tIntEnc / 1000) + "s");
			System.out.println("3- PreInstantiation done in: " + (tPreInst / 1000) + "s");
			System.out.println("4- Instantiation done in: " + ((tInst1 + tInst2) / 1000) + "s");
			System.out.println("	4.1- Operators instantiation done in: " + (tInst1 / 1000) + "s");
			System.out.println("	4.2- Methods instantiation done in: " + (tInst2 / 1000) + "s");
			System.out.println("		4.2.1- Instantiation1 done in: " + inst1 + "ms");
			System.out.println("		4.2.2- Instantiation2 done in: " + inst2 + "ms");
			System.out.println("5- Post-instantiation done in: " + ((tPostInst1 + tPostInst2 + tPostInst3 + tPostInst4 + tPostInst5 + tPostInst6) / 1000) + "s");
			System.out.println("	5.1- Inertia extraction done in: " + (tPostInst1 / 1000) + "s");
			System.out.println("	5.2- Operators simplification done in: " + (tPostInst2 / 1000) + "s");
			System.out.println("	5.3- Methods simplification done in: " + (tPostInst3 / 1000) + "s");
			System.out.println("	5.4- Relevant facts extraction done in: " + (tPostInst4 / 1000) + "s");
			System.out.println("	5.5- Relevant tasks extraction done in: " + (tPostInst5 / 1000) + "s");
			System.out.println("	5.6- Goal simplification done in: " + (tPostInst6 / 1000) + "s");
			System.out.println("6- Bit set encoding done in: " + ((tBitEnc1 + tBitEnc2 + tBitEnc3 + tBitEnc4 + tBitEnc5 + tBitEnc6) / 1000) + "s");
			System.out.println("	6.1- Goal encoding done in: " + (tBitEnc1 / 1000) + "s");
			System.out.println("	6.2- Initial state encoding done in: " + (tBitEnc2 / 1000) + "s");
			System.out.println("	6.3- Operators encoding done in: " + (tBitEnc3 / 1000) + "s");
			System.out.println("	6.4- Methods encoding done in: " + (tBitEnc4 / 1000) + "s");
			System.out.println("		6.4.1- Encoding1 done in: " + enc1 + "ms");
			System.out.println("		6.4.2- Encoding2 done in: " + enc2 + "ms");
			System.out.println("		6.4.3- Encoding3 done in: " + enc3 + "ms");
			System.out.println("		6.4.4- Encoding4 done in: " + enc4 + "ms");
			System.out.println("	6.5- Relevant Operators table done in: " + (tBitEnc5 / 1000) + "s");
			System.out.println("	6.6- Relevant Methods table done in: " + (tBitEnc6 / 1000) + "s");
		
		
		System.out.println("\n********* Preprocessing stats: **********");
		System.out.println("1- Standardization done in: " + (tStand / 1000) + "s");
		System.out.println("2- Integer Encoding done in: " + (tIntEnc / 1000) + "s");
		System.out.println("3- PreInstantiation done in: " + (tPreInst / 1000) + "s");
		System.out.println("4- Instantiation done in: " + ((tInst1 + tInst2) / 1000) + "s");
		System.out.println("	4.1- Operators instantiation done in: " + (tInst1 / 1000) + "s");
		System.out.println("	4.2- Methods instantiation done in: " + (tInst2 / 1000) + "s");
		System.out.println("		4.2.1- Instantiation1 done in: " + inst1 + "ms");
		System.out.println("		4.2.2- Instantiation2 done in: " + inst2 + "ms");
		System.out.println("5- Post-instantiation done in: " + ((tPostInst1 + tPostInst2 + tPostInst3 + tPostInst4 + tPostInst5 + tPostInst6) / 1000) + "s");
		System.out.println("	5.1- Inertia extraction done in: " + (tPostInst1 / 1000) + "s");
		System.out.println("	5.2- Operators simplification done in: " + (tPostInst2 / 1000) + "s");
		System.out.println("	5.3- Methods simplification done in: " + (tPostInst3 / 1000) + "s");
		System.out.println("	5.4- Relevant facts extraction done in: " + (tPostInst4 / 1000) + "s");
		System.out.println("	5.5- Relevant tasks extraction done in: " + (tPostInst5 / 1000) + "s");
		System.out.println("	5.6- Goal simplification done in: " + (tPostInst6 / 1000) + "s");
		System.out.println("6- Bit set encoding done in: " + ((tBitEnc1 + tBitEnc2 + tBitEnc3 + tBitEnc4 + tBitEnc5 + tBitEnc6) / 1000) + "s");
		System.out.println("	6.1- Goal encoding done in: " + (tBitEnc1 / 1000) + "s");
		System.out.println("	6.2- Initial state encoding done in: " + (tBitEnc2 / 1000) + "s");
		System.out.println("	6.3- Operators encoding done in: " + (tBitEnc3 / 1000) + "s");
		System.out.println("	6.4- Methods encoding done in: " + (tBitEnc4 / 1000) + "s");
		System.out.println("		6.4.1- Encoding1 done in: " + enc1 + "ms");
		System.out.println("		6.4.2- Encoding2 done in: " + enc2 + "ms");
		System.out.println("		6.4.3- Encoding3 done in: " + enc3 + "ms");
		System.out.println("		6.4.4- Encoding4 done in: " + enc4 + "ms");
		System.out.println("	6.5- Relevant Operators table done in: " + (tBitEnc5 / 1000) + "s");
		System.out.println("	6.6- Relevant Methods table done in: " + (tBitEnc6 / 1000) + "s");
		
		final CodedProblem codedProblem = new CodedProblem();
		codedProblem.setGoal(Preprocessing.goal);
		codedProblem.setInit(Preprocessing.init);
		codedProblem.setOperators(Preprocessing.operators);
		codedProblem.setConstants(Preprocessing.tableOfConstants);
		codedProblem.setDomains(Preprocessing.tableOfDomains);
		codedProblem.setFunctions(Preprocessing.tableOfFunctions);
		codedProblem.setInertia(Preprocessing.tableOfInertia);
		codedProblem.setInferedDomains(Preprocessing.tableOfInferredDomains);
		codedProblem.setPredicates(Preprocessing.tableOfPredicates);
		codedProblem.setRelevantFacts(Preprocessing.tableOfRevelantFacts);
		codedProblem.setFunctionsSignatures(Preprocessing.tableOfTypedFunctions);
		codedProblem.setPredicatesSignatures(Preprocessing.tableOfTypedPredicates);
		codedProblem.setTypes(Preprocessing.tableOfTypes);
		codedProblem.setTaskSymbols(Preprocessing.TableOfTaskSymbols);
		codedProblem.setRelevantTasks(Preprocessing.tableOfRelevantTasks);
		codedProblem.setMethods(Preprocessing.methods);
		codedProblem.setReachableTasks(Preprocessing.tableOfReachableTasks);
		codedProblem.setPositivePrimitiveEffectors(Preprocessing.tableOfPositivePrimitiveEffectors);
		codedProblem.setPositiveComposedEffectors(Preprocessing.tableOfPositiveComposedEffectors);
		codedProblem.setNegativePrimitiveEffectors(Preprocessing.tableOfNegativePrimitiveEffectors);
		codedProblem.setNegativeComposedEffectors(Preprocessing.tableOfNegativeComposedEffectors);
		
		codedProblem.setInitExpansion(Preprocessing.initExpansion);
		return codedProblem;
	}
	
	private static void createReachableTasksTable() {
		Preprocessing.tableOfReachableTasks = new ArrayList<BitVector>(Preprocessing.tableOfRelevantTasks.size());
		for (int task = 0; task < Preprocessing.tableOfRelevantTasks.size(); task++) {
			final BitVector reachableTasks = new BitVector();
			Preprocessing.getReachableTasks(task, reachableTasks);
			Preprocessing.tableOfReachableTasks.add(reachableTasks);
		}
	}
	
	private static void createPrimitiveEffectorsTable() {
		Preprocessing.tableOfPositivePrimitiveEffectors = new ArrayList<BitVector>(Preprocessing.tableOfRevelantFacts.size());
		Preprocessing.tableOfNegativePrimitiveEffectors = new ArrayList<BitVector>(Preprocessing.tableOfRevelantFacts.size());
		for (int fact = 0; fact < Preprocessing.tableOfRevelantFacts.size(); fact++) {
			final BitVector positiveEffectors = new BitVector(Preprocessing.tableOfRelevantTasks.size());
			final BitVector negativeEffectors = new BitVector(Preprocessing.tableOfRelevantTasks.size());
			Preprocessing.tableOfPositivePrimitiveEffectors.add(positiveEffectors);
			Preprocessing.tableOfNegativePrimitiveEffectors.add(negativeEffectors);
		}
		Preprocessing.getPrimitiveEffectors();
	}
	
	private static void createComposedEffectorsTable() {
		Preprocessing.tableOfPositiveComposedEffectors = new ArrayList<BitVector>(Preprocessing.tableOfRevelantFacts.size());
		Preprocessing.tableOfNegativeComposedEffectors = new ArrayList<BitVector>(Preprocessing.tableOfRevelantFacts.size());
		for (int fact = 0; fact < Preprocessing.tableOfRevelantFacts.size(); fact++) {
			final BitVector positiveEffectors = new BitVector();
			final BitVector negativeEffectors = new BitVector();
			Preprocessing.tableOfPositiveComposedEffectors.add(positiveEffectors);
			Preprocessing.tableOfNegativeComposedEffectors.add(negativeEffectors);
		}
		Preprocessing.getComposedEffectors();
	}
	
	/**
	 * Compute the relevant operator for a specified task. An operator is relevant if and only if:
	 * <ul>
	 * <li>the operator has the same name as the task and</li>
	 * <li>the same parameters.</li>
	 * </ul>
	 * 
	 * @param task the task.
	 * @return the list of operators relevant for the task in parameter.
	 * @author Ramoul
	 */
	private static List<BitOp> getRelevantOperators(final IntExp task) {
		final String taskName = Preprocessing.TableOfTaskSymbols.get(task.getPredicate());
		final int[] arguments = task.getArguments();
		final Signature taskSignature = new Signature(taskName, arguments);
		return operators.get(taskSignature);
	}
	
	/**
	 * Compute the relevant operators for a specified task. An operator is relevant if and only if:
	 * <ul>
	 * <li>the operator has the same name as the task and</li>
	 * <li>the same parameters.</li>
	 * </ul>
	 * 
	 * @param taskIndex the index of the task.
	 * @return the list of operators relevant for the task in parameter.
	 * @author Ramoul
	 */
	private static List<BitOp> getRelevantOperators(final int taskIndex) {
		final IntExp task = tableOfRelevantTasks.get(taskIndex);
		final int taskNameIndex = task.getPredicate();
		final String taskName = TableOfTaskSymbols.get(taskNameIndex);
		final int[] arguments = task.getArguments();
		final Signature taskSignature = new Signature(taskName, arguments);
		return operators.get(taskSignature);
	}
	
	/**
	 * Compute the relevant methods for a specified task. A method is relevant if and only if:
	 * <ul>
	 * <li>the method has the same name as the task and</li>
	 * <li>the same parameters.</li>
	 * </ul>
	 * 
	 * @param task the task.
	 * @return the list of methods relevant for the task in parameter.
	 * @author Ramoul
	 */
	private static List<OrderedMethod> getRelevantMethods(final IntExp task) {
		final String taskName = Preprocessing.TableOfTaskSymbols.get(task.getPredicate());
		final int[] arguments = task.getArguments();
		final Signature taskSignature = new Signature(taskName, arguments);
		return methods.get(taskSignature);
	}
	
	/**
	 * Compute the relevant methods for a specified task. A method is relevant if and only if:
	 * <ul>
	 * <li>the method has the same name as the task and</li>
	 * <li>the same parameters.</li>
	 * </ul>
	 * 
	 * @param taskIndex the index of the task.
	 * @return the list of methods relevant for the task in parameter.
	 * @author Ramoul
	 */
	private static List<OrderedMethod> getRelevantMethods(final int taskIndex) {
		final IntExp task = tableOfRelevantTasks.get(taskIndex);
		final int taskNameIndex = task.getPredicate();
		final String taskName = TableOfTaskSymbols.get(taskNameIndex);
		final int[] arguments = task.getArguments();
		final Signature taskSignature = new Signature(taskName, arguments);
		return methods.get(taskSignature);
	}
	
	/**
	 * Adds an Operator to the methods map
	 * 
	 * @param operator The method to add
	 * @author Ramoul
	 */
	public static void addOperator(final BitOp operator) {
		final Signature signature = new Signature(operator.getName(), operator.getInstantiations());
		if (operators.containsKey(signature)) {
			operators.get(signature).add(operator);
		} else {
			final List<BitOp> newOperatorsList = new ArrayList<>();
			newOperatorsList.add(operator);
			operators.put(signature, newOperatorsList);
		}
	}
	
	/**
	 * Adds a method to the methods map
	 * 
	 * @param method The method to add
	 * @author Ramoul
	 */
	public static void addMethod(final OrderedMethod method) {
		final Signature signature = new Signature(method.getName(), method.getInstantiations());
		if (methods.containsKey(signature)) {
			methods.get(signature).add(method);
		} else {
			final List<OrderedMethod> newMethodList = new ArrayList<>();
			newMethodList.add(method);
			methods.put(signature, newMethodList);
		}
	}
	
	/**
	 * Compute the relevant primitive tasks reachable from a specified task.
	 * 
	 * @param task the task.
	 * @param reachableTasks The bit vector containing reachable primitive tasks indexes
	 */
	private static void getReachableTasks(final int task, final BitVector reachableTasks) {
		final Stack<Integer> tasksStack = new Stack<>();
		final Set<Integer> treatedTasks = new HashSet<>();
		tasksStack.push(task);
		while (!tasksStack.isEmpty()) {
			final int provTask = tasksStack.pop();
			if (!treatedTasks.contains(provTask)) {
				treatedTasks.add(provTask);
				//If the tasks is primitive add it to the bit vector of reachable tasks
				if (!Preprocessing.tableOfRelevantOperators.get(provTask).getAllSetBits().isEmpty()) {
					reachableTasks.set(provTask);
					//Else find the all relevant methods and call getReachableTasks for every task in it
				} else if (!Preprocessing.getRelevantMethods(provTask).isEmpty()) {
					for (final OrderedMethod method : Preprocessing.methods.get(provTask)) {
						for (final int methodTask : method.getExpansion()) {
							tasksStack.push(methodTask);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Compute the primitive effectors for all relevant facts
	 */
	private static void getPrimitiveEffectors() {
		
		getInitEffector();
		for (int taskIndex = 0; taskIndex < Preprocessing.tableOfRelevantTasks.size(); taskIndex++) {
			final IntExp task = Preprocessing.tableOfRelevantTasks.get(taskIndex);
			final BitOp operator = Preprocessing.getRelevantOperators(task).get(0);
			if (operator != null) {
				final List<Integer> positiveFacts = operator.getUnconditionalEffects().getPositive().getAllSetBits();
				final List<Integer> negativeFacts = operator.getUnconditionalEffects().getNegative().getAllSetBits();
				for (final int positiveFact : positiveFacts) {
					Preprocessing.tableOfPositivePrimitiveEffectors.get(positiveFact).set(taskIndex);
					Preprocessing.tableOfNegativePrimitiveEffectors.get(positiveFact + 1).set(taskIndex);
				}
				for (final int negativeFact : negativeFacts) {
					Preprocessing.tableOfPositivePrimitiveEffectors.get(negativeFact + 1).set(taskIndex);
					Preprocessing.tableOfNegativePrimitiveEffectors.get(negativeFact).set(taskIndex);
				}
			}
		}
	}
	
	/**
	 * Put init task in primitive effectors table using Init facts
	 */
	private static void getInitEffector() {
		final BitVector positiveFacts = init.getPositive();
		for (final int positiveInitFact : positiveFacts.getAllSetBits()) {
			Preprocessing.tableOfPositivePrimitiveEffectors.get(positiveInitFact).set(0);
			Preprocessing.tableOfNegativePrimitiveEffectors.get(positiveInitFact + 1).set(0);
		}
		for (int i = 1; i < tableOfRevelantFacts.size(); i = i + 2) {
			if (!positiveFacts.get(i - 1)) {
				Preprocessing.tableOfPositivePrimitiveEffectors.get(i).set(0);
				Preprocessing.tableOfNegativePrimitiveEffectors.get(i - 1).set(0);
			}
		}
	}
	
	/**
	 * Compute the composed effectors for all facts
	 */
	private static void getComposedEffectors() {
		
		for (int taskIndex = 0; taskIndex < Preprocessing.tableOfRelevantTasks.size(); taskIndex++) {
			final IntExp task = Preprocessing.tableOfRelevantTasks.get(taskIndex);
			final int methsSize = Preprocessing.getRelevantMethods(task).size();
			if (methsSize != 0) {
				final List<Integer> reachableTasks = Preprocessing.tableOfReachableTasks.get(taskIndex).getAllSetBits();
				for (final int reachableTask : reachableTasks) {
					final IntExp opTask = Preprocessing.tableOfRelevantTasks.get(reachableTask);
					final BitOp operator = Preprocessing.getRelevantOperators(opTask).get(0);
					final List<Integer> positiveFacts = operator.getUnconditionalEffects().getPositive().getAllSetBits();
					final List<Integer> negativeFacts = operator.getUnconditionalEffects().getNegative().getAllSetBits();
					for (final int positiveFact : positiveFacts) {
						Preprocessing.tableOfPositiveComposedEffectors.get(positiveFact).set(taskIndex);
						Preprocessing.tableOfNegativeComposedEffectors.get(positiveFact + 1).set(taskIndex);
					}
					for (final int negativeFact : negativeFacts) {
						Preprocessing.tableOfPositiveComposedEffectors.get(negativeFact + 1).set(taskIndex);
						Preprocessing.tableOfNegativeComposedEffectors.get(negativeFact).set(taskIndex);
					}
				}
			}
		}
	}
	
	// *********************************************************************************************
	// Methods for printing the different structures used during preprocessing
	// *********************************************************************************************
	
	/**
	 * Print the table of types.
	 */
	static void printTableOfTypes() {
		System.out.println("Types table:");
		for (int i = 0; i < Preprocessing.tableOfTypes.size(); i++) {
			System.out.print(i + ": " + Preprocessing.tableOfTypes.get(i) + ":");
			final Set<Integer> domain = Preprocessing.tableOfDomains.get(i);
			for (final Integer constant : domain) {
				System.out.print(" " + constant);
			}
			System.out.println();
		}
	}
	
	/**
	 * Print the table of constants.
	 */
	static void printTableOfConstants() {
		System.out.println("Constants table:");
		for (int i = 0; i < Preprocessing.tableOfConstants.size(); i++) {
			System.out.println(i + ": " + Preprocessing.tableOfConstants.get(i));
		}
	}
	
	/**
	 * Print the table of predicates.
	 */
	static void printTableOfPredicates() {
		System.out.println("Predicates table:");
		for (int i = 0; i < Preprocessing.tableOfPredicates.size(); i++) {
			final String predicate = Preprocessing.tableOfPredicates.get(i);
			System.out.print(i + ": " + predicate + " :");
			for (int j = 0; j < Preprocessing.tableOfTypedPredicates.get(i).size(); j++) {
				System.out.print(" " + Preprocessing.tableOfTypes.get(Preprocessing.tableOfTypedPredicates.get(i).get(j)));
			}
			System.out.println();
		}
	}
	
	/**
	 * Print the table of functions.
	 */
	static void printTableOfFunctions() {
		System.out.println("Functions table:");
		for (int i = 0; i < Preprocessing.tableOfFunctions.size(); i++) {
			final String predicate = Preprocessing.tableOfFunctions.get(i);
			System.out.print(i + ": " + predicate + ":");
			for (int j = 0; j < Preprocessing.tableOfTypedFunctions.get(i).size(); j++) {
				System.out.print(" " + Preprocessing.tableOfTypes.get(Preprocessing.tableOfTypedFunctions.get(i).get(j)));
			}
			System.out.println();
		}
	}
	
	/**
	 * Print the table of inertia.
	 */
	static void printTableOfInertia() {
		System.out.println("Inertias table:");
		for (int i = 0; i < Preprocessing.tableOfPredicates.size(); i++) {
			final String predicate = Preprocessing.tableOfPredicates.get(i);
			System.out.println(i + ": " + predicate + " : " + Preprocessing.tableOfInertia.get(i));
		}
	}
	
	/**
	 * Print the relevant facts table.
	 */
	static void printRelevantFactsTable() {
		System.out.println("selected the following facts as relevant:");
		for (int i = 0; i < Preprocessing.tableOfRevelantFacts.size(); i++) {
			System.out.println(i + ": " + Preprocessing.toString(Preprocessing.tableOfRevelantFacts.get(i)));
		}
	}
	
	/**
	 * Print the goal.
	 */
	static void printGoal() {
		System.out.println("Goal state is:");
		for (final BitExp exp : Preprocessing.codedGoal) {
			System.out.println(Preprocessing.toString(exp));
		}
	}
	
	/**
	 * Print the initial state.
	 */
	static void printInitExp(final Set<IntExp> intInit) {
		System.out.println("Initial exp state is:");
		for (final IntExp f : intInit) {
			System.out.print(" ");
			System.out.println(Preprocessing.toString(f));
		}
	}
	
	/**
	 * Print the tasks symbols table.
	 */
	static void printTableOfTasks() {
		System.out.println("Tasks table:");
		for (int i = 0; i < Preprocessing.TableOfTaskSymbols.size(); i++) {
			System.out.println(i + ": " + Preprocessing.TableOfTaskSymbols.get(i));
		}
		System.out.println();
	}
	
	/**
	 * Print the table of reachable tasks
	 */
	static void printTableOfReachableTasks() {
		System.out.println("Reachable Tasks Table:");
		for (int i = 0; i < Preprocessing.tableOfReachableTasks.size(); i++) {
			System.out.print(i + "  " + Preprocessing.toString(Preprocessing.tableOfRelevantTasks.get(i)) + ": ");
			for (final int j : Preprocessing.tableOfReachableTasks.get(i).getAllSetBits()) {
				System.out.print(Preprocessing.toString(Preprocessing.tableOfRelevantTasks.get(j)) + " / ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Print the methods table.
	 */
	static void printMethods() {
		System.out.println("Problem Methods:");
		for (final Signature key : methods.keySet()) {
			System.out.println("* Methods for: " + key.toString(Preprocessing.tableOfConstants));
			final List<OrderedMethod> methList = methods.get(key);
			for (int i = 0; i < methList.size(); i++) {
				System.out.println(i + ": " + Preprocessing.toString(methList.get(i)));
			}
			System.out.println();
		}
		
	}
	
	/**
	 * Print the relevant tasks table.
	 */
	static void printRelevantTasks() {
		System.out.println("Relevant tasks:");
		for (int i = 0; i < Preprocessing.tableOfRelevantTasks.size(); i++) {
			System.out.println(i + ": " + Preprocessing.toString(Preprocessing.tableOfRelevantTasks.get(i)));
		}
		System.out.println();
	}
	
	/**
	 * Returns a string representation of the specified operator.
	 * 
	 * @param op the operator to print.
	 * @return a string representation of the specified operator.
	 */
	static String toString(final IntOp op) {
		return StringEncoder.toString(op, Preprocessing.tableOfConstants, Preprocessing.tableOfTypes, Preprocessing.tableOfPredicates, Preprocessing.tableOfFunctions, Preprocessing.TableOfTaskSymbols);
	}
	
	/**
	 * Returns a string representation of the specified method.
	 * 
	 * @param meth the method to print.
	 * @return a string representation of the specified method.
	 */
	static String toString(final IntMeth meth) {
		return StringEncoder.toString(meth, Preprocessing.tableOfConstants, Preprocessing.tableOfTypes, Preprocessing.tableOfPredicates, Preprocessing.tableOfFunctions, Preprocessing.TableOfTaskSymbols);
	}
	
	/**
	 * Returns a short string representation of the specified operator, i.e., only its name and the value of its parameters.
	 * 
	 * @param op the operator.
	 * @return a string representation of the specified operator.
	 */
	static final String toShortString(final IntOp op) {
		return StringEncoder.toShortString(op, Preprocessing.tableOfConstants, Preprocessing.TableOfTaskSymbols);
	}
	
	/**
	 * Returns a short string representation of the specified method, i.e., only its name and the value of its parameters.
	 * 
	 * @param meth the method.
	 * @return a string representation of the specified method.
	 */
	static final String toShortString(final IntMeth meth) {
		return StringEncoder.toShortString(meth, Preprocessing.tableOfConstants, Preprocessing.TableOfTaskSymbols);
	}
	
	/**
	 * Returns a string representation of the specified operator.
	 * 
	 * @param op the operator to print.
	 * @return a string representation of the specified operator.
	 */
	static String toString(final BitOp op) {
		return StringEncoder.toString(op, Preprocessing.tableOfConstants, Preprocessing.tableOfTypes, Preprocessing.tableOfPredicates, Preprocessing.tableOfFunctions, Preprocessing.tableOfRevelantFacts, "\t");
	}
	
	/**
	 * Returns a string representation of the specified method.
	 * 
	 * @param meth the method to print.
	 * @return a string representation of the specified method.
	 */
	static String toString(final OrderedMethod meth) {
		return StringEncoder.toString(meth, Preprocessing.tableOfConstants, Preprocessing.tableOfTypes, Preprocessing.tableOfPredicates, Preprocessing.tableOfFunctions, Preprocessing.tableOfRevelantFacts, Preprocessing.tableOfRelevantTasks, "\t");
	}
	
	/**
	 * Returns a string representation of an expression.
	 * 
	 * @param exp the expression.
	 * @return a string representation of the specified expression.
	 */
	public static String toString(final IntExp exp) {
		return StringEncoder.toString(exp, Preprocessing.tableOfConstants, Preprocessing.tableOfTypes, Preprocessing.tableOfPredicates, Preprocessing.tableOfFunctions, Preprocessing.TableOfTaskSymbols);
	}
	
	/**
	 * Returns a string representation of a bit expression.
	 * 
	 * @param exp the expression.
	 * @return a string representation of the specified expression.
	 */
	static String toString(final BitExp exp) {
		return StringEncoder.toString(exp, Preprocessing.tableOfConstants, Preprocessing.tableOfTypes, Preprocessing.tableOfPredicates, Preprocessing.tableOfFunctions, Preprocessing.tableOfRevelantFacts);
	}
	
	/**
	 * Returns a string representation of a conditional bit expression.
	 * 
	 * @param exp the conditional expression.
	 * @return a string representation of the specified expression.
	 */
	static String toString(final CondBitExp exp) {
		return StringEncoder.toString(exp, Preprocessing.tableOfConstants, Preprocessing.tableOfTypes, Preprocessing.tableOfPredicates, Preprocessing.tableOfFunctions, Preprocessing.tableOfRevelantFacts);
	}
	
	/**
	 * Print the table of inertia.
	 */
	static void printTableOfGroundInertia() {
		System.out.println("Ground inertia table:");
		for (final Entry<IntExp, Inertia> e : Preprocessing.tableOfGroundInertia.entrySet()) {
			System.out.println(Preprocessing.toString(e.getKey()) + ": " + e.getValue());
		}
	}
}
