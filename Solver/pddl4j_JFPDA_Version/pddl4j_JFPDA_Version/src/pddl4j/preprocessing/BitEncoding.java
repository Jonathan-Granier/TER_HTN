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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pddl4j.parser.Connective;
import pddl4j.util.BitExp;
import pddl4j.util.BitOp;
import pddl4j.util.BitVector;
import pddl4j.util.CondBitExp;
import pddl4j.util.IntExp;
import pddl4j.util.OneTaskConstraint;
import pddl4j.util.OrderConstraint;
import pddl4j.util.OrderedMethod;
import pddl4j.util.TaskList;
import pddl4j.util.TaskNetwork;
import pddl4j.util.TwoTasksConstraint;

/**
 * <p>
 * This class contains methods to encode operators, goal and initial state into <code>BitSet</code> representation.
 * </p>
 * <p>
 * Before encoding, every expression is normalized, i.e., put in disjunctive normal form (DNF) for preconditions and put in conjunctive normal form (CNF) for
 * effects. If an operator has disjunctive preconditions, a new operator is created such all operators after normalization have only conjunctive precondition.
 * </p>
 * <p>
 * <b>Notes:</b>
 * <ul>
 * <li>the problem of converting a well form formula of the first order-logic into DNF of CNF is exponential.</li>
 * <li>The method does not compute the shorter DNF or CNF formula. If you want it have a look to the Quine and McCluskey algorithm.</li>
 * </ul>
 * <p>
 * 
 * @author D. Pellier
 * @version 1.0 - 10.06.2010
 */
final class BitEncoding {
	
	/**
	 * The default constructor with a private access to prevent instance creation.
	 */
	private BitEncoding() {
	}
	
	/**
	 * Encode a list of specified operators into <code>BitSet</code> representation. The specified map is used to speed-up the search by mapping the an
	 * expression to this index.
	 * 
	 * @param operators the list of operators to encode.
	 * @param map the map that associates to a specified expression its index.
	 */
	static void encodeOperators(final List<IntOp> operators, final Map<IntExp, Integer> map) {
		
		// Normalize the operators
		BitEncoding.normalize(operators);
		for (final IntOp op : operators) {
			final int arity = op.getArity();
			final BitOp bOp = new BitOp(op.getName(), arity);
			
			// Initialize the parameters of the operator
			for (int i = 0; i < arity; i++) {
				bOp.setValueOfParameter(i, op.getValueOfParameter(i));
				bOp.setTypeOfParameter(i, op.getTypeOfParameters(i));
			}
			// Initialize the preconditions of the operator
			bOp.setPreconditions(BitEncoding.encode(op.getPreconditions(), map));
			
			// Initialize the effects of the operator
			final List<IntExp> effects = op.getEffects().getChildren();
			
			final CondBitExp unCondEffects = new CondBitExp();
			boolean hasUnConditionalEffects = false;
			for (final IntExp ei : effects) {
				final Connective connective = ei.getConnective();
				final List<IntExp> children = ei.getChildren();
				if (connective.equals(Connective.WHEN)) {
					final CondBitExp condBitExp = new CondBitExp();
					condBitExp.setCondition(BitEncoding.encode(children.get(0), map));
					condBitExp.setEffects(BitEncoding.encode(children.get(1), map));
					bOp.getCondEffects().add(condBitExp);
				} else if (connective.equals(Connective.ATOM)) {
					final Integer index = map.get(ei);
					if (index != null) {
						unCondEffects.getEffects().getPositive().set(index);
						hasUnConditionalEffects = true;
					}
				} else if (connective.equals(Connective.TRUE)) {
					// do nothing
				} else if (connective.equals(Connective.NOT)) {
					final Integer index = map.get(children.get(0));
					if (index != null) {
						unCondEffects.getEffects().getNegative().set(index);
						hasUnConditionalEffects = true;
					}
				} else {
					throw new RuntimeException("unexpected expression " + Preprocessing.toString(ei));
				}
			}
			if (hasUnConditionalEffects) {
				bOp.getCondEffects().add(unCondEffects);
			}
			Preprocessing.addOperator(bOp);
		}
	}
	
