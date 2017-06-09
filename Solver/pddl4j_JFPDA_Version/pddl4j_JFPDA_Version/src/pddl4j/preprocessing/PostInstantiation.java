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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import pddl4j.parser.Connective;
import pddl4j.util.IntExp;

/**
 * <p>
 * This class contains the methods needed for the post instantiation step the preprocessing. In other words, it contains methods to extract the relevant facts
 * from the instantiated operators and methods to simplify the operator based on ground inertia information.
 * </p>
 * <p>
 * <i>Definition:</i> A ground fact is a positive ground inertia if and only if it does not occur positively in an unconditional effect or the consequent of a
 * conditional effect of an action.
 * </p>
 * <p>
 * <i>Definition:</i> A ground fact is a negative ground inertia if and only if it does not occur negatively in an unconditional effect or the consequent of a
 * conditional effect of an action.
 * </p>
 * An initial fact, which is a negative ground inertia, is never made FALSE and thus always satisfied in all reachable world states. It can be removed from the
 * state description. All its occurrences in the preconditions of actions and in the antecedents of conditional effects can be simplified to TRUE. A fact, which
 * is a positive ground inertia and not contained in the initial state, is never satisfied in any reachable world state. All its occurrences in the
 * preconditions of actions and in the antecedents of conditional effects can be simplified to FALSE. The remaining facts are fluents that, roughly speaking,
 * can possibly change their truth value during the planning process. They are therefore relevant to the representation of the planning problem.
 * 
 * @author D. Pellier
 * @version 1.0 - 10.04.2010
 */
final class PostInstantiation {
	
	/**
	 * The default constructor with a private access to prevent instance creation.
	 */
	private PostInstantiation() {
	}
	
	/**
	 * Extracts the relevant facts from the instantiated operator. A ground fact is relevant if and only if:
	 * <ul>
	 * <li>1. it is an initial fact and not a negative ground inertia, or if</li>
	 * <li>2. it is not an initial fact and not a positive ground inertia.</li>
	 * </ul>
	 * 
	 * @param operators the list of operators.
	 * @param init the initial state.
	 */
	static void extractRelevantFacts(final List<IntOp> operators, final Set<IntExp> init) {
		final Set<IntExp> relevants = new LinkedHashSet<IntExp>(10000);
		for (final IntOp op : operators) {
			PostInstantiation.extractRelevantFacts(op.getPreconditions(), relevants, init);
			PostInstantiation.extractRelevantFacts(op.getEffects(), relevants, init);
		}
		
		Preprocessing.tableOfRevelantFacts = new ArrayList<IntExp>(relevants.size());
		for (final IntExp exp : relevants) {
			final IntExp relevant = new IntExp(exp);
			Preprocessing.tableOfRevelantFacts.add(relevant);
		}
		//Add equal (=) constraint to the tableOfRelevantFacts
		Preprocessing.tableOfPredicates.add("=");
		for (int i = 0; i < Preprocessing.tableOfConstants.size(); i++) {
			for (final int j = 0; i < Preprocessing.tableOfConstants.size(); i++) {
				final IntExp equalExp = new IntExp(Connective.ATOM);
				equalExp.setPredicate(Preprocessing.tableOfPredicates.indexOf("="));
				final int[] args = {
					i,
					j
				};
				equalExp.setArguments(args);
				Preprocessing.tableOfRevelantFacts.add(equalExp);
			}
		}
		//Add Not constraints in the tableOfRelevantFacts
		for (int i = 0; i < Preprocessing.tableOfRevelantFacts.size(); i += 2) {
			final IntExp notExp = new IntExp(Connective.NOT);
			notExp.getChildren().add(Preprocessing.tableOfRevelantFacts.get(i));
			Preprocessing.tableOfRevelantFacts.add(i + 1, notExp);
		}
	}
	
