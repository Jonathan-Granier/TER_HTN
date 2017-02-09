/*
 * Copyright (c) 2010 by Damien Pellier <Damien.Pellier@imag.fr>.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pddl4j.parser.Connective;
import pddl4j.util.BitExp;
import pddl4j.util.BitOp;
import pddl4j.util.BitVector;
import pddl4j.util.CondBitExp;
import pddl4j.util.IntExp;
import pddl4j.util.OrderConstraint;
import pddl4j.util.OrderedMethod;
import pddl4j.util.Signature;
import pddl4j.util.TaskNetwork;

/**
 * This class implements a problem where operators are instantiated and encoded in <code>BitSet</code> representation.
 * 
 * @author D. Pellier
 * @version 1.0 - 10.06.2010
 */
public class CodedProblem
implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7665524544039101600L;
	
	/**
	 * The table of types.
	 */
	private List<String> types;
	
	/**
	 * The table of inferred domains based on unary inertia encoding.
	 */
	private List<Set<Integer>> inferedDomains;
	
	/**
	 * The domain of associated to the type.
	 */
	private List<Set<Integer>> domains;
	
	/**
	 * The table of constants.
	 */
	private List<String> constants;
	
	/**
	 * The table of predicates.
	 */
	private List<String> predicates;
	
	/**
	 * The table that contains the types of the arguments of the predicates.
	 */
	private List<List<Integer>> predicatesSignatures;
	
	/**
	 * The table of the functions.
	 */
	private List<String> functions;
	
	/**
	 * The table that contains the types of the arguments of the functions.
	 */
	private List<List<Integer>> functionsSignatures;
	
	/**
	 * The table that defines for each predicates its type of inertia.
	 */
	private List<Inertia> inertia;
	
	/**
	 * The table of the relevant facts.
	 */
	private List<IntExp> relevantFacts;
	
	/**
	 * The list of instantiated operator encoded into bit sets.
	 */
	private Map<Signature, List<BitOp>> operators;
	
	/**
	 * The goal.
	 */
	private BitExp goal;
	
	/**
	 * The initial expansion.
	 */
	private TaskNetwork initTaskNetwork;
	
	/**
	 * The initial state.
	 */
	private BitExp init;
	
	/**
	 * The list of instantiated methods encoded into compact representation.
	 */
	private Map<Signature, List<OrderedMethod>> methods;
	
	/**
	 * The list of task symbols.
	 */
	private List<String> taskSymbols;
	
	/**
	 * The list of relevant tasks, i.e., the task used in the expansion of the methods.
	 */
	private List<IntExp> relevantTasks;
	
	/**
	 * The table of reachable tasks for a specified task.
	 */
	private List<BitVector> reachableTasks;
	
	/**
	 * The table of positive primitive effectors tasks for a specified all facts.
	 */
	private List<BitVector> positivePrimitiveEffectors;
	
	/**
	 * The table of positive composed effectors tasks for a specified all facts.
	 */
	private List<BitVector> positiveComposedEffectors;
	
	/**
	 * The table of negative primitive effectors tasks for a specified all facts.
	 */
	private List<BitVector> negativePrimitiveEffectors;
	
	/**
	 * The table of negative composed effectors tasks for a specified all facts.
	 */
	private List<BitVector> negativeComposedEffectors;
	
	/**
	 * The default constructor with a private package access to prevent instance creation.
	 */
	CodedProblem() {
	}
	
	/**
	 * Create a new <code>CodedProblem</code> copy of another.
	 * 
	 * @param other the other <code>CodedProblem</code>;
	 */
	public CodedProblem(final CodedProblem other) {
		super();
		types = new ArrayList<String>();
		for (final String str : other.types) {
			types.add(new String(str));
		}
		domains = new ArrayList<Set<Integer>>();
		for (final Set<Integer> si : other.domains) {
			final Set<Integer> copy = new LinkedHashSet<Integer>();
			for (final Integer i : si) {
				copy.add(i);
			}
			domains.add(copy);
		}
		constants = new ArrayList<>();
		for (final String str : other.constants) {
			constants.add(new String(str));
		}
		predicates = new ArrayList<>();
		for (final String str : other.predicates) {
			predicates.add(new String(str));
		}
		predicatesSignatures = new ArrayList<List<Integer>>();
		for (final List<Integer> si : other.predicatesSignatures) {
			final List<Integer> copy = new ArrayList<Integer>();
			for (final Integer i : si) {
				copy.add(i);
			}
			predicatesSignatures.add(copy);
		}
		functions = new ArrayList<String>();
		for (final String str : other.functions) {
			functions.add(new String(str));
		}
		functionsSignatures = new ArrayList<List<Integer>>();
		for (final List<Integer> si : other.functionsSignatures) {
			final List<Integer> copy = new ArrayList<Integer>();
			for (final Integer i : si) {
				copy.add(i);
			}
			functionsSignatures.add(copy);
		}
		inertia = new ArrayList<>();
		for (final Inertia i : other.inertia) {
			inertia.add(i);
		}
		relevantFacts = new ArrayList<>();
		for (final IntExp exp : other.relevantFacts) {
			relevantFacts.add(new IntExp(exp));
		}
		
		operators = new LinkedHashMap<>();
		final Map<Signature, List<BitOp>> opsMap = other.getOperators();
		for (final Signature sign : opsMap.keySet()) {
			final List<BitOp> ops = opsMap.get(sign);
			for (final BitOp op : ops) {
				addOperator(new BitOp(op));
			}
		}
		
		goal = new BitExp(other.goal);
		init = new BitExp(other.init);
		initTaskNetwork = new TaskNetwork(other.initTaskNetwork);
		
		methods = new LinkedHashMap<>();
		final Map<Signature, List<OrderedMethod>> methsMap = other.getMethods();
		for (final Signature sign : methsMap.keySet()) {
			final List<OrderedMethod> meths = methsMap.get(sign);
			for (final OrderedMethod method : meths) {
				addMethod(new OrderedMethod(method));
			}
		}
		
		taskSymbols = new ArrayList<>();
		for (final String s : other.taskSymbols) {
			taskSymbols.add(new String(s));
		}
		
		relevantTasks = new ArrayList<>();
		for (final IntExp t : other.relevantTasks) {
			relevantTasks.add(new IntExp(t));
		}
		
		reachableTasks = new ArrayList<BitVector>();
		for (final BitVector task : other.reachableTasks) {
			reachableTasks.add(new BitVector(task));
		}
		
	}
	
	/**
	 * Returns the types table of the problem.
	 * 
	 * @return the types table of the problem.
	 */
	public final List<String> getTypes() {
		return types;
	}
	
	/**
	 * Sets the table of type of the problem.
	 * 
	 * @param types the table of type to set
	 */
	final void setTypes(final List<String> types) {
		this.types = types;
	}
	
	/**
	 * TO REMOVE
	 * 
	 * @return the inferedDomains
	 */
	final List<Set<Integer>> getInferedDomains() {
		return inferedDomains;
	}
	
	/**
	 * @param inferedDomains the inferedDomains to set
	 */
	final void setInferedDomains(final List<Set<Integer>> inferedDomains) {
		this.inferedDomains = inferedDomains;
	}
	
	/**
	 * Returns the table of domains for each type of the problem.
	 * 
	 * @return the table of domains for each type of the problem.
	 */
	public final List<Set<Integer>> getDomains() {
		return domains;
	}
	
	/**
	 * Set the domains corresponding at each types of the problem.
	 * 
	 * @param domains the domains to set.
	 */
	final void setDomains(final List<Set<Integer>> domains) {
		this.domains = domains;
	}
	
	/**
	 * Returns the constants of the problem.
	 * 
	 * @return the constants of the problem.
	 */
	public final List<String> getConstants() {
		return constants;
	}
	
	/**
	 * Sets the constants of the problem.
	 * 
	 * @param constants the constants to set
	 */
	final void setConstants(final List<String> constants) {
		this.constants = constants;
	}
	
	/**
	 * Returns the predicates of the problem.
	 * 
	 * @return the predicates of the problem.
	 */
	public final List<String> getPredicates() {
		return predicates;
	}
	
	/**
	 * Sets the predicates of the problem.
	 * 
	 * @param predicates the predicates to set.
	 */
	final void setPredicates(final List<String> predicates) {
		this.predicates = predicates;
	}
	
	/**
	 * Returns the tasks symbols of the problem.
	 * 
	 * @return the tasks symbols of the problem.
	 */
	public final List<String> getTaskSymbols() {
		return taskSymbols;
	}
	
	/**
	 * Returns the list of instantiated operators of the problem.
	 * 
	 * @return the list of instantiated operators of the problem.
	 */
	public Map<Signature, List<BitOp>> getOperators() {
		return operators;
	}
	
	/**
	 * Sets the list of instantiated operators of the problem.
	 * 
	 * @param operators the list of instantiated operators.
	 */
	public void setOperators(final Map<Signature, List<BitOp>> operators) {
		this.operators = operators;
	}
	
	public final Map<Signature, List<OrderedMethod>> getMethods() {
		return methods;
	}
	
	public final void setMethods(final Map<Signature, List<OrderedMethod>> newMethods) {
		methods = newMethods;
	}
	
	public final List<IntExp> getRelevantTasks() {
		return relevantTasks;
	}
	
	public final void setRelevantTasks(final List<IntExp> relevantTasks) {
		this.relevantTasks = relevantTasks;
	}
	
	public final List<BitVector> getReachableTasks() {
		return reachableTasks;
	}
	
	/**
	 * @return the positivePrimitiveEffectors
	 */
	public List<BitVector> getPositivePrimitiveEffectors() {
		return positivePrimitiveEffectors;
	}
	
	/**
	 * @return the positiveComposedEffectors
	 */
	public List<BitVector> getPositiveComposedEffectors() {
		return positiveComposedEffectors;
	}
	
	/**
	 * @return the negativePrimitiveEffectors
	 */
	public List<BitVector> getNegativePrimitiveEffectors() {
		return negativePrimitiveEffectors;
	}
	
	/**
	 * @return the negativeComposedEffectors
	 */
	public List<BitVector> getNegativeComposedEffectors() {
		return negativeComposedEffectors;
	}
	
	/**
	 * Returns the list of tasks that add the property
	 * 
	 * @param property the fact added by the list of tasks returned
	 * @return the list of tasks adding p
	 */
	public List<Integer> getAdd(final int property) {
		return positivePrimitiveEffectors.get(property).getAllSetBits();
	}
	
	/**
	 * Returns the list of tasks that delete the property
	 * 
	 * @param property the fact deleted by the list of tasks returned
	 * @return the list of tasks deleting p
	 */
	public List<Integer> getDel(final int property) {
		return negativePrimitiveEffectors.get(property).getAllSetBits();
	}
	
	/**
	 * Returns the list of tasks that may add the property
	 * 
	 * @param property the fact that may be added by the list of tasks returned
	 * @return the list of tasks that may add p
	 */
	public List<Integer> getMayAdd(final int property) {
		return positiveComposedEffectors.get(property).getAllSetBits();
	}
	
	/**
	 * Returns the list of tasks that may delete the property
	 * 
	 * @param property the fact that may be deleted by the list of tasks returned
	 * @return the list of tasks that may delete p
	 */
	public List<Integer> getMayDel(final int property) {
		return negativeComposedEffectors.get(property).getAllSetBits();
	}
	
	public final void setReachableTasks(final List<BitVector> reachableTasks) {
		this.reachableTasks = reachableTasks;
	}
	
	public final void setPositivePrimitiveEffectors(final List<BitVector> effectorsList) {
		positivePrimitiveEffectors = effectorsList;
	}
	
	public final void setPositiveComposedEffectors(final List<BitVector> effectorsList) {
		positiveComposedEffectors = effectorsList;
	}
	
	public final void setNegativePrimitiveEffectors(final List<BitVector> effectorsList) {
		negativePrimitiveEffectors = effectorsList;
	}
	
	public final void setNegativeComposedEffectors(final List<BitVector> effectorsList) {
		negativeComposedEffectors = effectorsList;
	}
	
	/**
	 * Sets the task symbols of the problem.
	 * 
	 * @param taks the task symbols to set.
	 */
	final void setTaskSymbols(final List<String> tasks) {
		taskSymbols = tasks;
	}
	
	/**
	 * Returns the signatures of the predicates defined in the problem.
	 * 
	 * @return the signatures of the predicates defined in the problem.
	 */
	public final List<List<Integer>> getPredicatesSignatures() {
		return predicatesSignatures;
	}
	
	/**
	 * Sets the signatures of the predicates defined in the problem.
	 * 
	 * @param signatures the signatures of the predicates defined in the problem.
	 */
	final void setPredicatesSignatures(final List<List<Integer>> signatures) {
		predicatesSignatures = signatures;
	}
	
	/**
	 * Returns the functions of the problem.
	 * 
	 * @return the functions of the problem.
	 */
	public final List<String> getFunctions() {
		return functions;
	}
	
	/**
	 * Sets the functions of the problem.
	 * 
	 * @param functions the functions to set.
	 */
	public final void setFunctions(final List<String> functions) {
		this.functions = functions;
	}
	
	/**
	 * Returns the signatures of the functions defined in the problem.
	 * 
	 * @return the signatures of the functions defined in the problem.
	 */
	public final List<List<Integer>> getFunctionsSignatures() {
		return functionsSignatures;
	}
	
	/**
	 * Sets the signatures of the function defined in the problem.
	 * 
	 * @param signatures the signatures of the function defined in the problem.
	 */
	final void setFunctionsSignatures(final List<List<Integer>> signatures) {
		functionsSignatures = signatures;
	}
	
	/**
	 * Returns the inertia status of the predicates defined in the problem.
	 * 
	 * @return the inertia status of the predicates defined in the problem.
	 */
	public final List<Inertia> getInertia() {
		return inertia;
	}
	
	/**
	 * Sets the inertia status of the predicates defined in the problem.
	 * 
	 * @param inertia the inertia to set.
	 */
	final void setInertia(final List<Inertia> inertia) {
		this.inertia = inertia;
	}
	
	/**
	 * Returns the list of relevant facts used the problem.
	 * 
	 * @return the list of relevant facts used the problem.
	 */
	public final List<IntExp> getRelevantFacts() {
		return relevantFacts;
	}
	
	/**
	 * Sets the list of relevant facts used the problem.
	 * 
	 * @param relavants the list of relevant facts to set.
	 */
	final void setRelevantFacts(final List<IntExp> relavants) {
		relevantFacts = relavants;
	}
	
	/**
	 * Returns the goal of the problem or null if the goal can is not reachable.
	 * 
	 * @return the goal of the problem.
	 */
	public final BitExp getGoal() {
		return goal;
	}
	
	/**
	 * Returns <code>true</code> if this problem is solvable, i.e., if its goal was not simplified to FALSE during the preprocessing.
	 * 
	 * @return <code>true</code> if this problem is solvable; <code>false</code>.
	 */
	public final boolean isSolvable() {
		return goal != null;
	}
	
	public final boolean isHtnSolvable() {
		return initTaskNetwork != null;
	}
	
	/**
	 * Sets the goal of the problem.
	 * 
	 * @param goal the goal to set
	 */
	final void setGoal(final BitExp goal) {
		this.goal = goal;
	}
	
	/**
	 * Returns the initial state of the problem.
	 * 
	 * @return the initial state of the problem.
	 */
	public final BitExp getInit() {
		return init;
	}
	
	/**
	 * Sets the initial state of the problem.
	 * 
	 * @param init the initial state to set.
	 */
	final void setInit(final BitExp init) {
		this.init = init;
	}
	
	/**
	 * Returns the initial expansion or null if none initial expansion was declared in the planning problem. An initial initTaskNetwork is used to encode HTN
	 * planning problem.
	 * 
	 * @return the initial taskNetwork.
	 */
	public final TaskNetwork getInitExpansion() {
		return initTaskNetwork;
	}
	
	/**
	 * Sets the initial expansion of the planning problem. An initial initTaskNetwork is used to encode HTN planning problem.
	 * 
	 * @param init the initial expansion to set.
	 */
	public final void setInitExpansion(final TaskNetwork init) {
		initTaskNetwork = init;
	}
	
	/**
	 * Returns a short string representation of the specified operator, i.e., only its name and the value of its parameters.
	 * 
	 * @param op the operator.
	 * @return a string representation of the specified operator.
	 */
	public final String toShortString(final BitOp op) {
		return StringEncoder.toShortString(op, constants);
	}
	
	/**
	 * Returns a string representation of the specified operator.
	 * 
	 * @param op the operator.
	 * @return a string representation of the specified operator.
	 */
	public final String toString(final BitOp op) {
		return StringEncoder.toString(op, constants, types, predicates, functions, relevantFacts, "\t");
	}
	
	/**
	 * Returns a string representation of an expression.
	 * 
	 * @param exp the expression.
	 * @return a string representation of the specified expression.
	 */
	public final String toString(final IntExp exp) {
		return StringEncoder.toString(exp, constants, types, predicates, functions, taskSymbols);
	}
	
	/**
	 * Returns a string representation of an expression.
	 * 
	 * @param exp the expression.
	 * @param sep the string separator used between the predicate symbol and the arguments.
	 * @return a string representation of the specified expression.
	 */
	public final String toString(final IntExp exp, final String sep) {
		return StringEncoder.toString(exp, constants, types, predicates, functions, taskSymbols, sep);
	}
	
	/**
	 * Returns a string representation of a bit expression.
	 * 
	 * @param exp the expression.
	 * @return a string representation of the specified expression.
	 */
	public final String toString(final BitExp exp) {
		return StringEncoder.toString(exp, constants, types, predicates, functions, relevantFacts);
	}
	
	/**
	 * Returns a string representation of a conditional bit expression.
	 * 
	 * @param exp the conditional expression.
	 * @return a string representation of the specified expression.
	 */
	public final String toString(final CondBitExp exp) {
		return StringEncoder.toString(exp, constants, types, predicates, functions, relevantFacts);
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
	public List<BitOp> getRelevantOperators(final int taskIndex) {
		final IntExp task = relevantTasks.get(taskIndex);
		final int taskNameIndex = task.getPredicate();
		final String taskName = taskSymbols.get(taskNameIndex);
		final int[] arguments = task.getArguments();
		final Signature taskSignature = new Signature(taskName, arguments);
		if (operators.containsKey(taskSignature)) {
			return operators.get(taskSignature);
		} else {
			return null;
		}
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
	public List<OrderedMethod> getRelevantMethods(final int taskIndex) {
		final IntExp task = relevantTasks.get(taskIndex);
		final int taskNameIndex = task.getPredicate();
		final String taskName = taskSymbols.get(taskNameIndex);
		final int[] arguments = task.getArguments();
		final Signature taskSignature = new Signature(taskName, arguments);
		if (methods.containsKey(taskSignature)) {
			return methods.get(taskSignature);
		} else {
			return null;
		}
	}
	
	/**
	 * Adds an Operator to the methods map
	 * 
	 * @param operator The method to add
	 * @author Ramoul
	 */
	public void addOperator(final BitOp operator) {
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
	public void addMethod(final OrderedMethod method) {
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
	 * Print an intExp if it represents a relevant Task
	 * 
	 * @param task The IntExp to print
	 * @return a String representing the task passed in parameters
	 */
	public String toTaskString(final IntExp task) {
		final StringBuffer taskString = new StringBuffer();
		taskString.append(getTaskSymbols().get(task.getPredicate()) + "  ");
		for (int i = 0; i < task.getArguments().length; i++) {
			taskString.append(getConstants().get(task.getArguments()[i]) + " ");
		}
		return taskString.toString();
	}
	
	/**
	 * Prints a task taking in account relative index
	 * 
	 * @param task The index of the printed task
	 * @return a string representation of the task
	 */
	public String printTask(final int task) {
		final StringBuffer str = new StringBuffer();
		final int absoluteTask = getAbsoluteIndex(task);
		final int occurrence = getOccurrence(task);
		str.append(toString(relevantTasks.get(absoluteTask)) + "(" + occurrence + ")");
		return str.toString();
		
	}
	
	/**
	 * Prints the the releventFacts of the passed indexes.
	 */
	public void printRelevantFacts(final List<Integer> indexes) {
		for (final int i : indexes) {
			final String predicate = getPredicates().get(getRelevantFacts().get(i).getPredicate());
			System.out.print(i + ": " + predicate + " :");
			for (int j = 0; j < getRelevantFacts().get(i).getArguments().length; j++) {
				System.out.print(" " + getConstants().get(getRelevantFacts().get(i).getArguments()[j]));
			}
			System.out.println();
		}
	}
	
	/**
	 * Creates a string of the relevantTasks of the passed indexes.
	 * 
	 * @param indexes
	 * @return A string representing the tasks of passed indexes
	 */
	public String printRelevantTasks(final List<Integer> indexes) {
		final StringBuffer str = new StringBuffer();
		for (final int i : indexes) {
			final String task = toTaskString(getRelevantTasks().get(i));
			str.append(i + ": " + task);
		}
		return str.toString();
	}
	
	/**
	 * Creates a string of the ordering constraints of the passed indexes.
	 * 
	 * @param indexes
	 * @return A string representing the ordering constraints of the passed indexes
	 */
	public String printOrderingConstraint(final List<OrderConstraint> orderCons) {
		final StringBuffer str = new StringBuffer();
		for (final OrderConstraint con : orderCons) {
			final int firstTaskIndex = con.getFirstTask().getTaskWithIndex(0);
			final int secondTaskIndex = con.getSecondTask().getTaskWithIndex(0);
			str.append("\n" + toTaskString(getRelevantTasks().get(firstTaskIndex)) + " < " + toTaskString(getRelevantTasks().get(secondTaskIndex)));
		}
		return str.toString();
	}
	
	/**
	 * Return a String representing the bit method passed in parameters
	 * 
	 * @param method The method to print
	 * @return The String representing the method
	 */
	public String printMethod(final OrderedMethod method) {
		return Preprocessing.toString(method);
	}
	
	/**
	 * Returns the index of a specified constraint. (The index of the first task * The size of the tasks table) + The index of the second task
	 * 
	 * @param constraint the table of constraints indexes.
	 * @return the index of the constraints.
	 */
	public int getIndexOfConstraint(final int[] constraint) {
		return constraint[0] * (relevantTasks.size()) + constraint[1];
	}
	
	/**
	 * Says if a specified task is primitive or not
	 * 
	 * @param task The specified task absolute index
	 * @return <code> true </code> if the task is primitive and <code> false </code> otherwise.
	 */
	public boolean isPrimitive(final int task) {
		final List<BitOp> ops = getRelevantOperators(task);
		if (ops == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * It says if a literal is positive or negative
	 * 
	 * @param literal The literal treated
	 * @return <code> true </code> if the literal is positive and <code> false </code> if it's negative
	 */
	public boolean isPositiveLiteral(final int literal) {
		return (relevantFacts.get(literal).getConnective().equals(Connective.NOT)) ? false : true;
	}
	
	/**
	 * find the index of a negation for a specified literal index
	 * 
	 * @param literal The literal treated
	 * @return the negative literal index
	 */
	public int getNegativeLiteral(final int literal) {
		if (isPositiveLiteral(literal)) {
			return literal + 1;
		} else {
			return literal - 1;
		}
	}
	
	/**
	 * Calculate the reference index for a task. It corresponds to the index of the task in the relevant tasks table
	 * 
	 * @param task The task for which we search the reference index
	 * @return The reference index for the specified task
	 */
	public int getAbsoluteIndex(final int task) {
		return task % relevantTasks.size();
	}
	
	/**
	 * Calculate the occurrence of a task.
	 * 
	 * @param task The task for which we search the occurrence
	 * @return The occurrence for the specified task
	 */
	public int getOccurrence(final int task) {
		return task / relevantTasks.size();
	}
}