	/**
	 * Encode a specified goal in a disjunction of <code>BitExp</code>. The specified map is used to speed-up the search by mapping the an expression to this
	 * index.
	 * 
	 * @param goal the goal to encode.
	 * @param map the map that associates to a specified expression its index.
	 * @return a list of <code>BitExp</code> that represents the goal as a disjunction of <code>BitExp</code>.
	 */
	static BitExp encodeGoal(final IntExp goal, final Map<IntExp, Integer> map) {
		if (goal.getConnective().equals(Connective.FALSE)) {
			return null;
		}
		BitExp newGoal = null;
		BitEncoding.toDNF(goal);
		Preprocessing.codedGoal = new ArrayList<BitExp>(goal.getChildren().size());
		for (final IntExp exp : goal.getChildren()) {
			if (exp.getConnective().equals(Connective.ATOM)) {
				final IntExp and = new IntExp(Connective.AND);
				and.getChildren().add(exp);
				Preprocessing.codedGoal.add(BitEncoding.encode(and, map));
			} else {
				Preprocessing.codedGoal.add(BitEncoding.encode(exp, map));
			}
		}
		if (Preprocessing.codedGoal.size() > 1) {
			// Create a new dummy fact to encode the goal
			final int dummy_predicate_index = Preprocessing.tableOfPredicates.size();
			Preprocessing.tableOfPredicates.add(Constants.DUMMY_GOAL);
			Preprocessing.tableOfTypedPredicates.add(new ArrayList<Integer>());
			final IntExp dummy_goal = new IntExp(Connective.ATOM);
			dummy_goal.setPredicate(dummy_predicate_index);
			dummy_goal.setArguments(new int[0]);
			final int dummy_goal_index = Preprocessing.tableOfRevelantFacts.size();
			Preprocessing.tableOfRevelantFacts.add(dummy_goal);
			map.put(dummy_goal, dummy_goal_index);
			newGoal = new BitExp();
			newGoal.getPositive().set(dummy_goal_index);
			final CondBitExp cond_effect = new CondBitExp(newGoal);
			// for each disjunction create a dummy action
			for (final BitExp dis : Preprocessing.codedGoal) {
				final BitOp op = new BitOp(Constants.DUMMY_OPERATOR, 0);
				op.setDummy(true);
				op.setPreconditions(dis);
				op.getCondEffects().add(cond_effect);
				Preprocessing.addOperator(op);
			}
		} else {
			newGoal = Preprocessing.codedGoal.get(0);
		}
		return newGoal;
	}
	
	/**
	 * Encode a specified initial state in it <code>BitExp</code> representation. The specified map is used to speed-up the search by mapping the an expression
	 * to this index.
	 * 
	 * @param init the initial state to encode.
	 * @param map the map that associates to a specified expression its index.
	 * @return the <code>BitExp</code> that represents the initial encoded.
	 */
	static BitExp encodeInit(final Set<IntExp> init, final Map<IntExp, Integer> map) {
		final BitExp bitInit = new BitExp();
		for (final IntExp fact : init) {
			if (fact.getConnective().equals(Connective.ATOM)) {
				final Integer i = map.get(fact);
				if (i != null) {
					bitInit.getPositive().set(i);
				}
			} else {
				final Integer i = map.get(fact.getChildren().get(0));
				if (i != null) {
					bitInit.getNegative().set(i);
				}
			}
		}
		return bitInit;
	}
	
	static void encode(final List<IntMeth> intMeths) {
		// Create a HashMaps to speedup the process
		final Map<IntExp, Integer> cacheMap = new LinkedHashMap<>(Preprocessing.tableOfRelevantTasks.size());
		for (int i = 0; i < Preprocessing.tableOfRelevantTasks.size(); i++) {
			cacheMap.put(Preprocessing.tableOfRelevantTasks.get(i), i);
		}
		final Map<IntExp, Integer> cacheMap2 = new LinkedHashMap<>(Preprocessing.tableOfRevelantFacts.size());
		for (int i = 0; i < Preprocessing.tableOfRevelantFacts.size(); i++) {
			cacheMap2.put(Preprocessing.tableOfRevelantFacts.get(i), i);
		}
		for (final IntMeth intMeth : intMeths) {
			final OrderedMethod bitMeth = new OrderedMethod(BitEncoding.encode(intMeth, cacheMap, cacheMap2));
			boolean containsIrrelevants = false;
			for (final int task : bitMeth.getExpansion()) {
				if (task == -1) {
					containsIrrelevants = true;
				}
			}
			if (!containsIrrelevants) {
				Preprocessing.addMethod(bitMeth);
			}
		}
	}
	