	/**
	 * Extracts the relevant facts from a specified expression. A ground fact is relevant if and only if:
	 * <ul>
	 * <li>1. it is an initial fact and not a negative ground inertia, or if</li>
	 * <li>2. it is not an initial fact and not a positive ground inertia.</li>
	 * </ul>
	 * 
	 * @param exp the expression.
	 * @param relevants the set of relevant facts.
	 * @param init the initial state.
	 */
	private static void extractRelevantFacts(final IntExp exp, final Set<IntExp> relevants, final Set<IntExp> init) {
		switch (exp.getConnective()) {
			case ATOM:
				relevants.add(exp);
				
				break;
			case FN_HEAD:
				break;
			case EQUAL_ATOM:
				relevants.add(exp);
				break;
			case AND:
			case OR:
				for (final IntExp e : exp.getChildren()) {
					PostInstantiation.extractRelevantFacts(e, relevants, init);
				}
				break;
			case FORALL:
			case EXISTS:
			case AT_START:
			case AT_END:
			case UMINUS:
			case ALWAYS:
			case OVER_ALL:
			case SOMETIME:
			case AT_MOST_ONCE:
			case NOT:
				PostInstantiation.extractRelevantFacts(exp.getChildren().get(0), relevants, init);
				break;
			case WHEN:
			case LESS:
			case LESS_OR_EQUAL:
			case EQUAL:
			case GREATER:
			case GREATER_OR_EQUAL:
			case ASSIGN:
			case INCREASE:
			case DECREASE:
			case SCALE_UP:
			case SCALE_DOWN:
			case MUL:
			case DIV:
			case MINUS:
			case PLUS:
			case F_EXP:
			case SOMETIME_AFTER:
			case SOMETIME_BEFORE:
			case WITHIN:
			case HOLD_AFTER:
				PostInstantiation.extractRelevantFacts(exp.getChildren().get(0), relevants, init);
				PostInstantiation.extractRelevantFacts(exp.getChildren().get(1), relevants, init);
				break;
			case F_EXP_T:
				if (!exp.getChildren().isEmpty()) {
					PostInstantiation.extractRelevantFacts(exp.getChildren().get(0), relevants, init);
				}
				break;
			case ALWAYS_WITHIN:
			case HOLD_DURING:
				PostInstantiation.extractRelevantFacts(exp.getChildren().get(0), relevants, init);
				PostInstantiation.extractRelevantFacts(exp.getChildren().get(1), relevants, init);
				PostInstantiation.extractRelevantFacts(exp.getChildren().get(3), relevants, init);
				break;
			case FN_ATOM:
			case NUMBER:
			case DURATION_ATOM:
			case TIME_VAR:
			case IS_VIOLATED:
			case MINIMIZE:
			case MAXIMIZE:
				break;
		}
		
	}
	