	/**
	 * Encode a method in its bit representation.
	 * 
	 * @param meth the method to encode.
	 * @return the method encoded.
	 */
	static OrderedMethod encode(final IntMeth meth, final Map cacheMap, final Map cacheMap2) {
		final int arity = meth.getArity();
		final OrderedMethod orderedMeth = new OrderedMethod(meth.getName(), arity);
		
		// Initialize the parameters of the method
		long localNow = System.currentTimeMillis();
		
		for (int i = 0; i < arity; i++) {
			orderedMeth.setValueOfParameter(i, meth.getValueOfParameter(i));
			orderedMeth.setTypeOfParameter(i, meth.getTypeOfParameters(i));
		}
		Preprocessing.enc1 += System.currentTimeMillis() - localNow;
		
		// Encode the expansion
		localNow = System.currentTimeMillis();
		
		final List<Integer> expansion = orderedMeth.getExpansion();
		for (final IntExp task : meth.getExpansion().getChildren()) {
			expansion.add((Integer) cacheMap.get(task));
		}
		Preprocessing.enc2 += System.currentTimeMillis() - localNow;
		
		//Encode the preconditions
		localNow = System.currentTimeMillis();
		
		final BitVector positivePreconditions = orderedMeth.getPositivePreconditions();
		final BitVector negativePreconditions = orderedMeth.getNegativePreconditions();
		
		BitEncoding.normalizeConstraint(meth.getConstraints());
		Preprocessing.enc3 += System.currentTimeMillis() - localNow;
		
		localNow = System.currentTimeMillis();
		
		for (final IntExp c : meth.getConstraints().getChildren()) {
			switch (c.getConnective()) {
				case BEFORE:
				case AFTER: {
					int factIndex = -1;
					final IntExp predicate = c.getChildren().get(0);
					if (predicate.getConnective().equals(Connective.NOT)) {
						factIndex = (Integer) cacheMap2.get(predicate.getChildren().get(0));
						negativePreconditions.set(factIndex);
					} else {
						factIndex = (Integer) cacheMap2.get(predicate);
						positivePreconditions.set(factIndex);
						
					}
				}
					break;
					
				default:
					break;
			}
			Preprocessing.enc4 += System.currentTimeMillis() - localNow;
			
		}
		return orderedMeth;
		
	}
	
	/**
	 * Encode the goal expansion in its bit representation.
	 * 
	 * @param tasks the list of tasks of the expansion.
	 * @param constraints the constraints of the expansion.
	 * @param he bit representation.
	 */
	static TaskNetwork encodeGoalExpansion(final IntExp tasks, final IntExp constraints) {
		return BitEncoding.encodeExpansion(tasks, constraints);
	}
	
	/**
	 * Encode an expansion in its final representation.
	 * 
	 * @param tasks the list of tasks of the expansion.
	 * @param constraints the constraints of the expansion.
	 * @return the encoded expansion in the form of task network
	 */
	private static TaskNetwork encodeExpansion(final IntExp tasks, final IntExp constraints) {
		final TaskNetwork encodedTaskNetwork = new TaskNetwork();
		for (final IntExp task : tasks.getChildren()) {
			encodedTaskNetwork.getTasks().add(Preprocessing.tableOfRelevantTasks.indexOf(task));
		}
		BitEncoding.normalizeConstraint(constraints);
		for (final IntExp c : constraints.getChildren()) {
			switch (c.getConnective()) {
				case SERIES:
					final int firstTaskIndex = Preprocessing.tableOfRelevantTasks.indexOf(c.getChildren().get(0));
					final int secondTaskIndex = Preprocessing.tableOfRelevantTasks.indexOf(c.getChildren().get(1));
					final OrderConstraint orderCon = new OrderConstraint(firstTaskIndex, secondTaskIndex);
					encodedTaskNetwork.getOrderingConstraints().add(orderCon);
					break;
				case BEFORE:
				case AFTER:
					encodeOneTaskConstraint(encodedTaskNetwork, c);
					break;
				case BETWEEN:
					encodeTwoTasksConstraint(encodedTaskNetwork, c);
					break;
				default:
					break;
			}
		}
		return encodedTaskNetwork;
	}
	
	/**
	 * Encode the BEFORE and AFTER constraints into OneTaskConstraint and add them to Task Network
	 * 
	 * @param finalExp The task network in which the constraint will be added
	 * @param c The intExp constraint to encode
	 */
	private static void encodeOneTaskConstraint(final TaskNetwork finalExp, final IntExp c) {
		// The encoding of the fact
		// If it's a negative constraint we take the index of the fact + 1
		// which is the index of this fact negation
		int factIndex = -1;
		final IntExp predicate = c.getChildren().get(0);
		final IntExp task = c.getChildren().get(1);
		if (predicate.getConnective().equals(Connective.NOT)) {
			factIndex = Preprocessing.tableOfRevelantFacts.indexOf(predicate.getChildren().get(0)) + 1;
		} else {
			factIndex = Preprocessing.tableOfRevelantFacts.indexOf(predicate);
		}
		OneTaskConstraint oneTaskCon;
		
		// The encoding of the task or the task List
		if (task.getConnective().equals(Connective.TASK)) {
			final int taskIndex = Preprocessing.tableOfRelevantTasks.indexOf(task);
			oneTaskCon = new OneTaskConstraint(factIndex, taskIndex);
		} else {
			final List<Integer> indexesList = new ArrayList<>();
			for (final IntExp intTask : task.getChildren()) {
				indexesList.add(Preprocessing.tableOfRelevantTasks.indexOf(intTask));
			}
			final TaskList taskList = new TaskList(true, indexesList);
			if (task.getConnective().equals(Connective.LAST)) {
				taskList.setLabel(false);
			}
			oneTaskCon = new OneTaskConstraint(factIndex, taskList);
		}
		
		// Adding the encoded constraint to the after of the before list
		if (c.getConnective().equals(Connective.BEFORE)) {
			finalExp.getBeforeConstraints().add(oneTaskCon);
		} else if (c.getConnective().equals(Connective.AFTER)) {
			finalExp.getAfterConstraints().add(oneTaskCon);
		}
	}
	