	/**
	 * This method simplify a specified expression.
	 * 
	 * @param exp the expression to simplify.
	 */
	private static void simplify(final IntExp exp) {
		switch (exp.getConnective()) {
			case ATOM:
				break;
			case FN_HEAD:
				break;
			case AFTER:
			case BEFORE:
			case BETWEEN:
				final IntExp newExp = exp.getChildren().get(0);
				PostInstantiation.simplify(newExp);
				if (newExp.getConnective().equals(Connective.TRUE)) {
					exp.setConnective(Connective.TRUE);
				} else if (newExp.getConnective().equals(Connective.FALSE)) {
					exp.setConnective(Connective.FALSE);
				}
				break;
			case EQUAL_ATOM:
				final int[] args = exp.getArguments();
				// Get and substitute the first argument
				final int arg1 = args[0];
				// Get and substitute the second argument
				final int arg2 = args[1];
				// The equality is TRUE: arg1 and arg2 are the same variable or the same constant
				if (arg1 == arg2) {
					exp.setConnective(Connective.TRUE);
				}
				// The equality is ground and the equality is FALSE because arg1 != arg2
				else if (arg1 >= 0 && arg2 >= 0 && arg1 != arg2) {
					exp.setConnective(Connective.FALSE);
				}
				break;
			case AND:
				int i = 0;
				while (i < exp.getChildren().size() && exp.getConnective().equals(Connective.AND)) {
					final IntExp ei = exp.getChildren().get(i);
					PostInstantiation.simplify(ei);
					// If a child expression is FALSE, the whole conjunction becomes FALSE.
					if (ei.getConnective().equals(Connective.FALSE)) {
						exp.setConnective(Connective.FALSE);
					}
					// If a child expression is TRUE, we can remove the child expression.
					else if (ei.getConnective().equals(Connective.TRUE)) {
						exp.getChildren().remove(i);
					}
					// If the child expression to add is a conjunction, we can simplify the expression
					// by
					// removing the inner conjunction.
					else if (ei.getConnective().equals(Connective.AND)) {
						exp.getChildren().remove(i);
						int j = 0;
						int added = 0;
						while (j < ei.getChildren().size() && exp.getConnective().equals(Connective.AND)) {
							final IntExp ej = ei.getChildren().get(j);
							if (ej.getConnective().equals(Connective.FALSE)) {
								exp.setConnective(Connective.FALSE);
							} else if (ej.getConnective().equals(Connective.TRUE)) {
								// do nothing
							} else {
								exp.getChildren().add(i + added, ej);
								added++;
							}
							j++;
						}
						i += added + 1;
					}
					// Simplification of the conditional expression.
					else if (ei.getConnective().equals(Connective.WHEN)) {
						final IntExp antecedent = ei.getChildren().get(0);
						final IntExp consequent = ei.getChildren().get(1);
						// If the antecedent of a conditional effect becomes FALSE , the conditional
						// effect is removed from the operator. In this case, the effect is never
						// applicable
						// because it requires FALSE to hold, i.e., the state must be inconsistent.
						if (antecedent.getConnective().equals(Connective.FALSE)) {
							exp.getChildren().remove(i);
						}
						// If the antecedent of a conditional effect becomes TRUE, the conditional
						// effect becomes unconditional.
						else if (antecedent.getConnective().equals(Connective.TRUE)) {
							if (consequent.getConnective().equals(Connective.AND)) {
								exp.getChildren().remove(i);
								int j = 0;
								int added = 0;
								while (j < consequent.getChildren().size() && exp.getConnective().equals(Connective.AND)) {
									final IntExp ej = consequent.getChildren().get(j);
									if (ej.getConnective().equals(Connective.FALSE)) {
										exp.setConnective(Connective.FALSE);
									} else if (ej.getConnective().equals(Connective.TRUE)) {
										// do nothing
									} else {
										exp.getChildren().add(i + added, ej);
										added++;
									}
									j++;
								}
								i += added + 1;
							} else {
								exp.getChildren().set(i, consequent);
								i++;
							}
						}
						// If the consequent of a conditional effect becomes TRUE, the conditional
						// effect is removed because it does not lead to any state transition.
						else if (consequent.getConnective().equals(Connective.TRUE)) {
							exp.getChildren().remove(i);
						} else {
							i++;
						}
					} else {
						i++;
					}
				}
				// Finally, if the conjunction is empty, the expression becomes TRUE.
				if (exp.getChildren().isEmpty()) {
					exp.setConnective(Connective.TRUE);
				} else if (exp.getChildren().size() == 1) {
					exp.affect(exp.getChildren().get(0));
				}
				break;
			case OR:
				i = 0;
				while (i < exp.getChildren().size() && exp.getConnective().equals(Connective.OR)) {
					final IntExp ei = exp.getChildren().get(i);
					PostInstantiation.simplify(ei);
					// If a child expression is TRUE, the whole disjunction is TRUE.
					if (ei.getConnective().equals(Connective.TRUE)) {
						exp.setConnective(Connective.TRUE);
					}
					// If the child expression to add is a disjunction, we can simplify the expression
					// by
					// removing the inner disjunction.
					else if (ei.getConnective().equals(Connective.OR)) {
						exp.getChildren().remove(i);
						int j = 0;
						int added = 0;
						while (j < ei.getChildren().size() && exp.getConnective().equals(Connective.OR)) {
							final IntExp ej = ei.getChildren().get(j);
							if (ej.getConnective().equals(Connective.TRUE)) {
								exp.setConnective(Connective.TRUE);
							} else if (ej.getConnective().equals(Connective.FALSE)) {
								// do nothing
							} else {
								exp.getChildren().add(i + added, ej);
								added++;
							}
							j++;
						}
						i += added + 1;
					}
					// Simplification of the conditional expression.
					else if (ei.getConnective().equals(Connective.WHEN)) {
						final IntExp antecedent = ei.getChildren().get(0);
						final IntExp consequent = ei.getChildren().get(1);
						// If the antecedent of a conditional effect becomes FALSE , the conditional
						// effect is
						// removed from the operator. In this case, the effect is never applicable
						// because it
						// requires FALSE to hold, i.e., the state must be inconsistent.
						if (antecedent.getConnective().equals(Connective.FALSE)) {
							exp.getChildren().remove(i);
						}
						// If the antecedent of a conditional effect becomes TRUE, the conditional
						// effect
						// becomes unconditional.
						else if (antecedent.getConnective().equals(Connective.TRUE)) {
							if (consequent.getConnective().equals(Connective.OR)) {
								exp.getChildren().remove(i);
								int j = 0;
								int added = 0;
								while (j < consequent.getChildren().size() && exp.getConnective().equals(Connective.OR)) {
									final IntExp ej = consequent.getChildren().get(j);
									if (ej.getConnective().equals(Connective.TRUE)) {
										exp.setConnective(Connective.TRUE);
									} else if (ej.getConnective().equals(Connective.FALSE)) {
										// do nothing
									} else {
										exp.getChildren().add(i + added, ej);
										added++;
									}
									j++;
								}
								i += added + 1;
							} else {
								exp.getChildren().set(i, consequent);
								i++;
							}
						}
						// If the consequent of a conditional effect becomes TRUE, the conditional
						// effect is removed because it does not lead to any state transition.
						else if (consequent.getConnective().equals(Connective.TRUE)) {
							exp.getChildren().remove(i);
						} else {
							i++;
						}
					} else {
						i++;
					}
				}
				// Finally, if the disjunction is empty, the expression becomes TRUE.
				if (exp.getChildren().isEmpty()) {
					exp.setConnective(Connective.TRUE);
				} else if (exp.getChildren().size() == 1) {
					exp.affect(exp.getChildren().get(0));
				} else {
					final Iterator<IntExp> it = exp.getChildren().iterator();
					while (it.hasNext()) {
						if (it.next().getConnective().equals(Connective.FALSE)) {
							it.remove();
						}
					}
					if (exp.getChildren().isEmpty()) {
						exp.setConnective(Connective.FALSE);
					}
				}
				break;
			case FORALL:
			case EXISTS:
			case AT_START:
			case AT_END:
			case UMINUS:
			case ALWAYS:
			case OVER_ALL:
			case SOMETIME:
			case AT_MOST_ONCE:
				PostInstantiation.simplify(exp.getChildren().get(0));
				break;
			case NOT:
				final IntExp neg = exp.getChildren().get(0);
				PostInstantiation.simplify(neg);
				if (neg.getConnective().equals(Connective.TRUE)) {
					exp.setConnective(Connective.FALSE);
				} else if (neg.getConnective().equals(Connective.FALSE)) {
					exp.setConnective(Connective.TRUE);
				}
				break;
			case WHEN:
			case LESS:
			case LESS_OR_EQUAL:
			case EQUAL:
			case GREATER:
			case GREATER_OR_EQUAL:
			case ASSIGN:
			case INCREASE:
			case DECREASE:
			case SCALE_UP:
			case SCALE_DOWN:
			case MUL:
			case DIV:
			case MINUS:
			case PLUS:
			case F_EXP:
			case SOMETIME_AFTER:
			case SOMETIME_BEFORE:
			case WITHIN:
			case HOLD_AFTER:
				PostInstantiation.simplify(exp.getChildren().get(0));
				PostInstantiation.simplify(exp.getChildren().get(1));
				break;
			case F_EXP_T:
				if (!exp.getChildren().isEmpty()) {
					PostInstantiation.simplify(exp.getChildren().get(0));
				}
				break;
			case ALWAYS_WITHIN:
			case HOLD_DURING:
				PostInstantiation.simplify(exp.getChildren().get(0));
				PostInstantiation.simplify(exp.getChildren().get(1));
				PostInstantiation.simplify(exp.getChildren().get(3));
				break;
			case FN_ATOM:
			case NUMBER:
			case DURATION_ATOM:
			case TIME_VAR:
			case IS_VIOLATED:
			case MINIMIZE:
			case MAXIMIZE:
				break;
		}
	}
	