	/**
	 * Encode the BETWEEN constraints into OneTaskConstraint and add them to Task Network
	 * 
	 * @param finalExp The task network in which the constraint will be added
	 * @param c The intExp constraint to encode
	 */
	private static void encodeTwoTasksConstraint(final TaskNetwork finalExp, final IntExp c) {
		final int factIndex = Preprocessing.tableOfRevelantFacts.indexOf(c.getChildren().get(0));
		int firstTaskIndex, secondTaskIndex;
		TwoTasksConstraint twoTasksCon;
		
		// if the two tasks of the constraint are simple
		if (c.getChildren().get(1).getConnective().equals(Connective.TASK) && c.getChildren().get(2).getConnective().equals(Connective.TASK)) {
			firstTaskIndex = Preprocessing.tableOfRelevantTasks.indexOf(c.getChildren().get(1));
			secondTaskIndex = Preprocessing.tableOfRelevantTasks.indexOf(c.getChildren().get(2));
			twoTasksCon = new TwoTasksConstraint(factIndex, firstTaskIndex, secondTaskIndex);
			
			// if the first task of the constraint is simple and the second is task list
		} else if (c.getChildren().get(1).getConnective().equals(Connective.TASK)) {
			firstTaskIndex = Preprocessing.tableOfRelevantTasks.indexOf(c.getChildren().get(1));
			final List<Integer> indexesList = new ArrayList<Integer>();
			for (final IntExp intTask : c.getChildren().get(2).getChildren()) {
				indexesList.add(Preprocessing.tableOfRelevantTasks.indexOf(intTask));
			}
			final TaskList taskList = new TaskList(true, indexesList);
			if (c.getChildren().get(2).getConnective().equals(Connective.LAST)) {
				taskList.setLabel(false);
			}
			twoTasksCon = new TwoTasksConstraint(factIndex, firstTaskIndex, taskList);
			
			// if the first task of the constraint is a task list and the second is simple
		} else if (c.getChildren().get(2).getConnective().equals(Connective.TASK)) {
			secondTaskIndex = Preprocessing.tableOfRelevantTasks.indexOf(c.getChildren().get(2));
			final List<Integer> indexesList = new ArrayList<Integer>();
			for (final IntExp intTask : c.getChildren().get(1).getChildren()) {
				indexesList.add(Preprocessing.tableOfRelevantTasks.indexOf(intTask));
			}
			final TaskList taskList = new TaskList(true, indexesList);
			if (c.getChildren().get(1).getConnective().equals(Connective.LAST)) {
				taskList.setLabel(false);
			}
			twoTasksCon = new TwoTasksConstraint(factIndex, taskList, secondTaskIndex);
			
			// if the first and the second tasks of the constraint are task lists
		} else {
			final List<Integer> firstIndexesList = new ArrayList<Integer>();
			final List<Integer> secondIndexesList = new ArrayList<Integer>();
			for (final IntExp intTask : c.getChildren().get(1).getChildren()) {
				firstIndexesList.add(Preprocessing.tableOfRelevantTasks.indexOf(intTask));
			}
			for (final IntExp intTask : c.getChildren().get(2).getChildren()) {
				secondIndexesList.add(Preprocessing.tableOfRelevantTasks.indexOf(intTask));
			}
			final TaskList firstTaskList = new TaskList(true, firstIndexesList);
			final TaskList secondTaskList = new TaskList(true, secondIndexesList);
			if (c.getChildren().get(1).getConnective().equals(Connective.LAST)) {
				firstTaskList.setLabel(false);
			}
			if (c.getChildren().get(2).getConnective().equals(Connective.LAST)) {
				secondTaskList.setLabel(false);
			}
			twoTasksCon = new TwoTasksConstraint(factIndex, firstTaskList, secondTaskList);
		}
		
		finalExp.getBetweenConstraints().add(twoTasksCon);
	}
	
	/**
	 * Encode an specified <code>IntExp</code> in its <code>BitExp</code> representation. The specified map is used to speed-up the search by mapping the an
	 * expression to this index.
	 * 
	 * @param exp the <code>IntExp</code>.
	 * @param map the map that associate to a specified expression its index.
	 * @return the expression in bit set representation.
	 */
	private static BitExp encode(final IntExp exp, final Map<IntExp, Integer> map) {
		final BitExp bitExp = new BitExp();
		if (exp.getConnective().equals(Connective.ATOM)) {
			final Integer index = map.get(exp);
			if (index != null) {
				bitExp.getPositive().set(index);
			}
		} else if (exp.getConnective().equals(Connective.NOT)) {
			final Integer index = map.get(exp.getChildren().get(0));
			if (index != null) {
				bitExp.getNegative().set(index);
			}
		} else if (exp.getConnective().equals(Connective.AND)) {
			final List<IntExp> children = exp.getChildren();
			for (final IntExp ei : children) {
				if (ei.getConnective().equals(Connective.ATOM)) {
					final Integer index = map.get(ei);
					if (index != null) {
						bitExp.getPositive().set(index);
					}
				} else if (ei.getConnective().equals(Connective.NOT)) {
					final Integer index = map.get(ei.getChildren().get(0));
					if (index != null) {
						bitExp.getNegative().set(index);
					}
				} else if (ei.getConnective().equals(Connective.TRUE)) {
					// do nothing
				} else {
					throw new RuntimeException("unexpected expression " + Preprocessing.toString(exp));
				}
			}
		} else {
			System.out.println(Preprocessing.toString(exp));
			System.exit(0);
		}
		return bitExp;
	}
	
	/**
	 * Normalize the operators, i.e, put in disjunctive normal form (DNF) for preconditions and put in conjunctive normal form (CNF) for effects. If an operator
	 * has disjunctive preconditions, a new operator is created such all operators after normalization have only conjunctive precondition.
	 * 
	 * @param operators the list of operators to normalize.
	 */
	private static void normalize(final List<IntOp> operators) {
		final List<IntOp> tmpOps = new ArrayList<IntOp>(operators.size() + 100);
		for (final IntOp op : operators) {
			BitEncoding.toCNF(op.getEffects());
			BitEncoding.simplify(op.getEffects());
			final IntExp precond = op.getPreconditions();
			BitEncoding.toDNF(precond);
			for (final IntExp ei : precond.getChildren()) {
				final String name = op.getName();
				final int arity = op.getArity();
				final IntOp newOp = new IntOp(name, arity);
				for (int i = 0; i < arity; i++) {
					newOp.setTypeOfParameter(i, op.getTypeOfParameters(i));
				}
				for (int i = 0; i < arity; i++) {
					newOp.setValueOfParameter(i, op.getValueOfParameter(i));
				}
				newOp.setPreconditions(ei);
				
				newOp.setEffects(new IntExp(op.getEffects()));
				tmpOps.add(newOp);
			}
			
		}
		operators.clear();
		operators.addAll(tmpOps);
	}
	
	/**
	 * Remove overlapped expression from a specified expression.
	 * 
	 * @param exp the expression.
	 */
	private static void simplify(final IntExp exp) {
		boolean simplified = true;
		while (simplified) {
			simplified = false;
			final List<IntExp> children = exp.getChildren();
			for (int i = 0; i < children.size(); i++) {
				final IntExp ei = children.get(i);
				if (ei.getConnective().equals(Connective.AND) || ei.getConnective().equals(Connective.OR)) {
					simplified = true;
					children.remove(i);
					for (final IntExp ej : ei.getChildren()) {
						children.add(i, ej);
						i++;
					}
				}
			}
		}
	}
	
	static void normalizeConstraint(final IntExp exp) {
		switch (exp.getConnective()) {
			case AND:
				final List<IntExp> children = exp.getChildren();
				for (int i = 0; i < children.size(); i++) {
					final IntExp ei = children.get(i);
					BitEncoding.normalizeConstraint(ei);
				}
				BitEncoding.simplify(exp);
				break;
			case SERIES:
				exp.setConnective(Connective.AND);
				List<IntExp> constraints = new ArrayList<IntExp>();
				for (int i = 0; i < exp.getChildren().size(); i++) {
					final IntExp t1 = exp.getChildren().get(i);
					for (int j = i + 1; j < exp.getChildren().size(); j++) {
						final IntExp t2 = exp.getChildren().get(j);
						final IntExp series = new IntExp(Connective.SERIES);
						series.getChildren().add(new IntExp(t1));
						series.getChildren().add(new IntExp(t2));
						constraints.add(series);
					}
				}
				exp.getChildren().clear();
				exp.getChildren().addAll(constraints);
				break;
			case BEFORE:
			case AFTER:
				constraints = new ArrayList<IntExp>();
				final List<IntExp> predicates = new ArrayList<IntExp>();
				final IntExp gd = exp.getChildren().get(0);
				if (gd.getConnective().equals(Connective.AND)) {
					for (final IntExp p : gd.getChildren()) {
						predicates.add(p);
					}
				} else {
					predicates.add(gd);
				}
				for (final IntExp p : predicates) {
					for (int i = 1; i < exp.getChildren().size(); i++) {
						final IntExp c = new IntExp(exp.getConnective());
						c.getChildren().add(new IntExp(p));
						c.getChildren().add(new IntExp(exp.getChildren().get(i)));
						constraints.add(c);
					}
				}
				exp.setConnective(Connective.AND);
				exp.getChildren().clear();
				exp.getChildren().addAll(constraints);
				break;
			case BETWEEN:
				constraints = new ArrayList<IntExp>();
				final List<IntExp> betweenPredicates = new ArrayList<IntExp>();
				final IntExp gdBetween = exp.getChildren().get(0);
				if (gdBetween.getConnective().equals(Connective.AND)) {
					for (final IntExp p : gdBetween.getChildren()) {
						betweenPredicates.add(p);
					}
				} else {
					betweenPredicates.add(gdBetween);
				}
				for (final IntExp p : betweenPredicates) {
					final IntExp c = new IntExp(exp.getConnective());
					c.getChildren().add(new IntExp(p));
					c.getChildren().add(new IntExp(exp.getChildren().get(1)));
					c.getChildren().add(new IntExp(exp.getChildren().get(2)));
					constraints.add(c);
				}
				exp.setConnective(Connective.AND);
				exp.getChildren().clear();
				exp.getChildren().addAll(constraints);
				break;
			default:
		}
	}
	