	// *********************************************************************************************
	// Methods for extracting from the instantiated operators the ground inertia.
	// *********************************************************************************************
	
	/**
	 * Do a pass over the effects of all the instantiated operators and update the ground inertia table. Then, simplify the operators according to the extracted
	 * ground inertia.
	 * 
	 * @param operators the list of operators to simplified.
	 * @param init the initial state.
	 */
	static void simplyOperatorsWithGroundInertia(final List<IntOp> operators, final Set<IntExp> init) {
		
		// Then for each instantiated operator try to simplify it.
		final List<IntOp> tmpOps = new ArrayList<IntOp>(operators.size());
		for (final IntOp op : operators) {
			PostInstantiation.simplifyWithGroundInertia(op.getPreconditions(), false, init);
			PostInstantiation.simplify(op.getPreconditions());
			if (!op.getPreconditions().getConnective().equals(Connective.FALSE)) {
				PostInstantiation.simplifyWithGroundInertia(op.getEffects(), true, init);
				PostInstantiation.simplify(op.getEffects());
				if (!op.getEffects().getConnective().equals(Connective.FALSE) && !op.getEffects().getConnective().equals(Connective.TRUE) || op.getName().equals("nop")) {
					tmpOps.add(op);
				}
			}
		}
		operators.clear();
		final IntOp initOp = new IntOp("initTask", 0);
		initOp.getPreconditions().setConnective(Connective.TRUE);
		initOp.getEffects().setConnective(Connective.TRUE);
		operators.add(initOp);
		operators.addAll(tmpOps);
	}
	
	/**
	 * Do a pass over the constraints of all the instantiated method and remove not useful methods.
	 * 
	 * @param methods the list of methods to simplified.
	 * @param init the initial state.
	 */
	static void simplyMethodsWithGroundInertia(final List<IntMeth> methods, final Set<IntExp> init) {
		// Then for each instantiated method try to simplify it.
		final List<IntMeth> tmpMeths = new ArrayList<>(methods.size());
		for (final IntMeth meth : methods) {
			PostInstantiation.simplifyWithGroundInertia(meth.getConstraints(), false, init);
			PostInstantiation.simplify(meth.getConstraints());
			
			if (!meth.getConstraints().getConnective().equals(Connective.FALSE)) {
				tmpMeths.add(meth);
			}
		}
		methods.clear();
		methods.addAll(tmpMeths);
	}
	
	/**
	 * Do a pass over the constraints on the goal task. and remove not useful constraints.
	 * 
	 * @param constraints the constraints on the goal tasks.
	 * @param init the initial state.
	 */
	static void simplifyGoalTasksConstraintWithGroundInertia(final IntExp constraints, final Set<IntExp> init) {
		PostInstantiation.simplifyWithGroundInertia(constraints, false, init);
		PostInstantiation.simplify(constraints);
	}
	
	/**
	 * Checks if there are for all the tasks defined in the goal a corresponding operator or method. If this condition does not hold, the tasks expression is
	 * simplified false.
	 * 
	 * @param tasks the list of tasks to simplify.
	 * @param relevants the list of relevant tasks.
	 */
	static void simplifyGoalTasks(final IntExp tasks, final List<IntExp> relevants) {
		final Iterator<IntExp> i = tasks.getChildren().iterator();
		while (i.hasNext() && relevants.contains(i.next())) {
			;
		}
		if (i.hasNext()) {
			tasks.setConnective(Connective.FALSE);
			tasks.getChildren().clear();
		}
	}
	
	/**
	 * Simplify a specified goal expression based on the ground inertia information.
	 * 
	 * @param goal the expression to simply.
	 * @param init the initial state.
	 */
	static void simplifyGoalWithGroundInertia(final IntExp goal, final Set<IntExp> init) {
		PostInstantiation.simplifyWithGroundInertia(goal, false, init);
	}
	