	/**
	 * Convert an expression in disjunctive normal form (DNF).
	 *
	 * @param exp the expression to transform in DNF.
	 */
	static void toCNF(final IntExp exp) {
		switch (exp.getConnective()) {
			case WHEN:
				final IntExp antecedent = exp.getChildren().get(0);
				final IntExp consequence = exp.getChildren().get(1);
				BitEncoding.toDNF(antecedent);
				exp.setConnective(Connective.AND);
				exp.getChildren().clear();
				for (final IntExp ei : antecedent.getChildren()) {
					final IntExp newWhen = new IntExp(Connective.WHEN);
					newWhen.getChildren().add(ei);
					newWhen.getChildren().add(consequence);
					exp.getChildren().add(newWhen);
				}
				break;
			case AND:
				final List<IntExp> children = exp.getChildren();
				for (int i = 0; i < children.size(); i++) {
					final IntExp ei = children.get(i);
					BitEncoding.toCNF(ei);
					exp.getChildren().remove(i);
					for (final IntExp ej : ei.getChildren()) {
						exp.getChildren().add(i, ej);
						i++;
					}
				}
				break;
			case ATOM:
			case NOT:
			case TRUE:
				final IntExp copy = new IntExp(exp);
				exp.setConnective(Connective.AND);
				exp.getChildren().clear();
				exp.getChildren().add(copy);
				break;
			default:
				throw new RuntimeException("unexpected expression " + Preprocessing.toString(exp));
		}
	}
	
	/**
	 * Convert an expression in conjunctive normal form (CNF).
	 * 
	 * @param exp the expression to transform in CNF.
	 */
	static void toDNF(final IntExp exp) {
		switch (exp.getConnective()) {
			case OR:
				List<IntExp> children = exp.getChildren();
				for (int i = 0; i < children.size(); i++) {
					final IntExp ei = children.get(i);
					BitEncoding.toDNF(ei);
					if (ei.getConnective().equals(Connective.OR)) {
						children.remove(i);
						for (final IntExp ej : ei.getChildren()) {
							children.add(i, ej);
							i++;
						}
					}
				}
				break;
			case AND:
				children = exp.getChildren();
				for (int i = 0; i < children.size(); i++) {
					final IntExp ei = children.get(i);
					BitEncoding.toDNF(ei);
				}
				IntExp dnf = exp.getChildren().get(0);
				for (int i = 1; i < exp.getChildren().size(); i++) {
					final IntExp orExp = exp.getChildren().get(i);
					final IntExp newOr = new IntExp(Connective.OR);
					for (final IntExp ej : dnf.getChildren()) {
						for (final IntExp ek : orExp.getChildren()) {
							final IntExp newAnd = ej;
							for (final IntExp el : ek.getChildren()) {
								if (!newAnd.getChildren().contains(el)) {
									if (el.getConnective().equals(Connective.OR) || el.getConnective().equals(Connective.AND) && el.getChildren().size() == 1) {
										newAnd.getChildren().add(el.getChildren().get(0));
									} else {
										newAnd.getChildren().add(el);
									}
								}
							}
							boolean add = true;
							for (final IntExp el : newAnd.getChildren()) {
								if (el.getConnective().equals(Connective.FALSE)) {
									add = false;
									break;
								}
							}
							if (add) {
								if (newAnd.getChildren().size() == 1) {
									newOr.getChildren().add(newAnd.getChildren().get(0));
								} else {
									newOr.getChildren().add(newAnd);
								}
							}
						}
					}
					dnf = newOr;
				}
				exp.affect(dnf);
				break;
			case ATOM:
			case NOT:
			case TRUE:
				final IntExp and = new IntExp(Connective.AND);
				and.getChildren().add(new IntExp(exp));
				exp.setConnective(Connective.OR);
				exp.getChildren().clear();
				exp.getChildren().add(and);
				break;
			default:
				throw new RuntimeException("unexpected expression " + Preprocessing.toString(exp));
		}
	}
	
}