	/**
	 * Simplify a specified expression based on the ground inertia information.
	 * <p>
	 * <i>Definition:</i> A ground fact is a positive ground inertia iff it does not occur positively in an unconditional effect or the consequent of a
	 * conditional effect of an action.
	 * </p>
	 * <p>
	 * <i>Definition:</i> A ground fact is a negative ground inertia iff it does not occur negatively in an unconditional effect or the consequent of a
	 * conditional effect of an action.
	 * </p>
	 * An initial fact, which is a negative ground inertia, is never made FALSE and thus always satisfied in all reachable world states. It can be removed from
	 * the state description. All its occurrences in the preconditions of actions and in the antecedents of conditional effects can be simplified to TRUE. A
	 * fact, which is a positive ground inertia and not contained in the initial state, is never satisfied in any reachable world state. All its occurrences in
	 * the preconditions of actions and in the antecedents of conditional effects can be simplified to FALSE. The remaining facts are fluents that, roughly
	 * speaking, can possibly change their truth value during the planning process. They are therefore relevant to the representation of the planning problem.
	 * 
	 * @param exp the expression to simply.
	 * @param effect a boolean to indicate if the expression is an effect or a precondition.
	 * @param init the initial state.
	 */
	private static void simplifyWithGroundInertia(final IntExp exp, final boolean effect, final Set<IntExp> init) {
		switch (exp.getConnective()) {
			case ATOM:
				Inertia inertia = Preprocessing.tableOfGroundInertia.get(exp);
				if (inertia == null) {
					inertia = Inertia.INERTIA;
				}
				// An initial fact, which is a negative ground inertia, is never made FALSE and thus
				// always satisfied in all reachable world states. All its occurrences in the
				// preconditions of actions and in the
				// antecedents of conditional effects can be simplified to TRUE.
				if (!effect && (inertia.equals(Inertia.INERTIA) || inertia.equals(Inertia.NEGATIVE)) && init.contains(exp)) {
					exp.setConnective(Connective.TRUE);
				}
				// A fact, which is a positive ground inertia and not contained in the initial state, is
				// never satisfied in any reachable world state. All its occurrences in the
				// preconditions of actions and in the antecedents of conditional effects can be
				// simplified to FALSE.
				else if (!effect && (inertia.equals(Inertia.INERTIA) || inertia.equals(Inertia.POSITIVE)) && !init.contains(exp)) {
					exp.setConnective(Connective.FALSE);
				}
				break;
			case AND:
				Iterator<IntExp> i = exp.getChildren().iterator();
				while (i.hasNext() && exp.getConnective().equals(Connective.AND)) {
					final IntExp ei = i.next();
					PostInstantiation.simplifyWithGroundInertia(ei, effect, init);
					// If a child expression is FALSE, the whole conjunction becomes FALSE.
					if (ei.getConnective().equals(Connective.FALSE)) {
						exp.setConnective(Connective.FALSE);
					}
					if (ei.getConnective().equals(Connective.TRUE)) {
						i.remove();
					}
				}
				if (exp.getChildren().size() == 1) {
					exp.affect(exp.getChildren().get(0));
				}
				break;
			case OR:
				i = exp.getChildren().iterator();
				while (i.hasNext() && exp.getConnective().equals(Connective.OR)) {
					final IntExp ei = i.next();
					PostInstantiation.simplifyWithGroundInertia(ei, effect, init);
					// If a child expression is TRUE, the whole disjunction is TRUE.
					if (ei.getConnective().equals(Connective.TRUE)) {
						exp.setConnective(Connective.TRUE);
					}
					if (ei.getConnective().equals(Connective.FALSE)) {
						i.remove();
					}
				}
				if (exp.getChildren().size() == 1) {
					exp.affect(exp.getChildren().get(0));
				}
				break;
			case FORALL:
			case EXISTS:
			case AT_START:
			case AT_END:
			case UMINUS:
			case ALWAYS:
			case OVER_ALL:
			case SOMETIME:
			case AT_MOST_ONCE:
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(0), effect, init);
				break;
			case NOT:
				final IntExp neg = exp.getChildren().get(0);
				PostInstantiation.simplifyWithGroundInertia(neg, effect, init);
				if (!effect) {
					if (neg.getConnective().equals(Connective.TRUE)) {
						exp.setConnective(Connective.FALSE);
					} else if (neg.getConnective().equals(Connective.FALSE)) {
						exp.setConnective(Connective.TRUE);
					}
				}
				break;
			case WHEN:
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(0), false, init);
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(1), true, init);
				break;
			case LESS:
			case LESS_OR_EQUAL:
			case EQUAL:
			case GREATER:
			case GREATER_OR_EQUAL:
			case ASSIGN:
			case INCREASE:
			case DECREASE:
			case SCALE_UP:
			case SCALE_DOWN:
			case MUL:
			case DIV:
			case MINUS:
			case PLUS:
			case F_EXP:
			case SOMETIME_AFTER:
			case SOMETIME_BEFORE:
			case WITHIN:
			case HOLD_AFTER:
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(0), effect, init);
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(1), effect, init);
				break;
			case F_EXP_T:
				if (!exp.getChildren().isEmpty()) {
					PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(0), effect, init);
				}
				break;
			case ALWAYS_WITHIN:
			case HOLD_DURING:
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(0), effect, init);
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(1), effect, init);
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(3), effect, init);
				break;
			case FN_ATOM:
			case NUMBER:
			case DURATION_ATOM:
			case TIME_VAR:
			case IS_VIOLATED:
			case MINIMIZE:
			case MAXIMIZE:
			case FN_HEAD:
			case EQUAL_ATOM:
				break;
			case BEFORE:
			case AFTER:
			case BETWEEN:
				PostInstantiation.simplifyWithGroundInertia(exp.getChildren().get(0), false, init);
				if (exp.getChildren().get(0).getConnective().equals(Connective.FALSE)) {
					exp.setConnective(Connective.FALSE);
				} else if (exp.getChildren().get(0).getConnective().equals(Connective.TRUE)) {
					exp.setConnective(Connective.TRUE);
				}
				break;
		}
	}
	
	/**
	 * Do a pass over the effects of a specified list of instantiated operator and update the ground inertia table.
	 * 
	 * @param operators the list of instantiated operators.
	 */
	static void extractGroundInertia(final List<IntOp> operators) {
		Preprocessing.tableOfGroundInertia = new LinkedHashMap<IntExp, Inertia>(Constants.DEFAULT_RELEVANT_FACTS_TABLE);
		for (final IntOp op : operators) {
			PostInstantiation.extractGroundInertia(op.getEffects());
		}
		
	}
	
	/**
	 * Do a pass over the effects of an instantiated operator and update the ground inertia table.
	 * 
	 * @param exp the effect.
	 */
	private static void extractGroundInertia(final IntExp exp) {
		switch (exp.getConnective()) {
			case ATOM:
				Inertia inertia = Preprocessing.tableOfGroundInertia.get(exp);
				if (inertia == null) {
					inertia = Inertia.INERTIA;
				}
				switch (inertia) {
					case INERTIA:
						Preprocessing.tableOfGroundInertia.put(exp, Inertia.NEGATIVE);
						break;
					case POSITIVE:
						Preprocessing.tableOfGroundInertia.put(exp, Inertia.FLUENT);
						break;
				}
				break;
			case AND:
			case OR:
				for (final IntExp ei : exp.getChildren()) {
					PostInstantiation.extractGroundInertia(ei);
				}
				break;
			case FORALL:
			case EXISTS:
			case AT_START:
			case AT_END:
				PostInstantiation.extractGroundInertia(exp.getChildren().get(0));
				break;
			case WHEN:
				PostInstantiation.extractGroundInertia(exp.getChildren().get(1));
				break;
			case NOT:
				final IntExp neg = exp.getChildren().get(0);
				if (neg.getConnective().equals(Connective.ATOM)) {
					inertia = Preprocessing.tableOfGroundInertia.get(neg);
					if (inertia == null) {
						inertia = Inertia.INERTIA;
					}
					switch (inertia) {
						case INERTIA:
							Preprocessing.tableOfGroundInertia.put(neg, Inertia.POSITIVE);
							break;
						case NEGATIVE:
							Preprocessing.tableOfGroundInertia.put(neg, Inertia.FLUENT);
							break;
					}
				}
				break;
			case F_EXP_T:
			case EQUAL_ATOM:
			case FN_HEAD:
			case FN_ATOM:
			case DURATION_ATOM:
			case LESS:
			case LESS_OR_EQUAL:
			case EQUAL:
			case GREATER:
			case GREATER_OR_EQUAL:
			case ASSIGN:
			case INCREASE:
			case DECREASE:
			case SCALE_UP:
			case SCALE_DOWN:
			case MUL:
			case DIV:
			case MINUS:
			case PLUS:
			case SOMETIME_AFTER:
			case SOMETIME_BEFORE:
			case WITHIN:
			case HOLD_AFTER:
			case ALWAYS_WITHIN:
			case HOLD_DURING:
			case TIME_VAR:
			case IS_VIOLATED:
			case NUMBER:
			case MINIMIZE:
			case MAXIMIZE:
			case UMINUS:
			case ALWAYS:
			case OVER_ALL:
			case SOMETIME:
			case AT_MOST_ONCE:
			case F_EXP:
				break;
		}
	}
	
	/**
	 * Extracts the relevant tasks from the instantiated method. A task is relevant if it appears in the expansion of a method or in the goal tasks.
	 * 
	 * @param methods the list of methods.
	 */
	static void extractRelevantTasks(final List<IntMeth> methods, final List<IntOp> operators, final IntExp initTasks) {
		final Set<IntExp> relevants = new LinkedHashSet<>(10000);
		
		//Add the initTask to the relevant tasks table
		final IntExp initTask = new IntExp(Connective.TASK);
		initTask.setPredicate(0);
		initTask.setArguments(new int[0]);
		relevants.add(initTask);
		
		//Adding tasks contained in the problem expansion to the revelantTasks list
		for (final IntExp task : initTasks.getChildren()) {
			relevants.add(task);
		}
		
		//Adding tasks contained in the methods expansions to the revelantTasks list
		for (final IntMeth meth : methods) {
			final IntExp expansion = meth.getExpansion();
			for (final IntExp task : expansion.getChildren()) {
				relevants.add(task);
			}
		}
		Preprocessing.tableOfRelevantTasks = new ArrayList<>(relevants.size());
		for (final IntExp exp : relevants) {
			//if (hasMethod(methods, exp) || hasOperator(operators, exp)) {
			final IntExp relevant = new IntExp(exp);
			Preprocessing.tableOfRelevantTasks.add(relevant);
			//}
		}
	}
	
	private static boolean hasMethod(final List<IntMeth> methods, final IntExp task) {
		for (int i = 0; i < methods.size(); i++) {
			final IntMeth meth = methods.get(i);
			final String taskName = Preprocessing.TableOfTaskSymbols.get(task.getPredicate());
			boolean relevant = meth.getName().equals(taskName) && meth.getArity() == task.getArguments().length;
			int j = 0;
			while (j < meth.getArity() && relevant) {
				relevant = meth.getValueOfParameter(j) == task.getArguments()[j];
				j++;
			}
			if (relevant) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean hasOperator(final List<IntOp> operators, final IntExp task) {
		for (int i = 0; i < operators.size(); i++) {
			final IntOp op = operators.get(i);
			final String taskName = Preprocessing.TableOfTaskSymbols.get(task.getPredicate());
			boolean relevant = op.getName().equals(taskName) && op.getArity() == task.getArguments().length;
			int j = 0;
			while (j < op.getArity() && relevant) {
				relevant = op.getValueOfParameter(j) == task.getArguments()[j];
				j++;
			}
			if (relevant) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Compute the constraint at a specified index.
	 * 
	 * @param index the index.
	 * @return the constraint.
	 */
	static int[] getConstraint(final int index) {
		final int[] constraint = new int[2];
		final int modulo = Preprocessing.tableOfRelevantTasks.size();
		final int rest = index % modulo;
		constraint[0] = (index - rest) / modulo;
		constraint[1] = rest;
		return constraint;
	}
	
	/**
	 * Returns the index of a specified constraint. (The index of the first task * The size of the tasks table) + The index of the second task
	 * 
	 * @param constraint the table of constraints indexes.
	 * @return the index of the constraints.
	 */
	static int getIndexOfConstraint(final int[] constraint) {
		return constraint[0] * (Preprocessing.tableOfRelevantTasks.size()) + constraint[1];
	}
}
